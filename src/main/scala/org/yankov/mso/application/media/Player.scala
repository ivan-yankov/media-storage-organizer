package org.yankov.mso.application.media

import java.io.File

import org.yankov.mso.application.{Main, Resources}
import org.yankov.mso.application.model.DataModel.FolkloreTrack

object Player {
  private var player: FolkloreTrackMediaPlayer = _

  def play(track: FolkloreTrack, storageFileName: Int => File): Unit = {
    val tmpDir = Main.getApplicationArgument(Resources.ApplicationArguments.tmpDir, "./tmp")
    player = FolkloreTrackMediaPlayer(track, storageFileName, tmpDir)
    player.open()
  }

  def close(): Unit = if (player != null) player.close()
}
