package org.yankov.mso.application.media

import chromaprint.Fingerprint
import chromaprint.quick.Fingerprinter
import org.slf4j.LoggerFactory
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.model.DatabasePaths
import org.yankov.mso.application.{AudioMatcher, FileUtils, Id, Resources}

import java.io.File

case class AudioIndex(database: Database, databasePaths: DatabasePaths) {
  private val log = LoggerFactory.getLogger(getClass)

  implicit class AudioIndexItemAsSearchData(item: DbAudioIndexItem) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = item.hash, data = item.data.map(x => x.toDouble))
  }

  implicit class FingerprintAsSearchData(fp: Fingerprint) {
    def asSearchData: AudioSearchData = AudioSearchData(hash = fp.compressed, data = fp.data.map(x => x.toDouble).toList)
  }

  def buildIfNotExists(): Unit = {
    log.info("Build audio index")
    val ext = Resources.Media.flacExtension
    val files = FileUtils
      .getFiles(databasePaths.media)
      .filter(x => FileUtils.fileNameExtension(x).equals(ext))
      .map(x => x.toFile)
    calculateAndInsertItems(files)
  }

  def add(id: Id): Boolean = calculateAndInsertItems(List(databasePaths.mediaFile(id)))

  def remove(id: Id): Boolean = {
    database.delete[DbAudioIndexItem](List(id), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to delete audio index item [$e]")
        false
      case Right(deleted) =>
        deleted == 1
    }
  }

  def search(files: List[File], audioMatch: AudioMatcher): List[AudioSearchResult] = {
    database.read[DbAudioIndexItem](List(), databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Unable to read audio index [$e]")
        List()
      case Right(items) =>
        files.map {
          file =>
            val fp = calculateFingerprint(file)
            val matches = items
              .map(x => (audioMatch(fp.asSearchData, x.asSearchData), x.id))
              .filterNot(x => x._1 == Different)
            if (matches.nonEmpty) {
              Some(
                AudioSearchResult(
                  sampleFile = file,
                  exactMatches = matches.filter(x => x._1 == ExactMatch).map(x => x._2),
                  similarMatches = matches.filter(x => x._1 == SimilarMatch).map(x => x._2)
                )
              )
            }
            else None
        }.filter(x => x.isDefined).map(x => x.get)
    }
  }

  private def calculateFingerprint(file: File): Fingerprint = {
    log.info(s"Calculate fingerprint for file [${file.toString}]")
    Fingerprinter(file).unsafeRunSync()
  }

  private def calculateAndInsertItems(files: List[File]): Boolean = {
    val items = files.map {
      file =>
        val fp = calculateFingerprint(file)
        DbAudioIndexItem(
          id = FileUtils.fileNameWithoutExtension(file.toPath),
          hash = fp.compressed,
          data = fp.data.map(y => y.toLong).toList
        )
    }
    database.insert(items, databasePaths.audioIndex) match {
      case Left(e) =>
        log.error(s"Error during building audio index [$e]")
        false
      case Right(_) =>
        true
    }
  }
}
