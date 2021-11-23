package org.yankov.mso.application.database

import org.slf4j.LoggerFactory
import org.yankov.mso.application.Id
import org.yankov.mso.application.Main.dataManager._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DatabaseModel._

case class DatabaseCache(database: Database) {
  private val log = LoggerFactory.getLogger(getClass)

  lazy val artists: Map[Id, Artist] = {
    database.read[DbArtist](List(), artistsPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(x => (x.id, x.asArtist)).toMap
    }
  }

  lazy val sourceTypes: Map[Id, SourceType] = {
    database.read[DbSourceType](List(), sourceTypesPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(x => (x.id, SourceType(id = x.id, name = x.name.getOrElse("")))).toMap
    }
  }

  lazy val ethnographicRegions: Map[Id, EthnographicRegion] = {
    database.read[DbEthnographicRegion](List(), ethnographicRegionsPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(x => (x.id, EthnographicRegion(id = x.id, name = x.name.getOrElse("")))).toMap
    }
  }

  lazy val sources: Map[Id, Source] = {
    database.read[DbSource](List(), sourcesPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(
          x => (
            x.id,
            Source(
              id = x.id,
              sourceType = sourceTypes.getByOptionOrElse(x.typeId, SourceType()),
              signature = x.signature.getOrElse("")
            )
          )
        ).toMap
    }
  }

  lazy val instruments: Map[Id, Instrument] = {
    database.read[DbInstrument](List(), instrumentsPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(x => (x.id, Instrument(id = x.id, name = x.name.getOrElse("")))).toMap
    }
  }

  lazy val tracks: Map[Id, FolkloreTrack] = {
    database.read[DbFolkloreTrack](List(), tracksPath) match {
      case Left(e) =>
        log.error(e)
        Map()
      case Right(result) =>
        result.map(x => (x.id, x.asFolkloreTrack)).toMap
    }
  }
}
