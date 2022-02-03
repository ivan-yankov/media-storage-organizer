package org.yankov.mso.application.model

import org.yankov.mso.application.Id

import java.io.File
import java.time.Duration

object DataModel {
  def isValidId(id: Id): Boolean = id.nonEmpty

  def invalidId: Id = ""

  def asIdOption(id: Id): Option[Id] = if (isValidId(id)) Option(id) else Option.empty

  trait ArtistMission

  case object Singer extends ArtistMission

  case object InstrumentPlayer extends ArtistMission

  case object Composer extends ArtistMission

  case object Conductor extends ArtistMission

  case object Orchestra extends ArtistMission

  case object Choir extends ArtistMission

  case object Ensemble extends ArtistMission

  case object ChamberGroup extends ArtistMission

  object ArtistMissions {
    val singer: String = "Singer"
    val instrumentPlayer: String = "InstrumentPlayer"
    val composer: String = "Composer"
    val conductor: String = "Conductor"
    val orchestra: String = "Orchestra"
    val choir: String = "Choir"
    val ensemble: String = "Ensemble"
    val chamberGroup: String = "ChamberGroup"
  }

  def artistMissionToString(mission: ArtistMission): String = mission match {
    case Singer => ArtistMissions.singer
    case InstrumentPlayer => ArtistMissions.instrumentPlayer
    case Composer => ArtistMissions.composer
    case Conductor => ArtistMissions.conductor
    case Orchestra => ArtistMissions.orchestra
    case Choir => ArtistMissions.choir
    case Ensemble => ArtistMissions.ensemble
    case ChamberGroup => ArtistMissions.chamberGroup
  }

  def artistMissionFromString(mission: String): ArtistMission = mission match {
    case ArtistMissions.singer => Singer
    case ArtistMissions.instrumentPlayer => InstrumentPlayer
    case ArtistMissions.composer => Composer
    case ArtistMissions.conductor => Conductor
    case ArtistMissions.orchestra => Orchestra
    case ArtistMissions.choir => Choir
    case ArtistMissions.ensemble => Ensemble
    case ArtistMissions.chamberGroup => ChamberGroup
  }

  case class Instrument(id: Id = invalidId,
                        name: String = "")

  case class EthnographicRegion(id: Id = invalidId,
                                name: String = "")

  case class SourceType(id: Id = invalidId,
                        name: String = "")

  case class Source(id: Id = invalidId,
                    sourceType: SourceType = SourceType(),
                    signature: String = "")

  case class Artist(id: Id = invalidId,
                    name: String = "",
                    instruments: List[Instrument] = List(),
                    note: String = "",
                    missions: List[ArtistMission] = List(),
                    members: List[Artist] = List()) {

    def withId(newId: Id): Artist = Artist(
      id = newId,
      name = name,
      instruments = instruments,
      note = note,
      missions = missions,
      members = members
    )

    def displayName: String = {
      if (members.nonEmpty) members.map(y => y.name).mkString(", ")
      else name
    }
  }

  case class FolkloreTrack(id: Id = invalidId,
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

    def withId(newId: Id): FolkloreTrack = FolkloreTrack(
      id = newId,
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
      file = file
    )

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
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = newFile
    )

    def withDuration(newDuration: Duration): FolkloreTrack = FolkloreTrack(
      id = id,
      title = title,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = newDuration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = file
    )
  }

  case class AudioSearchSample(id: Id, audioData: Array[Byte])

  case class AudioSearchData(hash: String, data: Vector[Double])

  case class MatchDetails(matchId: Id,
                          matchType: AudioMatchType,
                          correlation: Double)

  case class AudioSearchResult(sample: AudioSearchSample,
                               matchDetails: Option[MatchDetails])

  trait AudioMatchType

  case object ExactMatch extends AudioMatchType

  case object SimilarMatch extends AudioMatchType
}
