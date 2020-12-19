package org.yankov.mso.application.ui.controls.artifacts

import java.beans.{PropertyChangeEvent, PropertyChangeListener}

import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import org.yankov.mso.application.{Main, Resources}
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ListView, SelectionMode}
import scalafx.scene.layout.{HBox, Pane, VBox}

case class ArtifactControlsContainer[T](artifactControls: ArtifactControls[T], containerId: String) extends PropertyChangeListener {
  private val console: ConsoleService = ApplicationConsole
  private val dataManager: DataManager = Main.dataManager

  private val existingArtifactsLabel = new Label {
    text = Resources.Artifacts.existingArtifacts
  }

  private val existingArtifacts = new ListView[T]

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

  override def propertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = refreshExistingArtifacts()

  def getContainer: Pane = container

  private def init(): Unit = {
    dataManager.addPropertyChangeListener(this)

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
      if (artifactControls.createArtifact()) {
        artifactControls.cleanup()
        console.writeMessageWithTimestamp(Resources.Artifacts.artifactAdded)
      }
      else console.writeMessageWithTimestamp(Resources.Artifacts.artifactExists)
    }
  }

  private def handleBtnUpdate(): Unit = {
    if (artifactControls.validateUserInput()) {
      val selectedIndex = existingArtifacts.getSelectionModel.getSelectedIndex
      if (selectedIndex < 0) {
        console.writeMessageWithTimestamp(Resources.Artifacts.noSelectedArtifact)
      }
      else {
        artifactControls.updateArtifact(existingArtifacts.getItems.get(selectedIndex))
        console.writeMessageWithTimestamp(Resources.Artifacts.artifactUpdated)
      }
    }
  }

  private def refreshExistingArtifacts(): Unit = {
    existingArtifacts.getItems.clear()
    artifactControls.getExistingArtifacts
      .sortWith((x, y) => artifactControls.artifactToString(x).compareToIgnoreCase(artifactControls.artifactToString(y)) < 0)
      .foreach(x => existingArtifacts.getItems.add(x))
  }
}
