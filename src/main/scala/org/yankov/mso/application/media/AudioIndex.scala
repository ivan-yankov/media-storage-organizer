package org.yankov.mso.application.media

import chromaprint.Fingerprint
import chromaprint.quick.Fingerprinter
import com.yankov.math.MathUtils
import com.yankov.math.Model.{DoubleNumber, _}
import com.yankov.math.xcorr.Correlation.crossCorrelation
import org.slf4j.LoggerFactory
import org.yankov.mso.application._
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.model.DatabasePaths
import org.yankov.mso.application.ui.console.ApplicationConsole

import java.io.{ByteArrayInputStream, FileInputStream, InputStream}

case class AudioIndex(database: Database, databasePaths: DatabasePaths) {
  private val log = LoggerFactory.getLogger(getClass)

  implicit class AudioIndexItemAsSearchData(item: DbAudioIndexItem) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = item.hash, data = item.data.map(x => x.toDouble).toVector)
  }

  implicit class FingerprintAsSearchData(fp: Fingerprint) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = fp.compressed, data = fp.data.map(x => x.toDouble).toVector)
  }

  private def fileInputs: Map[Id, InputStream] = {
    val ext = Resources.Media.flacExtension
    FileUtils
      .getFiles(databasePaths.media)
      .filter(x => FileUtils.fileNameExtension(x).equals(ext))
      .map(x => FileUtils.fileNameWithoutExtension(x) -> new FileInputStream(x.toFile))
      .toMap
  }

  def build(inputs: Map[Id, InputStream] = fileInputs): Unit = {
    log.info("Build audio index")
    inputs
      .map(x => x._1 -> calculateAndInsertItem(x._1, x._2))
      .filterNot(x => x._2)
      .keys
      .foreach(x => log.error(s"Input with id [$x] is not indexed in audio index"))
  }

  def add(id: Id): Boolean = calculateAndInsertItem(id, new FileInputStream(databasePaths.mediaFile(id)))

  def remove(id: Id): Boolean = {
    database.delete[DbAudioIndexItem](List(id), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to delete audio index item [$e]")
        false
      case Right(deleted) =>
        deleted == 1
    }
  }

  def search(samples: Map[Id, InputStream],
             correlationThreshold: Double,
             crossCorrelationShift: Int): List[AudioSearchResult] = {
    def audioMatchData: (AudioSearchData, AudioSearchData) => AudioMatchType =
      audioMatch(correlationThreshold, crossCorrelationShift)(_, _)

    database.read[DbAudioIndexItem](List(), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to read audio index [$e]")
        List()
      case Right(items) =>
        samples.par.map {
          sample =>
            calculateFingerprint(sample._1, sample._2) match {
              case Some(fp) =>
                val matches = items
                  .map(item => (audioMatchData(fp.asSearchData, item.asSearchData), item.id))
                  .filterNot(x => x._1 == NonMatch)
                if (matches.nonEmpty) {
                  Some(
                    AudioSearchResult(
                      sampleId = sample._1,
                      exactMatches = matches.filter(x => x._1 == ExactMatch).map(x => x._2),
                      similarMatches = matches.filter(x => x._1 == SimilarMatch).map(x => x._2)
                    )
                  )
                }
                else None
              case None =>
                ApplicationConsole.writeMessageWithTimestamp(Resources.ConsoleMessages.errorFingerprintCalculation(sample._1))
                None
            }
        }.filter(x => x.isDefined).map(x => x.get).toList
    }
  }

  private def audioMatch(correlationThreshold: Double, crossCorrelationShift: Int)
                        (a: AudioSearchData, b: AudioSearchData): AudioMatchType = {
    if (a.hash.equals(b.hash)) ExactMatch
    else {
      crossCorrelation(a.data.asNumbers, b.data.asNumbers, crossCorrelationShift) match {
        case Some(correlation) =>
          if (MathUtils.abs(correlation) > DoubleNumber(correlationThreshold)) SimilarMatch
          else NonMatch
        case None =>
          ApplicationConsole.writeMessageWithTimestamp(Resources.Search.audioSearchError)
          NonMatch
      }
    }
  }

  private def calculateFingerprint(id: Id, input: InputStream): Option[Fingerprint] = {
    FlacDecoder.decode(input.readAllBytes()) match {
      case Some(bytes) =>
        log.info(s"Calculate audio fingerprint for [$id]")
        Some(Fingerprinter(new ByteArrayInputStream(bytes)).unsafeRunSync())
      case None =>
        None
    }
  }

  private def calculateAndInsertItem(id: Id, input: InputStream): Boolean = {
    calculateFingerprint(id, input) match {
      case Some(fp) =>
        val item = DbAudioIndexItem(
          id = id,
          hash = fp.compressed,
          data = fp.data.map(y => y.toLong).toList
        )
        database.insert(List(item), databasePaths.audioIndex) match {
          case Left(e) =>
            log.error(s"Error during insert audio index item [$e]")
            false
          case Right(_) => true
        }
      case None =>
        false
    }
  }
}
