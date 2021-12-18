package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.ui.controls.FontModel._
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{SelectionMode, TableColumn, TableRow, TableView}
import scalafx.scene.layout.{Pane, StackPane}

abstract class UiTable[T] {
  private val table: TableView[T] = {
    val t = new TableView[T]() {
      editable = isEditable
      selectionModel.value.setSelectionMode(selectionMode)
      selectionModel.value.setCellSelectionEnabled(cellSelectionEnabled)

      createTableColumns.foreach(x => columns.add(x))
      columns.foreach(x => x.setStyle(tableFont.cssRepresentation))

      items = new ObservableBuffer[T]()

      rowFactory = _ => new TableRow[T] {
        onMouseClicked = event => if (event.getClickCount == 2) onRowDoubleClick()
      }

      userData = tableUserData
    }

    t.columns.zip(columnWidths).foreach(x => x._1.setPrefWidth(x._2))

    t
  }

  private val container: StackPane = new StackPane {
    children = List(table)
  }

  def tableFont: CustomFont = CustomFont(SansSerif, NormalStyle, NormalWeight, 12)

  def getContainer: Pane = container

  def pure: TableView[T] = table

  def stringTableColumn(textValue: String, valueFactory: T => StringProperty): TableColumn[T, String] = {
    new TableColumn[T, String]() {
      text = textValue
      cellValueFactory = {
        x => valueFactory(x.value)
      }
    }
  }

  def isEditable: Boolean

  def selectionMode: SelectionMode

  def cellSelectionEnabled: Boolean

  def onRowDoubleClick(): Unit

  def tableUserData: AnyRef

  def columnWidths: List[Double]

  def createTableColumns: List[TableColumn[T, _]]
}
