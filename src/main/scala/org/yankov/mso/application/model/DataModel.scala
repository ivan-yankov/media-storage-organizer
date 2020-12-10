package org.yankov.mso.application.model

import java.time.Duration

object DataModel {

  trait ArtistMission

  case object Singer extends ArtistMission

  case object InstrumentPlayer

  case object Composer

  case object Conductor

  case object Orchestra

  case object Choir

  case object Ensemble

  case object ChamberGroup

  case class Instrument(id: Int, name: String)

  case class EthnographicRegion(id: Int, name: String)

  case class SourceType(id: Int, name: String)

  case class Source(id: Int, sourceType: SourceType, signature: Option[String])

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
                           note: Option[String],
                           source: Option[Source],
                           ethnographicRegion: Option[EthnographicRegion],
                           record: Array[Byte],
                           recordFormat: String)

  def emptyArtist: Artist = Artist(-1, "", Option.empty, Option.empty, List())

  def emptyEthnographicRegion: EthnographicRegion = EthnographicRegion(-1, "")

  def emptyFolkloreTrack: FolkloreTrack = FolkloreTrack(
    id = -1,
    title = "",
    performer = Option.empty,
    accompanimentPerformer = Option.empty,
    author = Option.empty,
    arrangementAuthor = Option.empty,
    conductor = Option.empty,
    soloist = Option.empty,
    duration = Duration.ZERO,
    note = Option.empty,
    source = Option.empty,
    ethnographicRegion = Option.empty,
    record = Array(),
    recordFormat = "FLAC"
  )
}
