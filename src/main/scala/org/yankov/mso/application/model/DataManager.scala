package org.yankov.mso.application.model

import org.slf4j.LoggerFactory
import org.yankov.mso.application.{Id, Resources}
import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.Database
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.search.{SearchIndexes, SearchIndexesInstance}

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.UUID

case class DataManager(dbRootDir: String,
                       mediaDir: String,
                       dbCache: DatabaseCache,
                       readRecord: File => Array[Byte] = x => Files.readAllBytes(x.toPath),
                       writeRecord: (File, Array[Byte]) => Unit = (x, y) => Files.write(x.toPath, y),
                       deleteRecord: File => Unit = x => Files.deleteIfExists(x.toPath)) {
  refreshCacheAndIndex()

  private val log = LoggerFactory.getLogger(getClass)
  private val equal = "="

  private val metadataPath = Paths.get(dbRootDir, "meta")
  private val artistsPath = Paths.get(metadataPath.toString, "artists")
  private val sourceTypesPath = Paths.get(metadataPath.toString, "source-types")
  private val sourcesPath = Paths.get(metadataPath.toString, "sources")
  private val ethnographicRegionsPath = Paths.get(metadataPath.toString, "ethnographic-regions")
  private val tracksPath = Paths.get(metadataPath.toString, "tracks")

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

  def insertTracks(tracks: List[FolkloreTrack], onTrackInserted: FolkloreTrack => Unit): Boolean = {
    val tracksWithIds = tracks.map(x => x.withId(generateId))
    Database.insert(tracksWithIds.map(x => (x.id, x.asDbEntry)), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        tracksWithIds.foreach(
          x => {
            if (x.file.isDefined) writeRecord(storageFileName(x.id), readRecord(x.file.get))
            onTrackInserted(x)
          }
        )
        refreshCacheAndIndex()
        true
    }
  }

  def updateTracks(tracks: List[FolkloreTrack], onTrackUpdated: FolkloreTrack => Unit): Boolean = {
    def updateRecord(track: FolkloreTrack): Unit = {
      if (track.file.isDefined) {
        deleteRecord(storageFileName(track.id))
        writeRecord(storageFileName(track.id), readRecord(track.file.get))
      }
    }

    Database.update(tracks.map(x => (x.id, x.asDbEntry)).toMap, tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        if (updated.size == tracks.size) {
          updated.map(x => tracks.find(y => y.id.equals(x)).get).foreach(
            x => {
              updateRecord(x)
              onTrackUpdated(x)
            }
          )
          refreshCacheAndIndex()
          true
        }
        else false
    }
  }

  def getTracks: List[FolkloreTrack] = {
    Database.read[DbFolkloreTrack](List(), tracksPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => x.asFolkloreTrack)
    }
  }

  def deleteTrack(track: FolkloreTrack): Boolean = {
    Database.delete(List(track.id), tracksPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(number) =>
        number == 1
    }
  }

  def getSourceTypes: List[SourceType] = {
    Database.read[DbSourceType](List(), sourceTypesPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => SourceType(id = x.id, name = x.name.getOrElse("")))
    }
  }

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val dbEntry = DbEthnographicRegion(generateId, asStringOption(ethnographicRegion.name))
    Database.insert(List((dbEntry.id, dbEntry)), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(_) =>
        refreshCacheAndIndex()
        true
    }
  }

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    val dbEntry = DbEthnographicRegion(ethnographicRegion.id, asStringOption(ethnographicRegion.name))
    Database.update(Map(dbEntry.id -> dbEntry), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        false
      case Right(updated) =>
        refreshCacheAndIndex()
        updated.size == 1
    }
  }

  def getEthnographicRegions: List[EthnographicRegion] = {
    Database.read[DbEthnographicRegion](List(), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        List()
      case Right(result) =>
        result.map(x => EthnographicRegion(id = x.id, name = x.name.getOrElse("")))
    }
  }

  def insertSource(source: Source): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlInsert(
          connection,
          schema,
          Tables.source,
          Tables.sourceColumns,
          List(
            IntSqlValue(Option(dbCache.getNextSourceId)),
            VarcharSqlValue(asStringOption(source.signature)),
            IntSqlValue(asIdOption(source.sourceType.id))
          )
        ) match {
          case Left(throwable) =>
            log.error("Unable to insert artist", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            disconnect(connection)
            refreshCacheAndIndex()
            true
        }
      case None =>
        false
    }
  }

  def updateSource(source: Source): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlUpdate(
          connection,
          schema,
          Tables.source,
          Tables.sourceColumns.filter(x => !x.equals(id)),
          List(VarcharSqlValue(asStringOption(source.signature)), IntSqlValue(asIdOption(source.sourceType.id))),
          List(WhereClause(id, equal, IntSqlValue(asIdOption(source.id))))
        ) match {
          case Left(throwable) =>
            log.error("Unable to update source", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            disconnect(connection)
            refreshCacheAndIndex()
            true
        }
      case None =>
        false
    }
  }

  def getSources: List[Source] = {
    dbCache
      .getCache
      .sources
      .map(x => Source(id = x.id, sourceType = getSourceType(x.typeId), signature = x.signature.getOrElse("")))
  }

  def insertInstrument(instrument: Instrument): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlInsert(
          connection,
          schema,
          Tables.instrument,
          Tables.instrumentColumns,
          List(
            IntSqlValue(Option(dbCache.getNextInstrumentId)),
            VarcharSqlValue(asStringOption(instrument.name))
          )
        ) match {
          case Left(throwable) =>
            log.error("Unable to insert instrument", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            disconnect(connection)
            refreshCacheAndIndex()
            true
        }
      case None =>
        false
    }
  }

  def updateInstrument(instrument: Instrument): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlUpdate(
          connection,
          schema,
          Tables.instrument,
          Tables.instrumentColumns.filter(x => !x.equals(id)),
          List(VarcharSqlValue(asStringOption(instrument.name))),
          List(WhereClause(id, equal, IntSqlValue(asIdOption(instrument.id))))
        ) match {
          case Left(throwable) =>
            log.error("Unable to update instrument", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            disconnect(connection)
            refreshCacheAndIndex()
            true
        }
      case None =>
        false
    }
  }

  def getInstruments: List[Instrument] = {
    dbCache
      .getCache
      .instruments
      .map(x => Instrument(id = x.id, name = x.name.getOrElse("")))
  }

  def insertArtist(artist: Artist): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        val artistId = dbCache.getNextArtistId
        sqlInsert(
          connection,
          schema,
          Tables.artist,
          Tables.artistColumns,
          List(
            IntSqlValue(Option(artistId)),
            VarcharSqlValue(asStringOption(artist.name)),
            VarcharSqlValue(asStringOption(artist.note)),
            IntSqlValue(asIdOption(artist.instrument.id))
          )
        ) match {
          case Left(throwable) =>
            log.error("Unable to insert artist", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            val result = artist
              .missions
              .map(x => DataModel.artistMissionToString(x))
              .map(x =>
                sqlInsert(
                  connection,
                  schema,
                  Tables.artistMissions,
                  Tables.artistMissionsColumns,
                  List(IntSqlValue(Option(artistId)), VarcharSqlValue(asStringOption(x)))
                )
              )
              .forall(x => x.isRight)
            disconnect(connection)
            refreshCacheAndIndex()
            result
        }
      case None =>
        false
    }
  }

  def updateArtist(artist: Artist): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlUpdate(
          connection,
          schema,
          Tables.artist,
          Tables.artistColumns.filter(x => !x.equals(id)),
          List(
            VarcharSqlValue(asStringOption(artist.name)),
            VarcharSqlValue(asStringOption(artist.note)),
            IntSqlValue(asIdOption(artist.instrument.id)),
          ),
          List(WhereClause(id, equal, IntSqlValue(asIdOption(artist.id))))
        ) match {
          case Left(throwable) =>
            log.error("Unable to update instrument", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            sqlDelete(
              connection,
              schema,
              Tables.artistMissions,
              List(WhereClause(Tables.artistId, equal, IntSqlValue(asIdOption(artist.id))))
            )
            val result = artist
              .missions
              .map(x => DataModel.artistMissionToString(x))
              .map(x =>
                sqlInsert(
                  connection,
                  schema,
                  Tables.artistMissions,
                  Tables.artistMissionsColumns,
                  List(IntSqlValue(asIdOption(artist.id)), VarcharSqlValue(asStringOption(x)))
                )
              )
              .forall(x => x.isRight)
            disconnect(connection)
            refreshCacheAndIndex()
            result
        }
      case None =>
        false
    }
  }

  def getArtists: List[Artist] = {
    dbCache
      .getCache
      .artists
      .map(x => extractArtist(x))
  }

  def getRecord(id: Int): Array[Byte] = {
    if (isValidId(id)) readRecord(storageFileName(id))
    else Array()
  }

  def storageFileName(trackId: String): File =
    Paths.get(mediaDir, trackId + Resources.Media.flacExtension).toFile

  private def asStringOption(x: String): Option[String] = if (x.nonEmpty) Option(x) else Option.empty

  private def getArtist(idOption: Option[Id]): Artist = {
    idOption match {
      case Some(id) =>
        dbCache
          .getCache
          .artists
          .find(x => x.id.equals(id)) match {
          case Some(dbArtist) =>
            extractArtist(dbArtist)
          case None =>
            Artist()
        }
      case None =>
        Artist()
    }
  }

  private def extractArtist(dbArtist: DbArtist): Artist = {
    Artist(
      id = dbArtist.id,
      name = dbArtist.name.getOrElse(""),
      instrument = getInstrument(dbArtist.instrumentId),
      note = dbArtist.note.getOrElse(""),
      missions = dbCache
        .getCache
        .artistMissions
        .filter(x => x.artistId.equals(dbArtist.id))
        .map(x => artistMissionFromString(x.missions.get))
    )
  }

  private def getInstrument(idOption: Option[Int]): Instrument = {
    idOption match {
      case Some(id) =>
        dbCache
          .getCache
          .instruments
          .find(x => x.id.equals(id)) match {
          case Some(dbInstrument) =>
            Instrument(
              id = dbInstrument.id,
              name = dbInstrument.name.getOrElse("")
            )
          case None =>
            Instrument()
        }
      case None =>
        Instrument()
    }
  }

  private def getSource(idOption: Option[Id]): Source = {
    idOption match {
      case Some(id) =>
        dbCache
          .getCache
          .sources
          .find(x => x.id.equals(id)) match {
          case Some(dbSource) =>
            Source(
              id = dbSource.id,
              sourceType = getSourceType(dbSource.typeId),
              signature = dbSource.signature.getOrElse("")
            )
          case None =>
            Source()
        }
      case None =>
        Source()
    }
  }

  private def getSourceType(idOption: Option[Int]): SourceType = {
    idOption match {
      case Some(id) =>
        dbCache
          .getCache
          .sourceTypes
          .find(x => x.id.equals(id)) match {
          case Some(dbSourceType) =>
            SourceT$(
              id = dbSourceType.id,
              name = dbSourceType.name.getOrElse("")
            )
          case None =>
            SourceT$()
        }
      case None =>
        SourceT$()
    }
  }

  private def getEthnographicRegion(idOption: Option[Id]): EthnographicRegion = {
    idOption match {
      case Some(id) =>
        dbCache
          .getCache
          .ethnographicRegions
          .find(x => x.id.equals(id)) match {
          case Some(dbEthnographicRegion) =>
            EthnographicRegion(
              id = dbEthnographicRegion.id,
              name = dbEthnographicRegion.name.getOrElse("")
            )
          case None =>
            EthnographicRegion()
        }
      case None =>
        EthnographicRegion()
    }
  }

  private def refreshCacheAndIndex(): Unit = {
    dbCache.refresh()
    SearchIndexesInstance.setInstance(SearchIndexes(getTracks))
  }

  private def generateId: String = UUID.randomUUID().toString
}
