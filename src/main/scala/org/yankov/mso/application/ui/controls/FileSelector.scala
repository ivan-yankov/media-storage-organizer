package org.yankov.mso.application.ui.controls

import java.io.File

import org.yankov.mso.application.ui.Utils
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{HBox, Pane, Priority, VBox}

case class FileSelector(labelText: String) {
  private var file: Option[File] = Option.empty

  private val textField = new TextField {
    editable = false
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
          file = {
            if (selection.isDefined && selection.nonEmpty) Option(selection.get.head)
            else Option.empty
          }
          setText()
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

  def setFile(file: Option[File]): Unit = this.file = file

  def getFile: Option[File] = file

  private def setText(): Unit = textField.setText(if (file.isDefined) file.get.getName else "")
}
