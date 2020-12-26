package org.yankov.mso.application.model

import java.sql.Connection

import org.yankov.mso.application.database.SqlModel.{Clause, SqlValue}

object SqlFunctions {
  type SqlInsert = (Connection, String, String, List[String], List[SqlValue]) => Either[Throwable, Unit]
  type SqlUpdate = (Connection, String, String, List[String], List[SqlValue], List[Clause]) => Either[Throwable, Unit]
  type SqlDelete = (Connection, String, String, List[Clause]) => Either[Throwable, Unit]
}
