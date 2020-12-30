package org.yankov.mso.application.database

import java.sql.Connection

import org.slf4j.LoggerFactory

object DbUtils {
  private val log = LoggerFactory.getLogger(getClass)

  def connect(dbConnectionString: String): Option[Connection] = {
    DatabaseConnection.connect(dbConnectionString) match {
      case Left(throwable) =>
        log.error("Unable to connect database", throwable)
        Option.empty
      case Right(connection) =>
        Option(connection)
    }
  }

  def disconnect(connection: Connection): Unit = DatabaseConnection.close(connection)
}
