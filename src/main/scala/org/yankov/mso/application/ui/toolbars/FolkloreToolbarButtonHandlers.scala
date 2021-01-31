package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.{FolkloreTrackEditor, Utils}
import org.yankov.mso.application.{Commands, Main, Resources}
import scalafx.application.Platform
import scalafx.scene.Cursor
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}

import java.io.File
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[FolkloreTrackProperties] = Option.empty
  private val dataManager = Main.dataManager
  private val console = ApplicationConsole

  override def updateItems(targetInputTab: Boolean): Unit = {
    if (Utils.confirmOverwrite) {
      val table = targetTable(targetInputTab)
      runAsync(
        () => {
          val items = table
            .getItems
            .asScala
            .toList

          console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadStarted)
          val success = dataManager.updateTracks(
            items.map(y => y.track),
            (x, y) => console.writeMessageWithTimestamp(
              if (y) Resources.ConsoleMessages.updateTrackSuccessful(x.title)
              else Resources.ConsoleMessages.updateTrackFailed(x.title)
            )
          )
          if (success) console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadSuccessful)
          else console.writeMessageWithTimestamp(Resources.ConsoleMessages.uploadFailed)
        },
        () => Commands.clearTable(table)
      )
    }
  }

  override def exportItems(targetInputTab: Boolean): Unit = {
    val directory = Utils.selectDirectory
    if (directory.isDefined) {
      runAsync(
        () => {
          Commands.exportItems[FolkloreTrackProperties](
            targetTable(targetInputTab),
            directory.get,
            (x, y) => createOutputFileName(x, y.track),
            x => dataManager.getRecord(x.track.id)
          )
        },
        () => console.writeMessageWithTimestamp(Resources.ConsoleMessages.exportCompleted)
      )
    }
  }

  override def loadTracks(targetInputTab: Boolean): Unit = {
    Commands.loadItems(
      targetTable(targetInputTab),
      x => FolkloreTrackProperties(FolkloreTrack().withFile(Option(x)).withDuration(Utils.calculateDuration(Option(x))))
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
    val table = targetTable(targetInputTab)
    val index = Commands.getTableSelectedIndex(table)
    if (index.isDefined) {
      val item = table
        .items
        .getValue
        .get(index.get)

      if (Utils.confirmDeleteFromDatabase(item.track.id)) {
        runAsync(
          () => {
            val success = dataManager.deleteTrack(item.track)
            if (success) console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackSuccessful(item.track.id))
            else console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackFailed(item.track.id))
          },
          () => table.getItems.remove(item)
        )
      }
    }
  }

  override def addItem(targetInputTab: Boolean): Unit = {
    Commands.addItem(
      targetTable(targetInputTab),
      FolkloreTrackProperties(FolkloreTrack())
    )
  }

  override def uploadItems(targetInputTab: Boolean): Unit = {
    val table = targetTable(targetInputTab)
    runAsync(
      () => {
        Commands.uploadItems[FolkloreTrackProperties](
          table,
          x => dataManager.insertTracks(
            x.map(y => y.track),
            (x, y) => console.writeMessageWithTimestamp(
              if (y) Resources.ConsoleMessages.insertTrackSuccessful(x.title)
              else Resources.ConsoleMessages.insertTrackFailed(x.title)
            )
          )
        )
      },
      () => Commands.clearTable(table)
    )
  }

  override def play(targetInputTab: Boolean): Unit = {
    Commands.play[FolkloreTrackProperties](
      targetTable(targetInputTab),
      x => Player.play(x.track, dataManager.storageFileName)
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
      StringConverters.artistToString(track.performer) +
      Resources.Media.flacExtension
  }

  private def runAsync(f: () => Unit, onComplete: () => Unit): Unit = {
    val cursor = Main.stage.getScene.getCursor
    Main.stage.getScene.setCursor(Cursor.Wait)
    val future = Future {
      Platform.runLater(() => f())
    }
    future.onComplete(_ => {
      Platform.runLater(() => {
        onComplete()
        Main.stage.getScene.setCursor(cursor)
      })
    })
  }
}
