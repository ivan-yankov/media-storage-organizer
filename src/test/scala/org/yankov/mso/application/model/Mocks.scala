package org.yankov.mso.application.model

import java.sql.Connection

import org.scalamock.function.{MockFunction4, MockFunction5, MockFunction6}
import org.scalamock.scalatest.MockFactory
import org.yankov.mso.application.database.SqlModel.{Clause, SqlValue}

case class Mocks() extends MockFactory {
  val sqlInsert: MockFunction5[Connection, String, String, List[String], List[SqlValue], Either[Throwable, Unit]] =
    mockFunction[Connection, String, String, List[String], List[SqlValue], Either[Throwable, Unit]]

  val sqlUpdate: MockFunction6[Connection, String, String, List[String], List[SqlValue], List[Clause], Either[Throwable, Unit]] =
    mockFunction[Connection, String, String, List[String], List[SqlValue], List[Clause], Either[Throwable, Unit]]

  val sqlDelete: MockFunction4[Connection, String, String, List[Clause], Either[Throwable, Unit]] =
    mockFunction[Connection, String, String, List[Clause], Either[Throwable, Unit]]

  val dbCache: DatabaseCache = mock[DatabaseCache]
}
