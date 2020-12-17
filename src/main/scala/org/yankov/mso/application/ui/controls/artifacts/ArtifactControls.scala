package org.yankov.mso.application.ui.controls.artifacts

import java.beans.{PropertyChangeEvent, PropertyChangeListener}

import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import org.yankov.mso.application.{Main, Resources}
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, ListView, SelectionMode}
import scalafx.scene.layout.{HBox, Pane, VBox}

abstract class ArtifactControls[T](containerId: String) extends PropertyChangeListener {
  protected def validateUserInput(): Boolean

  protected def updateArtifact(artifact: T): Unit

  protected def createArtifact(): Boolean

  protected def cleanup(): Unit

  protected def artifactToString(artifact: T): String

  protected def getExistingArtifacts: List[T]

  protected def onArtifactSelect(artifact: T): Unit

  protected def createControls(): Pane

  protected val whiteSpace: Double = 20.0

  protected val console: ConsoleService = ApplicationConsole

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
      createControls(),
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

  override def propertyChange(propertyChangeEvent: PropertyChangeEvent): Unit = refreshExistingArtifacts()

  def initialize(): Unit = {
    Main.dataManager.addPropertyChangeListener(this)

    existingArtifacts
      .selectionModel
      .value
      .selectedItemProperty()
      .addListener(_ => onArtifactSelect(existingArtifacts.getSelectionModel.getSelectedItem))
    existingArtifacts
      .selectionModel
      .value
      .setSelectionMode(SelectionMode.Single)

    refreshExistingArtifacts()
  }

  def getContainer: Pane = container

  private def handleBtnAdd(): Unit = {
    if (validateUserInput()) {
      if (createArtifact()) {
        cleanup()
        console.writeMessageWithTimestamp(Resources.Artifacts.artifactAdded)
      }
      else console.writeMessageWithTimestamp(Resources.Artifacts.artifactExists)
    }
  }

  private def handleBtnUpdate(): Unit = {
    if (validateUserInput()) {
      val selectedIndex = existingArtifacts.getSelectionModel.getSelectedIndex
      if (selectedIndex < 0) {
        console.writeMessageWithTimestamp(Resources.Artifacts.noSelectedArtifact)
      }
      else {
        updateArtifact(existingArtifacts.getItems.get(selectedIndex))
        refreshExistingArtifacts()
        console.writeMessageWithTimestamp(Resources.Artifacts.artifactUpdated)
      }
    }
  }

  private def refreshExistingArtifacts(): Unit = {
    existingArtifacts.getItems.clear()
    getExistingArtifacts
      .sortWith((x, y) => artifactToString(x).compareToIgnoreCase(artifactToString(y)) < 0)
      .foreach(x => existingArtifacts.getItems.add(x))
  }
}
