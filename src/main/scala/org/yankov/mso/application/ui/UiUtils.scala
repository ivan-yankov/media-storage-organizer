package org.yankov.mso.application.ui

import java.io.File
import java.time.Duration

import org.jaudiotagger.audio.AudioFileIO
import org.slf4j.LoggerFactory
import org.yankov.mso.application.{Main, Resources}
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.stage.{DirectoryChooser, FileChooser}

import scala.collection.JavaConverters._

object UiUtils {
  private val log = LoggerFactory.getLogger(getClass)

  def confirmCloseApplication: Boolean = confirmDialog(Resources.Dialogs.closeApplication)

  def confirmOverwrite: Boolean = confirmDialog(Resources.Dialogs.overwriteRecordsInDatabase)

  def confirmDeleteFromDatabase: Boolean = confirmDialog(Resources.Dialogs.deleteTrackFromDatabase)

  def selectDirectory: Option[File] = {
    val directoryChooser = new DirectoryChooser()
    directoryChooser.setTitle(Resources.Dialogs.selectExportDirectory)
    val file = directoryChooser.showDialog(Main.stage.scene.value.getWindow)
    if (file != null) Option(file) else Option.empty
  }

  def selectFlacFiles(singleSelection: Boolean): Option[List[File]] = {
    val fileChooser = new FileChooser()
    fileChooser.setTitle(Resources.Dialogs.selectAudioFiles)
    fileChooser.getExtensionFilters.add(
      new FileChooser.ExtensionFilter(Resources.Dialogs.flacFilterName, Resources.Dialogs.flacFilterExtension)
    )
    if (singleSelection) {
      val file = fileChooser.showOpenDialog(Main.stage.getScene.getWindow)
      if (file != null) Option(List(file))
      else Option.empty
    }
    else {
      val files = fileChooser.showOpenMultipleDialog(Main.stage.getScene.getWindow)
      if (files != null) Option(files.asScala.toList) else Option.empty
    }
  }

  def calculateDuration(fileOption: Option[File]): Duration = {
    fileOption match {
      case Some(file) =>
        try {
          val audioFile = AudioFileIO.read(file)
          val header = audioFile.getAudioHeader
          val lengthInSec = header.getTrackLength
          Duration.ofSeconds(lengthInSec)
        } catch {
          case e: Exception =>
            log.error("Unable to calculate audio file duration.", e)
            Duration.ZERO
        }
      case None =>
        Duration.ZERO
    }
  }

  private def confirmDialog(headerText: String): Boolean = {
    val alert = new Alert(Alert.AlertType.Confirmation)
    alert.setTitle(Resources.Dialogs.confirmation)
    alert.setHeaderText(headerText)
    alert.setContentText(Resources.Dialogs.areYouSure)

    val yes = new ButtonType(Resources.Dialogs.yes)
    val no = new ButtonType(Resources.Dialogs.no)

    alert.getButtonTypes.setAll(yes, no)
    val answer = alert.showAndWait
    answer.isDefined && answer.get.equals(yes)
  }
}
