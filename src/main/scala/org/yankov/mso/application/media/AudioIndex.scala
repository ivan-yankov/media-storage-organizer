package org.yankov.mso.application.media

import org.slf4j.LoggerFactory
import org.yankov.mso.application.Id
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.model.DatabasePaths

case class AudioIndex(db: Database, databasePaths: DatabasePaths) {
  private val log = LoggerFactory.getLogger(getClass)

  def buildIfNotExists(): Unit = {
  }

  def add(id: Id): Boolean = {
    ???
  }

  def remove(id: Id): Boolean = {
    ???
  }
}
