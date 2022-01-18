package org.yankov.mso.application.media

import org.yankov.mso.application._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.{Modality, Stage}

case class FolkloreTrackMediaPlayer(sources: Vector[String]) {
  private var currentSourceIndex: Int = 0

  private val buttonWidth = 75.0

  private def createMediaPlayer: MediaPlayer = {
    val mp = new MediaPlayer(new Media(sources(currentSourceIndex)))
    mp.setAutoPlay(true)
    mp.setVolume(1.0)
    mp.setOnEndOfMedia(() => if (currentSourceIndex < sources.size - 1) next())
    mp
  }

  private def mediaPlayer: javafx.scene.media.MediaPlayer = mediaView.getMediaPlayer

  private val mediaView = new MediaView(createMediaPlayer)

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

  private val btnNext = new Button {
    text = Resources.Media.next
    prefWidth = buttonWidth
    onAction = _ => next()
  }

  private val btnPrevious = new Button {
    text = Resources.Media.previous
    prefWidth = buttonWidth
    onAction = _ => previous()
  }

  private val buttons = new HBox {
    spacing = 20.0
    padding = Insets(25.0)
    children = List(
      btnPlayPause,
      btnNext,
      btnPrevious,
      btnStop,
      btnClose
    )
    alignment = Pos.Center
  }

  private val stage = {
    val st = new Stage {
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

  def open(): Unit = {
    stage.initModality(Modality.None)
    stage.show()
  }

  def close(): Unit = {
    stop()
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

  private def next(): Unit = {
    currentSourceIndex = nextIndex
    stop()
    mediaView.setMediaPlayer(createMediaPlayer)
  }

  private def nextIndex: Int = {
    if (currentSourceIndex == sources.size - 1) currentSourceIndex
    else currentSourceIndex + 1
  }

  private def previous(): Unit = {
    currentSourceIndex = previousIndex
    stop()
    mediaView.setMediaPlayer(createMediaPlayer)
  }

  private def previousIndex: Int = {
    if (currentSourceIndex == 0) currentSourceIndex
    else currentSourceIndex - 1
  }

  private def stop(): Unit = {
    mediaPlayer.stop()
    mediaPlayer.dispose()
  }
}
