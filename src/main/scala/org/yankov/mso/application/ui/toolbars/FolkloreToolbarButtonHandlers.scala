package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application._
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel._
import org.yankov.mso.application.ui.UiUtils._
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.{FolkloreTrackEditor, UiUtils}
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}
import org.yankov.mso.application.search.TextAnalyzer._

import java.io.File
import java.nio.file.Files
import java.util.regex.Pattern

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  private var copiedProperties: Option[TrackTableProperties] = Option.empty
  private val dataManager = Main.dataManager
  private val console = ApplicationConsole
  private val maxFileNameLength = 250

  override def updateItems(targetInputTab: Boolean): Unit = {
    Commands.updateItems[TrackTableProperties](
      targetTable(targetInputTab),
      x => dataManager.updateTracks(x.map(y => y.track))
    )
  }

  override def exportItems(targetInputTab: Boolean): Unit = {
    Commands.exportItems[TrackTableProperties](
      targetTable(targetInputTab),
      (x, y) => createOutputFileName(x, y.track),
      x => dataManager.getRecord(x.track.id)
    )
  }

  override def loadTracks(targetInputTab: Boolean): Unit = {
    def extractTitle(file: File): String = {
      try {
        val pattern = Pattern.compile(Main.extractTitleFromFileNameRegex.getValue)
        val matcher = pattern.matcher(file.getName.replace(Resources.Media.flacExtension, ""))
        if (matcher.find()) {
          val s = matcher.group().trim.refineMultipleSpaces.toLowerCase
          s.head.toUpper + s.tail
        }
        else ""
      } catch {
        case _: Exception =>
          ApplicationConsole.writeMessageWithTimestamp(Resources.MainForm.invalidRegex)
          ""
      }
    }

    Commands.loadItems(
      targetTable(targetInputTab),
      x => {
        if (Main.extractTitleFromFileNameCheckBox.isSelected) {
          TrackTableProperties(
            FolkloreTrack()
              .withTitle(extractTitle(x))
              .withFile(Option(x))
              .withDuration(UiUtils.calculateDuration(Option(x)))
          )
        }
        else {
          TrackTableProperties(
            FolkloreTrack()
              .withFile(Option(x))
              .withDuration(UiUtils.calculateDuration(Option(x)))
          )
        }
      }
    )
  }

  override def clearTable(targetInputTab: Boolean): Unit = Commands.clearTable(targetTable(targetInputTab))

  override def importTitlesFromClipboard(targetInputTab: Boolean): Unit = {
    val clipboard = Clipboard.systemClipboard
    if (clipboard.hasContent(DataFormat.PlainText)) {
      Commands.importTitlesFromClipboard[TrackTableProperties](
        targetTable(targetInputTab),
        clipboard.getString,
        (x, y) => TrackTableProperties(x.track.withTitle(y))
      )
    }
  }

  override def applyProperties(targetInputTab: Boolean): Unit = {
    val table = targetTable(targetInputTab)

    def createProperties(row: Int, col: Int): TrackTableProperties = {
      val sourceTrack = copiedProperties.get.track
      val destinationTrack = table.items.getValue.get(row).track

      val mergeTrackMap = table.getUserData.asInstanceOf[MergeTrackMap]

      val newTrack = mergeTrackMap.getOrElse[MergeTrack](col, (_, x) => x)(sourceTrack, destinationTrack)
      TrackTableProperties(newTrack)
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
    Commands.cloneItem[TrackTableProperties](
      targetTable(targetInputTab),
      x => TrackTableProperties(x.track)
    )
  }

  override def removeItem(targetInputTab: Boolean): Unit = Commands.removeItem(targetTable(targetInputTab))

  override def deleteItem(targetInputTab: Boolean): Unit = {
    Commands.deleteItem[TrackTableProperties](
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
      TrackTableProperties(FolkloreTrack())
    )
  }

  override def uploadItems(targetInputTab: Boolean): Unit = {
    longOperation(
      () => Commands.uploadItems[TrackTableProperties](
        targetTable(targetInputTab),
        x => dataManager.insertTracks(x.map(y => y.track))
      )
    ).inThread.start()
  }

  override def play(targetInputTab: Boolean): Unit = {
    def fileAudioData(track: FolkloreTrack): (Id, Array[Byte]) = {
      if (track.file.isDefined) {
        (
          track.file.get.getName.replace(" ", "_"),
          FlacDecoder.decode(Files.readAllBytes(track.file.get.toPath)).getOrElse(Array())
        )
      }
      else if (isValidId(track.id)) {
        (
          track.id,
          FlacDecoder.decode(Files.readAllBytes(dataManager.databasePaths.mediaFile(track.id).toPath)).getOrElse(Array())
        )
      }
      else ("", Array())
    }

    Commands.play[TrackTableProperties](
      targetTable(targetInputTab),
      items => Player.play(items.map(x => fileAudioData(x.track)))
    )
  }

  override def editTrack(targetInputTab: Boolean): Unit = {
    val table = targetTable(targetInputTab)
    Commands.editTrack(
      table,
      x => FolkloreTrackEditor(table, x).open()
    )
  }

  private def targetTable(targetInputTab: Boolean): TableView[TrackTableProperties] = {
    if (targetInputTab) Main.inputTable.pure
    else Main.searchTable.pure
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
