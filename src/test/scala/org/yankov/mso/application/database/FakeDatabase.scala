package org.yankov.mso.application.database

import io.circe.{Decoder, Encoder}
import org.yankov.mso.application.model.DatabaseModel.DbEntry

import java.io.InputStream
import java.nio.file.Path

case class FakeDatabase() extends Database {
  private var insertEntries: List[_] = _
  private var insertPath: Path = _
  private var insertResult: Either[String, Unit] = _

  private var readKeys: List[String] = _
  private var readPath: Path = _
  private var readResult: Either[String, List[_]] = _


  def getInsertEntries: List[_] = insertEntries

  def getInsertPath: Path = insertPath

  def setInsertResult(result: Either[String, Unit]): Unit = insertResult = result

  def getReadKeys: List[String] = readKeys

  def getReadPath: Path = readPath

  def setReadResult(result: Either[String, List[_]]): Unit = readResult = result

  override def insert[T <: DbEntry](entries: List[T], path: Path)
                                   (implicit encoder: Encoder[T]): Either[String, Unit] = {
    insertEntries = entries
    insertPath = path
    insertResult
  }

  override def read[T](keys: List[String], path: Path, inputStream: Path => InputStream)
                      (implicit decoder: Decoder[T]): Either[String, List[T]] = {
    readKeys = keys
    readPath = path
    readResult match {
      case Left(value) => Left(value)
      case Right(value) => Right(value.map(x => x.asInstanceOf[T]))
    }
  }

  override def update[T <: DbEntry](entries: List[T], path: Path, inputStream: Path => InputStream)
                                   (implicit encoder: Encoder[T]): Either[String, List[String]] = ???

  override def delete(keys: List[String], path: Path): Either[String, Int] = ???
}
