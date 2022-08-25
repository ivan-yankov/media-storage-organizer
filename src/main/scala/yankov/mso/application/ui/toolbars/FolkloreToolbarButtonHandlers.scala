package yankov.mso.application.ui.toolbars

import scalafx.scene.control.{Button, TableView}
import scalafx.scene.input.{Clipboard, DataFormat}
import yankov.mso.application._
import yankov.mso.application.converters.StringConverters
import yankov.mso.application.media.Player
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel._
import yankov.mso.application.model.UiModel.TrackTableProperties
import yankov.mso.application.search.TextAnalyzer._
import yankov.mso.application.ui.UiUtils._
import yankov.mso.application.ui.console.ApplicationConsole
import yankov.mso.application.ui.{FolkloreTrackEditor, UiUtils}

import java.io.File
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
        val pattern = Pattern.compile(Main.mainControls.extractDataFromFileNameRegex.getValue)
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

    def extractSourceSignature(file: File): String = {
      try {
        val pattern = Pattern.compile(Main.mainControls.extractDataFromFileNameRegex.getValue)
        val matcher = pattern.matcher(file.getName.replace(Resources.Media.flacExtension, ""))
        if (matcher.find()) matcher.group().trim.refineMultipleSpaces.toLowerCase
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
        if (Main.mainControls.extractTitleFromFileNameCheckBox.isSelected) {
          TrackTableProperties(
            FolkloreTrack()
              .withTitle(extractTitle(x))
              .withFile(Option(x))
              .withDuration(UiUtils.calculateDuration(Option(x)))
          )
        }
        else if (Main.mainControls.extractSourceSignatureFromFileNameCheckBox.isSelected) {
          val source = dataManager.insertSource(
            Source(
              id = DataModel.invalidId,
              sourceType = Main.mainControls.sourceType.getValue,
              label = Main.mainControls.sourceLabel.getValue,
              signature = extractSourceSignature(x)
            )
          ) match {
            case Some(sourceId) => dataManager.getSources.find(_.id.equals(sourceId)).get
            case None => Source()
          }

          TrackTableProperties(
            FolkloreTrack()
              .copy(source = source)
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

    def createProperties(row: Int, colKey: String): TrackTableProperties = {
      val sourceTrack = copiedProperties.get.track
      val destinationTrack = table.items.getValue.get(row).track

      val mergeTrackMap = table.getUserData.asInstanceOf[MergeTrackMap]

      val newTrack = mergeTrackMap.getOrElse[MergeTrack](colKey, (_, x) => x)(sourceTrack, destinationTrack)
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

  override def removeItems(targetInputTab: Boolean): Unit = Commands.removeItems(targetTable(targetInputTab))

  override def deleteItems(targetInputTab: Boolean): Unit = {
    Commands.deleteItems[TrackTableProperties](
      targetTable(targetInputTab),
      () => UiUtils.confirmDeleteFromDatabase,
      items => {
        if (dataManager.deleteTracks(items.map(_.track.id))) console.writeMessageWithTimestamp(Resources.ConsoleMessages.deleteTrackSuccessful)
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
    def audioFile(track: FolkloreTrack): Option[File] = {
      if (track.file.isDefined) track.file
      else if (isValidId(track.id)) Some(dataManager.databasePaths.mediaFile(track.id))
      else None
    }

    Commands.play[TrackTableProperties](
      targetTable(targetInputTab),
      items => Player.play(items.map(x => audioFile(x.track)).filter(_.isDefined).map(_.get))
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
    if (targetInputTab) Main.mainControls.inputTable.pure
    else Main.mainControls.searchTable.pure
  }

  private def targetButtons(targetInputTab: Boolean): List[Button] = {
    if (targetInputTab) Main.mainControls.toolbarButtons.inputTabButtons
    else Main.mainControls.toolbarButtons.searchTabButtons
  }

  private def createOutputFileName(dir: File, track: FolkloreTrack): String = {
    val fileName = (track.id + " - " + track.title + " - " + StringConverters.artistToString(track.performer))
      .take(maxFileNameLength)

    val fullFileName = dir.getAbsolutePath + File.separator + fileName + Resources.Media.flacExtension

    fullFileName.replace("\"", "")
  }
}
