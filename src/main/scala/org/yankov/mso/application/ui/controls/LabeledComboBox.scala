package org.yankov.mso.application.ui.controls

import javafx.beans.value.{ChangeListener, ObservableValue}
import org.yankov.mso.application.ui.UiUtils
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.util.StringConverter

import scala.collection.JavaConverters._

case class LabeledComboBox[T](labelText: String,
                              cbItems: List[T],
                              value: T,
                              itemToString: T => String,
                              sortItems: Boolean = true,
                              emptyValue: Option[T]) {
  private val comboBox = new ComboBox[T] {
    editable = false
    converter = new StringConverter[T] {
      override def fromString(s: String): T = ???

      override def toString(x: T): String = itemToString(x)
    }
    prefWidth = 250.0
    getItems.foreach(x => items.getValue.add(x))
  }

  private val label = new Label {
    text = labelText
  }

  InputTextHandler(
    parent = comboBox,
    onInput = x => UiUtils.filterItems[T](
      comboBox.items.getValue.asScala.toList,
      y => itemToString(y),
      x,
      y => comboBox.getSelectionModel.select(y)
    ),
    onDelete = Some(() => clear())
  )

  private val container: Pane = new VBox {
    children = List(
      label,
      comboBox
    )
  }

  init()

  def getContainer: Pane = container

  def getValue: T = comboBox.getValue

  def setValue(value: T): Unit = comboBox.setValue(value)

  def clear(): Unit = if (emptyValue.isDefined) setValue(emptyValue.get)

  def setDisable(flag: Boolean): Unit = comboBox.setDisable(flag)

  def setOnSelect(f: T => Unit): Unit = {
    val listener = new ChangeListener[T] {
      override def changed(observableValue: ObservableValue[_ <: T], oldValue: T, newValue: T): Unit = f(newValue)
    }
    comboBox.valueProperty().addListener(listener)
  }

  private def init(): Unit = setValue(value)

  private def compareItems(x: T, y: T): Boolean = itemToString(x).compareToIgnoreCase(itemToString(y)) < 0

  private def getItems: List[T] = if (sortItems) cbItems.sortWith((x, y) => compareItems(x, y)) else cbItems
}
