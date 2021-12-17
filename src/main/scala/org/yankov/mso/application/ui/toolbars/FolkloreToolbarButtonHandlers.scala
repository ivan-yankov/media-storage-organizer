package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.{FolkloreTrackEditor, UiUtils}
import org.yankov.mso.application._
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}

import java.io.File

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[FolkloreTrackProperties] = Option.empty
  private val dataManager = Main.dataManager
  private val console = ApplicationConsole
  private val maxFileNameLength = 250

  override def updateItems(targetInputTab: Boolean): Unit = {
    Commands.updateItems[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => dataManager.updateTracks(x.map(y => y.track))
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
      x => FolkloreTrackProperties(FolkloreTrack().withFile(Option(x)).withDuration(UiUtils.calculateDuration(Option(x))))
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

    def createProperties(row: Int, col: Int): FolkloreTrackProperties = {
      val sourceTrack = copiedProperties.get.track
      val destinationTrack = table.items.getValue.get(row).track

      val mergeTrackMap = table.getUserData.asInstanceOf[MergeTrackMap]

      val newTrack = mergeTrackMap.getOrElse[MergeTrack](col, (_, x) => x)(sourceTrack, destinationTrack)
      FolkloreTrackProperties(newTrack)
    }

    Commands.applyProperties(
      table,
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
      () => UiUtils.confirmDeleteFromDatabase,
      x => {
        if (dataManager.deleteTrack(x.track)) console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackSuccessful)
        else console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackFailed)
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
      x => dataManager.insertTracks(x.map(y => y.track))
    )
  }

  override def play(targetInputTab: Boolean): Unit = {
    Commands.play[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => Player.play(x.track, dataManager.databasePaths.mediaFile)
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
    val fileName = (track.id + " - " + track.title + " - " + StringConverters.artistToString(track.performer))
      .take(maxFileNameLength)

    val fullFileName = dir.getAbsolutePath + File.separator + fileName + Resources.Media.flacExtension

    fullFileName.replace("\"", "")
  }
}
