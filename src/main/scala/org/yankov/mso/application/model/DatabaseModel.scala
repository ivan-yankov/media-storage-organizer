package org.yankov.mso.application.model

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

object DatabaseModel {
  trait DbEntry

  case class DbArtist(id: String,
                      name: Option[String],
                      note: Option[String],
                      instrumentId: Option[String]) extends DbEntry

  case class DbSource(id: String,
                      sourceType: Option[String],
                      signature: Option[String]) extends DbEntry

  case class DbFolkloreTrack(id: String,
                             duration: Option[String],
                             note: Option[String],
                             title: Option[String],
                             accompanimentPerformerId: Option[String],
                             arrangementAuthorId: Option[String],
                             authorId: Option[String],
                             conductorId: Option[String],
                             performerId: Option[String],
                             soloistId: Option[String],
                             sourceId: Option[String],
                             ethnographicRegionId: Option[String]) extends DbEntry

  implicit val dbArtistEncoder: Encoder[DbArtist] = deriveEncoder[DbArtist]
  implicit val dbSourceEncoder: Encoder[DbSource] = deriveEncoder[DbSource]
  implicit val dbFolkloreTrackEncoder: Encoder[DbFolkloreTrack] = deriveEncoder[DbFolkloreTrack]
}
