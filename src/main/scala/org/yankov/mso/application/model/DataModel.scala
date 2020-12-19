package org.yankov.mso.application.model

import java.io.File
import java.time.Duration

object DataModel {

  val invalidId: Int = -1

  trait ArtistMission

  case object Singer extends ArtistMission

  case object InstrumentPlayer extends ArtistMission

  case object Composer extends ArtistMission

  case object Conductor extends ArtistMission

  case object Orchestra extends ArtistMission

  case object Choir extends ArtistMission

  case object Ensemble extends ArtistMission

  case object ChamberGroup extends ArtistMission

  case class Instrument(id: Int = invalidId,
                        name: String = "")

  case class EthnographicRegion(id: Int = invalidId,
                                name: String = "")

  case class SourceType(id: Int = invalidId,
                        name: String = "")

  case class Source(id: Int = invalidId,
                    sourceType: SourceType = SourceType(),
                    signature: String = "")

  case class Artist(id: Int = invalidId,
                    name: String = "",
                    instrument: Instrument = Instrument(),
                    note: String = "",
                    missions: List[ArtistMission] = List())

  case class FolkloreTrack(id: Int = invalidId,
                           title: String = "",
                           performer: Artist = Artist(),
                           accompanimentPerformer: Artist = Artist(),
                           author: Artist = Artist(),
                           arrangementAuthor: Artist = Artist(),
                           conductor: Artist = Artist(),
                           soloist: Artist = Artist(),
                           duration: Duration = Duration.ZERO,
                           note: String = "",
                           source: Source = Source(),
                           ethnographicRegion: EthnographicRegion = EthnographicRegion(),
                           file: Option[File] = Option.empty,
                           recordFormat: String = "FLAC") {
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
