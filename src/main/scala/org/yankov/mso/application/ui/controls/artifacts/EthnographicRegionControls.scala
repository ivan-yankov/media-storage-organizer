package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel.EthnographicRegion
import org.yankov.mso.application.model.EmptyValues
import org.yankov.mso.application.ui.controls.LabeledTextField
import scalafx.scene.layout.{Pane, VBox}

case class EthnographicRegionControls(containerId: String) extends ArtifactControls[EthnographicRegion](containerId) {
  private val ethnographicRegion: LabeledTextField = LabeledTextField(Resources.EthnographicRegions.name, "")

  override protected def validateUserInput(): Boolean = {
    if (ethnographicRegion.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.EthnographicRegions.nameUndefined)
      false
    }
    else true
  }

  override protected def createArtifact(): Boolean = {
    dataManager.insertEthnographicRegion(
      EthnographicRegion(
        EmptyValues.invalidId,
        ethnographicRegion.getValue
      )
    )
  }

  override protected def updateArtifact(artifact: EthnographicRegion): Unit =
    dataManager.updateEthnographicRegion(artifact)

  override protected def cleanup(): Unit =
    ethnographicRegion.setValue("")

  override protected def artifactToString(artifact: EthnographicRegion): String =
    StringConverters.ethnographicRegionToString(Option(artifact))

  override protected def getExistingArtifacts: List[EthnographicRegion] =
    dataManager.getEthnographicRegions

  override protected def onArtifactSelect(artifact: EthnographicRegion): Unit =
    ethnographicRegion.setValue(artifact.name)

  override protected def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(ethnographicRegion.getContainer)
  }
}
