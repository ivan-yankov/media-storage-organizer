package yankov.mso.application.ui.controls.artifacts

import yankov.mso.application.Resources
import yankov.mso.application.converters.StringConverters
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel.EthnographicRegion
import yankov.mso.application.ui.controls.LabeledTextField
import scalafx.scene.layout.{Pane, VBox}
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel.EthnographicRegion
import yankov.mso.application.ui.controls.LabeledTextField

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
    dataManager.updateEthnographicRegion(EthnographicRegion(artifact.id, ethnographicRegion.getValue))

  override def cleanup(): Unit =
    ethnographicRegion.setValue("")

  override def artifactToString(artifact: EthnographicRegion): String =
    StringConverters.ethnographicRegionToString(artifact)

  override def getExistingArtifacts: List[EthnographicRegion] =
    dataManager.getEthnographicRegions

  override def onArtifactSelect(artifact: EthnographicRegion): Unit =
    if (artifact != null) ethnographicRegion.setValue(artifact.name)

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(ethnographicRegion.getContainer)
  }

  override def artifactExists: Boolean =
    getExistingArtifacts.exists(x => x.name.equalsIgnoreCase(ethnographicRegion.getValue))
}
