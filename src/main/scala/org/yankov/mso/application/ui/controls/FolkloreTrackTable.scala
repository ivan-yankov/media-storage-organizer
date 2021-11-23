package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.FolkloreTrackEditor
import org.yankov.mso.application.ui.controls.FontModel.{CustomFont, NormalStyle, NormalWeight, SansSerif}
import org.yankov.mso.application.{MergeTrack, Resources}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.{Pane, StackPane}

case class FolkloreTrackTable(inputTable: Boolean) {
  private val tableFont = CustomFont(SansSerif, NormalStyle, NormalWeight, 12)

  private val table: TableView[FolkloreTrackProperties] = {
    val t = new TableView[FolkloreTrackProperties] {
      editable = false
      selectionModel.value.setSelectionMode(SelectionMode.Multiple)
      selectionModel.value.setCellSelectionEnabled(true)

      createTableColumns.foreach(x => columns.add(x))
      columns.foreach(x => x.setStyle(tableFont.cssRepresentation))

      items = new ObservableBuffer[FolkloreTrackProperties]()

      rowFactory = _ => new TableRow[FolkloreTrackProperties] {
        onMouseClicked = event =>
          if (event.getClickCount == 2) FolkloreTrackEditor(table, table.getSelectionModel.getSelectedIndex).open()
      }

      userData = Map[Int, MergeTrack](
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

    setColumnSizes(t)

    t
  }

  private val container: StackPane = new StackPane {
    children = List(table)
  }

  private def createTableColumns: List[TableColumn[FolkloreTrackProperties, _]] = {
    val columns = List(
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.title
        cellValueFactory = {
          _.value.title
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.performer
        cellValueFactory = {
          _.value.performer
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.accompanimentPerformer
        cellValueFactory = {
          _.value.accompanimentPerformer
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.arrangementAuthor
        cellValueFactory = {
          _.value.arrangementAuthor
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.conductor
        cellValueFactory = {
          _.value.conductor
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.author
        cellValueFactory = {
          _.value.author
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.soloist
        cellValueFactory = {
          _.value.soloist
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.duration
        cellValueFactory = {
          _.value.duration
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.source
        cellValueFactory = {
          _.value.source
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.ethnographicRegion
        cellValueFactory = {
          _.value.ethnographicRegion
        }
      },
      new TableColumn[FolkloreTrackProperties, String] {
        text = Resources.TableColumns.note
        cellValueFactory = {
          _.value.note
        }
      }
    )

    val fileColumn = new TableColumn[FolkloreTrackProperties, String] {
      text = Resources.TableColumns.file
      cellValueFactory = {
        _.value.file
      }
    }

    if (inputTable) columns ++ List(fileColumn)
    else columns
  }

  private lazy val columnWidths: List[Double] =
    List(200, 175, 175, 175, 175, 150, 150, 50, 150, 150, 200, 150)
      .map(x => x.toDouble)

  private def setColumnSizes(table: TableView[_]): Unit = {
    val widths = columnWidths.take(table.columns.size)
    table
      .columns
      .zip(widths)
      .foreach(x => x._1.setPrefWidth(x._2))
  }

  def getContainer: Pane = container

  def getValue: TableView[FolkloreTrackProperties] = table
}
