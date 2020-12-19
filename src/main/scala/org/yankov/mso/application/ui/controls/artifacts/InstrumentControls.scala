package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel.Instrument
import org.yankov.mso.application.ui.controls.LabeledTextField
import scalafx.scene.layout.{Pane, VBox}

case class InstrumentControls(containerId: String) extends ArtifactControls[Instrument](containerId) {
  private val instrument = LabeledTextField(Resources.Instruments.instrument, "")

  override protected def validateUserInput(): Boolean = {
    if (instrument.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Instruments.instrumentNameUndefined)
      false
    }
    else true
  }

  override protected def createArtifact(): Boolean = {
    dataManager.insertInstrument(
      Instrument(
        DataModel.invalidId,
        instrument.getValue
      )
    )
  }

  override protected def updateArtifact(artifact: Instrument): Unit =
    dataManager.updateInstrument(artifact)

  override protected def cleanup(): Unit =
    instrument.setValue("")

  override protected def artifactToString(artifact: Instrument): String =
    StringConverters.instrumentToString(artifact)

  override protected def getExistingArtifacts: List[Instrument] =
    dataManager.getInstruments

  override protected def onArtifactSelect(artifact: Instrument): Unit =
    instrument.setValue(artifact.name)

  override protected def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(instrument.getContainer)
  }
}
