package org.yankov.mso.application.model

import java.io.File
import java.time.Duration

object DataModel {

  trait ArtistMission

  case object Singer extends ArtistMission

  case object InstrumentPlayer extends ArtistMission

  case object Composer extends ArtistMission

  case object Conductor extends ArtistMission

  case object Orchestra extends ArtistMission

  case object Choir extends ArtistMission

  case object Ensemble extends ArtistMission

  case object ChamberGroup extends ArtistMission

  case class Instrument(id: Int, name: String)

  case class EthnographicRegion(id: Int, name: String)

  case class SourceType(id: Int, name: String)

  case class Source(id: Int, sourceType: Option[SourceType], signature: Option[String])

  case class Artist(id: Int, name: String, instrument: Option[Instrument], note: Option[String], missions: List[ArtistMission])

  case class FolkloreTrack(id: Int,
                           title: String,
                           performer: Option[Artist],
                           accompanimentPerformer: Option[Artist],
                           author: Option[Artist],
                           arrangementAuthor: Option[Artist],
                           conductor: Option[Artist],
                           soloist: Option[Artist],
                           duration: Duration,
                           note: String,
                           source: Option[Source],
                           ethnographicRegion: Option[EthnographicRegion],
                           file: Option[File],
                           recordFormat: String) {
    def hasValidId: Boolean = id > 0

    def withTitle(newTitle: String): FolkloreTrack = FolkloreTrack(
      id = id,
      title = newTitle,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = file,
      recordFormat = recordFormat
    )

    def withFile(newFile: Option[File]): FolkloreTrack = FolkloreTrack(
      id = id,
      title = title,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = newFile,
      recordFormat = recordFormat
    )
  }
}
