package org.yankov.mso.application.ui.toolbars

import javafx.event.ActionEvent
import org.yankov.mso.application.MediaStorageOrganizer
import org.yankov.mso.application.commands.Commands
import org.yankov.mso.application.model.DataModel.{FolkloreTrack, emptyFolkloreTrack}
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import scalafx.scene.control.{Button, TableView}

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[FolkloreTrackProperties] = Option.empty

  override def updateDatabase(event: ActionEvent): Unit = Commands.updateDatabase()

  override def exportItems(event: ActionEvent): Unit = Commands.exportItems()

  override def loadTracks(event: ActionEvent): Unit = ???

  override def clearTable(event: ActionEvent): Unit = Commands.clearTable(targetTable(event))

  override def importFromClipboard(event: ActionEvent): Unit = ???

  override def applyProperties(event: ActionEvent): Unit = {
    val table = targetTable(event)

    def applyPropertiesToTrack(index: Int): Unit = {
      val copiedTrack = copiedProperties.get.folkloreTrack
      val selectedProperties = table.items.getValue.get(index)
      val newTrack = FolkloreTrack(
        id = copiedTrack.id,
        title = selectedProperties.folkloreTrack.title,
        performer = copiedTrack.performer,
        accompanimentPerformer = copiedTrack.accompanimentPerformer,
        author = copiedTrack.author,
        arrangementAuthor = copiedTrack.arrangementAuthor,
        conductor = copiedTrack.conductor,
        soloist = copiedTrack.soloist,
        duration = selectedProperties.folkloreTrack.duration,
        note = copiedTrack.note,
        source = copiedTrack.source,
        ethnographicRegion = copiedTrack.ethnographicRegion,
        record = selectedProperties.folkloreTrack.record,
        recordFormat = copiedTrack.recordFormat
      )
      table.getItems.set(index, FolkloreTrackProperties(newTrack, selectedProperties.file))
    }

    Commands.applyProperties(targetTable(event), targetButtons(event), copiedProperties, applyPropertiesToTrack)
    copiedProperties = Option.empty
  }

  override def copyProperties(event: ActionEvent): Unit = {
    copiedProperties = Commands.copyProperties(targetTable(event), targetButtons(event))
  }

  override def cloneItem(event: ActionEvent): Unit = {
    Commands.cloneItem[FolkloreTrackProperties](
      targetTable(event),
      x => FolkloreTrackProperties(x.folkloreTrack, x.file)
    )
  }

  override def removeItem(event: ActionEvent): Unit = Commands.removeItem(targetTable(event))

  override def addItem(event: ActionEvent): Unit = {
    Commands.addItem(
      targetTable(event),
      FolkloreTrackProperties(emptyFolkloreTrack, Option.empty)
    )
  }

  override def upload(event: ActionEvent): Unit = ???

  override def playStop(event: ActionEvent): Unit = ???

  override def editTrack(event: ActionEvent): Unit = ???

  private def targetTable(event: ActionEvent): TableView[FolkloreTrackProperties] = {
    val button = event.getSource.asInstanceOf[Button]
    if (MediaStorageOrganizer.toolbarButtons.atInputTabToolbar(button)) MediaStorageOrganizer.inputTable.table
    else MediaStorageOrganizer.searchTable.table
  }

  private def targetButtons(event: ActionEvent): List[Button] = {
    val button = event.getSource.asInstanceOf[Button]
    if (MediaStorageOrganizer.toolbarButtons.atInputTabToolbar(button)) MediaStorageOrganizer.toolbarButtons.inputTabButtons
    else MediaStorageOrganizer.toolbarButtons.searchTabButtons
  }
}
