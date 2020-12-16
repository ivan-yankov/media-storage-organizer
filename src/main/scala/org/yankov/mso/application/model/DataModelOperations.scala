package org.yankov.mso.application.model

import java.nio.file.Files

import org.yankov.mso.application.model.DataModel._

object DataModelOperations {
  def insertTracks(tracks: List[FolkloreTrack]): Boolean = ???

  def updateTracks(tracks: List[FolkloreTrack]): Boolean = {
//    x => x.track.file.isDefined && x.track.hasValidId,
//    x => DataModelOperations.setRecord(x.track.id, Files.readAllBytes(x.track.file.get.toPath))
    ???
  }

  def getTracks: List[FolkloreTrack] = ???

  def getSourceTypes: List[SourceType] = ???

  def getEthnographicRegions: List[EthnographicRegion] = ???

  def getSources: List[Source] = ???

  def getInstruments: List[Instrument] = ???

  def getArtists: List[Artist] = ???

  def getRecord(id: Int): Array[Byte] = ???

  private def updateRecord(id: Int, record: Array[Byte]): Unit = ???
}
