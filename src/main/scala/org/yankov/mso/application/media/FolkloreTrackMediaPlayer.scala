package org.yankov.mso.application.media

import org.yankov.mso.application._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.{Modality, Stage}

case class FolkloreTrackMediaPlayer(source: String, trackTitle: String) {
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
    cancelButton = true
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
      title = trackTitle
      scene = new Scene {
        root = new VBox {
          children.add(mediaView)
          children.add(buttons)
        }
      }
      onCloseRequest = _ => mediaPlayer.stop()
    }

    st.initOwner(Main.stage)
    st.initModality(Modality.ApplicationModal)

    st
  }

  def open(): Unit = stage.showAndWait()

  def close(): Unit = {
    mediaPlayer.stop()
    stage.close()
  }

  private def handlePlayPause(): Unit = {
    if (mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.STOPPED) || mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.PAUSED)) {
      mediaPlayer.play()
    }
    else if (mediaPlayer.getStatus.equals(javafx.scene.media.MediaPlayer.Status.PLAYING)) {
      mediaPlayer.pause()
    }
  }
}
