package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel.Source
import org.yankov.mso.application.ui.controls.{FolkloreControlsFactory, LabeledTextField}
import scalafx.scene.layout.{Pane, VBox}

case class SourceControls() extends ArtifactControls[Source] {
  private val sourceType = FolkloreControlsFactory.createSourceTypeInput()

  private val sourceSignature = LabeledTextField(Resources.Sources.signature, "")

  override def validateUserInput(): Boolean = {
    if (sourceSignature.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Sources.signatureUndefined)
      false
    }
    else true
  }

  override def createArtifact(): Boolean = {
    dataManager.insertSource(
      Source(
        DataModel.invalidId,
        sourceType.getValue,
        sourceSignature.getValue
      )
    )
  }

  override def updateArtifact(artifact: Source): Unit =
    dataManager.updateSource(artifact)

  override def cleanup(): Unit =
    sourceSignature.setValue("")

  override def artifactToString(artifact: Source): String =
    StringConverters.sourceToString(artifact)

  override def getExistingArtifacts: List[Source] =
    dataManager.getSources

  override def onArtifactSelect(artifact: Source): Unit = {
    sourceType.setValue(artifact.sourceType)
    sourceSignature.setValue(artifact.signature)
  }

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(sourceType.getContainer)
    children.add(sourceSignature.getContainer)
  }
}
