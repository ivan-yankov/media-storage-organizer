package org.yankov.mso.application

import java.io.{File, FileOutputStream, IOException}

import org.slf4j.LoggerFactory
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.ui.UiUtils
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import scalafx.scene.control.{Button, SelectionMode, TableView}

import collection.JavaConverters._

object Commands {
  private val log = LoggerFactory.getLogger(getClass)
  private val console: ConsoleService = ApplicationConsole

  def updateItems[T](table: TableView[T], update: List[T] => Boolean): Unit = {
    if (UiUtils.confirmOverwrite) {
      val items = table
        .getItems
        .asScala
        .toList

      console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadStarted)
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
      console.writeMessageWithTimestamp(Resources.ConsoleMessages.exportStarted)
      table
        .getSelectionModel
        .getSelectedItems
        .forEach(x => {
          val outputFileName = createOutputFileName(directory.get, x)
          try {
            val out = new FileOutputStream(outputFileName)
            out.write(getRecord(x))
            out.close()
          } catch {
            case e: IOException =>
              log.error(s"Unable to write file [$outputFileName]", e)
              console.writeMessageWithTimestamp(s"${Resources.ConsoleMessages.unableWriteFile} [$outputFileName]")
          }
        })
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

  def clearTable(table: TableView[_]): Unit = {
    Player.close()
    table.getItems.clear()
  }

  def importTitlesFromClipboard[T](table: TableView[T], data: String, withTitle: (T, String) => T): Unit = {
    val titles = data.split(System.lineSeparator()).filter(x => x.nonEmpty)
    Range(0, table.getItems.size())
      .map(x => table.getItems.set(x, withTitle(table.getItems.get(x), titles(x))))
  }

  def applyProperties[T](table: TableView[T],
                         applyPropertiesButton: Button,
                         copiedProperties: Option[T],
                         createProperties: (Int, Int) => T): Unit = {
    val selectedRows = table.getSelectionModel.getSelectedIndices.asScala.toList
    val selectedCell = getTableSelectedCell(table)

    if (copiedProperties.isDefined && selectedRows.nonEmpty && selectedCell.isDefined) {
      selectedRows.foreach(x => table.getItems.set(x, createProperties(x, selectedCell.get)))
      applyPropertiesButton.setDisable(true)
    }
  }

  def copyProperties[T](table: TableView[T], applyPropertiesButton: Button): Option[T] = {
    getTableSelectedIndex(table) match {
      case Some(index) =>
        val copiedProperties = Option(table.items.getValue.get(index))
        applyPropertiesButton.setDisable(false)
        table.getSelectionModel.clearSelection()
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

  def removeItem(table: TableView[_]): Unit = {
    val index = getTableSelectedIndex(table)
    if (index.isDefined) {
      table
        .items
        .getValue
        .remove(index.get)
    }
  }

  def deleteItem[T](table: TableView[T], confirm: () => Boolean, deleteFromDatabase: T => Unit): Unit = {
    val index = getTableSelectedIndex(table)
    if (index.isDefined) {
      val item = table
        .items
        .getValue
        .get(index.get)
      if (confirm()) {
        deleteFromDatabase(item)
        table.getItems.remove(item)
      }
    }
  }

  def addItem[T](table: TableView[T], item: T): Unit = table.getItems.add(item)

  def uploadItems[T](table: TableView[T], insertItems: List[T] => Boolean): Unit = {
    val items = table
      .items
      .getValue
      .asScala
      .toList

    console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadStarted)
    if (insertItems(items)) {
      console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadSuccessful)
      clearTable(table)
    }
    else console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadFailed)
  }

  def play[T](table: TableView[T], play: T => Unit): Unit = {
    val index = getTableSelectedIndex(table)
    if (index.isDefined) play(table.getItems.get(index.get))
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

  private def getTableSelectedCell(table: TableView[_]): Option[Int] = {
    val index = table.getFocusModel.getFocusedCell.getColumn
    if (index < 0) Option.empty
    else Option(index)
  }
}
