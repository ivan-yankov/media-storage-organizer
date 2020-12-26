package org.yankov.mso.application.model

object DatabaseModel {

  val schema: String = "ADMIN"

  object Tables {
    val artist: String = "ARTIST"
    val artistMissions: String = "ARTIST_MISSIONS"
    val source: String = "SOURCE"
    val instrument: String = "INSTRUMENT"
    val ethnographicRegion: String = "ETHNOGRAPHIC_REGION"
    val folkloreTrack: String = "FOLKLORE_TRACK"

    val artistColumns: List[String] = List(
      "ID",
      "NAME",
      "NOTE",
      "INSTRUMENT_ID"
    )

    val artistMissionsColumns: List[String] = List(
      "ARTIST_ID",
      "MISSIONS"
    )

    val sourceColumns: List[String] = List(
      "ID",
      "SIGNATURE",
      "TYPE_ID"
    )

    val instrumentColumns: List[String] = List(
      "ID",
      "NAME"
    )

    val ethnographicRegionColumns: List[String] = List(
      "ID",
      "NAME"
    )

    val folkloreTrackColumns: List[String] = List(
      "ID",
      "DURATION",
      "NOTE",
      "TITLE",
      "ACCOMPANIMENTPERFORMER_ID",
      "ARRANGEMENTAUTHOR_ID",
      "AUTHOR_ID",
      "CONDUCTOR_ID",
      "PERFORMER_ID",
      "SOLOIST_ID",
      "SOURCE_ID",
      "ETHNOGRAPHICREGION_ID"
    )
  }

  object ArtistMissions {
    val singer: String = "SINGER"
    val instrumentPlayer: String = "INSTRUMENT_PLAYER"
    val composer: String = "COMPOSER"
    val conductor: String = "CONDUCTOR"
    val orchestra: String = "ORCHESTRA"
    val choir: String = "CHOIR"
    val ensemble: String = "ENSEMBLE"
    val chamberGroup: String = "CHAMBER_GROUP"
  }

  case class DbArtist(id: Int,
                      name: Option[String],
                      note: Option[String],
                      instrumentId: Option[Int])

  case class DbArtistMissions(artistId: Int,
                              missions: Option[String])

}
