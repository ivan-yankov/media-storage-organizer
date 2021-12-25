package org.yankov.mso.application.database

import io.circe.{Decoder, Encoder}
import org.yankov.mso.application.Id
import org.yankov.mso.application.model.DatabaseModel.DbEntry

import java.io.InputStream
import java.nio.file.Path

case class FakeDatabase() extends Database {
  private var insertEntries: List[_] = List()
  private var insertPath: Path = _
  private var insertResult: Either[String, Unit] = _

  private var readKeys: List[Id] = _
  private var readPath: Path = _
  private var readResult: Either[String, List[_]] = _

  private var updateEntries: List[_] = List()
  private var updatePath: Path = _
  private var updateResult: Either[String, List[Id]] = _

  private var deleteKeys: List[Id] = _
  private var deletePath: Path = _
  private var deleteResult: Either[String, Unit] = _

  def getInsertEntries: List[_] = insertEntries

  def getInsertPath: Path = insertPath

  def setInsertResult(result: Either[String, Unit]): Unit = insertResult = result

  def getReadKeys: List[Id] = readKeys

  def getReadPath: Path = readPath

  def setReadResult(result: Either[String, List[_]]): Unit = readResult = result

  def getUpdateEntries: List[_] = updateEntries

  def getUpdatePath: Path = updatePath

  def setUpdateResult(result: Either[String, List[Id]]): Unit = updateResult = result

  def getDeleteKeys: List[Id] = deleteKeys

  def getDeletePath: Path = deletePath

  def setDeleteResult(result: Either[String, Unit]): Unit = deleteResult = result

  override def setOnChange(f: () => Unit): Unit = ()

  override def insert[T <: DbEntry](entries: List[T], path: Path)
                                   (implicit encoder: Encoder[T]): Either[String, Unit] = {
    insertEntries = insertEntries ++ entries
    insertPath = path
    insertResult
  }

  override def read[T <: DbEntry](keys: List[Id], path: Path, inputStream: Path => InputStream)
                      (implicit decoder: Decoder[T]): Either[String, List[T]] = {
    readKeys = keys
    readPath = path
    readResult match {
      case Left(value) => Left(value)
      case Right(value) => Right(value.map(x => x.asInstanceOf[T]))
    }
  }

  override def update[T <: DbEntry](entries: List[T], path: Path, inputStream: Path => InputStream)
                                   (implicit encoder: Encoder[T], decoder: Decoder[T]): Either[String, List[Id]] = {
    updateEntries = updateEntries.filterNot(x => entries.contains(x)) ++ entries
    updatePath = path
    updateResult
  }

  override def delete[T <: DbEntry](keys: List[Id], path: Path)
                                   (implicit decoder: Decoder[T]): Either[String, Unit] = {
    deleteKeys = keys
    deletePath = path
    deleteResult
  }
}
