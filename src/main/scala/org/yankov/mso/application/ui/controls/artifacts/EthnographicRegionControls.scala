package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel.EthnographicRegion
import org.yankov.mso.application.ui.controls.LabeledTextField
import scalafx.scene.layout.{Pane, VBox}

case class EthnographicRegionControls() extends ArtifactControls[EthnographicRegion] {
  private val ethnographicRegion: LabeledTextField = LabeledTextField(Resources.EthnographicRegions.name, "")

  override def validateUserInput(): Boolean = {
    if (ethnographicRegion.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.EthnographicRegions.nameUndefined)
      false
    }
    else true
  }

  override def createArtifact(): Boolean = {
    dataManager.insertEthnographicRegion(
      EthnographicRegion(
        DataModel.invalidId,
        ethnographicRegion.getValue
      )
    )
  }

  override def updateArtifact(artifact: EthnographicRegion): Boolean =
    dataManager.updateEthnographicRegion(artifact)

  override def cleanup(): Unit =
    ethnographicRegion.setValue("")

  override def artifactToString(artifact: EthnographicRegion): String =
    StringConverters.ethnographicRegionToString(artifact)

  override def getExistingArtifacts: List[EthnographicRegion] =
    dataManager.getEthnographicRegions

  override def onArtifactSelect(artifact: EthnographicRegion): Unit =
    ethnographicRegion.setValue(artifact.name)

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(ethnographicRegion.getContainer)
  }
}
