package org.yankov.mso.application.media

import java.io.File
import java.nio.file.{Files, Paths}

import org.yankov.mso.application._
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.{Modality, Stage}

case class FolkloreTrackMediaPlayer(track: FolkloreTrack, storageFileName: Int => File, tmpDir: String) {
  Files.createDirectories(Paths.get(tmpDir))

  private val tmpFile = Paths.get(tmpDir, "play.wav")

  private val buttonWidth = 250.0

  private val media = new Media(source)

  private val mediaPlayer = new MediaPlayer(media)
  mediaPlayer.setAutoPlay(true)
  mediaPlayer.setVolume(1.0)

  private val mediaView = new MediaView(mediaPlayer)

  private val btnPlayPause = new Button {
    text = Resources.Media.playPause
    prefWidth = buttonWidth
    onAction = _ => handlePlayPause()
  }

  private val btnStop = new Button {
    text = Resources.Media.stop
    prefWidth = buttonWidth
    onAction = _ => mediaPlayer.stop()
  }

  private val btnClose = new Button {
    text = Resources.Media.close
    prefWidth = buttonWidth
    onAction = _ => close()
  }

  private val buttons = new HBox {
    spacing = 20.0
    padding = Insets(25.0)
    children = List(
      btnPlayPause,
      btnStop,
      btnClose
    )
    alignment = Pos.Center
  }

  private val stage = {
    val st = new Stage {
      title = track.title
      scene = new Scene {
        root = new VBox {
          children.add(mediaView)
          children.add(buttons)
        }
      }
      onCloseRequest = _ => {
        mediaPlayer.stop()
        clearCache()
      }
    }

    st.initOwner(Main.stage)
    st.initModality(Modality.ApplicationModal)

    st
  }

  def open(): Unit = stage.showAndWait()

  def close(): Unit = {
    mediaPlayer.stop()
    clearCache()
    stage.close()
  }

  private def source: String = {
    clearCache()
    if (track.file.isDefined) FlacDecoder.decode(track.file.get.toPath, tmpFile)
    else if (isValidId(track.id)) FlacDecoder.decode(storageFileName(track.id).toPath, tmpFile)
    tmpFile.toUri.toString
  }

  private def handlePlayPause(): Unit = {
    if (mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.STOPPED) || mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.PAUSED)) {
      mediaPlayer.play()
    }
    else if (mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.PLAYING)) {
      mediaPlayer.pause()
    }
  }

  private def clearCache(): Unit = Files.deleteIfExists(tmpFile)
}
