package org.yankov.mso.application.ui.controls.artifacts

import java.beans.{PropertyChangeEvent, PropertyChangeListener}

import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import org.yankov.mso.application.{Main, Resources}
import scalafx.geometry.Insets
import scalafx.scene.control.cell.TextFieldListCell
import scalafx.scene.control.{Button, Label, ListView, SelectionMode}
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.util.StringConverter

case class ArtifactControlsContainer[T](artifactControls: ArtifactControls[T], containerId: String) {
  private val console: ConsoleService = ApplicationConsole
  private val dataManager: DataManager = Main.dataManager

  private val existingArtifactsLabel = new Label {
    text = Resources.Artifacts.existingArtifacts
  }

  private val existingArtifacts = new ListView[T] {
    cellFactory = x => {
      val cell = new TextFieldListCell[T]()
      val converter = new StringConverter[T] {
        override def fromString(s: String): T = ???

        override def toString(x: T): String = artifactControls.artifactToString(x)
      }
      cell.setConverter(converter)
      cell
    }
  }

  private val btnAddArtifact = new Button {
    text = Resources.Artifacts.btnAddArtifact
    onAction = _ => handleBtnAdd()
  }

  private val btnUpdateArtifact = new Button {
    text = Resources.Artifacts.btnUpdateArtifact
    onAction = _ => handleBtnUpdate()
  }

  private val existingArtifactsContainer = new VBox {
    children = List(
      existingArtifactsLabel,
      existingArtifacts
    )
    minWidth = 350.0
  }

  private val btnContainer = new HBox {
    padding = Insets(20.0, 0.0, 20.0, 0.0)
    spacing = 20.0
    children = List(
      btnAddArtifact,
      btnUpdateArtifact
    )
  }

  private val actionControlsContainer = new VBox {
    children = List(
      artifactControls.createControls(),
      btnContainer
    )
    minWidth = 350.0
  }

  private val container = new HBox {
    id = containerId
    padding = Insets(20.0, 0.0, 20.0, 0.0)
    spacing = 20.0
    children = List(
      existingArtifactsContainer,
      actionControlsContainer
    )
  }

  init()

  def getContainer: Pane = container

  private def init(): Unit = {
    existingArtifacts
      .selectionModel
      .value
      .selectedItemProperty()
      .addListener(_ => artifactControls.onArtifactSelect(existingArtifacts.getSelectionModel.getSelectedItem))
    existingArtifacts
      .selectionModel
      .value
      .setSelectionMode(SelectionMode.Single)

    refreshExistingArtifacts()
  }

  private def handleBtnAdd(): Unit = {
    if (artifactControls.validateUserInput()) {
      if (artifactControls.artifactExists) console.writeMessageWithTimestamp(Resources.Artifacts.artifactExists)
      else if (artifactControls.createArtifact()) {
        artifactControls.cleanup()
        console.writeMessageWithTimestamp(Resources.Artifacts.artifactAdded)
      }
    }
    refreshExistingArtifacts()
  }

  private def handleBtnUpdate(): Unit = {
    if (artifactControls.validateUserInput()) {
      val selectedIndex = existingArtifacts.getSelectionModel.getSelectedIndex
      if (selectedIndex < 0) {
        console.writeMessageWithTimestamp(Resources.Artifacts.noSelectedArtifact)
      }
      else {
        val r = artifactControls.updateArtifact(existingArtifacts.getItems.get(selectedIndex))
        if (r) console.writeMessageWithTimestamp(Resources.Artifacts.artifactUpdated)
        else console.writeMessageWithTimestamp(Resources.Artifacts.artifactUpdateFailed)
      }
    }
    refreshExistingArtifacts()
  }

  private def refreshExistingArtifacts(): Unit = {
    existingArtifacts.getItems.clear()
    artifactControls.getExistingArtifacts
      .sortWith((x, y) => artifactControls.artifactToString(x).compareToIgnoreCase(artifactControls.artifactToString(y)) < 0)
      .foreach(x => existingArtifacts.getItems.add(x))
  }
}
