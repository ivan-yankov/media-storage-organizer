package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.FolkloreTrackEditor
import org.yankov.mso.application.{MergeTrack, Resources}
import scalafx.scene.control._

case class FolkloreTrackTable(inputTable: Boolean) extends UiTable[FolkloreTrackProperties] {
  override def isEditable: Boolean = false

  override def selectionMode: SelectionMode = SelectionMode.Multiple

  override def cellSelectionEnabled: Boolean = true

  override def onRowDoubleClick(): Unit = FolkloreTrackEditor(pure, pure.getSelectionModel.getSelectedIndex).open()

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

  override def columnWidths: List[Double] =
    List(200, 175, 175, 175, 175, 150, 150, 50, 150, 150, 200, 150).map(x => x.toDouble)

  override def createTableColumns: List[TableColumn[FolkloreTrackProperties, _]] = {
    val columns = List(
      stringTableColumn(Resources.TableColumns.title, _.title),
      stringTableColumn(Resources.TableColumns.performer, _.performer),
      stringTableColumn(Resources.TableColumns.accompanimentPerformer, _.accompanimentPerformer),
      stringTableColumn(Resources.TableColumns.arrangementAuthor, _.arrangementAuthor),
      stringTableColumn(Resources.TableColumns.conductor, _.conductor),
      stringTableColumn(Resources.TableColumns.author, _.author),
      stringTableColumn(Resources.TableColumns.soloist, _.soloist),
      stringTableColumn(Resources.TableColumns.duration, _.duration),
      stringTableColumn(Resources.TableColumns.source, _.source),
      stringTableColumn(Resources.TableColumns.ethnographicRegion, _.ethnographicRegion),
      stringTableColumn(Resources.TableColumns.note, _.note)
    )

    if (inputTable) columns ++ List(stringTableColumn(Resources.TableColumns.file, _.file))
    else columns
  }
}
