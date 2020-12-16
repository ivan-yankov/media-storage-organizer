package org.yankov.mso.application.ui.toolbars

import java.io.File

import org.yankov.mso.application.Main
import org.yankov.mso.application.commands.Commands
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DataModelOperations
import org.yankov.mso.application.model.EmptyValues._
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.ui.{FolkloreTrackEditor, FxUtils}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[FolkloreTrackProperties] = Option.empty

  override def updateItems(targetInputTab: Boolean): Unit = {
    Commands.updateItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => DataModelOperations.updateTrack(x.track),
    )
  }

  override def exportItems(targetInputTab: Boolean): Unit = {
    Commands.exportItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      (x, y) => createOutputFileName(x, y.track),
      x => DataModelOperations.getRecord(x.track.id)
    )
  }

  override def loadTracks(targetInputTab: Boolean): Unit = {
    Commands.loadItems(
      targetTable(targetInputTab),
      x => FolkloreTrackProperties(emptyFolkloreTrack.withFile(Option(x)))
    )
  }

  override def clearTable(targetInputTab: Boolean): Unit = Commands.clearTable(targetTable(targetInputTab))

  override def importTitlesFromClipboard(targetInputTab: Boolean): Unit = {
    val clipboard = Clipboard.systemClipboard
    if (clipboard.hasContent(DataFormat.PlainText)) {
      Commands.importTitlesFromClipboard[FolkloreTrackProperties](
        targetTable(targetInputTab),
        clipboard.getString,
        (x, y) => FolkloreTrackProperties(x.track.withTitle(y))
      )
    }
  }

  override def applyProperties(targetInputTab: Boolean): Unit = {
    val table = targetTable(targetInputTab)

    def createProperties(index: Int): FolkloreTrackProperties = {
      val copiedTrack = copiedProperties.get.track
      val selectedProperties = table.items.getValue.get(index)
      val newTrack = FolkloreTrack(
        id = copiedTrack.id,
        title = selectedProperties.track.title,
        performer = copiedTrack.performer,
        accompanimentPerformer = copiedTrack.accompanimentPerformer,
        author = copiedTrack.author,
        arrangementAuthor = copiedTrack.arrangementAuthor,
        conductor = copiedTrack.conductor,
        soloist = copiedTrack.soloist,
        duration = selectedProperties.track.duration,
        note = copiedTrack.note,
        source = copiedTrack.source,
        ethnographicRegion = copiedTrack.ethnographicRegion,
        file = selectedProperties.track.file,
        recordFormat = copiedTrack.recordFormat
      )
      FolkloreTrackProperties(newTrack)
    }

    Commands.applyProperties(table,
      targetButtons(targetInputTab).find(x => x.id.value.equals(ButtonIds.btnApplyProperties)).get,
      copiedProperties,
      createProperties
    )
    copiedProperties = Option.empty
  }

  override def copyProperties(targetInputTab: Boolean): Unit = {
    copiedProperties = Commands.copyProperties(
      targetTable(targetInputTab),
      targetButtons(targetInputTab).find(x => x.id.value.equals(ButtonIds.btnApplyProperties)).get,
    )
  }

  override def cloneItem(targetInputTab: Boolean): Unit = {
    Commands.cloneItem[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => FolkloreTrackProperties(x.track)
    )
  }

  override def removeItem(targetInputTab: Boolean): Unit = Commands.removeItem(targetTable(targetInputTab))

  override def addItem(targetInputTab: Boolean): Unit = {
    Commands.addItem(
      targetTable(targetInputTab),
      FolkloreTrackProperties(emptyFolkloreTrack)
    )
  }

  override def uploadItems(targetInputTab: Boolean): Unit = {
    Commands.uploadItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => DataModelOperations.insertTrack(x.track)
    )
  }

  override def playStop(targetInputTab: Boolean): Unit = {
    Commands.playStop[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => Player.play(x.track)
    )
  }

  override def editTrack(targetInputTab: Boolean): Unit = {
    val table = targetTable(targetInputTab)
    Commands.editTrack(
      table,
      x => FolkloreTrackEditor(table, x).open()
    )
  }

  private def targetTable(targetInputTab: Boolean): TableView[FolkloreTrackProperties] = {
    if (targetInputTab) Main.inputTable.getValue
    else Main.searchTable.getValue
  }

  private def targetButtons(targetInputTab: Boolean): List[Button] = {
    if (targetInputTab) Main.toolbarButtons.inputTabButtons
    else Main.toolbarButtons.searchTabButtons
  }

  private def createOutputFileName(dir: File, track: FolkloreTrack): String = {
    dir.getAbsolutePath +
      File.separator +
      track.id + "_" +
      track.title + "_" +
      track.performer + "_" +
      track.source + "." +
      track.recordFormat.toLowerCase
  }
}