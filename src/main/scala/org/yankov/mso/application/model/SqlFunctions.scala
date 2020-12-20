package org.yankov.mso.application.model

import java.sql.Connection

import org.yankov.mso.application.database.SqlModel.SqlValue

object SqlFunctions {
  type SqlInsert = (Connection, String, String, List[String], List[SqlValue]) => Either[Throwable, Unit]
}
