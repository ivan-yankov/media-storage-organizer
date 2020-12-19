package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel.Source
import org.yankov.mso.application.ui.controls.{FolkloreControlsFactory, LabeledTextField}
import scalafx.scene.layout.{Pane, VBox}

case class SourceControls(containerId: String) extends ArtifactControls[Source](containerId) {
  private val sourceType = FolkloreControlsFactory.createSourceTypeInput()

  private val sourceSignature = LabeledTextField(Resources.Sources.signature, "")

  override protected def validateUserInput(): Boolean = {
    if (sourceSignature.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Sources.signatureUndefined)
      false
    }
    else true
  }

  override protected def createArtifact(): Boolean = {
    dataManager.insertSource(
      Source(
        DataModel.invalidId,
        sourceType.getValue,
        sourceSignature.getValue
      )
    )
  }

  override protected def updateArtifact(artifact: Source): Unit =
    dataManager.updateSource(artifact)

  override protected def cleanup(): Unit =
    sourceSignature.setValue("")

  override protected def artifactToString(artifact: Source): String =
    StringConverters.sourceToString(artifact)

  override protected def getExistingArtifacts: List[Source] =
    dataManager.getSources

  override protected def onArtifactSelect(artifact: Source): Unit = {
    sourceType.setValue(artifact.sourceType)
    sourceSignature.setValue(artifact.signature)
  }

  override protected def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(sourceType.getContainer)
    children.add(sourceSignature.getContainer)
  }
}
