package org.yankov.mso.application.media

import org.yankov.mso.application.Main.getApplicationArgument
import org.yankov.mso.application.Resources

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(data: Array[Byte], title: String): Unit = {
    val port = getApplicationArgument(
      Resources.ApplicationArgumentKeys.mediaServerPort,
      Resources.ApplicationArgumentValues.mediaServerPort,
      required = false
    )

    val url = s"http://localhost:$port/${Resources.Media.audioHttpApi}"

    AudioService.setAudioData(data)

    player = FolkloreTrackMediaPlayer(url, title)
    player.open()
  }

  def close(): Unit = if (player != null) player.close()
}
