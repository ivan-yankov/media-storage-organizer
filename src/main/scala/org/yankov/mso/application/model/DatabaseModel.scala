package org.yankov.mso.application.model

object DatabaseModel {

  val schema: String = "ADMIN"

  object TblArtist {
    val name: String = "ARTIST"
    val colId: String = "ID"
    val colName: String = "NAME"
    val colNote: String = "NOTE"
    val colInstrumentId: String = "INSTRUMENT_ID"
  }

  object TblArtistMissions {
    val name: String = "ARTIST_MISSIONS"
    val colArtistId: String = "ARTIST_ID"
    val colMissions: String = "MISSIONS"
  }

  case class DbArtist(id: Int,
                      name: Option[String],
                      note: Option[String],
                      instrumentId: Option[Int])

  case class DbArtistMissions(artistId: Int,
                              missions: Option[String])

}
