package org.yankov.mso.application.model

import org.scalatest.{FreeSpec, Matchers}
import org.yankov.mso.application.Id
import org.yankov.mso.application.database.{Database, FakeDatabase}
import org.yankov.mso.application.media.AudioIndex
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._

import java.io.File
import java.nio.file.Paths
import java.time.Duration
import scala.collection.JavaConverters._

class DataManagerTest extends FreeSpec with Matchers {
  private val dbDir = "db-dir"

  private val artistsPath = Paths.get(dbDir, "data", "artists")
  private val sourcesPath = Paths.get(dbDir, "data", "sources")
  private val sourceTypesPath = Paths.get(dbDir, "data", "source-types")
  private val instrumentsPath = Paths.get(dbDir, "data", "instruments")
  private val ethnographicRegionsPath = Paths.get(dbDir, "data", "ethnographic-regions")
  private val tracksPath = Paths.get(dbDir, "data", "tracks")

  private val missions = List(
    Singer,
    InstrumentPlayer,
    Composer,
    Conductor,
    Orchestra,
    Choir,
    Ensemble,
    ChamberGroup
  )

  private val tracks = (1 to 3).toList.map(
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

  case class RecordCheck() {
    private val files: java.util.List[String] = new java.util.ArrayList[String]()

    def getFiles: List[String] = files.asScala.toList

    def putRecord(file: File): Boolean = files.add(file.toPath.toString)

    def deleteRecord(id: Id): Boolean = files.remove(id)
  }

  private def dataManager(db: Database): DataManager = {
    DataManager(db, Paths.get("db-dir", "data"), Paths.get("db-dir", "media"), None)
  }

  "insert" - {
    "artist" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertArtist(Artist()) shouldBe true

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

        dataManager(db).insertArtist(
          Artist(
            id = "id",
            name = "name",
            instrument = Instrument("instrument-id"),
            note = "note",
            missions = missions
          )
        ) shouldBe true

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

        dataManager(db).insertSource(Source()) shouldBe true

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbSource].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbSource].signature shouldBe None
        db.getInsertEntries.head.asInstanceOf[DbSource].typeId shouldBe None
        db.getInsertPath shouldBe sourcesPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertSource(
          Source(
            id = "id",
            signature = "signature",
            sourceType = SourceType("source-type-id")
          )
        ) shouldBe true

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbSource].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbSource].signature shouldBe Some("signature")
        db.getInsertEntries.head.asInstanceOf[DbSource].typeId shouldBe Some("source-type-id")
        db.getInsertPath shouldBe sourcesPath
      }
    }

    "source type" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertSourceType(SourceType(id = "id", name = "name")) shouldBe true

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbSourceType].id shouldBe "id"
        db.getInsertEntries.head.asInstanceOf[DbSourceType].name shouldBe Some("name")
        db.getInsertPath shouldBe sourceTypesPath
      }

      "non-empty" in {

      }
    }

    "instrument" - {
      "empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertInstrument(Instrument()) shouldBe true

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbInstrument].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbInstrument].name shouldBe None
        db.getInsertPath shouldBe instrumentsPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertInstrument(Instrument("id", "name")) shouldBe true

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

        dataManager(db).insertEthnographicRegion(EthnographicRegion()) shouldBe true

        db.getInsertEntries.size shouldBe 1
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].id.nonEmpty shouldBe true
        db.getInsertEntries.head.asInstanceOf[DbEthnographicRegion].name shouldBe None
        db.getInsertPath shouldBe ethnographicRegionsPath
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        dataManager(db).insertEthnographicRegion(EthnographicRegion("id", "name")) shouldBe true

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

        val recordCheck = RecordCheck()

        val tracks = List.fill(3)(FolkloreTrack())

        dataManager(db).insertTracks(tracks, (_, f) => recordCheck.putRecord(f)) shouldBe true

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
        recordCheck.getFiles shouldBe List()
      }

      "non-empty" in {
        val db = FakeDatabase()
        db.setInsertResult(Right(()))

        val recordCheck = RecordCheck()

        dataManager(db).insertTracks(tracks, (_, f) => recordCheck.putRecord(f)) shouldBe true

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
        recordCheck.getFiles shouldBe tracks.map(x => x.file.get.toPath.toString)
      }
    }
  }

  "delete track" in {
    val db = FakeDatabase()
    db.setDeleteResult(Right(1))

    val recordCheck = RecordCheck()
    recordCheck.putRecord(new File("id"))

    dataManager(db).deleteTrack(FolkloreTrack("id"), x => recordCheck.deleteRecord(x)) shouldBe true

    db.getDeleteKeys.size shouldBe 1
    db.getDeleteKeys.head shouldBe "id"
    db.getDeletePath shouldBe tracksPath
    recordCheck.getFiles.isEmpty shouldBe true
  }

  "update" - {
    "ethnographic region" in {
      val db = FakeDatabase()
      db.setUpdateResult(Right(List("id")))

      dataManager(db).updateEthnographicRegion(EthnographicRegion("id", "name")) shouldBe true

      db.getUpdateEntries.size shouldBe 1
      db.getUpdateEntries.head.asInstanceOf[DbEthnographicRegion].id shouldBe "id"
      db.getUpdateEntries.head.asInstanceOf[DbEthnographicRegion].name shouldBe Some("name")
      db.getUpdatePath shouldBe ethnographicRegionsPath
    }

    "source" in {
      val db = FakeDatabase()
      db.setUpdateResult(Right(List("id")))

      dataManager(db).updateSource(
        Source(
          id = "id",
          signature = "signature",
          sourceType = SourceType("source-type-id")
        )
      ) shouldBe true

      db.getUpdateEntries.size shouldBe 1
      db.getUpdateEntries.head.asInstanceOf[DbSource].id shouldBe "id"
      db.getUpdateEntries.head.asInstanceOf[DbSource].signature shouldBe Some("signature")
      db.getUpdateEntries.head.asInstanceOf[DbSource].typeId shouldBe Some("source-type-id")
      db.getUpdatePath shouldBe sourcesPath
    }

    "instrument" in {
      val db = FakeDatabase()
      db.setUpdateResult(Right(List("id")))

      dataManager(db).updateInstrument(Instrument("id", "name")) shouldBe true

      db.getUpdateEntries.size shouldBe 1
      db.getUpdateEntries.head.asInstanceOf[DbInstrument].id shouldBe "id"
      db.getUpdateEntries.head.asInstanceOf[DbInstrument].name shouldBe Some("name")
      db.getUpdatePath shouldBe instrumentsPath
    }

    "artist" in {
      val db = FakeDatabase()
      db.setUpdateResult(Right(List("id")))

      dataManager(db).updateArtist(
        Artist(
          id = "id",
          name = "name",
          instrument = Instrument("instrument-id"),
          note = "note",
          missions = missions
        )
      ) shouldBe true

      db.getUpdateEntries.size shouldBe 1
      db.getUpdateEntries.head.asInstanceOf[DbArtist].id shouldBe "id"
      db.getUpdateEntries.head.asInstanceOf[DbArtist].name shouldBe Some("name")
      db.getUpdateEntries.head.asInstanceOf[DbArtist].note shouldBe Some("note")
      db.getUpdateEntries.head.asInstanceOf[DbArtist].instrumentId shouldBe Some("instrument-id")
      db.getUpdateEntries.head.asInstanceOf[DbArtist].missions shouldBe Some(missions.map(x => artistMissionToString(x)))
      db.getUpdatePath shouldBe artistsPath
    }

    "tracks" in {
      val db = FakeDatabase()
      db.setUpdateResult(Right(tracks.map(x => x.id)))

      val recordCheck = RecordCheck()

      dataManager(db).updateTracks(tracks, (_, f) => recordCheck.putRecord(f)) shouldBe true

      db.getUpdateEntries.size shouldBe tracks.size
      db.getUpdateEntries.zip(tracks).foreach(
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
      db.getUpdatePath shouldBe tracksPath
      recordCheck.getFiles shouldBe tracks.map(x => x.file.get.toPath.toString)
    }
  }
}
