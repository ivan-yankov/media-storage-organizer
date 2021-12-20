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
    calculateAndInsertItems(inputs)
  }

  def add(id: Id): Boolean = calculateAndInsertItems(Map(id -> new FileInputStream(databasePaths.mediaFile(id))))

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
              case None => None
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
        ApplicationConsole.writeMessageWithTimestamp(Resources.Search.errorFingerprintCalculation(id))
        None
    }

  }

  private def calculateAndInsertItems(inputs: Map[Id, InputStream]): Boolean = {
    val items = inputs.map {
      input =>
        calculateFingerprint(input._1, input._2) match {
          case Some(fp) =>
            Right(
              DbAudioIndexItem(
                id = input._1,
                hash = fp.compressed,
                data = fp.data.map(y => y.toLong).toList
              )
            )
          case None => Left(input._1)
        }
    }.toList

    val notIndexedIds = items.filter(x => x.isLeft).map(x => x.left.get)
    notIndexedIds.foreach(x => ApplicationConsole.writeMessageWithTimestamp(Resources.Search.audioIndexItemError(x)))

    val indexedItems = items.filter(x => x.isRight).map(x => x.right.get)
    database.insert(indexedItems, databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Error during insert audio index items [$e]")
        false
      case Right(_) =>
        notIndexedIds.isEmpty
    }
  }
}
