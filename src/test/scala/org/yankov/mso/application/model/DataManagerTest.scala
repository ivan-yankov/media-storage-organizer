package org.yankov.mso.application.model

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.database._
import org.yankov.mso.application.database.SqlModel._
import org.yankov.mso.application.model.DataModel._

class DataManagerTest extends FreeSpec with Matchers with MockFactory {
  private val connectionString =
    ConnectionStringFactory.createDerbyConnectionString(InMemoryDatabaseProtocol, "db", Map("create" -> "true"))

  "insert artist" - {
    "empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextArtistId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST",
          List("ID", "NAME", "NOTE", "INSTRUMENT_ID"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option.empty),
            StringSqlValue(Option.empty),
            IntSqlValue(Option.empty)
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertArtist(Artist()) shouldBe true
    }

    "non-empty should succeed" in {
      val missions = List(
        Singer,
        InstrumentPlayer,
        Composer,
        Conductor,
        Orchestra,
        Choir,
        Ensemble,
        ChamberGroup
      )

      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextArtistId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST",
          List("ID", "NAME", "NOTE", "INSTRUMENT_ID"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("name")),
            StringSqlValue(Option("note")),
            IntSqlValue(Option(10))
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("SINGER")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("INSTRUMENT_PLAYER")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("COMPOSER")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("CONDUCTOR")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("ORCHESTRA")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("CHOIR")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("ENSEMBLE")),
          )
        ).returns(Right())
        .once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ARTIST_MISSIONS",
          List("ARTIST_ID", "MISSIONS"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("CHAMBER_GROUP")),
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertArtist(
        Artist(
          -1,
          "name",
          Instrument(10, "instrument"),
          "note",
          missions
        )
      ) shouldBe true
    }
  }

  "insert source" - {
    "empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextSourceId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "SOURCE",
          List("ID", "SIGNATURE", "TYPE_ID"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option.empty),
            IntSqlValue(Option.empty)
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertSource(Source()) shouldBe true
    }

    "non-empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextSourceId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "SOURCE",
          List("ID", "SIGNATURE", "TYPE_ID"),
          List(
            IntSqlValue(Option(1)),
            StringSqlValue(Option("signature")),
            IntSqlValue(Option(3))
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertSource(Source(1, SourceType(3, "source-type"), "signature")) shouldBe true
    }
  }
}
