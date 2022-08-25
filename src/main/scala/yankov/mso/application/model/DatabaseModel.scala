package yankov.mso.application.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import yankov.mso.application.Id

object DatabaseModel {
  trait DbEntry {
    def id: Id
  }

  case class DbArtist(id: Id,
                      name: Option[String],
                      note: Option[String],
                      instruments: Option[List[Id]],
                      missions: Option[List[String]],
                      members: Option[List[Id]]) extends DbEntry
  implicit val dbArtistEncoder: Encoder[DbArtist] = deriveEncoder[DbArtist]
  implicit val dbArtistDecoder: Decoder[DbArtist] = deriveDecoder[DbArtist]

  case class DbEthnographicRegion(id: Id, name: Option[String]) extends DbEntry
  implicit val dbEthnographicRegionEncoder: Encoder[DbEthnographicRegion] = deriveEncoder[DbEthnographicRegion]
  implicit val dbEthnographicRegionDecoder: Decoder[DbEthnographicRegion] = deriveDecoder[DbEthnographicRegion]

  case class DbInstrument(id: Id, name: Option[String]) extends DbEntry
  implicit val dbInstrumentEncoder: Encoder[DbInstrument] = deriveEncoder[DbInstrument]
  implicit val dbInstrumentRegionDecoder: Decoder[DbInstrument] = deriveDecoder[DbInstrument]

  case class DbSourceType(id: Id, name: Option[String]) extends DbEntry
  implicit val dbSourceTypeEncoder: Encoder[DbSourceType] = deriveEncoder[DbSourceType]
  implicit val dbSourceTypeDecoder: Decoder[DbSourceType] = deriveDecoder[DbSourceType]

  case class DbSource(id: Id, label: Option[String], signature: Option[String], typeId: Option[Id]) extends DbEntry
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
                             ethnographicRegionId: Option[Id]) extends DbEntry
  implicit val dbFolkloreTrackEncoder: Encoder[DbFolkloreTrack] = deriveEncoder[DbFolkloreTrack]
  implicit val dbFolkloreTrackDecoder: Decoder[DbFolkloreTrack] = deriveDecoder[DbFolkloreTrack]

  case class DbAudioIndexItem(id: Id, hash: String, data: List[Long]) extends DbEntry
  implicit val dbAudioIndexItemEncoder: Encoder[DbAudioIndexItem] = deriveEncoder[DbAudioIndexItem]
  implicit val dbAudioIndexItemDecoder: Decoder[DbAudioIndexItem] = deriveDecoder[DbAudioIndexItem]
}
