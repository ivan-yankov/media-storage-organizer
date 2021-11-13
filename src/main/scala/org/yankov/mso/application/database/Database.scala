package org.yankov.mso.application.database

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}
import org.yankov.mso.application.FileUtils

import java.nio.file.Path

object Database {
  private val separator = "|"

  def insert[T](entries: List[(String, T)], path: Path)
               (implicit encoder: Encoder[T]): Either[String, Unit] = {
    FileUtils.writeTextFile(
      lines = entries.map(x => x._1 + separator + serialize(x._2)),
      path = path.toString,
      append = true
    ) match {
      case Left(e) => Left(e)
      case Right(_) => Right(())
    }
  }

  def read[T](keys: List[String], path: Path)
             (implicit decoder: Decoder[T]): Either[String, List[T]] = {
    readJsonObjects(path, keys) match {
      case Left(error) => Left(error)
      case Right(entries) =>
        val deserialized = entries.map(x => deserialize(x))
        if (deserialized.forall(x => x.isRight)) Right(deserialized.map(x => x.right.get))
        else Left(deserialized.filter(x => x.isLeft).map(x => x.left.get).mkString(System.lineSeparator()))
    }
  }

  def update[T](entries: Map[String, T], path: Path)
               (implicit encoder: Encoder[T]): Either[String, List[String]] = {
    FileUtils.readTextFile(path.toString) match {
      case Left(e) => Left(e)
      case Right(lines) =>
        val updated: List[(String, Option[String])] = lines.map(
          x => {
            val key = x.substring(0, x.indexOf(separator))
            entries.get(key) match {
              case Some(entry) => (serialize(entry), Some(key))
              case None => (x, None)
            }
          }
        )
        FileUtils.writeTextFile(updated.map(x => x._1), path.toString) match {
          case Left(e) => Left(e)
          case Right(_) => Right(updated.withFilter(x => x._2.nonEmpty).map(x => x._2.get))
        }
    }
  }

  def delete(keys: List[String], path: Path): Either[String, Int] = {
    def acceptLine(line: String): Boolean =
      !keys.exists(x => line.startsWith(x))

    FileUtils.readTextFile(path.toString, acceptLine) match {
      case Left(e) => Left(e)
      case Right(lines) => FileUtils.writeTextFile(lines, path.toString) match {
        case Left(e) => Left(e)
        case Right(_) => Right(lines.size)
      }
    }
  }

  private def readJsonObjects(path: Path, keys: List[String]): Either[String, List[String]] = {
    def acceptLine(line: String): Boolean = {
      if (keys.nonEmpty) keys.exists(x => line.startsWith(x))
      else true
    }

    FileUtils.readTextFile(path.toString, acceptLine) match {
      case Left(e) => Left(e)
      case Right(lines) => Right(lines.map(x => x.substring(x.indexOf(separator) + 1)))
    }
  }

  private def serialize[T](x: T)(implicit encoder: Encoder[T]): String = x.asJson.deepDropNullValues.noSpaces

  private def deserialize[T](s: String)(implicit decoder: Decoder[T]): Either[String, T] = {
    parser.parse(s) match {
      case Left(parsingFailure) => Left(parsingFailure.message)
      case Right(json) => json.as[T] match {
        case Left(decodingFailure) => Left(decodingFailure.message)
        case Right(result) => Right(result)
      }
    }
  }
}
