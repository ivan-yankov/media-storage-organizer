package org.yankov.mso.application.model

import java.beans.{PropertyChangeListener, PropertyChangeSupport}
import java.io.File
import java.nio.file.{Files, Paths}
import java.sql.Connection

import org.slf4j.LoggerFactory
import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.database.SqlModel._
import org.yankov.mso.application.database._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._
import org.yankov.mso.application.model.SqlFunctions._

case class DataManager(dbConnectionString: String,
                       mediaDir: String,
                       dbCache: DatabaseCache = DatabaseCache(),
                       sqlInsert: SqlInsert = DatabaseManager.insert,
                       sqlUpdate: SqlUpdate = DatabaseManager.update,
                       sqlDelete: SqlDelete = DatabaseManager.delete,
                       readRecord: File => Array[Byte] = x => Files.readAllBytes(x.toPath),
                       writeRecord: (File, Array[Byte]) => Unit = (x, y) => Files.write(x.toPath, y),
                       deleteRecord: File => Unit = x => Files.deleteIfExists(x.toPath)) {
  dbCache.refresh()

  private val log = LoggerFactory.getLogger(getClass)
  private val dataModelChangeSupport = new PropertyChangeSupport(this)
  private val equal = "="

  def addPropertyChangeListener(listener: PropertyChangeListener): Unit =
    dataModelChangeSupport.addPropertyChangeListener(listener)

  def insertTracks(tracks: List[FolkloreTrack],
                   onTrackComplete: (FolkloreTrack, Boolean) => Unit): Boolean = {
    def insertTrack(track: FolkloreTrack): Boolean = {
      val trackId = dbCache.getNextTrackId
      connect match {
        case Some(connection) =>
          sqlInsert(
            connection,
            schema,
            Tables.folkloreTrack,
            Tables.folkloreTrackColumns,
            List(
              IntSqlValue(Option(trackId)),
              VarcharSqlValue(Option(DurationConverter.toHourMinSecString(track.duration))),
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
            )
          ) match {
            case Left(throwable) =>
              log.error("Unable to insert track", throwable)
              disconnect(connection)
              false
            case Right(_) =>
              if (track.file.isDefined) writeRecord(storageFileName(trackId), readRecord(track.file.get))
              disconnect(connection)
              true
          }
        case None =>
          false
      }
    }

    val result = tracks
      .map(x => {
        val r = insertTrack(x)
        onTrackComplete(x, r)
        r
      })
      .forall(x => x)

    dbCache.refresh()

    result
  }

  def updateTracks(tracks: List[FolkloreTrack]): Boolean = {
    //    x => x.track.file.isDefined && x.track.hasValidId,
    //    x => DataModelOperations.setRecord(x.track.id, Files.readAllBytes(x.track.file.get.toPath))
    ???
  }

  def getTracks: List[FolkloreTrack] = ???

  def deleteTrack(track: FolkloreTrack): Boolean = {
    connect match {
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
            dbCache.refresh()
            disconnect(connection)
            true
        }
      case None =>
        false
    }
  }

  def getSourceTypes: List[SourceType] = ???

  def insertEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def updateEthnographicRegion(ethnographicRegion: EthnographicRegion): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def getEthnographicRegions: List[EthnographicRegion] = ???

  def insertSource(source: Source): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def updateSource(source: Source): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def getSources: List[Source] = ???

  def insertInstrument(instrument: Instrument): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def updateInstrument(instrument: Instrument): Boolean = {
    connect match {
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
            dbCache.refresh()
            true
        }
      case None =>
        false
    }
  }

  def getInstruments: List[Instrument] = ???

  def insertArtist(artist: Artist): Boolean = {
    connect match {
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
            dbCache.refresh()
            result
        }
      case None =>
        false
    }
  }

  def updateArtist(artist: Artist): Boolean = {
    connect match {
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
            dbCache.refresh()
            result
        }
      case None =>
        false
    }
  }

  def getArtists: List[Artist] = ???

  def getRecord(id: Int): Array[Byte] = ???

  private def connect: Option[Connection] = {
    DatabaseConnection.connect(dbConnectionString) match {
      case Left(throwable) =>
        log.error("Unable to connect database", throwable)
        Option.empty
      case Right(connection) =>
        Option(connection)
    }
  }

  private def disconnect(connection: Connection): Unit = DatabaseConnection.close(connection)

  private def asStringOption(x: String): Option[String] = if (x.nonEmpty) Option(x) else Option.empty

  private def storageFileName(trackId: Int): File =
    Paths.get(mediaDir, trackId + Resources.Media.flacExtension).toFile
}
