package org.yankov.mso.application.commands

import java.io.{File, FileOutputStream, IOException}

import org.slf4j.LoggerFactory
import org.yankov.mso.application.Resources
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.ui.FxUtils
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import scalafx.scene.control.{Button, SelectionMode, TableView}

object Commands {
  private val log = LoggerFactory.getLogger(getClass)
  private val console: ConsoleService = ApplicationConsole

  def updateItems[T](table: TableView[T], updateTrack: T => Unit): Unit = {
    if (FxUtils.confirmOverwrite) {
      table
        .getSelectionModel
        .getSelectedItems
        .forEach(x => updateTrack(x))
      clearTable(table)
    }
  }

  def exportItems[T](table: TableView[T], createOutputFileName: (File, T) => String, getRecord: T => Array[Byte]): Unit = {
    val directory = FxUtils.selectDirectory
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
    val files = FxUtils.selectFlacFiles(false)
    if (files.isDefined) {
      files
        .get
        .map(x => createItem(x))
        .foreach(x => addItem(table, x))
    }
  }

  def clearTable(table: TableView[_]): Unit = {
    if (Player.isPlaying) {
      Player.stop()
    }
    table.getItems.clear()
  }

  def importTitlesFromClipboard[T](table: TableView[T], data: String, withTitle: (T, String) => T): Unit = {
    val titles = data.split(System.lineSeparator())
    Range(0, table.getItems.size())
      .map(x => table.getItems.set(x, withTitle(table.getItems.get(x), titles(x))))
  }

  def applyProperties[T](table: TableView[T], applyPropertiesButton: Button, copiedProperties: Option[T], createProperties: Int => T): Unit = {
    val selected = table.getSelectionModel.getSelectedIndices

    if (copiedProperties.isDefined && !selected.isEmpty) {
      selected.forEach(x => table.getItems.set(x, createProperties(x)))

      applyPropertiesButton.setDisable(true)

      table.getSelectionModel.clearSelection()
      table.getSelectionModel.setSelectionMode(SelectionMode.Single)
    }
  }

  def copyProperties[T](table: TableView[T], applyPropertiesButton: Button): Option[T] = {
    val index = getTableSelectedIndex(table)
    if (index.isDefined) {
      val copiedProperties = Option(
        table
          .items
          .getValue
          .get(index.get)
      )

      applyPropertiesButton.setDisable(false)

      table.getSelectionModel.clearSelection()
      table.getSelectionModel.setSelectionMode(SelectionMode.Multiple)

      copiedProperties
    }
    else Option.empty
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

  def addItem[T](table: TableView[T], item: T): Unit = table.getItems.add(item)

  def uploadItems[T](table: TableView[T], uploadItem: T => Unit): Unit = {
    table
      .items
      .getValue
      .forEach(x => uploadItem(x))
  }

  def playStop[T](table: TableView[T], play: T => Unit): Unit = {
    if (Player.isPlaying) Player.stop()
    else {
      val index = getTableSelectedIndex(table)
      if (index.isDefined) play(table.getItems.get(index.get))
    }
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
}
