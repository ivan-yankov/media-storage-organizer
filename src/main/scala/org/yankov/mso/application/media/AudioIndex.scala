package org.yankov.mso.application.media

import org.yankov.mso.application.{Id, Resources}
import org.yankov.mso.application.ui.console.ApplicationConsole

import java.nio.file.{Path, Paths}

case class AudioIndex(dataPath: Path) {
  private val console = ApplicationConsole

  val audioIndexPath: Path = Paths.get(dataPath.toString, "audio-index")

  def buildIfNotExists(): Unit = {

  }

  def add(id: Id): Boolean = {
    console.writeMessageWithTimestamp(Resources.ConsoleMessages.refreshAudioIndex)
    ???
  }

  def remove(id: Id): Boolean = {
    console.writeMessageWithTimestamp(Resources.ConsoleMessages.refreshAudioIndex)
    ???
  }
}
