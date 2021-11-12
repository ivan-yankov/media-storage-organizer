package org.yankov.mso.application.model

import java.sql.Connection
import org.slf4j.LoggerFactory
import org.yankov.mso.application.database.DatabaseManager
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._

case class Cache(artists: List[Artist] = List(),
                 sources: List[Source] = List(),
                 folkloreTracks: List[FolkloreTrack] = List())

case class DatabaseCache(dbRootDir: String) {
  private var cache: Cache = _

  private val log = LoggerFactory.getLogger(getClass)

  def getCache: Cache = cache

  def refresh(): Unit = cache = loadCache

  private def loadCache: Cache = {
    connect(dbConnectionString) match {
      case Some(connection) =>
        val artists = getCollection(
          connection,
          Tables.artist,
          Tables.artistColumns,
          x => DbArtist(
            id = x.head.asIntOption.get,
            name = x(1).asStringOption,
            note = x(2).asStringOption,
            instrumentId = x(3).asIntOption
          )
        )
        val artistMissions = getCollection(
          connection,
          Tables.artistMissions,
          Tables.artistMissionsColumns,
          x => DbArtistMissions(
            artistId = x.head.asIntOption.get,
            missions = x(1).asStringOption
          )
        )
        val ethnographicRegions = getCollection(
          connection,
          Tables.ethnographicRegion,
          Tables.ethnographicRegionColumns,
          x => DbEthnographicRegion(
            id = x.head.asIntOption.get,
            name = x(1).asStringOption
          )
        )
        val instruments = getCollection(
          connection,
          Tables.instrument,
          Tables.instrumentColumns,
          x => DbInstrument(
            id = x.head.asIntOption.get,
            name = x(1).asStringOption
          )
        )
        val sourceTypes = getCollection(
          connection,
          Tables.sourceType,
          Tables.sourceTypeColumns,
          x => DbSourceType(
            id = x.head.asIntOption.get,
            name = x(1).asStringOption
          )
        )
        val sources = getCollection(
          connection,
          Tables.source,
          Tables.sourceColumns,
          x => DbSource(
            id = x.head.asIntOption.get,
            signature = x(1).asStringOption,
            typeId = x(2).asIntOption
          )
        )
        val folkloreTracks = getCollection(
          connection,
          Tables.folkloreTrack,
          Tables.folkloreTrackColumns,
          x => DbFolkloreTrack(
            id = x.head.asIntOption.get,
            duration = x(1).asStringOption,
            note = x(2).asStringOption,
            title = x(3).asStringOption,
            accompanimentPerformerId = x(4).asIntOption,
            arrangementAuthorId = x(5).asIntOption,
            authorId = x(6).asIntOption,
            conductorId = x(7).asIntOption,
            performerId = x(8).asIntOption,
            soloistId = x(9).asIntOption,
            sourceId = x(10).asIntOption,
            ethnographicRegionId = x(11).asIntOption,
          )
        )
        Cache(
          artists = artists,
          artistMissions = artistMissions,
          ethnographicRegions = ethnographicRegions,
          folkloreTracks = folkloreTracks,
          instruments = instruments,
          sources = sources,
          sourceTypes = sourceTypes
        )
      case None =>
        log.error("Unable to create database cache")
        Cache()
    }
  }

  private def getCollection[T](connection: Connection, table: String, columns: List[String], extract: List[SqlValue] => T): List[T] = {
    sqlSelect(
      connection,
      schema,
      table,
      columns,
      List()
    ) match {
      case Left(throwable) =>
        log.error(s"Unable to get database table data for [$table]", throwable)
        List()
      case Right(values) =>
        values.map(x => extract(x))
    }
  }
}
