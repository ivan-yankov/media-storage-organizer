package org.yankov.mso.application.ui.controls

import java.io.File

import org.yankov.mso.application.ui.Utils
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{HBox, Pane, Priority, VBox}

case class FileSelector(labelText: String, file: Option[File]) {
  private val textField = new TextField {
    editable = false
    text = if (file.isDefined) file.get.getName else ""
  }

  private val container: Pane = {
    val label = new Label {
      text = labelText
    }

    HBox.setHgrow(textField, Priority.Always)

    val button = new Button {
      text = "..."
      onAction = _ => {
        val selection = Utils.selectFlacFiles(true)
        if (selection.isDefined) {
          textField.setUserData(selection.get.head)
          textField.setText(selection.get.head.getName)
        }
      }
    }

    val selectionContainer = new HBox {
      children = List(
        textField,
        button
      )
    }

    new VBox {
      children = List(
        label,
        selectionContainer
      )
    }
  }

  def getContainer: Pane = container

  def getValue: Option[File] = {
    if (textField.getUserData != null) Option(textField.getUserData.asInstanceOf[File])
    else Option.empty
  }
}
