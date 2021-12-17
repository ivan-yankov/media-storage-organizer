package org.yankov.mso.application.model

import org.slf4j.LoggerFactory
import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.{Database, DatabaseCache}
import org.yankov.mso.application.media.AudioIndex
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.{FileUtils, Id, Resources}

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.UUID

case class DataManager(database: Database,
                       databasePaths: DatabasePaths,
                       audioIndex: Option[AudioIndex]) {
  private val log = LoggerFactory.getLogger(getClass)

  private def artistsPath: Path = databasePaths.artists
  private def instrumentsPath: Path = databasePaths.instruments
  private def sourceTypesPath: Path = databasePaths.sourceTypes
  private def sourcesPath: Path = databasePaths.sources
  private def ethnographicRegionsPath: Path = databasePaths.ethnographicRegions
  private def tracksPath: Path = databasePaths.tracks
  private def mediaPath: Path = databasePaths.media

  private var dbCache: DatabaseCache = _

  private def updateCache(): Unit = dbCache = DatabaseCache(database, databasePaths)

  updateCache()
  database.setOnChange(() => updateCache())

  implicit class StringOption(x: String) {
    def asOption: Option[String] = if (x.nonEmpty) Option(x) else Option.empty
  }

  implicit class MapGet[T](x: Map[Id, T]) {
    def getByOptionOrElse(maybeId: Option[Id], defaultValue: T): T = {
      maybeId match {
        case Some(id) => x.getOrElse(id, defaultValue)
        case None => defaultValue
      }
    }
  }

  implicit class FolkloreTrackAsDbFolkloreTrack(track: FolkloreTrack) {
    def asDbEntry: DbFolkloreTrack = DbFolkloreTrack(
      id = track.id,
      duration = Option(DurationConverter.toHourMinSecString(track.duration)),
      note = track.note.asOption,
      title = track.title.asOption,
      accompanimentPerformerId = track.accompanimentPerformer.id.asOption,
      arrangementAuthorId = track.arrangementAuthor.id.asOption,
      authorId = track.author.id.asOption,
      conductorId = track.conductor.id.asOption,
      performerId = track.performer.id.asOption,
      soloistId = track.soloist.id.asOption,
      sourceId = track.source.id.asOption,
      ethnographicRegionId = track.ethnographicRegion.id.asOption
    )
  }

  implicit class DbFolkloreTrackAsFolkloreTrack(track: DbFolkloreTrack) {
    def asFolkloreTrack: FolkloreTrack = FolkloreTrack(
      id = track.id,
      title = track.title.getOrElse(""),
      performer = dbCache.artists.getByOptionOrElse(track.performerId, Artist()),
      accompanimentPerformer = dbCache.artists.getByOptionOrElse(track.accompanimentPerformerId, Artist()),
      author = dbCache.artists.getByOptionOrElse(track.authorId, Artist()),
      arrangementAuthor = dbCache.artists.getByOptionOrElse(track.arrangementAuthorId, Artist()),
      conductor = dbCache.artists.getByOptionOrElse(track.conductorId, Artist()),
      soloist = dbCache.artists.getByOptionOrElse(track.soloistId, Artist()),
      duration = DurationConverter.fromString(track.duration.getOrElse("")),
      note = track.note.getOrElse(""),
      source = dbCache.sources.getByOptionOrElse(track.sourceId, Source()),
      ethnographicRegion = dbCache.ethnographicRegions.getByOptionOrElse(track.ethnographicRegionId, EthnographicRegion())
    )
  }

  implicit class ArtistAsDbArtist(artist: Artist) {
    def asDbEntry: DbArtist = {
      val missions = artist.missions.map(x => DataModel.artistMissionToString(x))
      DbArtist(
        id = artist.id,
        name = artist.name.asOption,
        note = artist.note.asOption,
        instrumentId = artist.instrument.id.asOption,
        missions = if (missions.nonEmpty) Option(missions) else Option.empty
      )
    }
  }

  implicit class DbArtisAsArtist(dbArtist: DbArtist) {
    def asArtist: Artist = Artist(
      id = dbArtist.id,
      name = dbArtist.name.getOrElse(""),
      instrument = dbCache.instruments.getByOptionOrElse(dbArtist.instrumentId, Instrument()),
      note = dbArtist.note.getOrElse(""),
      missions = dbArtist.missions.getOrElse(List[String]()).map(x => DataModel.artistMissionFromString(x))
    )
  }

  def insertTracks(tracks: List[FolkloreTrack], insertRecord: (Id, File) => Boolean = putRecord): Boolean = {
    val tracksWithIds = tracks.map(
      x => {
        val id = if (isValidId(x.id)) x.id else generateId
        x.withId(id)
      }
    )
    database.insert(tracksWithIds.map(x => x.asDbEntry), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        tracksWithIds.filter(x => x.file.isDefined).map(x => insertRecord(x.id, x.file.get)).forall(x => x)
    }
  }

  def updateTracks(tracks: List[FolkloreTrack], updateRecord: (Id, File) => Boolean = putRecord): Boolean = {
    database.update(tracks.map(x => x.asDbEntry), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        if (updated.size == tracks.size) {
          updated
            .map(x => tracks.find(y => y.id.equals(x)).get)
            .filter(x => x.file.isDefined)
            .map(x => updateRecord(x.id, x.file.get))
            .forall(x => x)
        }
        else false
    }
  }

  def getTracks: List[FolkloreTrack] = dbCache.tracks

  def deleteTrack(track: FolkloreTrack, removeTrackFile: Id => Boolean = deleteTrackFile): Boolean = {
    database.delete[DbFolkloreTrack](List(track.id), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(number) =>
        number == 1 && removeTrackFile(track.id)
    }
  }

  def getSourceTypes: List[SourceType] = dbCache.sourceTypes.values.toList

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val id = if (isValidId(ethnographicRegion.id)) ethnographicRegion.id else generateId
    val dbEntry = DbEthnographicRegion(id, ethnographicRegion.name.asOption)
    database.insert(List(dbEntry), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val dbEntry = DbEthnographicRegion(ethnographicRegion.id, ethnographicRegion.name.asOption)
    database.update(List(dbEntry), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        updated.size == 1
    }
  }

  def getEthnographicRegions: List[EthnographicRegion] = dbCache.ethnographicRegions.values.toList

  def insertSource(source: Source): Boolean = {
    val id = if (isValidId(source.id)) source.id else generateId
    val dbEntry = DbSource(id, source.signature.asOption, asIdOption(source.sourceType.id))
    database.insert(List(dbEntry), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def updateSource(source: Source): Boolean = {
    val dbEntry = DbSource(source.id, source.signature.asOption, asIdOption(source.sourceType.id))
    database.update(List(dbEntry), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        updated.size == 1
    }
  }

  def getSources: List[Source] = dbCache.sources.values.toList

  def insertSourceType(sourceType: SourceType): Boolean = {
    val id = if (isValidId(sourceType.id)) sourceType.id else generateId
    val dbEntry = DbSourceType(id, sourceType.name.asOption)
    database.insert(List(dbEntry), sourceTypesPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def insertInstrument(instrument: Instrument): Boolean = {
    val id = if (isValidId(instrument.id)) instrument.id else generateId
    val dbEntry = DbInstrument(id, instrument.name.asOption)
    database.insert(List(dbEntry), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def updateInstrument(instrument: Instrument): Boolean = {
    val dbEntry = DbInstrument(instrument.id, instrument.name.asOption)
    database.update(List(dbEntry), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        updated.size == 1
    }
  }

  def getInstruments: List[Instrument] = dbCache.instruments.values.toList

  def insertArtist(artist: Artist): Boolean = {
    val id = if (isValidId(artist.id)) artist.id else generateId
    database.insert(List(artist.withId(id).asDbEntry), artistsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def updateArtist(artist: Artist): Boolean = {
    database.update(List(artist.asDbEntry), artistsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        true
    }
  }

  def getArtists: List[Artist] = dbCache.artists.values.toList

  def getRecord(id: Id): Array[Byte] = {
    if (isValidId(id)) {
      FileUtils.readBinaryFile(mediaFile(id)) match {
        case Left(e) =>
          log.error(e)
          Array()
        case Right(data) =>
          data
      }
    }
    else Array()
  }

  def mediaFile(trackId: Id): File = Paths.get(mediaPath.toString, trackId + Resources.Media.flacExtension).toFile

  private def putRecord(id: Id, file: File): Boolean = {
    FileUtils.readBinaryFile(file) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(data) =>
        if (audioIndex.isDefined) {
          audioIndex.get.remove(id)
          audioIndex.get.add(id)
        }
        FileUtils.deleteFile(mediaFile(id)) && FileUtils.writeBinaryFile(mediaFile(id), data)
    }
  }

  private def deleteTrackFile(id: Id): Boolean = {
    if (audioIndex.isDefined) audioIndex.get.remove(id)
    FileUtils.deleteFile(mediaFile(id))
  }

  private def generateId: String = UUID.randomUUID.toString
}
