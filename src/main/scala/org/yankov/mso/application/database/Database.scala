package org.yankov.mso.application.database

import io.circe.{Decoder, Json, parser}
import io.circe.syntax.EncoderOps
import org.slf4j.LoggerFactory
import org.yankov.mso.application.model.DatabaseModel._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.collection.JavaConverters._

case class Database(dbRootDir: String) {
  private val log = LoggerFactory.getLogger(getClass)
  private val charset = StandardCharsets.UTF_8
  private val metadataDir = "meta"
  private val separator = "|"

  private val artists = "artists"
  private val sources = "sources"
  private val tracks = "tracks"

  def insert(entry: DbEntry): Either[String, Path] = {
    entry match {
      case x: DbArtist => insertJsonObject(sourcePath(ArtistsDataSource), x.id, x.asJson)
      case x: DbSource => insertJsonObject(sourcePath(SourcesDataSource), x.id, x.asJson)
      case x: DbFolkloreTrack => insertJsonObject(sourcePath(TracksDataSource), x.id, x.asJson)
      case _ => Left("Unsupported DbEntry")
    }
  }

  def read(ids: List[String], dataSource: DataSource): Either[String, List[DbEntry]] = {
    readJsonObjects(sourcePath(dataSource), ids) match {
      case Left(error) => Left(error)
      case Right(entries) =>
        dataSource match {
          case ArtistsDataSource => parseEntries[DbArtist](entries)
          case SourcesDataSource => parseEntries[DbSource](entries)
          case TracksDataSource => parseEntries[DbFolkloreTrack](entries)
          case _ => Left("Unsupported DbEntry")
        }
    }
  }

  def update(entries: List[DbEntry]): Either[String, Int] = ???

  def delete(ids: List[String]): Either[String, Int] = ???

  private def insertJsonObject(path: Path, key: String, json: Json): Either[String, Path] = {
    try {
      Right(
        Files.write(
          path,
          List(key + separator + json.deepDropNullValues.noSpaces).asJava,
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

  private def readJsonObjects(path: Path, keys: List[String]): Either[String, List[String]] = {
    try {
      val all = Files.readAllLines(path, charset).asScala.toList
      if (keys.isEmpty) Right(all)
      else Right(
        all
          .filter(x => keys.exists(y => x.startsWith(y)))
          .map(x => x.substring(x.indexOf(separator) + 1))
      )
    }
    catch {
      case e: Exception =>
        val message = "Unable to read from database"
        log.error(message, e)
        Left(message)
    }
  }

  private def parseEntries[T](entries: List[String])
                             (implicit decoder: Decoder[T]): Either[String, List[T]] = {
    val parseResults = entries.map(x => parser.parse(x))
    if (parseResults.forall(x => x.isRight)) {
      val decodeResults = parseResults.map(x => x.right.get.as[T])
      if (decodeResults.forall(x => x.isRight)) Right(decodeResults.map(x => x.right.get))
      else Left(decodeResults.filter(x => x.isLeft).map(x => x.left.get.message).mkString(System.lineSeparator()))
    }
    else Left(parseResults.filter(x => x.isLeft).map(x => x.left.get.message).mkString(System.lineSeparator()))
  }

  private def sourcePath(dataSource: DataSource): Path = {
    val file = dataSource match {
      case ArtistsDataSource => artists
      case SourcesDataSource => sources
      case TracksDataSource => tracks
    }
    Paths.get(dbRootDir, metadataDir, file)
  }
}
