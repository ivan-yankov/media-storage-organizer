package org.yankov.mso.application.commands

import java.io.{File, FileOutputStream}
import java.time.Duration

import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.{ApplicationContext, MediaStorageOrganizer}
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.{FxUtils, Resources}
import scalafx.scene.control.{Button, SelectionMode, TableView}

object Commands {
  def updateDatabase(): Unit = {
    if (FxUtils.confirmOverwrite) {
      updateDataModel()
      save()
      clearTable(MediaStorageOrganizer.inputTable.table)
    }
  }

  def exportItems(): Unit = {
    //    val directory = FxUtils.selectDirectory
    //    if (directory.isDefined) {
    //      ApplicationContext.logger.info(resourceBundle.getString(EXPORT_STARTED))
    //      for (PropertiesType item : table.getSelectionModel().getSelectedItems()) {
    //        String outputFileName = createOutputFileName(directory.get(), item);
    //        try {
    //          val out = new FileOutputStream(outputFileName);
    //          out.write(item.getRecord());
    //          out.close();
    //        } catch (IOException e) {
    //          String msg = resourceBundle.getString(UNABLE_WRITE_FILE) + outputFileName;
    //          ApplicationContext.getInstance().getLogger().log(Level.SEVERE, msg, e);
    //        }
    //      }
    //      ApplicationContext.getInstance().getLogger().info(resourceBundle.getString(EXPORT_COMPLETED));
    //    }
  }

  def clearTable(table: TableView[_]): Unit = {
    if (FlacPlayer.isPlaying()) {
      FlacPlayer.stop()
    }
    table.getItems.clear()
  }

  def importFromClipboard(): Unit = ???

  def applyProperties[T](table: TableView[T], buttons: List[Button], copiedProperties: Option[T], applyPropertiesToTrack: Int => Unit): Unit = {
    val selected = table.getSelectionModel.getSelectedIndices

    if (copiedProperties.isDefined && !selected.isEmpty) {
      selected.forEach(x => applyPropertiesToTrack(x))

      buttons
        .find(x => x.id.getValue.equals(Resources.ControlIds.btnApplyProperties))
        .get
        .setDisable(true)

      table.getSelectionModel.clearSelection()
      table.getSelectionModel.setSelectionMode(SelectionMode.Single)
    }
  }

  def copyProperties[T](table: TableView[T], buttons: List[Button]): Option[T] = {
    val index = getSelectedIndex(table)
    if (index.isDefined) {
      val copiedProperties = Option(
        table
          .items
          .getValue
          .get(index.get)
      )

      buttons
        .find(x => x.id.getValue.equals(Resources.ControlIds.btnApplyProperties))
        .get
        .setDisable(false)

      table.getSelectionModel.clearSelection()
      table.getSelectionModel.setSelectionMode(SelectionMode.Multiple)

      copiedProperties
    }
    else Option.empty
  }

  def cloneItem[T](table: TableView[T], newItem: T => T): Unit = {
    val index = getSelectedIndex(table)
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
    val index = getSelectedIndex(table)
    if (index.isDefined) {
      table
        .items
        .getValue
        .remove(index.get)
    }
  }

  def addItem[T](table: TableView[T], item: T): Unit = table.items.getValue.add(item)

  //  private def createOutputFileName(directory: File, trackProperties: FolkloreTrackProperties): String = {
  //    directory.getAbsolutePath +
  //    File.separator +
  //
  //    trackProperties.item.getId().toString());
  //    "_");
  //    item.getTitle());
  //    "_");
  //    item.getPerformer().getName());
  //    "_");
  //    item.getSource().getType().getName());
  //    "_");
  //    item.getSource().getSignature());
  //    ".");
  //    item.getRecordFormat().toLowerCase());
  //  }

  private def getSelectedIndex(table: TableView[_]): Option[Int] = {
    val index = table.getSelectionModel.getSelectedIndex
    if (index < 0) Option.empty
    else Option(index)
  }
}
