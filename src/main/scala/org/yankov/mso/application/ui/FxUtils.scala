package org.yankov.mso.application.ui

import java.io.File

import org.yankov.mso.application.{ApplicationContext, MediaStorageOrganizer}
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.stage.DirectoryChooser

object FxUtils {
  def confirmCloseApplication: Boolean = confirmDialog(Resources.Dialogs.closeApplication)

  def confirmOverwrite: Boolean = confirmDialog(Resources.Dialogs.overwriteRecordsInDatabase)

  def selectDirectory: Option[File] = {
    val directoryChooser = new DirectoryChooser()
    directoryChooser.setTitle(Resources.Dialogs.selectExportDirectory)
    val file = directoryChooser.showDialog(MediaStorageOrganizer.stage.scene.value.getWindow)
    if (file != null) Option(file) else Option.empty
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
