package org.yankov.mso.application.ui.controls

import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.{Pane, VBox}

case class LabeledTextField(labelText: String,
                            value: String,
                            validator: Option[String => Boolean] = Option.empty,
                            readOnly: Boolean = false) {
  private val textField: TextField = new TextField {
    editable = !readOnly
    text = value
  }

  private val container = {
    val label = new Label {
      text = labelText
    }

    if (validator.isDefined) {
      textField.textProperty().addListener((observable, oldValue, newValue) => {
        if (validator.get.apply(newValue)) observable.asInstanceOf[StringProperty].setValue(newValue)
        else observable.asInstanceOf[StringProperty].setValue(oldValue)
      })
    }

    new VBox {
      children = List(
        label,
        textField
      )
    }
  }

  def getContainer: Pane = container

  def getValue: String = textField.getText

  def setValue(value: String): Unit = textField.setText(value)

  def setOnKeyReleased(f: KeyEvent => Unit): Unit = textField.setOnKeyReleased(f)
}
