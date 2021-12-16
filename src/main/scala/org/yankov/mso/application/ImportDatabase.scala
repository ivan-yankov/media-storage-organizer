package org.yankov.mso.application

import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.RealDatabase
import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.model.DataModel._

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.UUID
import scala.collection.JavaConverters._

object ImportDatabase {
  private val columnDelimiter = "#"
  private val textWrapper = "@"

  def main(args: Array[String]): Unit = {
    val dir = args(0)
    val dataManager = DataManager(dir, RealDatabase())

    clear(Paths.get(dir, "data"))

    val sourceTypesInput = Paths.get(dir, "source-type.txt")
    val sourceTypesRaw = parse(sourceTypesInput)
    assert(sourceTypesRaw.forall(x => x.length == 2))
    val sourceTypeIds = sourceTypesRaw.map(x => (x(0), generateId)).toMap
    val sourceTypes = sourceTypesRaw.map(x => (x(0), x(1).replace(textWrapper, "")))
    insertSourceTypes(sourceTypeIds, sourceTypes, dataManager)

    val sourcesInput = Paths.get(dir, "source.txt")
    val sourcesRaw = parse(sourcesInput)
    assert(sourcesRaw.forall(x => x.length == 3))
    val sourceIds = sourcesRaw.map(x => (x(0), generateId)).toMap
    val sources = sourcesRaw.map(x => (x(0), x(1).replace(textWrapper, ""), x(2)))
    insertSources(sourceIds, sources, sourceTypeIds, dataManager)

    val instrumentsInput = Paths.get(dir, "instrument.txt")
    val instrumentsRaw = parse(instrumentsInput)
    assert(instrumentsRaw.forall(x => x.length == 2))
    val instrumentIds = instrumentsRaw.map(x => (x(0), generateId)).toMap
    val instruments = instrumentsRaw.map(x => (x(0), x(1).replace(textWrapper, "")))
    insertInstruments(instrumentIds, instruments, dataManager)

    val ethnographicRegionsInput = Paths.get(dir, "ethnographic-region.txt")
    val ethnographicRegionsRaw = parse(ethnographicRegionsInput)
    assert(ethnographicRegionsRaw.forall(x => x.length == 2))
    val ethnographicRegionsIds = ethnographicRegionsRaw.map(x => (x(0), generateId)).toMap
    val ethnographicRegions = ethnographicRegionsRaw.map(x => (x(0), x(1).replace(textWrapper, "")))
    insertEthnographicRegions(ethnographicRegionsIds, ethnographicRegions, dataManager)

    val missionsInput = Paths.get(dir, "artist-missions.txt")
    val missionsRaw = parse(missionsInput)
    assert(missionsRaw.forall(x => x.length == 2))
    val missions = missionsRaw.map(x => (x(0), replaceMission(x(1).replace(textWrapper, ""))))

    val artistsInput = Paths.get(dir, "artist.txt")
    val artistsRaw = parse(artistsInput)
    assert(artistsRaw.forall(x => x.length == 4))
    val artistIds = artistsRaw.map(x => (x(0), generateId)).toMap
    val artists = artistsRaw.map(x => (x(0), x(1).replace(textWrapper, ""), x(2).replace(textWrapper, ""), x(3)))
    insertArtists(artistIds, artists, instrumentIds, missions, dataManager)

    val tracksInput = Paths.get(dir, "folklore-track.txt")
    val tracksRaw = parse(tracksInput)
    assert(tracksRaw.forall(x => x.length == 12))
    val trackIds = tracksRaw.map(x => (x(0), generateId)).toMap
    insertTracks(trackIds, tracksRaw, artistIds, sourceIds, ethnographicRegionsIds, dataManager)

    val renameRecordsScript = Paths.get(dir, "move-track-records.sh")
    val scriptRecords = trackIds.map(x => s"mv media/${x._1}.flac media/${x._2}.flac").toList

    FileUtils.writeTextFile(scriptRecords, renameRecordsScript)

    println("Done")
  }

  private def replaceMission(mission: String): String = {
    mission match {
      case "SINGER" => "Singer"
      case "INSTRUMENT_PLAYER" => "InstrumentPlayer"
      case "COMPOSER" => "Composer"
      case "CONDUCTOR" => "Conductor"
      case "ORCHESTRA" => "Orchestra"
      case "CHOIR" => "Choir"
      case "ENSEMBLE" => "Ensemble"
      case "CHAMBER_GROUP" => "ChamberGroup"
    }
  }

  private def clear(path: Path): Unit = {
    List("source-types", "sources", "instruments", "ethnographic-regions", "artists", "tracks")
      .map(x => Paths.get(path.toString, x))
      .foreach(
        x => {
          Files.deleteIfExists(x)
          Files.createFile(x)
        }
      )
  }

  private def parse(path: Path): List[Array[String]] = {
    Files
      .readAllLines(path)
      .asScala
      .toList
      .map(
        x => {
          val row = {
            if (x.endsWith(columnDelimiter)) x + "LAST_MISSING_VALUE"
            else x
          }
          row.split(columnDelimiter).map(x => x.replace("LAST_MISSING_VALUE", ""))
        }
      )
  }

  private def generateId: String = UUID.randomUUID.toString

  private def insertSourceTypes(ids: Map[String, String],
                                sourceTypes: List[(String, String)],
                                dataManager: DataManager): Unit = {
    val result = sourceTypes
      .map(x => SourceType(id = ids(x._1), name = x._2))
      .map(x => dataManager.insertSourceType(x))
      .forall(x => x)
    assert(result)
  }

  private def insertSources(ids: Map[String, String],
                            sources: List[(String, String, String)],
                            sourceTypeIds: Map[String, String],
                            dataManager: DataManager): Unit = {
    val result = sources
      .map(x => Source(id = ids(x._1), signature = x._2, sourceType = SourceType(id = sourceTypeIds(x._3))))
      .map(x => dataManager.insertSource(x))
      .forall(x => x)
    assert(result)
  }

  private def insertInstruments(ids: Map[String, String],
                                instruments: List[(String, String)],
                                dataManager: DataManager): Unit = {
    val result = instruments
      .map(x => Instrument(id = ids(x._1), name = x._2))
      .map(x => dataManager.insertInstrument(x))
      .forall(x => x)
    assert(result)
  }

  private def insertEthnographicRegions(ids: Map[String, String],
                                        ethnographicRegions: List[(String, String)],
                                        dataManager: DataManager): Unit = {
    val result = ethnographicRegions
      .map(x => EthnographicRegion(id = ids(x._1), name = x._2))
      .map(x => dataManager.insertEthnographicRegion(x))
      .forall(x => x)
    assert(result)
  }

  private def insertArtists(ids: Map[String, String],
                            artists: List[(String, String, String, String)],
                            instrumentIds: Map[String, String],
                            missions: List[(String, String)],
                            dataManager: DataManager): Unit = {
    val result = artists
      .map(
        x => Artist(
          id = ids(x._1),
          name = x._2,
          note = x._3,
          instrument = Instrument(id = instrumentIds.getOrElse(x._4, "")),
          missions = missions.filter(y => y._1.equals(x._1)).map(y => artistMissionFromString(y._2))
        )
      ).map(x => dataManager.insertArtist(x))
      .forall(x => x)
    assert(result)
  }

  private def insertTracks(ids: Map[String, String],
                           tracks: List[Array[String]],
                           artistIds: Map[String, String],
                           sourceIds: Map[String, String],
                           ethnographicRegionIds: Map[String, String],
                           dataManager: DataManager): Unit = {
    def artist(oldId: String): Artist = Artist(id = artistIds.getOrElse(oldId, ""))

    val folkloreTracks = tracks.map(
      x => FolkloreTrack(
        id = ids(x(0)),
        duration = DurationConverter.fromString(x(1).replace(textWrapper, "")),
        note = x(2).replace(textWrapper, ""),
        title = x(3).replace(textWrapper, ""),
        accompanimentPerformer = artist(x(4)),
        arrangementAuthor = artist(x(5)),
        author = artist(x(6)),
        conductor = artist(x(7)),
        performer = artist(x(8)),
        soloist = artist(x(9)),
        source = Source(id = sourceIds.getOrElse(x(10), "")),
        ethnographicRegion = EthnographicRegion(id = ethnographicRegionIds.getOrElse(x(11), ""))
      )
    )

    dataManager.insertTracks(folkloreTracks)
  }
}
