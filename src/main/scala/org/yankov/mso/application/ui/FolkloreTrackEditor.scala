package org.yankov.mso.application.ui

import org.yankov.mso.application.{Main, Resources}
import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.model.UiModel.FolkloreTrackProperties
import org.yankov.mso.application.ui.controls.FolkloreControlsFactory._
import org.yankov.mso.application.ui.controls.{FileSelector, LabeledTextField}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TableView}
import scalafx.scene.layout.{GridPane, HBox, StackPane, VBox}
import scalafx.stage.{Modality, Stage}

case class FolkloreTrackEditor(table: TableView[FolkloreTrackProperties], trackIndex: Int) {
  private val title = LabeledTextField(Resources.TableColumns.title, track.title)
  private val performer = createPerformer(track.performer)
  private val accompanimentPerformer = createAccompanimentPerformer(track.accompanimentPerformer)
  private val arrangementAuthor = createArrangementAuthor(track.arrangementAuthor)
  private val conductor = createConductor(track.conductor)
  private val author = createAuthor(track.author)
  private val soloist = createSoloist(track.soloist)
  private val ethnographicRegion = createEthnographicRegion(track.ethnographicRegion)
  private val source = createSource(track.source)
  private val note = LabeledTextField(Resources.TableColumns.note, track.note)
  private val fileSelector = FileSelector(Resources.TableColumns.file, track.file)

  private val margins = Insets(25.0)
  private val horizontalSpace = 25.0
  private val verticalSpace = 25.0
  private val formButtonWidth = 150.0

  private val gridPane = {
    val gp = new GridPane()

    gp.add(title.getContainer, 0, 0, 4, 1)
    gp.add(performer.getContainer, 0, 1)
    gp.add(accompanimentPerformer.getContainer, 1, 1)
    gp.add(arrangementAuthor.getContainer, 2, 1)
    gp.add(conductor.getContainer, 3, 1)
    gp.add(author.getContainer, 0, 2)
    gp.add(soloist.getContainer, 1, 2)
    gp.add(ethnographicRegion.getContainer, 0, 3)
    gp.add(source.getContainer, 1, 3)
    gp.add(note.getContainer, 2, 3)
    gp.add(fileSelector.getContainer, 3, 3)

    gp.setPadding(margins)
    gp.setAlignment(Pos.BaselineLeft)
    gp.setHgap(horizontalSpace)
    gp.setVgap(verticalSpace)

    gp
  }

  private val btnOk = new Button {
    text = Resources.FormButtons.ok
    prefWidth = formButtonWidth
    defaultButton = true
    onAction = _ => handleOk()
  }

  private val btnCancel = new Button {
    text = Resources.FormButtons.cancel
    prefWidth = formButtonWidth
    cancelButton = true
    onAction = _ => handleCancel()
  }

  private val buttons = {
    new HBox {
      spacing = horizontalSpace
      padding = margins
      children = List(
        btnOk,
        btnCancel
      )
      alignment = Pos.Center
    }
  }

  private val container = new VBox {
    children = List(
      gridPane,
      buttons
    )
  }

  private val stage = {
    val st = new Stage {
      title = Resources.TrackEditor.title
      scene = new Scene {
        root = {
          new StackPane {
            children = List(
              container
            )
          }
        }
      }
    }

    st.initOwner(Main.stage)
    st.initModality(Modality.ApplicationModal)

    st
  }

  def open(): Unit = stage.showAndWait()

  private def track: FolkloreTrack = table.getItems.get(trackIndex).track

  private def handleOk(): Unit = {
    val newTrack = FolkloreTrack(
      id = track.id,
      title = title.getValue,
      performer = performer.getValue,
      accompanimentPerformer = accompanimentPerformer.getValue,
      author = author.getValue,
      arrangementAuthor = arrangementAuthor.getValue,
      conductor = conductor.getValue,
      soloist = soloist.getValue,
      duration = Utils.calculateDuration(fileSelector.getValue),
      note = note.getValue,
      source = source.getValue,
      ethnographicRegion = ethnographicRegion.getValue,
      file = fileSelector.getValue
    )

    table.getItems.set(trackIndex, FolkloreTrackProperties(newTrack))
  }

  private def handleCancel(): Unit = stage.close()
}
