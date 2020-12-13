package org.yankov.mso.application.ui

import java.io.File

import org.yankov.mso.application.{Main, Resources}
import scalafx.scene.control.{Alert, ButtonType, TableView}
import scalafx.stage.{DirectoryChooser, FileChooser}

import collection.JavaConverters._

object FxUtils {
  def confirmCloseApplication: Boolean = confirmDialog(Resources.Dialogs.closeApplication)

  def confirmOverwrite: Boolean = confirmDialog(Resources.Dialogs.overwriteRecordsInDatabase)

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
      val files = fileChooser.showOpenMultipleDialog(Main.stage.getScene.getWindow).asScala.toList
      if (files != null) Option(files) else Option.empty
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
