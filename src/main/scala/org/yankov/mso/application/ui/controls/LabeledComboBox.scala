package org.yankov.mso.application.ui.controls

import scalafx.scene.control._
import javafx.scene.input.{KeyCode, KeyEvent}
import org.yankov.mso.application.model.UiModel.Control
import scalafx.scene.layout._
import scalafx.stage.Popup
import scalafx.util.StringConverter


import scala.collection.JavaConverters._

case class LabeledComboBox[T](labelText: String,
                              cbItems: List[T],
                              value: T,
                              itemToString: T => String,
                              isEditable: Boolean = false,
                              sortItems: Boolean = true,
                              nullable: Boolean = true,
                              emptyValue: Option[T]) extends Control[T] {
  private val comboBox = {
    val cb = new ComboBox[T] {
      editable = isEditable
      converter = new StringConverter[T] {
        override def fromString(s: String): T = ???

        override def toString(x: T): String = itemToString(x)
      }
      prefWidth = 250.0
      onKeyTyped = x => handleKeyTyped(x)
      onKeyReleased = x => handleKeyReleased(x)
      getItems.foreach(x => items.getValue.add(x))
    }

    cb.focusedProperty().addListener((_, _, newValue) => handleFocusChanged(newValue))

    setValue(value)

    cb
  }

  private val label = new Label {
    text = labelText
  }

  private val filterTextField = new TextField {
    disable = true
  }

  private val popup = new Popup {
    content.add(filterTextField)
  }

  private val filterText = new StringBuilder()

  private val container: Pane = new VBox {
    children = List(
      label,
      comboBox
    )
  }

  override def getContainer: Pane = container

  override def getValue: T = comboBox.getValue

  private def setValue(value: T): Unit = comboBox.setValue(value)

  private def handleFocusChanged(focused: Boolean): Unit = if (!focused) closePopup()

  private def showPopup(): Unit = {
    val bounds = comboBox.localToScreen(comboBox.getBoundsInLocal)
    val x = bounds.getMinX
    val y = bounds.getMinY - comboBox.getHeight - label.getHeight
    popup.show(comboBox, x, y)
  }

  private def closePopup(): Unit = {
    filterText.setLength(0)
    popup.hide()
  }

  private def compareItems(x: T, y: T): Boolean =
    itemToString(x).compareToIgnoreCase(itemToString(y)) < 0

  private def getItems: List[T] = if (sortItems) cbItems.sortWith((x, y) => compareItems(x, y)) else cbItems

  private def filterItems(): Unit = {
    val search = filterText.toString().toLowerCase()
    val filteredItems = {
      val startsWith = comboBox
        .items
        .getValue
        .asScala
        .toList
        .filter(x => itemToString(x).toLowerCase.startsWith(search))

      if (startsWith.nonEmpty) startsWith
      else {
        comboBox
          .items
          .getValue
          .asScala
          .toList
          .filter(x => itemToString(x).toLowerCase.contains(search))
      }
    }

    if (filteredItems.nonEmpty) {
      comboBox.getSelectionModel.select(filteredItems.head)
    }
  }

  private def handleKeyTyped(event: KeyEvent): Unit = {
    if (event.getCharacter.toLowerCase.trim.nonEmpty) {
      filterText.append(event.getCharacter.toLowerCase)
      filterTextField.setText(filterText.toString)
      showPopup()
      filterItems()
    }
  }

  private def handleKeyReleased(event: KeyEvent): Unit = {
    if (event.getCode.equals(KeyCode.BACK_SPACE) && filterText.length() > 0) {
      filterText.deleteCharAt(filterText.length() - 1)
      filterTextField.setText(filterText.toString)
      filterItems()
      showPopup()
    }
    else if (event.getCode.equals(KeyCode.DELETE) && nullable && emptyValue.isDefined) {
      setValue(emptyValue.get)
    }
  }
}