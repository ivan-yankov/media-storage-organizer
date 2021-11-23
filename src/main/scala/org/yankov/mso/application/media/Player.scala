package org.yankov.mso.application.media

import org.yankov.mso.application.Main.getApplicationArgument
import org.yankov.mso.application.{Id, Resources}
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel.{FolkloreTrack, isValidId}

import java.io.File
import java.nio.file.Files

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(track: FolkloreTrack, storageFileName: Id => File): Unit = {
    val port = getApplicationArgument(
      Resources.ApplicationArgumentKeys.mediaServerPort,
      Resources.ApplicationArgumentValues.mediaServerPort,
      required = false
    )

    val url = s"http://localhost:$port/${Resources.Media.audioHttpApi}"

    val data: Option[Array[Byte]] = {
      if (track.file.isDefined) FlacDecoder.decode(Files.readAllBytes(track.file.get.toPath))
      else if (isValidId(track.id)) FlacDecoder.decode(Files.readAllBytes(storageFileName(track.id).toPath))
      else Option.empty
    }

    AudioService.setAudioData(data.getOrElse(Array()))

    player = FolkloreTrackMediaPlayer(url, track.title)
    player.open()
  }

  def close(): Unit = if (player != null) player.close()
}
