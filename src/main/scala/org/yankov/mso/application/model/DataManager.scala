package org.yankov.mso.application.model

import java.beans.{PropertyChangeListener, PropertyChangeSupport}
import java.nio.file.Files

import org.yankov.mso.application.model.DataModel._

case class DataManager(dbConnectionString: String) {
  private val dataModelChangeSupport = new PropertyChangeSupport(this)

  def addPropertyChangeListener(listener: PropertyChangeListener): Unit =
    dataModelChangeSupport.addPropertyChangeListener(listener)

  def insertTracks(tracks: List[FolkloreTrack]): Boolean = ???

  def updateTracks(tracks: List[FolkloreTrack]): Boolean = {
//    x => x.track.file.isDefined && x.track.hasValidId,
//    x => DataModelOperations.setRecord(x.track.id, Files.readAllBytes(x.track.file.get.toPath))
    ???
  }

  def getTracks: List[FolkloreTrack] = ???

  def getSourceTypes: List[SourceType] = ???

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = ???

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Unit = ???

  def getEthnographicRegions: List[EthnographicRegion] = ???

  def insertSource(source: Source): Boolean = ???

  def updateSource(source: Source): Unit = ???

  def getSources: List[Source] = ???

  def insertInstrument(instrument: Instrument): Boolean = ???

  def updateInstrument(instrument: Instrument): Unit = ???

  def getInstruments: List[Instrument] = ???

  def insertArtist(artist: Artist): Boolean = ???

  def updateArtist(artist: Artist): Unit = ???

  def getArtists: List[Artist] = ???

  def getRecord(id: Int): Array[Byte] = ???

  private def updateRecord(id: Int, record: Array[Byte]): Unit = ???
}