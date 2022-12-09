package yankov.mso.application

import yankov.args.annotations.{ProgramArgument, ProgramFlag, ProgramOption}

class AppArguments {
  @ProgramArgument(order = 0, defaultValue = "")
  private var dbDir: String = _

  @ProgramFlag(shortName = "", longName = "build-audio-index")
  private var buildAudioIndex: Boolean = _

  @ProgramOption(shortName = "", longName = "media-server-port", defaultValue = "5432")
  private var mediaServerPort: String = _

  def getDbDir: String = dbDir

  def isBuildAudioIndex: Boolean = buildAudioIndex

  def getMediaServerPort: String = mediaServerPort
}
