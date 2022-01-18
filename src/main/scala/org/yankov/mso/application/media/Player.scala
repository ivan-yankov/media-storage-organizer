package org.yankov.mso.application.media

import org.yankov.mso.application.Main.getApplicationArgument
import org.yankov.mso.application.{Id, Resources}

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(data: List[(Id, Array[Byte])]): Unit = {
    val port = getApplicationArgument(
      Resources.ApplicationArgumentKeys.mediaServerPort,
      Resources.ApplicationArgumentValues.mediaServerPort,
      required = false
    )

    val urls = data.map(x => s"http://localhost:$port/${Resources.Media.audioHttpApi}/${x._1}").toVector

    AudioService.setAudioData(data.toMap)

    player = FolkloreTrackMediaPlayer(urls)
    player.open()
  }

  def close(): Unit = if (player != null) player.close()
}
