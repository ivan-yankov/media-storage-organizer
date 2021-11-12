package org.yankov.mso.application.model

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.slf4j.LoggerFactory
import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.DatabaseManager
import org.yankov.mso.application.database.DatabaseManager._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.search.{SearchIndexes, SearchIndexesInstance}

import java.io.File
import java.nio.file.{Files, Path, Paths}
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

  implicit class FolkloreTrackAsDbEntry(track: FolkloreTrack) {
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
      ethnographicRegionId = asStringOption(track.ethnographicRegion)
    )
  }

  def insertTracks(tracks: List[FolkloreTrack], onTrackInserted: (FolkloreTrack, Boolean) => Unit): Boolean = {
    def insertTrack(track: FolkloreTrack): Boolean = {
      val trackId = generateId
      insert(dbRootDir, track.withId(trackId).asDbEntry) match {
        case Left(error) =>
          log.error(error)
          false
        case Right(_) =>
          if (track.file.isDefined) writeRecord(storageFileName(trackId), readRecord(track.file.get))
          refreshCacheAndIndex()
          true
      }
    }

    tracks.map(
      x => {
        val r = insertTrack(x)
        onTrackInserted(x, r)
        r
      }
    ).forall(x => x)
  }

  def updateTracks(tracks: List[FolkloreTrack], onTrackUpdated: (FolkloreTrack, Boolean) => Unit): Boolean = {
    def updateTrack(track: FolkloreTrack): Boolean = {
      connect(dbRootDir) match {
        case Some(connection) =>
          sqlUpdate(
            connection,
            schema,
            Tables.folkloreTrack,
            Tables.folkloreTrackColumns.filter(x => !x.equals(id)),
            List(
              VarcharSqlValue(Option(DurationConverter.toHourMinSecString(track.duration, withLeadingZero = true))),
              VarcharSqlValue(asStringOption(track.note)),
              VarcharSqlValue(asStringOption(track.title)),
              IntSqlValue(asIdOption(track.accompanimentPerformer.id)),
              IntSqlValue(asIdOption(track.arrangementAuthor.id)),
              IntSqlValue(asIdOption(track.author.id)),
              IntSqlValue(asIdOption(track.conductor.id)),
              IntSqlValue(asIdOption(track.performer.id)),
              IntSqlValue(asIdOption(track.soloist.id)),
              IntSqlValue(asIdOption(track.source.id)),
              IntSqlValue(asIdOption(track.ethnographicRegion.id))
            ),
            List(WhereClause(id, equal, IntSqlValue(asIdOption(track.id))))
          ) match {
            case Left(throwable) =>
              log.error("Unable to update track", throwable)
              disconnect(connection)
              false
            case Right(_) =>
              if (track.file.isDefined) {
                deleteRecord(storageFileName(track.id))
                writeRecord(storageFileName(track.id), readRecord(track.file.get))
              }
              refreshCacheAndIndex()
              disconnect(connection)
              true
          }
        case None =>
          false
      }
    }

    val result = tracks
      .map(x => {
        val r = updateTrack(x)
        onTrackUpdated(x, r)
        r
      })
      .forall(x => x)

    result
  }

  def getTracks: List[FolkloreTrack] = {
    dbCache
      .getCache
      .folkloreTracks
      .map(x => FolkloreTrack(
        id = x.id,
        title = x.title.getOrElse(""),
        performer = getArtist(x.performerId),
        accompanimentPerformer = getArtist(x.accompanimentPerformerId),
        author = getArtist(x.authorId),
        arrangementAuthor = getArtist(x.arrangementAuthorId),
        conductor = getArtist(x.conductorId),
        soloist = getArtist(x.soloistId),
        duration = DurationConverter.fromString(x.duration.getOrElse("")),
        note = x.note.getOrElse(""),
        source = getSource(x.sourceId),
        ethnographicRegion = getEthnographicRegion(x.ethnographicRegionId)
      ))
  }

  def deleteTrack(track: FolkloreTrack): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlDelete(
          connection,
          schema,
          Tables.folkloreTrack,
          List(WhereClause(id, equal, IntSqlValue(Option(track.id))))
        ) match {
          case Left(throwable) =>
            log.error("Unable to delete track", throwable)
            disconnect(connection)
            false
          case Right(_) =>
            deleteRecord(storageFileName(track.id))
            refreshCacheAndIndex()
            disconnect(connection)
            true
        }
      case None =>
        false
    }
  }

  def getSourceTypes: List[SourceType] = {
    dbCache
      .getCache
      .sourceTypes
      .map(x => SourceT$(id = x.id, name = x.name.getOrElse("")))
  }

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlInsert(
          connection,
          schema,
          Tables.ethnographicRegion,
          Tables.ethnographicRegionColumns,
          List(
            IntSqlValue(Option(dbCache.getNextEthnographicRegionId)),
            VarcharSqlValue(asStringOption(ethnographicRegion.name))
          )
        ) match {
          case Left(throwable) =>
            log.error("Unable to insert ethnographic region", throwable)
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

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    connect(dbRootDir) match {
      case Some(connection) =>
        sqlUpdate(
          connection,
          schema,
          Tables.ethnographicRegion,
          Tables.ethnographicRegionColumns.filter(x => !x.equals(id)),
          List(VarcharSqlValue(asStringOption(ethnographicRegion.name))),
          List(WhereClause(id, equal, IntSqlValue(asIdOption(ethnographicRegion.id))))
        ) match {
          case Left(throwable) =>
            log.error("Unable to update ethnographic region", throwable)
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

  def getEthnographicRegions: List[EthnographicRegion] = {
    dbCache
      .getCache
      .ethnographicRegions
      .map(x => EthnographicRegion(id = x.id, name = x.name.getOrElse("")))
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

  private def getArtist(idOption: Option[Int]): Artist = {
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

  private def getSource(idOption: Option[Int]): Source = {
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

  private def getEthnographicRegion(idOption: Option[Int]): EthnographicRegion = {
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
