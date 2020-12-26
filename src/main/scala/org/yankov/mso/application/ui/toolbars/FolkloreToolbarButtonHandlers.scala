package org.yankov.mso.application.ui.toolbars

import java.io.File

import org.yankov.mso.application.{Main, Resources}
import org.yankov.mso.application.commands.Commands
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.{FolkloreTrackEditor, Utils}
import org.yankov.mso.application.ui.console.ApplicationConsole
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[FolkloreTrackProperties] = Option.empty
  private val dataManager = Main.dataManager
  private val console = ApplicationConsole

  override def updateItems(targetInputTab: Boolean): Unit = {
    Commands.updateItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => dataManager.updateTracks(x.map(y => y.track)),
    )
  }

  override def exportItems(targetInputTab: Boolean): Unit = {
    Commands.exportItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      (x, y) => createOutputFileName(x, y.track),
      x => dataManager.getRecord(x.track.id)
    )
  }

  override def loadTracks(targetInputTab: Boolean): Unit = {
    Commands.loadItems(
      targetTable(targetInputTab),
      x => FolkloreTrackProperties(FolkloreTrack().withFile(Option(x)))
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
        file = selectedProperties.track.file
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

  override def deleteItem(targetInputTab: Boolean): Unit = {
    Commands.deleteItem[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => {
        if (Utils.confirmDeleteFromDatabase(x.track.id)) {
          if (dataManager.deleteTrack(x.track)) console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackSuccessful(x.track.id))
          else console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackFailed(x.track.id))
        }
      }
    )
  }

  override def addItem(targetInputTab: Boolean): Unit = {
    Commands.addItem(
      targetTable(targetInputTab),
      FolkloreTrackProperties(FolkloreTrack())
    )
  }

  override def uploadItems(targetInputTab: Boolean): Unit = {
    Commands.uploadItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => dataManager.insertTracks(
        x.map(y => y.track),
        (x, y) => console.writeMessageWithTimestamp(
          if (y) Resources.ConsoleMessages.uploadTrackSuccessful(x.title)
          else Resources.ConsoleMessages.uploadTrackFailed(x.title)
        )
      )
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
      track.source +
      Resources.Media.flacExtension
  }
}
