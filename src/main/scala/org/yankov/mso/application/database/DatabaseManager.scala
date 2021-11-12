package org.yankov.mso.application.database

import io.circe.Json
import io.circe.syntax.EncoderOps
import org.slf4j.LoggerFactory
import org.yankov.mso.application.model.DatabaseModel._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.collection.JavaConverters._

object DatabaseManager {
  private val log = LoggerFactory.getLogger(getClass)
  private val charset = StandardCharsets.UTF_8
  private val metadataDir = "meta"
  private val separator = ","

  private val artistEntries = "artists"
  private val sourceEntries = "sources"
  private val folkloreTrackEntries = "tracks"

  def insert(dbRootDir: String, entry: DbEntry): Either[String, Path] = {
    entry match {
      case x: DbArtist => insertJson(getEntriesPath(dbRootDir, artistEntries), x.id, x.asJson)
      case x: DbSource => insertJson(getEntriesPath(dbRootDir, sourceEntries), x.id, x.asJson)
      case x: DbFolkloreTrack => insertJson(getEntriesPath(dbRootDir, folkloreTrackEntries), x.id, x.asJson)
      case _ => Left("Unsupported DbEntry")
    }
  }

  private def insertJson(path: Path, key: String, json: Json): Either[String, Path] = {
    try {
      Right(
        Files.write(
          path,
          List(key + separator + json.dropEmptyValues.dropNullValues.noSpaces).asJava,
          charset,
          StandardOpenOption.APPEND
        )
      )
    }
    catch {
      case e: Exception =>
        log.error("Unable to insert database object", e)
        Left(e.getMessage)
    }
  }

  private def getEntriesPath(dbRootDir: String, entries: String): Path = Paths.get(dbRootDir, metadataDir, entries)
}
