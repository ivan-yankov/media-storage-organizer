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
            VarcharSqlValue(Option.empty),
            VarcharSqlValue(Option.empty),
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
            VarcharSqlValue(Option("name")),
            VarcharSqlValue(Option("note")),
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
            VarcharSqlValue(Option("SINGER")),
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
            VarcharSqlValue(Option("INSTRUMENT_PLAYER")),
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
            VarcharSqlValue(Option("COMPOSER")),
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
            VarcharSqlValue(Option("CONDUCTOR")),
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
            VarcharSqlValue(Option("ORCHESTRA")),
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
            VarcharSqlValue(Option("CHOIR")),
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
            VarcharSqlValue(Option("ENSEMBLE")),
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
            VarcharSqlValue(Option("CHAMBER_GROUP")),
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
            VarcharSqlValue(Option.empty),
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
            VarcharSqlValue(Option("signature")),
            IntSqlValue(Option(3))
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertSource(Source(1, SourceType(3, "source-type"), "signature")) shouldBe true
    }
  }

  "insert instrument" - {
    "empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextInstrumentId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "INSTRUMENT",
          List("ID", "NAME"),
          List(
            IntSqlValue(Option(1)),
            VarcharSqlValue(Option.empty),
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertInstrument(Instrument()) shouldBe true
    }

    "non-empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextInstrumentId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "INSTRUMENT",
          List("ID", "NAME"),
          List(
            IntSqlValue(Option(1)),
            VarcharSqlValue(Option("instrument")),
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertInstrument(Instrument(1, "instrument")) shouldBe true
    }
  }

  "insert ethnographic region" - {
    "empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextEthnographicRegionId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ETHNOGRAPHIC_REGION",
          List("ID", "NAME"),
          List(
            IntSqlValue(Option(1)),
            VarcharSqlValue(Option.empty),
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertEthnographicRegion(EthnographicRegion()) shouldBe true
    }

    "non-empty should succeed" in {
      val mocks = Mocks()

      (mocks.dbCache.refresh _).expects().returns(()).twice()

      (mocks.dbCache.getNextEthnographicRegionId _).expects().returns(1).once()

      mocks
        .sqlInsert
        .expects(
          *,
          "ADMIN",
          "ETHNOGRAPHIC_REGION",
          List("ID", "NAME"),
          List(
            IntSqlValue(Option(1)),
            VarcharSqlValue(Option("ethnographic-region")),
          )
        ).returns(Right())
        .once()

      val dataManager = DataManager(connectionString, mocks.dbCache, mocks.sqlInsert)
      dataManager.insertEthnographicRegion(EthnographicRegion(1, "ethnographic-region")) shouldBe true
    }
  }
}
