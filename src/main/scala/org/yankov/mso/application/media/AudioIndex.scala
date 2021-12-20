package org.yankov.mso.application.media

import chromaprint.Fingerprint
import chromaprint.quick.Fingerprinter
import org.slf4j.LoggerFactory
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.model.DatabasePaths
import org.yankov.mso.application.{AudioMatcher, FileUtils, Id, Resources}

import java.io.{ByteArrayInputStream, FileInputStream, InputStream}

case class AudioIndex(database: Database, databasePaths: DatabasePaths) {
  private val log = LoggerFactory.getLogger(getClass)

  implicit class AudioIndexItemAsSearchData(item: DbAudioIndexItem) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = item.hash, data = item.data.map(x => x.toDouble).toVector)
  }

  implicit class FingerprintAsSearchData(fp: Fingerprint) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = fp.compressed, data = fp.data.map(x => x.toDouble).toVector)
  }

  private def decode(input: InputStream): Option[InputStream] = {
    FlacDecoder.decode(input.readAllBytes()) match {
      case Some(bytes) =>
        Some(new ByteArrayInputStream(bytes))
      case None =>
        log.error("Error on flac decoding")
        None
    }
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
    calculateAndInsertItems(inputs.map(x => x._1 -> decode(x._2)).filter(x => x._2.isDefined).map(x => x._1 -> x._2.get))
  }

  def add(id: Id): Boolean = {
    decode(new FileInputStream(databasePaths.mediaFile(id))) match {
      case Some(input) => calculateAndInsertItems(Map(id -> input))
      case None => false
    }
  }

  def remove(id: Id): Boolean = {
    database.delete[DbAudioIndexItem](List(id), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to delete audio index item [$e]")
        false
      case Right(deleted) =>
        deleted == 1
    }
  }

  def search(inputs: Map[Id, InputStream], audioMatch: AudioMatcher): List[AudioSearchResult] = {
    database.read[DbAudioIndexItem](List(), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to read audio index [$e]")
        List()
      case Right(items) =>
        inputs.map {
          input =>
            val fp = calculateFingerprint(input._1, input._2)
            val matches = items
              .map(x => (audioMatch(fp.asSearchData, x.asSearchData), x.id))
              .filterNot(x => x._1 == NonMatch)
            if (matches.nonEmpty) {
              Some(
                AudioSearchResult(
                  sampleId = input._1,
                  exactMatches = matches.filter(x => x._1 == ExactMatch).map(x => x._2),
                  similarMatches = matches.filter(x => x._1 == SimilarMatch).map(x => x._2)
                )
              )
            }
            else None
        }.filter(x => x.isDefined).map(x => x.get).toList
    }
  }

  private def calculateFingerprint(id: Id, input: InputStream): Fingerprint = {
    log.info(s"Calculate audio fingerprint for [$id]")
    Fingerprinter(input).unsafeRunSync()
  }

  private def calculateAndInsertItems(inputs: Map[Id, InputStream]): Boolean = {
    val items = inputs.map {
      input =>
        val fp = calculateFingerprint(input._1, input._2)
        DbAudioIndexItem(
          id = input._1,
          hash = fp.compressed,
          data = fp.data.map(y => y.toLong).toList
        )
    }.toList
    database.insert(items, databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Error during building audio index [$e]")
        false
      case Right(_) =>
        true
    }
  }
}
