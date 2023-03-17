package yankov.mso.application

import yankov.args.annotations.{ProgramFlag, ProgramOption}

import java.nio.file.{Path, Paths}

class ProgramArguments {
  @ProgramFlag(shortName = "", longName = "build-audio-index")
  private var buildAudioIndex: Boolean = _

  @ProgramOption(shortName = "", longName = "db-dir", defaultValue = ".")
  private var dbDir: String = _

  @ProgramOption(shortName = "", longName = "media-server-port", defaultValue = "5432")
  private var mediaServerPort: String = _

  def getDbDir: Path = {
    if (dbDir.equals(".")) Paths.get(System.getenv("APPIMAGE")).getParent.toAbsolutePath
    else Paths.get(dbDir).toAbsolutePath
  }

  def isBuildAudioIndex: Boolean = buildAudioIndex

  def getMediaServerPort: Int = mediaServerPort.toInt
}
