package org.yankov.mso.application.model

object DatabaseModel {
  case class DbArtist(id: Int,
                      name: Option[String],
                      note: Option[String],
                      instrumentId: Option[Int])

  case class DbArtistMissions(artistId: Int,
                              missions: Option[String])
}
