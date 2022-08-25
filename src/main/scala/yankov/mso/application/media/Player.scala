package yankov.mso.application.media

import yankov.mso.application.Main.getApplicationArgument
import yankov.mso.application.Resources

import java.io.File

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(files: List[File]): Unit = {
    val port = getApplicationArgument(
      Resources.ApplicationArgumentKeys.mediaServerPort,
      Resources.ApplicationArgumentValues.mediaServerPort,
      required = false
    )

    val urls = files.zipWithIndex.map(x => s"http://localhost:$port/${Resources.Media.audioHttpApi}/${x._2}").toVector

    AudioService.setAudioData(files.zipWithIndex.map(x => x._2 -> x._1).toMap)

    player = FolkloreTrackMediaPlayer(urls, AudioService.clearAudioData)
    player.start()
  }
}
