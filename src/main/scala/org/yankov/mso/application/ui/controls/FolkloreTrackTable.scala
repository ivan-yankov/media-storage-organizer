package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.model.UiModel._
import org.yankov.mso.application.ui.FolkloreTrackEditor
import org.yankov.mso.application.ui.toolbars.FolkloreToolbarButtonHandlers
import org.yankov.mso.application.{MergeTrack, Resources}
import scalafx.scene.control._

class FolkloreTrackTable(inputTable: Boolean, buttonHandlers: FolkloreToolbarButtonHandlers) extends UiTable[TrackTableProperties] {
  override def isEditable: Boolean = false

  override def selectionMode: SelectionMode = SelectionMode.Multiple

  override def cellSelectionEnabled: Boolean = true

  override def onRowDoubleClick(): Unit = openEditor()

  override def tableUserData: AnyRef = {
    Map[Int, MergeTrack](
      1 -> ((src, dest) => dest.copy(performer = src.performer)),
      2 -> ((src, dest) => dest.copy(accompanimentPerformer = src.accompanimentPerformer)),
      3 -> ((src, dest) => dest.copy(arrangementAuthor = src.arrangementAuthor)),
      4 -> ((src, dest) => dest.copy(conductor = src.conductor)),
      5 -> ((src, dest) => dest.copy(author = src.author)),
      6 -> ((src, dest) => dest.copy(soloist = src.soloist)),
      8 -> ((src, dest) => dest.copy(source = src.source)),
      9 -> ((src, dest) => dest.copy(ethnographicRegion = src.ethnographicRegion)),
      10 -> ((src, dest) => dest.copy(note = src.note)),
      11 -> ((src, dest) => dest.copy(source = src.source)),
    )
  }

  override def createTableColumns: List[(TableColumn[TrackTableProperties, String], Double)] = {
    val columns = List(
      stringTableColumn(Resources.TableColumns.title, _.title, 200.0),
      stringTableColumn(Resources.TableColumns.performer, _.performer, 175.0),
      stringTableColumn(Resources.TableColumns.accompanimentPerformer, _.accompanimentPerformer, 175.0),
      stringTableColumn(Resources.TableColumns.arrangementAuthor, _.arrangementAuthor, 175.0),
      stringTableColumn(Resources.TableColumns.conductor, _.conductor, 175.0),
      stringTableColumn(Resources.TableColumns.author, _.author, 150.0),
      stringTableColumn(Resources.TableColumns.soloist, _.soloist, 150.0),
      stringTableColumn(Resources.TableColumns.duration, _.duration, 50.0),
      stringTableColumn(Resources.TableColumns.source, _.source, 150.0),
      stringTableColumn(Resources.TableColumns.ethnographicRegion, _.ethnographicRegion, 200.0),
      stringTableColumn(Resources.TableColumns.note, _.note, 150.0)
    )

    if (inputTable) columns ++ List(stringTableColumn(Resources.TableColumns.file, _.file, 150.0))
    else columns ++ List(stringTableColumn(Resources.TableColumns.id, _.id, 150.0))
  }

  override def onSpaceKey(): Unit = buttonHandlers.play(inputTable)

  override def onF2Key(): Unit = openEditor()

  override def onCtrlShiftCopy(): Unit = buttonHandlers.copyProperties(inputTable)

  override def onCtrlShiftPaste(): Unit = buttonHandlers.applyProperties(inputTable)

  private def openEditor(): Unit = FolkloreTrackEditor(pure, pure.getSelectionModel.getSelectedIndex).open()
}
