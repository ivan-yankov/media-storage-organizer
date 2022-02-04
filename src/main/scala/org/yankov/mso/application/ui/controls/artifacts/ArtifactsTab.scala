package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.ui.controls.LabeledComboBox
import scalafx.geometry.Insets
import scalafx.scene.layout.{Pane, StackPane, VBox}

case class ArtifactsTab() {
  private val artifacts = List(
    Resources.ArtifactsTab.source,
    Resources.ArtifactsTab.instrument,
    Resources.ArtifactsTab.artist,
    Resources.ArtifactsTab.ethnographicRegion
  )

  private val sourceId = "source"
  private val instrumentId = "instrument"
  private val artistId = "artis"
  private val ethnographicRegionId = "ethnographic-region"

  private val artifactIds = Map(
    Resources.ArtifactsTab.source -> sourceId,
    Resources.ArtifactsTab.instrument -> instrumentId,
    Resources.ArtifactsTab.artist -> artistId,
    Resources.ArtifactsTab.ethnographicRegion -> ethnographicRegionId
  )

  private val defaultArtifact = artifacts.head

  private val artifactType = LabeledComboBox[String](
    labelText = Resources.ArtifactsTab.artifactType,
    cbItems = () => artifacts,
    value = defaultArtifact,
    itemToString = x => x,
    sortItems = false,
    emptyValue = Option.empty
  )

  private val inputControls = List(
    ArtifactControlsContainer(SourceControls(), sourceId),
    ArtifactControlsContainer(InstrumentControls(), instrumentId),
    ArtifactControlsContainer(ArtistControls(), artistId),
    ArtifactControlsContainer(EthnographicRegionControls(), ethnographicRegionId)
  )

  private val artifactsContainer = new StackPane {
    inputControls.foreach(x => children.add(x.getContainer))
  }

  private val container = new VBox {
    padding = Insets(25.0)
    children.add(artifactType.getContainer)
    children.add(artifactsContainer)
  }

  init()

  def getContainer: Pane = container

  private def init(): Unit = {
    artifactType.setOnSelect(x => refreshArtifactContainer(x))
    refreshArtifactContainer(defaultArtifact)
  }

  private def refreshArtifactContainer(artifactType: String): Unit = {
    artifactsContainer
      .getChildren
      .forEach(x => x.setVisible(x.getId.equals(artifactIds.getOrElse(artifactType, ""))))
  }
}
