package org.yankov.mso.application.database

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}
import org.yankov.mso.application.model.DatabaseModel.DbEntry
import org.yankov.mso.application.{FileUtils, Id}

import java.io.{FileInputStream, InputStream}
import java.nio.file.Path

trait Database {
  def setOnChange(f: () => Unit): Unit

  def insert[T <: DbEntry](entries: List[T], path: Path)
                          (implicit encoder: Encoder[T]): Either[String, Unit]

  def read[T](keys: List[Id], path: Path, inputStream: Path => InputStream = inputStreamFromPath)
             (implicit decoder: Decoder[T]): Either[String, List[T]]

  def update[T <: DbEntry](entries: List[T], path: Path, inputStream: Path => InputStream = inputStreamFromPath)
                          (implicit encoder: Encoder[T]): Either[String, List[Id]]

  def delete(keys: List[Id], path: Path): Either[String, Int]

  def inputStreamFromPath(path: Path): InputStream = new FileInputStream(path.toString)
}

case class RealDatabase() extends Database {
  private var onChange: Option[() => Unit] = None
  private val separator = "|"

  override def setOnChange(f: () => Unit): Unit = onChange = Some(f)

  override def insert[T <: DbEntry](entries: List[T], path: Path)
                                   (implicit encoder: Encoder[T]): Either[String, Unit] = {
    FileUtils.writeTextFile(
      lines = entries.map(x => x.id + separator + serialize(x)),
      path = path,
      append = true
    ) match {
      case Left(e) =>
        Left(e)
      case Right(_) =>
        onChange match {
          case Some(f) => f()
          case None => ()
        }
        Right(())
    }
  }

  override def read[T](keys: List[Id], path: Path, inputStream: Path => InputStream = inputStreamFromPath)
                      (implicit decoder: Decoder[T]): Either[String, List[T]] = {
    def readJsonObjects: Either[String, List[String]] = {
      def acceptLine(line: String): Boolean = {
        if (keys.nonEmpty) keys.exists(x => line.startsWith(x))
        else true
      }

      FileUtils.readTextFile(inputStream(path), acceptLine) match {
        case Left(e) => Left(e)
        case Right(lines) => Right(lines.map(x => x.substring(x.indexOf(separator) + 1)))
      }
    }

    readJsonObjects match {
      case Left(error) => Left(error)
      case Right(entries) =>
        val deserialized = entries.map(x => deserialize(x))
        if (deserialized.forall(x => x.isRight)) Right(deserialized.map(x => x.right.get))
        else Left(deserialized.filter(x => x.isLeft).map(x => x.left.get).mkString(System.lineSeparator()))
    }
  }

  override def update[T <: DbEntry](entries: List[T], path: Path, inputStream: Path => InputStream = inputStreamFromPath)
                                   (implicit encoder: Encoder[T]): Either[String, List[Id]] = {
    FileUtils.readTextFile(inputStream(path)) match {
      case Left(e) => Left(e)
      case Right(lines) =>
        val updated: List[(String, Option[String])] = lines.map(
          x => {
            val key = x.substring(0, x.indexOf(separator))
            entries.find(x => x.id.equals(key)) match {
              case Some(entry) => (key + separator + serialize(entry), Some(key))
              case None => (x, None)
            }
          }
        )
        FileUtils.writeTextFile(updated.map(x => x._1), path) match {
          case Left(e) =>
            Left(e)
          case Right(_) =>
            onChange match {
              case Some(f) => f()
              case None => ()
            }
            Right(updated.withFilter(x => x._2.nonEmpty).map(x => x._2.get))
        }
    }
  }

  override def delete(keys: List[Id], path: Path): Either[String, Int] = {
    FileUtils.readTextFile(new FileInputStream(path.toString), x => !keys.exists(y => x.startsWith(y))) match {
      case Left(e) => Left(e)
      case Right(lines) =>
        FileUtils.writeTextFile(lines, path) match {
          case Left(e) =>
            Left(e)
          case Right(_) =>
            onChange match {
              case Some(f) => f()
              case None => ()
            }
            Right(keys.size - lines.size)
        }
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
