package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.model.UiModel.Control
import scalafx.beans.property.StringProperty
import scalafx.scene.control.{Label, TextField}
import scalafx.scene.layout.{Pane, VBox}

case class LabeledTextField(labelText: String,
                            value: String,
                            validator: Option[String => Boolean] = Option.empty) extends Control[String] {
  private val textField = new TextField {
    editable = true
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

  override def getContainer: Pane = container

  override def getValue: String = textField.getText
}
