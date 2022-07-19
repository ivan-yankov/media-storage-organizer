package org.yankov.mso.application.media

import chromaprint.Fingerprint
import chromaprint.quick.Fingerprinter
import com.yankov.math.MathUtils
import com.yankov.math.xcorr.Correlation.crossCorrelation
import org.slf4j.LoggerFactory
import org.yankov.mso.application._
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.model.DatabasePaths
import org.yankov.mso.application.ui.console.ApplicationConsole

import java.io.ByteArrayInputStream

case class AudioIndex(database: Database, databasePaths: DatabasePaths) {
  private val log = LoggerFactory.getLogger(getClass)
  private val maxResultCountPerSample = 10

  implicit class AudioIndexItemAsSearchData(item: DbAudioIndexItem) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = item.hash, data = item.data.map(x => x.toDouble).toVector)
  }

  implicit class FingerprintAsSearchData(fp: Fingerprint) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = fp.compressed, data = fp.data.map(x => x.toDouble).toVector)
  }

  private def inputs: List[(Id, AudioInput)] = {
    val ext = Resources.Media.flacExtension
    FileUtils
      .getFiles(databasePaths.media)
      .filter(x => FileUtils.fileNameExtension(x).equals(ext))
      .map(x => FileUtils.fileNameWithoutExtension(x) -> AudioInput(x.toFile))
  }

  def build(inputs: List[(Id, AudioInput)] = inputs): Unit = {
    log.info("Build audio index")
    inputs
      .map(x => x._1 -> calculateAndInsertItem(x._1, x._2))
      .filterNot(x => x._2)
      .map(x => x._1)
      .foreach(x => log.error(s"Input with id [$x] is not indexed in audio index"))
  }

  def add(id: Id): Boolean = calculateAndInsertItem(id, AudioInput(databasePaths.mediaFile(id)))

  def remove(id: Id): Boolean = {
    database.delete[DbAudioIndexItem](List(id), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to delete audio index item [$e]")
        false
      case Right(_) => true
    }
  }

  def search(samples: List[AudioSearchSample],
             trackIds: List[Id],
             correlationThreshold: Double,
             crossCorrelationShift: Int): List[(AudioSearchSample, List[AudioSearchResult])] = {
    def audioMatchData: (AudioSearchSample, Id, AudioSearchData, AudioSearchData) => AudioSearchResult =
      audioMatch(correlationThreshold, crossCorrelationShift)(_, _, _, _)

    def searchSample(sample: AudioSearchSample, indexItems: List[DbAudioIndexItem]): List[AudioSearchResult] = {
      calculateFingerprint(sample.id, sample.audioInput) match {
        case Some(fp) =>
          indexItems
            .par
            .map(item => audioMatchData(sample, item.id, fp.asSearchData, item.asSearchData))
            .filter(x => x.matchDetails.isDefined)
            .toList
            .sortBy(x => scala.math.abs(x.matchDetails.get.correlation)).reverse
            .take(maxResultCountPerSample)
        case None =>
          ApplicationConsole.writeMessageWithTimestamp(Resources.ConsoleMessages.errorFingerprintCalculation(sample.id))
          List()
      }
    }

    database.read[DbAudioIndexItem](trackIds, databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to read audio index [$e]")
        List()
      case Right(items) =>
        samples.par.map(x => x -> searchSample(x, items)).toList
    }
  }

  private def audioMatch(correlationThreshold: Double, crossCorrelationShift: Int)
                        (sample: AudioSearchSample, matchId: Id, a: AudioSearchData, b: AudioSearchData): AudioSearchResult = {
    if (a.hash.equals(b.hash)) {
      AudioSearchResult(sample, Some(MatchDetails(matchId, ExactMatch, 1.0)))
    }
    else {
      crossCorrelation(a.data, b.data, crossCorrelationShift) match {
        case Some(correlation) =>
          if (MathUtils.abs(correlation) > correlationThreshold) {
            AudioSearchResult(sample, Some(MatchDetails(matchId, SimilarMatch, correlation)))
          }
          else {
            AudioSearchResult(sample, None)
          }
        case None =>
          ApplicationConsole.writeMessageWithTimestamp(Resources.Search.audioSearchError)
          AudioSearchResult(sample, None)
      }
    }
  }

  private def calculateFingerprint(id: Id, audioInput: AudioInput): Option[Fingerprint] = {
    FlacDecoder.decode(audioInput.getBytes) match {
      case Some(bytes) =>
        log.info(s"Calculate audio fingerprint for [$id]")
        Some(Fingerprinter(new ByteArrayInputStream(bytes)).unsafeRunSync())
      case None =>
        None
    }
  }

  private def calculateAndInsertItem(id: Id, audioInput: AudioInput): Boolean = {
    calculateFingerprint(id, audioInput) match {
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
