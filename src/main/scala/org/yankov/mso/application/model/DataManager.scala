package org.yankov.mso.application.model

import org.slf4j.LoggerFactory
import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.search.{SearchIndexes, SearchIndexesInstance}
import org.yankov.mso.application.{FileUtils, Id, Resources}

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.UUID

case class DataManager(dbRootDir: String,
                       mediaDir: String,
                       database: Database) {
  refreshIndex()

  private val log = LoggerFactory.getLogger(getClass)

  val metadataPath: Path = Paths.get(dbRootDir, "meta")
  val artistsPath: Path = Paths.get(metadataPath.toString, "artists")
  val instrumentsPath: Path = Paths.get(metadataPath.toString, "instruments")
  val sourceTypesPath: Path = Paths.get(metadataPath.toString, "source-types")
  val sourcesPath: Path = Paths.get(metadataPath.toString, "sources")
  val ethnographicRegionsPath: Path = Paths.get(metadataPath.toString, "ethnographic-regions")
  val tracksPath: Path = Paths.get(metadataPath.toString, "tracks")

  implicit class FolkloreTrackAsDbFolkloreTrack(track: FolkloreTrack) {
    def asDbEntry: DbFolkloreTrack = DbFolkloreTrack(
      id = track.id,
      duration = Option(DurationConverter.toHourMinSecString(track.duration, withLeadingZero = true)),
      note = asStringOption(track.note),
      title = asStringOption(track.title),
      accompanimentPerformerId = asStringOption(track.accompanimentPerformer.id),
      arrangementAuthorId = asStringOption(track.arrangementAuthor.id),
      authorId = asStringOption(track.author.id),
      conductorId = asStringOption(track.conductor.id),
      performerId = asStringOption(track.performer.id),
      soloistId = asStringOption(track.soloist.id),
      sourceId = asStringOption(track.source.id),
      ethnographicRegionId = asStringOption(track.ethnographicRegion.id)
    )
  }

  implicit class DbFolkloreTrackAsFolkloreTrack(track: DbFolkloreTrack) {
    def asFolkloreTrack: FolkloreTrack = FolkloreTrack(
      id = track.id,
      title = track.title.getOrElse(""),
      performer = getArtist(track.performerId),
      accompanimentPerformer = getArtist(track.accompanimentPerformerId),
      author = getArtist(track.authorId),
      arrangementAuthor = getArtist(track.arrangementAuthorId),
      conductor = getArtist(track.conductorId),
      soloist = getArtist(track.soloistId),
      duration = DurationConverter.fromString(track.duration.getOrElse("")),
      note = track.note.getOrElse(""),
      source = getSource(track.sourceId),
      ethnographicRegion = getEthnographicRegion(track.ethnographicRegionId)
    )
  }

  implicit class ArtistAsDbArtist(artist: Artist) {
    def asDbEntry: DbArtist = {
      val missions = artist.missions.map(x => DataModel.artistMissionToString(x))
      DbArtist(
        id = artist.id,
        name = asStringOption(artist.name),
        note = asStringOption(artist.note),
        instrumentId = asStringOption(artist.instrument.id),
        missions = if (missions.nonEmpty) Option(missions) else Option.empty
      )
    }
  }

  implicit class DbArtisAsArtist(dbArtist: DbArtist) {
    def asArtist: Artist = Artist(
      id = dbArtist.id,
      name = dbArtist.name.getOrElse(""),
      instrument = getInstrument(dbArtist.instrumentId),
      note = dbArtist.note.getOrElse(""),
      missions = dbArtist.missions.getOrElse(List[String]()).map(x => DataModel.artistMissionFromString(x))
    )
  }

  def insertTracks(tracks: List[FolkloreTrack]): Boolean = {
    val tracksWithIds = tracks.map(x => x.withId(generateId))
    database.insert(tracksWithIds.map(x => x.asDbEntry), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        tracksWithIds.map(x => putRecord(x)).forall(x => x)
    }
  }

  def updateTracks(tracks: List[FolkloreTrack]): Boolean = {
    database.update(tracks.map(x => x.asDbEntry), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        if (updated.size == tracks.size) {
          refreshIndex()
          updated.map(x => tracks.find(y => y.id.equals(x)).get).map(x => putRecord(x)).forall(x => x)
        }
        else false
    }
  }

  def getTracks: List[FolkloreTrack] = {
    database.read[DbFolkloreTrack](List(), tracksPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => x.asFolkloreTrack)
    }
  }

  def deleteTrack(track: FolkloreTrack): Boolean = {
    database.delete(List(track.id), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(number) =>
        number == 1
    }
  }

  def getSourceTypes: List[SourceType] = {
    database.read[DbSourceType](List(), sourceTypesPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => SourceType(id = x.id, name = x.name.getOrElse("")))
    }
  }

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val id = if (isValidId(ethnographicRegion.id)) ethnographicRegion.id else generateId
    val dbEntry = DbEthnographicRegion(id, asStringOption(ethnographicRegion.name))
    database.insert(List(dbEntry), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        true
    }
  }

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val dbEntry = DbEthnographicRegion(ethnographicRegion.id, asStringOption(ethnographicRegion.name))
    database.update(List(dbEntry), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        refreshIndex()
        updated.size == 1
    }
  }

  def getEthnographicRegions: List[EthnographicRegion] = {
    database.read[DbEthnographicRegion](List(), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => EthnographicRegion(id = x.id, name = x.name.getOrElse("")))
    }
  }

  def insertSource(source: Source): Boolean = {
    val id = if (isValidId(source.id)) source.id else generateId
    val dbEntry = DbSource(id, asStringOption(source.signature), asIdOption(source.sourceType.id))
    database.insert(List(dbEntry), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        true
    }
  }

  def updateSource(source: Source): Boolean = {
    val dbEntry = DbSource(source.id, asStringOption(source.signature), asIdOption(source.sourceType.id))
    database.update(List(dbEntry), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        refreshIndex()
        updated.size == 1
    }
  }

  def getSources: List[Source] = {
    database.read[DbSource](List(), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => Source(id = x.id, sourceType = getSourceType(x.typeId), signature = x.signature.getOrElse("")))
    }
  }

  def insertInstrument(instrument: Instrument): Boolean = {
    val id = if (isValidId(instrument.id)) instrument.id else generateId
    val dbEntry = DbInstrument(id, asStringOption(instrument.name))
    database.insert(List(dbEntry), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        true
    }
  }

  def updateInstrument(instrument: Instrument): Boolean = {
    val dbEntry = DbInstrument(instrument.id, asStringOption(instrument.name))
    database.update(List(dbEntry), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        refreshIndex()
        updated.size == 1
    }
  }

  def getInstruments: List[Instrument] = {
    database.read[DbInstrument](List(), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => Instrument(id = x.id, name = x.name.getOrElse("")))
    }
  }

  def insertArtist(artist: Artist): Boolean = {
    val id = if (isValidId(artist.id)) artist.id else generateId
    database.insert(List(artist.withId(id).asDbEntry), artistsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        true
    }
  }

  def updateArtist(artist: Artist): Boolean = {
    database.update(List(artist.asDbEntry), artistsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshIndex()
        true
    }
  }

  def getArtists: List[Artist] = {
    database.read[DbArtist](List(), artistsPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => x.asArtist)
    }
  }

  def getRecord(id: Id): Array[Byte] = {
    if (isValidId(id)) {
      FileUtils.readBinaryFile(mediaFile(id)) match {
        case Left(e) =>
          log.error(e)
          Array()
        case Right(data) => data
      }
    }
    else Array()
  }

  def mediaFile(trackId: Id): File = Paths.get(mediaDir, trackId + Resources.Media.flacExtension).toFile

  private def asStringOption(x: String): Option[String] = if (x.nonEmpty) Option(x) else Option.empty

  private def putRecord(track: FolkloreTrack): Boolean = {
    track.file match {
      case Some(file) =>
        FileUtils.readBinaryFile(file) match {
          case Left(e) =>
            log.error(e)
            false
          case Right(data) =>
            FileUtils.deleteFile(mediaFile(track.id)) && FileUtils.writeBinaryFile(mediaFile(track.id), data)
        }
      case None => true
    }
  }

  private def getArtist(idOption: Option[Id]): Artist = {
    idOption match {
      case Some(id) =>
        database.read[DbArtist](List(id), artistsPath) match {
          case Left(_) => Artist()
          case Right(result) => result.headOption match {
            case Some(dbEntry) => dbEntry.asArtist
            case None => Artist()
          }
        }
      case None =>
        Artist()
    }
  }

  private def getInstrument(idOption: Option[Id]): Instrument = {
    idOption match {
      case Some(id) =>
        database.read[DbInstrument](List(id), instrumentsPath) match {
          case Left(_) => Instrument()
          case Right(result) => result.headOption match {
            case Some(dbEntry) => Instrument(dbEntry.id, dbEntry.name.getOrElse(""))
            case None => Instrument()
          }
        }
      case None =>
        Instrument()
    }
  }

  private def getSource(idOption: Option[Id]): Source = {
    idOption match {
      case Some(id) =>
        database.read[DbSource](List(id), sourcesPath) match {
          case Left(_) => Source()
          case Right(result) => result.headOption match {
            case Some(dbEntry) => Source(
              id = dbEntry.id,
              sourceType = getSourceType(dbEntry.typeId),
              signature = dbEntry.signature.getOrElse("")
            )
            case None => Source()
          }
        }
      case None =>
        Source()
    }
  }

  private def getSourceType(idOption: Option[Id]): SourceType = {
    idOption match {
      case Some(id) =>
        database.read[DbSourceType](List(id), sourceTypesPath) match {
          case Left(_) => SourceType()
          case Right(result) => result.headOption match {
            case Some(dbEntry) => SourceType(
              id = dbEntry.id,
              name = dbEntry.name.getOrElse("")
            )
            case None => SourceType()
          }
        }
      case None =>
        SourceType()
    }
  }

  private def getEthnographicRegion(idOption: Option[Id]): EthnographicRegion = {
    idOption match {
      case Some(id) =>
        database.read[DbEthnographicRegion](List(id), ethnographicRegionsPath) match {
          case Left(_) => EthnographicRegion()
          case Right(result) => result.headOption match {
            case Some(dbEntry) => EthnographicRegion(
              id = dbEntry.id,
              name = dbEntry.name.getOrElse("")
            )
            case None => EthnographicRegion()
          }
        }
      case None =>
        EthnographicRegion()
    }
  }

  private def generateId: String = UUID.randomUUID().toString

  private def refreshIndex(): Unit = SearchIndexesInstance.setInstance(SearchIndexes(getTracks))
}
