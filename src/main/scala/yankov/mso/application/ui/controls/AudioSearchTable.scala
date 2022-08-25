package yankov.mso.application.ui.controls

import scalafx.scene.control.{SelectionMode, TableColumn}
import yankov.mso.application.Resources
import yankov.mso.application.model.UiModel.TrackTableProperties

class AudioSearchTable() extends UiTable[TrackTableProperties] {
  override def isEditable: Boolean = false

  override def selectionMode: SelectionMode = SelectionMode.Multiple

  override def cellSelectionEnabled: Boolean = true

  override def onRowDoubleClick(): Unit = {}

  override def tableUserData: AnyRef = null

  override def createTableColumns: List[(TableColumn[TrackTableProperties, String], Double)] = {
    List(
      stringTableColumn(Resources.TableColumns.sampleId, _.sampleId, 300.0),
      stringTableColumn(Resources.TableColumns.matchType, _.matchType, 125.0),
      stringTableColumn(Resources.TableColumns.title, _.title, 200.0),
      stringTableColumn(Resources.TableColumns.performer, _.performer, 175.0),
      stringTableColumn(Resources.TableColumns.accompanimentPerformer, _.accompanimentPerformer, 175.0),
      stringTableColumn(Resources.TableColumns.arrangementAuthor, _.arrangementAuthor, 175.0),
      stringTableColumn(Resources.TableColumns.conductor, _.conductor, 175.0),
      stringTableColumn(Resources.TableColumns.source, _.source, 150.0),
      stringTableColumn(Resources.TableColumns.id, _.id, 150.0),
      stringTableColumn(Resources.TableColumns.correlation, _.correlation, 100.0),
    )
  }
}
