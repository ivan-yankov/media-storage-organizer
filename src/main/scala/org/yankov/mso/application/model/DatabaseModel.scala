package org.yankov.mso.application.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.yankov.mso.application.Id

object DatabaseModel {
  case class DbArtist(id: Id,
                      name: Option[String],
                      note: Option[String],
                      instrumentId: Option[Id],
                      missions: Option[List[Id]])
  implicit val dbArtistEncoder: Encoder[DbArtist] = deriveEncoder[DbArtist]
  implicit val dbArtistDecoder: Decoder[DbArtist] = deriveDecoder[DbArtist]

  case class DbArtistMissions(artistId: Id, missions: Option[String])
  implicit val dbArtistMissionsEncoder: Encoder[DbArtistMissions] = deriveEncoder[DbArtistMissions]
  implicit val dbArtistMissionsSourceDecoder: Decoder[DbArtistMissions] = deriveDecoder[DbArtistMissions]

  case class DbEthnographicRegion(id: Id, name: Option[String])
  implicit val dbEthnographicRegionEncoder: Encoder[DbEthnographicRegion] = deriveEncoder[DbEthnographicRegion]
  implicit val dbEthnographicRegionDecoder: Decoder[DbEthnographicRegion] = deriveDecoder[DbEthnographicRegion]

  case class DbInstrument(id: Id, name: Option[String])
  implicit val dbInstrumentEncoder: Encoder[DbInstrument] = deriveEncoder[DbInstrument]
  implicit val dbInstrumentRegionDecoder: Decoder[DbInstrument] = deriveDecoder[DbInstrument]

  case class DbSourceType(id: Id, name: Option[String])
  implicit val dbSourceTypeEncoder: Encoder[DbSourceType] = deriveEncoder[DbSourceType]
  implicit val dbSourceTypeDecoder: Decoder[DbSourceType] = deriveDecoder[DbSourceType]

  case class DbSource(id: Id, signature: Option[String], typeId: Option[Id])
  implicit val dbSourceEncoder: Encoder[DbSource] = deriveEncoder[DbSource]
  implicit val dbSourceDecoder: Decoder[DbSource] = deriveDecoder[DbSource]

  case class DbFolkloreTrack(id: Id,
                             duration: Option[String],
                             note: Option[String],
                             title: Option[String],
                             accompanimentPerformerId: Option[Id],
                             arrangementAuthorId: Option[Id],
                             authorId: Option[Id],
                             conductorId: Option[Id],
                             performerId: Option[Id],
                             soloistId: Option[Id],
                             sourceId: Option[Id],
                             ethnographicRegionId: Option[Id])
  implicit val dbFolkloreTrackEncoder: Encoder[DbFolkloreTrack] = deriveEncoder[DbFolkloreTrack]
  implicit val dbFolkloreTrackDecoder: Decoder[DbFolkloreTrack] = deriveDecoder[DbFolkloreTrack]
}
