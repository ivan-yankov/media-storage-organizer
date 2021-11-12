package org.yankov.mso.application.model

import java.io.File
import java.time.Duration

object DataModel {
  def isValidId(id: String): Boolean = id.nonEmpty

  def invalidId: String = ""

  object ArtistMissions {
    val singer: String = "singer"
    val instrumentPlayer: String = "instrumentPlayer"
    val composer: String = "composer"
    val conductor: String = "conductor"
    val orchestra: String = "orchestra"
    val choir: String = "choir"
    val ensemble: String = "ensemble"
    val chamberGroup: String = "chamberGroup"
  }

  case class Source(id: String = invalidId,
                    sourceType: String = "",
                    signature: String = "")

  case class Artist(id: String = invalidId,
                    name: String = "",
                    instrument: String = "",
                    note: String = "",
                    missions: List[String] = List())

  case class FolkloreTrack(id: String = invalidId,
                           title: String = "",
                           performer: Artist = Artist(),
                           accompanimentPerformer: Artist = Artist(),
                           author: Artist = Artist(),
                           arrangementAuthor: Artist = Artist(),
                           conductor: Artist = Artist(),
                           soloist: Artist = Artist(),
                           duration: Duration = Duration.ZERO,
                           note: String = "",
                           source: Source = Source(),
                           ethnographicRegion: String = "",
                           file: Option[File] = Option.empty) {

    def withId(newId: String): FolkloreTrack = FolkloreTrack(
      id = newId,
      title = title,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = file
    )

    def withTitle(newTitle: String): FolkloreTrack = FolkloreTrack(
      id = id,
      title = newTitle,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = file
    )

    def withFile(newFile: Option[File]): FolkloreTrack = FolkloreTrack(
      id = id,
      title = title,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = duration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = newFile
    )

    def withDuration(newDuration: Duration): FolkloreTrack = FolkloreTrack(
      id = id,
      title = title,
      performer = performer,
      accompanimentPerformer = accompanimentPerformer,
      author = author,
      arrangementAuthor = arrangementAuthor,
      conductor = conductor,
      soloist = soloist,
      duration = newDuration,
      note = note,
      source = source,
      ethnographicRegion = ethnographicRegion,
      file = file
    )
  }
}
