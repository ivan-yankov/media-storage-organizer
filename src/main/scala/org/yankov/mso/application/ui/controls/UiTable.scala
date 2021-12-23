package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.ui.controls.FontModel._
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{SelectionMode, TableColumn, TableRow, TableView}
import scalafx.scene.input._
import scalafx.scene.layout.{Pane, StackPane}

import scala.collection.JavaConverters.asScalaBufferConverter

abstract class UiTable[T] {
  private val table: TableView[T] = new TableView[T]() {
    editable = isEditable
    selectionModel.value.setSelectionMode(selectionMode)
    selectionModel.value.setCellSelectionEnabled(cellSelectionEnabled)

    createTableColumns.foreach(
      x => {
        columns.add(x._1)
        columns.last.setStyle(tableFont.cssRepresentation)
        columns.last.setPrefWidth(x._2)
      }
    )

    items = new ObservableBuffer[T]()

    rowFactory = _ => new TableRow[T] {
      onMouseClicked = event => if (event.getClickCount == 2) onRowDoubleClick()
    }

    val keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.ControlAny)
    onKeyPressed = event => if (keyCodeCopy.`match`(event)) copySelectionToClipboard()

    userData = tableUserData
  }

  private val container: StackPane = new StackPane {
    children = List(table)
  }

  private def copySelectionToClipboard(): Unit = {
    def cellToString(cell: Any): String = if (cell == null) "" else cell.toString

    val selected = table.getSelectionModel.getSelectedCells.asScala.toList
    val rows = selected.map(x => x.getRow).distinct.sorted
    val cols = selected.map(x => x.getColumn).distinct.sorted
    val data = rows.map(r => cols.map(c => cellToString(table.getColumns.get(c).getCellData(r))).mkString("\t")).mkString("\n")
    val clipboardContent = new ClipboardContent()
    clipboardContent.putString(data)
    Clipboard.systemClipboard.content = clipboardContent
  }

  def tableFont: CustomFont = CustomFont(SansSerif, NormalStyle, NormalWeight, 12)

  def getContainer: Pane = container

  def pure: TableView[T] = table

  def setItems(items: List[T]): Unit = {
    clear()
    items.foreach(x => table.getItems.add(x))
  }

  def clear(): Unit = table.getItems.clear()

  def stringTableColumn(textValue: String, valueFactory: T => StringProperty, width: Double): (TableColumn[T, String], Double) = {
    (
      new TableColumn[T, String]() {
        text = textValue
        cellValueFactory = {
          x => valueFactory(x.value)
        }
      },
      width
    )
  }

  def isEditable: Boolean

  def selectionMode: SelectionMode

  def cellSelectionEnabled: Boolean

  def onRowDoubleClick(): Unit

  def tableUserData: AnyRef

  def createTableColumns: List[(TableColumn[T, _], Double)]
}
