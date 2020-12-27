package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel.Instrument
import org.yankov.mso.application.ui.controls.LabeledTextField
import scalafx.scene.layout.{Pane, VBox}

case class InstrumentControls() extends ArtifactControls[Instrument] {
  private val instrument = LabeledTextField(Resources.Instruments.instrument, "")

  override def validateUserInput(): Boolean = {
    if (instrument.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Instruments.instrumentNameUndefined)
      false
    }
    else true
  }

  override def createArtifact(): Boolean = {
    dataManager.insertInstrument(
      Instrument(
        DataModel.invalidId,
        instrument.getValue
      )
    )
  }

  override def updateArtifact(artifact: Instrument): Boolean =
    dataManager.updateInstrument(artifact)

  override def cleanup(): Unit =
    instrument.setValue("")

  override def artifactToString(artifact: Instrument): String =
    StringConverters.instrumentToString(artifact)

  override def getExistingArtifacts: List[Instrument] =
    dataManager.getInstruments

  override def onArtifactSelect(artifact: Instrument): Unit =
    instrument.setValue(artifact.name)

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(instrument.getContainer)
  }
}
