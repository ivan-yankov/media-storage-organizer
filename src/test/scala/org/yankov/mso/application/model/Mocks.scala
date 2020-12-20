package org.yankov.mso.application.model

import java.sql.Connection

import org.scalamock.function.MockFunction5
import org.scalamock.scalatest.MockFactory
import org.yankov.mso.application.database.SqlModel.SqlValue

case class Mocks() extends MockFactory {
  val sqlInsert: MockFunction5[Connection, String, String, List[String], List[SqlValue], Either[Throwable, Unit]] =
    mockFunction[Connection, String, String, List[String], List[SqlValue], Either[Throwable, Unit]]

  val dbCache: DatabaseCache = mock[DatabaseCache]
}
