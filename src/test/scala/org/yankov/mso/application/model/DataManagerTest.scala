package org.yankov.mso.application.model

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.Id
import org.yankov.mso.application.database.FakeDatabase
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import scala.collection.JavaConverters._

import java.io.File
import java.nio.file.Paths
import java.time.Duration

class DataManagerTest extends FreeSpec with Matchers {
  private val dbDir = "db-dir"
  private val mediaDir = "media-dir"

  private val artistsPath = Paths.get(dbDir, "meta", "artists")
  private val sourcesPath = Paths.get(dbDir, "meta", "sources")
  private val instrumentsPath = Paths.get(dbDir, "meta", "instruments")
  private val ethnographicRegionsPath = Paths.get(dbDir, "meta", "ethnographic-regions")
  private val tracksPath = Paths.get(dbDir, "meta", "tracks")

  case class PutRecordCheck() {
    private var files: java.util.List[String] = new java.util.ArrayList[String]()

    def getFiles: List[String] = files.asScala.toList

    def putRecord(id: Id, file: File): Boolean = files.add(file.toPath.toString)
  }

  "insert" - {
    "artist" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertArtist(Artist())

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbArtist].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbArtist].name shouldBe None
        db.getInsertEntries.head.asInstanceOf[DbArtist].note shouldBe None
        db.getInsertEntries.head.asInstanceOf[DbArtist].instrumentId shouldBe None
        db.getInsertEntries.head.asInstanceOf[DbArtist].missions shouldBe None
        db.getInsertPath shouldBe artistsPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

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

        DataManager(dbDir, mediaDir, db).insertArtist(
          Artist(
            id = "id",
            name = "name",
            instrument = Instrument("instrument-id"),
            note = "note",
            missions = missions
          )
        )

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbArtist].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbArtist].name shouldBe Some("name")
        db.getInsertEntries.head.asInstanceOf[DbArtist].note shouldBe Some("note")
        db.getInsertEntries.head.asInstanceOf[DbArtist].instrumentId shouldBe Some("instrument-id")
        db.getInsertEntries.head.asInstanceOf[DbArtist].missions shouldBe Some(missions.map(x => artistMissionToString(x)))
        db.getInsertPath shouldBe artistsPath
      }
    }
    "source" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertSource(Source())

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbSource].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbSource].signature shouldBe None
        db.getInsertEntries.head.asInstanceOf[DbSource].typeId shouldBe None
        db.getInsertPath shouldBe sourcesPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertSource(
          Source(
            id = "id",
            signature = "signature",
            sourceType = SourceType("source-type-id", "")
          )
        )

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbSource].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbSource].signature shouldBe Some("signature")
        db.getInsertEntries.head.asInstanceOf[DbSource].typeId shouldBe Some("source-type-id")
        db.getInsertPath shouldBe sourcesPath
      }
    }
    "instrument" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertInstrument(Instrument())

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbInstrument].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbInstrument].name shouldBe None
        db.getInsertPath shouldBe instrumentsPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertInstrument(Instrument("id", "name"))

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbInstrument].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbInstrument].name shouldBe Some("name")
        db.getInsertPath shouldBe instrumentsPath
      }
    }
    "ethnographic region" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertEthnographicRegion(EthnographicRegion())

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].name shouldBe None
        db.getInsertPath shouldBe ethnographicRegionsPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        DataManager(dbDir, mediaDir, db).insertEthnographicRegion(EthnographicRegion("id", "name"))

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].name shouldBe Some("name")
        db.getInsertPath shouldBe ethnographicRegionsPath
      }
    }
    "tracks" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        val putRecordCheck = PutRecordCheck()

        val tracks = List(
          FolkloreTrack(),
          FolkloreTrack(),
          FolkloreTrack()
        )

        DataManager(dbDir, mediaDir, db).insertTracks(tracks, putRecordCheck.putRecord)

        db.getInsertEntries.size shouldBe tracks.size
        db.getInsertEntries.foreach(
          x => {
            x.asInstanceOf[DbFolkloreTrack].id.nonEmpty shouldBe true
            x.asInstanceOf[DbFolkloreTrack].duration shouldBe Some("00:00:00")
            x.asInstanceOf[DbFolkloreTrack].note shouldBe None
            x.asInstanceOf[DbFolkloreTrack].title shouldBe None
            x.asInstanceOf[DbFolkloreTrack].accompanimentPerformerId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].arrangementAuthorId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].authorId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].conductorId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].performerId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].soloistId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].sourceId shouldBe None
            x.asInstanceOf[DbFolkloreTrack].ethnographicRegionId shouldBe None
          }
        )
        db.getInsertPath shouldBe tracksPath
        putRecordCheck.getFiles shouldBe List()
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))
        db.setReadResult(Right(List[DbFolkloreTrack]()))

        val putRecordCheck = PutRecordCheck()

        val tracks = (1 to 3).toList.map(
          x => FolkloreTrack(
            id = s"$x",
            title = s"title$x",
            performer = Artist(s"performer$x"),
            accompanimentPerformer = Artist(s"accompanimentPerformer$x"),
            author = Artist(s"author$x"),
            arrangementAuthor = Artist(s"arrangementAuthor$x"),
            conductor = Artist(s"conductor$x"),
            soloist = Artist(s"soloist$x"),
            duration = Duration.ofSeconds(123),
            note = s"note$x",
            source = Source(s"source$x"),
            ethnographicRegion = EthnographicRegion(s"ethnographicRegion$x"),
            file = Some(new File(s"file$x"))
          )
        )

        DataManager(dbDir, mediaDir, db).insertTracks(tracks, putRecordCheck.putRecord)

        db.getInsertEntries.size shouldBe tracks.size
        db.getInsertEntries.zip(tracks).foreach(
          pair => {
            val x = pair._1
            val y = pair._2
            x.asInstanceOf[DbFolkloreTrack].id shouldBe y.id
            x.asInstanceOf[DbFolkloreTrack].duration shouldBe Some("00:02:03")
            x.asInstanceOf[DbFolkloreTrack].note shouldBe Some(y.note)
            x.asInstanceOf[DbFolkloreTrack].title shouldBe Some(y.title)
            x.asInstanceOf[DbFolkloreTrack].accompanimentPerformerId shouldBe Some(y.accompanimentPerformer.id)
            x.asInstanceOf[DbFolkloreTrack].arrangementAuthorId shouldBe Some(y.arrangementAuthor.id)
            x.asInstanceOf[DbFolkloreTrack].authorId shouldBe Some(y.author.id)
            x.asInstanceOf[DbFolkloreTrack].conductorId shouldBe Some(y.conductor.id)
            x.asInstanceOf[DbFolkloreTrack].performerId shouldBe Some(y.performer.id)
            x.asInstanceOf[DbFolkloreTrack].soloistId shouldBe Some(y.soloist.id)
            x.asInstanceOf[DbFolkloreTrack].sourceId shouldBe Some(y.source.id)
            x.asInstanceOf[DbFolkloreTrack].ethnographicRegionId shouldBe Some(y.ethnographicRegion.id)
          }
        )
        db.getInsertPath shouldBe tracksPath
        putRecordCheck.getFiles shouldBe tracks.map(x => x.file.get.toPath.toString)
      }
    }
  }




    //  "delete track should succeed" in {
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlDelete
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "FOLKLORE_TRACK",
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    val deleteFile = mockFunction[File, Unit]
    //    deleteFile.expects(new File("/media/1.flac")).returns().once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "/media",
    //      dbCache = mocks.dbCache,
    //      sqlDelete = mocks.sqlDelete,
    //      deleteFile = deleteFile
    //    )
    //    dataManager.deleteTrack(FolkloreTrack(id = 1)) shouldBe true
    //  }
    //
    //  "update ethnographic region should succeed" in {
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlUpdate
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ETHNOGRAPHIC_REGION",
    //        List("NAME"),
    //        List(VarcharSqlValue(Option("ethnographic-region"))),
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "",
    //      dbCache = mocks.dbCache,
    //      sqlUpdate = mocks.sqlUpdate
    //    )
    //    dataManager.updateEthnographicRegion(EthnographicRegion(1, "ethnographic-region")) shouldBe true
    //  }
    //
    //  "update source should succeed" in {
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlUpdate
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "SOURCE",
    //        List("SIGNATURE", "TYPE_ID"),
    //        List(VarcharSqlValue(Option("source")), IntSqlValue(Option(10))),
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "",
    //      dbCache = mocks.dbCache,
    //      sqlUpdate = mocks.sqlUpdate
    //    )
    //    dataManager.updateSource(Source(1, SourceType(10), "source")) shouldBe true
    //  }
    //
    //  "update instrument should succeed" in {
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlUpdate
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "INSTRUMENT",
    //        List("NAME"),
    //        List(VarcharSqlValue(Option("instrument"))),
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "",
    //      dbCache = mocks.dbCache,
    //      sqlUpdate = mocks.sqlUpdate
    //    )
    //    dataManager.updateInstrument(Instrument(1, "instrument")) shouldBe true
    //  }
    //
    //  "update artist should succeed" in {
    //    val missions = List(
    //      Singer,
    //      InstrumentPlayer,
    //      Composer,
    //      Conductor,
    //      Orchestra,
    //      Choir,
    //      Ensemble,
    //      ChamberGroup
    //    )
    //
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlUpdate
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST",
    //        List("NAME", "NOTE", "INSTRUMENT_ID"),
    //        List(
    //          VarcharSqlValue(Option("name")),
    //          VarcharSqlValue(Option("note")),
    //          IntSqlValue(Option(10))
    //        ),
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlDelete
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List(WhereClause("ARTIST_ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("SINGER")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("INSTRUMENT_PLAYER")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("COMPOSER")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("CONDUCTOR")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("ORCHESTRA")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("CHOIR")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("ENSEMBLE")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    mocks
    //      .sqlInsert
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "ARTIST_MISSIONS",
    //        List("ARTIST_ID", "MISSIONS"),
    //        List(
    //          IntSqlValue(Option(1)),
    //          VarcharSqlValue(Option("CHAMBER_GROUP")),
    //        )
    //      ).returns(Right(()))
    //      .once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "",
    //      dbCache = mocks.dbCache,
    //      sqlInsert = mocks.sqlInsert,
    //      sqlUpdate = mocks.sqlUpdate,
    //      sqlDelete = mocks.sqlDelete
    //    )
    //    dataManager.updateArtist(
    //      Artist(
    //        1,
    //        "name",
    //        Instrument(10, "instrument"),
    //        "note",
    //        missions
    //      )
    //    ) shouldBe true
    //  }
    //
    //  "update tracks should succeed" in {
    //    val mocks = Mocks()
    //
    //    (mocks.dbCache.refresh _).expects().returns(()).twice()
    //
    //    mocks
    //      .sqlUpdate
    //      .expects(
    //        *,
    //        "ADMIN",
    //        "FOLKLORE_TRACK",
    //        List(
    //          "DURATION",
    //          "NOTE",
    //          "TITLE",
    //          "ACCOMPANIMENTPERFORMER_ID",
    //          "ARRANGEMENTAUTHOR_ID",
    //          "AUTHOR_ID",
    //          "CONDUCTOR_ID",
    //          "PERFORMER_ID",
    //          "SOLOIST_ID",
    //          "SOURCE_ID",
    //          "ETHNOGRAPHICREGION_ID"
    //        ),
    //        List(
    //          VarcharSqlValue(Option("00:01:00")),
    //          VarcharSqlValue(Option("note")),
    //          VarcharSqlValue(Option("title")),
    //          IntSqlValue(Option(10)),
    //          IntSqlValue(Option(11)),
    //          IntSqlValue(Option(12)),
    //          IntSqlValue(Option(13)),
    //          IntSqlValue(Option(14)),
    //          IntSqlValue(Option(15)),
    //          IntSqlValue(Option(16)),
    //          IntSqlValue(Option(17))
    //        ),
    //        List(WhereClause("ID", "=", IntSqlValue(Option(1))))
    //      ).returns(Right(()))
    //      .once()
    //
    //    val onTrackUpdated = mockFunction[FolkloreTrack, Boolean, Unit]
    //    onTrackUpdated.expects(*, true).returns().once()
    //
    //    val bytes = "record".getBytes
    //
    //    val readFile = mockFunction[File, Array[Byte]]
    //    readFile.expects(new File("/path/to/the/file/input.flac")).returns(bytes).once()
    //
    //    val deleteFile = mockFunction[File, Unit]
    //    deleteFile.expects(new File("/media/1.flac")).returns().once()
    //
    //    val writeFile = mockFunction[File, Array[Byte], Unit]
    //    writeFile.expects(new File("/media/1.flac"), bytes).returns().once()
    //
    //    val dataManager = DataManager(
    //      dbRootDir = connectionString,
    //      mediaDir = "/media",
    //      dbCache = mocks.dbCache,
    //      sqlInsert = mocks.sqlInsert,
    //      sqlUpdate = mocks.sqlUpdate,
    //      readBinaryFile = readFile,
    //      writeBinaryFile = writeFile,
    //      deleteFile = deleteFile
    //    )
    //    dataManager.updateTracks(
    //      List(
    //        FolkloreTrack(
    //          id = 1,
    //          duration = Duration.ofSeconds(60),
    //          note = "note",
    //          title = "title",
    //          accompanimentPerformer = Artist(10, "accompaniment-performer"),
    //          arrangementAuthor = Artist(11, "arrangement-author"),
    //          author = Artist(12, "author"),
    //          conductor = Artist(13, "conductor"),
    //          performer = Artist(14, "performer"),
    //          soloist = Artist(15, "soloist"),
    //          source = Source(16),
    //          ethnographicRegion = EthnographicRegion(17),
    //          file = Option(new File("/path/to/the/file/input.flac"))
    //        )
    //      ),
    //      onTrackUpdated
    //    ) shouldBe true
    //  }
}
