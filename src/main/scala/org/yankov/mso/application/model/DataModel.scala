package org.yankov.mso.application.model

import java.io.File
import java.time.Duration

import org.yankov.mso.application.ui.Utils

object DataModel {

  val invalidId: Int = -1

  def isValidId(id: Int): Boolean = id > 0

  def asIdOption(id: Int): Option[Int] = if (isValidId(id)) Option(id) else Option.empty

  trait ArtistMission

  case object Singer extends ArtistMission

  case object InstrumentPlayer extends ArtistMission

  case object Composer extends ArtistMission

  case object Conductor extends ArtistMission

  case object Orchestra extends ArtistMission

  case object Choir extends ArtistMission

  case object Ensemble extends ArtistMission

  case object ChamberGroup extends ArtistMission

  def artistMissionToString(mission: ArtistMission): String = mission match {
    case Singer => DatabaseModel.ArtistMissions.singer
    case InstrumentPlayer => DatabaseModel.ArtistMissions.instrumentPlayer
    case Composer => DatabaseModel.ArtistMissions.composer
    case Conductor => DatabaseModel.ArtistMissions.conductor
    case Orchestra => DatabaseModel.ArtistMissions.orchestra
    case Choir => DatabaseModel.ArtistMissions.choir
    case Ensemble => DatabaseModel.ArtistMissions.ensemble
    case ChamberGroup => DatabaseModel.ArtistMissions.chamberGroup
  }

  def artistMissionFromString(mission: String): ArtistMission = mission match {
    case DatabaseModel.ArtistMissions.singer => Singer
    case DatabaseModel.ArtistMissions.instrumentPlayer => InstrumentPlayer
    case DatabaseModel.ArtistMissions.composer => Composer
    case DatabaseModel.ArtistMissions.conductor => Conductor
    case DatabaseModel.ArtistMissions.orchestra => Orchestra
    case DatabaseModel.ArtistMissions.choir => Choir
    case DatabaseModel.ArtistMissions.ensemble => Ensemble
    case DatabaseModel.ArtistMissions.chamberGroup => ChamberGroup
  }

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
                           file: Option[File] = Option.empty) {

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
      file = file
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
      duration = Utils.calculateDuration(newFile),
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = newFile
    )
  }

}
