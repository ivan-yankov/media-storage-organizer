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
  private val maxResultCountPerSample = 10

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
    def audioMatchData: (Id, Id, AudioSearchData, AudioSearchData) => AudioSearchResult =
      audioMatch(correlationThreshold, crossCorrelationShift)(_, _, _, _)

    database.read[DbAudioIndexItem](List(), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to read audio index [$e]")
        List()
      case Right(items) =>
        samples.par.map {
          sample =>
            calculateFingerprint(sample._1, sample._2) match {
              case Some(fp) =>
                Some(
                  items
                    .map(item => audioMatchData(sample._1, item.id, fp.asSearchData, item.asSearchData))
                    .filterNot(x => x.matchType == NonMatch)
                    .sortBy(x => scala.math.abs(x.correlation)).reverse
                    .take(maxResultCountPerSample)
                )
              case None =>
                ApplicationConsole.writeMessageWithTimestamp(Resources.ConsoleMessages.errorFingerprintCalculation(sample._1))
                None
            }
        }.filter(x => x.isDefined).flatMap(x => x.get).toList
    }
  }

  private def audioMatch(correlationThreshold: Double, crossCorrelationShift: Int)
                        (aId: Id, bId: Id, a: AudioSearchData, b: AudioSearchData): AudioSearchResult = {
    if (a.hash.equals(b.hash)) {
      AudioSearchResult(aId, bId, ExactMatch, 1.0)
    }
    else {
      crossCorrelation(a.data.asNumbers, b.data.asNumbers, crossCorrelationShift) match {
        case Some(correlation) =>
          if (MathUtils.abs(correlation) > DoubleNumber(correlationThreshold)) AudioSearchResult(aId, bId, SimilarMatch, correlation.value)
          else AudioSearchResult(aId, bId, NonMatch, 0.0)
        case None =>
          ApplicationConsole.writeMessageWithTimestamp(Resources.Search.audioSearchError)
          AudioSearchResult(aId, bId, NonMatch, 0.0)
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
