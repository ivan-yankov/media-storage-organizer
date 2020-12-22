package org.yankov.mso.application.model

case class DatabaseCache() {
  def refresh(): Unit = ???

  def getNextArtistId: Int = ???

  def getNextSourceId: Int = ???

  def getNextInstrumentId: Int = ???

  def getNextEthnographicRegionId: Int = ???
}
