package yankov.mso.application.ui.controls

import scalafx.scene.control._
import yankov.mso.application.model.UiModel.TrackTableProperties
import yankov.mso.application.ui.FolkloreTrackEditor
import yankov.mso.application.ui.toolbars.FolkloreToolbarButtonHandlers
import yankov.mso.application.{MergeTrack, Resources}

class FolkloreTrackTable(inputTable: Boolean, buttonHandlers: FolkloreToolbarButtonHandlers) extends UiTable[TrackTableProperties] {
  object ColumnKeys {
    val title: String = "title"
    val performer: String = "performer"
    val accompanimentPerformer: String = "accompanimentPerformer"
    val arrangementAuthor: String = "arrangementAuthor"
    val conductor: String = "conductor"
    val author: String = "author"
    val soloist: String = "soloist"
    val duration: String = "duration"
    val source: String = "source"
    val ethnographicRegion: String = "ethnographicRegion"
    val note: String = "note"
    val file: String = "file"
    val id = "id"
  }
  import ColumnKeys._

  override def isEditable: Boolean = false

  override def selectionMode: SelectionMode = SelectionMode.Multiple

  override def cellSelectionEnabled: Boolean = true

  override def onRowDoubleClick(): Unit = openEditor()

  override def tableUserData: AnyRef = {
    Map[String, MergeTrack](
      performer -> ((src, dest) => dest.copy(performer = src.performer)),
      accompanimentPerformer -> ((src, dest) => dest.copy(accompanimentPerformer = src.accompanimentPerformer)),
      arrangementAuthor -> ((src, dest) => dest.copy(arrangementAuthor = src.arrangementAuthor)),
      conductor -> ((src, dest) => dest.copy(conductor = src.conductor)),
      author -> ((src, dest) => dest.copy(author = src.author)),
      soloist -> ((src, dest) => dest.copy(soloist = src.soloist)),
      source -> ((src, dest) => dest.copy(source = src.source)),
      ethnographicRegion -> ((src, dest) => dest.copy(ethnographicRegion = src.ethnographicRegion)),
      note -> ((src, dest) => dest.copy(note = src.note))
    )
  }

  override def createTableColumns: List[(TableColumn[TrackTableProperties, String], Double)] = {
    List(
      stringTableColumn(Resources.TableColumns.title, _.title, 200.0, title),
      stringTableColumn(Resources.TableColumns.performer, _.performer, 175.0, performer),
      stringTableColumn(Resources.TableColumns.accompanimentPerformer, _.accompanimentPerformer, 175.0, accompanimentPerformer),
      stringTableColumn(Resources.TableColumns.arrangementAuthor, _.arrangementAuthor, 175.0, arrangementAuthor),
      stringTableColumn(Resources.TableColumns.conductor, _.conductor, 175.0, conductor),
      stringTableColumn(Resources.TableColumns.author, _.author, 150.0, author, isVisible = false),
      stringTableColumn(Resources.TableColumns.soloist, _.soloist, 150.0, soloist, isVisible = false),
      stringTableColumn(Resources.TableColumns.duration, _.duration, 50.0, duration),
      stringTableColumn(Resources.TableColumns.source, _.source, 250.0, source),
      stringTableColumn(Resources.TableColumns.ethnographicRegion, _.ethnographicRegion, 200.0, ethnographicRegion),
      stringTableColumn(Resources.TableColumns.note, _.note, 200.0, note),
      stringTableColumn(Resources.TableColumns.file, _.file, 200.0, file, isVisible = inputTable),
      stringTableColumn(Resources.TableColumns.id, _.id, 150.0, id, isVisible = !inputTable)
    )
  }

  override def onSpaceKey(): Unit = buttonHandlers.play(inputTable)

  override def onF2Key(): Unit = openEditor()

  override def onCtrlShiftCopy(): Unit = buttonHandlers.copyProperties(inputTable)

  override def onCtrlShiftPaste(): Unit = buttonHandlers.applyProperties(inputTable)

  private def openEditor(): Unit = FolkloreTrackEditor(pure, pure.getSelectionModel.getSelectedIndex).open()
}
