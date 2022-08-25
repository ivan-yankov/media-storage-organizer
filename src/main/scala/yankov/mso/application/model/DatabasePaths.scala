package yankov.mso.application.model

import yankov.mso.application.{Id, Resources}

import java.io.File
import java.nio.file.{Path, Paths}

case class DatabasePaths(dbDir: Path) {
  val data: Path = Paths.get(dbDir.toString, "data")
  val media: Path = Paths.get(dbDir.toString, "media")
  val artists: Path = Paths.get(data.toString, "artists")
  val instruments: Path = Paths.get(data.toString, "instruments")
  val sourceTypes: Path = Paths.get(data.toString, "source-types")
  val sources: Path = Paths.get(data.toString, "sources")
  val ethnographicRegions: Path = Paths.get(data.toString, "ethnographic-regions")
  val tracks: Path = Paths.get(data.toString, "tracks")
  val audioIndex: Path = Paths.get(data.toString, "audio-index")

  def mediaFile(trackId: Id): File = Paths.get(media.toString, trackId + Resources.Media.flacExtension).toFile
}
