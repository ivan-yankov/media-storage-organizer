package org.yankov.mso.application

import org.yankov.mso.application.ui.UiUtils
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import scalafx.scene.control.{Button, TableView}

import java.io.File
import collection.JavaConverters._

object Commands {
  private val console: ConsoleService = ApplicationConsole

  def updateItems[T](table: TableView[T], update: List[T] => Boolean): Unit = {
    if (UiUtils.confirmOverwrite) {
      val items = table
        .getItems
        .asScala
        .toList

      if (update(items)) {
        console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadSuccessful)
        clearTable(table)
      }
      else console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadFailed)
    }
  }

  def exportItems[T](table: TableView[T], createOutputFileName: (File, T) => String, getRecord: T => Array[Byte]): Unit = {
    val directory = UiUtils.selectDirectory
    if (directory.isDefined) {
      table
        .getSelectionModel
        .getSelectedItems
        .forEach(
          x => {
            val outputFileName = createOutputFileName(directory.get, x)
            FileUtils.writeBinaryFile(new File(outputFileName), getRecord(x))
          }
        )
      console.writeMessageWithTimestamp(Resources.ConsoleMessages.exportCompleted)
    }
  }

  def loadItems[T](table: TableView[T], createItem: File => T): Unit = {
    val files = UiUtils.selectFlacFiles(false)
    if (files.isDefined) {
      files
        .get
        .map(x => createItem(x))
        .foreach(x => addItem(table, x))
    }
  }

  def clearTable(table: TableView[_]): Unit = table.getItems.clear()

  def importTitlesFromClipboard[T](table: TableView[T], data: String, withTitle: (T, String) => T): Unit = {
    val titles = data.split(System.lineSeparator()).filter(x => x.nonEmpty)
    Range(0, table.getItems.size())
      .map(x => table.getItems.set(x, withTitle(table.getItems.get(x), titles(x))))
  }

  def applyProperties[T](table: TableView[T],
                         applyPropertiesButton: Button,
                         copiedProperties: Option[T],
                         createProperties: (Int, String) => T): Unit = {
    val selectedRows = table.getSelectionModel.getSelectedIndices.asScala.toList
    val selectedCellIndex = getTableSelectedColumnIndex(table)
    val selectedCellKey = getTableSelectedColumnKey(table)

    if (copiedProperties.isDefined && selectedRows.nonEmpty && selectedCellKey.isDefined) {
      selectedRows.foreach(x => table.getItems.set(x, createProperties(x, selectedCellKey.get)))
      applyPropertiesButton.setDisable(true)
      table.getSelectionModel.clearSelection()
      table.getFocusModel.focus(selectedRows.max, table.getColumns.get(selectedCellIndex.getOrElse(0)))
    }
  }

  def copyProperties[T](table: TableView[T], applyPropertiesButton: Button): Option[T] = {
    getTableSelectedIndex(table) match {
      case Some(index) =>
        val copiedProperties = Option(table.items.getValue.get(index))
        applyPropertiesButton.setDisable(false)
        copiedProperties
      case None =>
        Option.empty
    }
  }

  def cloneItem[T](table: TableView[T], newItem: T => T): Unit = {
    val index = getTableSelectedIndex(table)
    if (index.isDefined) {
      val selectedItem = table
        .items
        .getValue
        .get(index.get)
      table
        .items
        .getValue
        .add(newItem(selectedItem))
    }
  }

  def removeItems[T](table: TableView[T]): Unit = {
    val items = table.getSelectionModel.getSelectedItems.asScala.toList
    if (items.nonEmpty) {
      table.getItems.removeAll(items.asJava)
      table.getSelectionModel.clearSelection()
    }
  }

  def deleteItems[T](table: TableView[T], confirm: () => Boolean, deleteFromDatabase: List[T] => Unit): Unit = {
    val items = table.getSelectionModel.getSelectedItems.asScala.toList
    if (items.nonEmpty && confirm()) {
      table.getItems.removeAll(items.asJava)
      table.getSelectionModel.clearSelection()
      deleteFromDatabase(items)
    }
  }

  def addItem[T](table: TableView[T], item: T): Unit = table.getItems.add(item)

  def uploadItems[T](table: TableView[T], insertItems: List[T] => Boolean): Unit = {
    val items = table
      .items
      .getValue
      .asScala
      .toList

    if (insertItems(items)) {
      console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadSuccessful)
      clearTable(table)
    }
    else console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadFailed)
  }

  def play[T](table: TableView[T], play: List[T] => Unit): Unit = {
    val selected = table.getSelectionModel.getSelectedItems.asScala.toList
    if (selected.nonEmpty) play(selected)
  }

  def editTrack(table: TableView[_], edit: Int => Unit): Unit = {
    val selected = getTableSelectedIndex(table)
    if (selected.isDefined) edit(selected.get)
  }

  private def getTableSelectedIndex(table: TableView[_]): Option[Int] = {
    val index = table.getSelectionModel.getSelectedIndex
    if (index < 0) Option.empty
    else Option(index)
  }

  private def getTableSelectedColumnIndex(table: TableView[_]): Option[Int] = {
    val index = table.getFocusModel.getFocusedCell.getColumn
    if (index < 0) Option.empty
    else Option(index)
  }

  private def getTableSelectedColumnKey(table: TableView[_]): Option[String] = {
    try {
      val key = table.getFocusModel.getFocusedCell.getTableColumn.getUserData.asInstanceOf[String]
      Some(key)
    } catch {
      case _: Exception => None
    }
  }
}
