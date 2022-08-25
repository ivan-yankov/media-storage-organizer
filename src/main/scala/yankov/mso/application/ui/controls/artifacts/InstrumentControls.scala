package yankov.mso.application.ui.controls.artifacts

import scalafx.scene.layout.{Pane, VBox}
import yankov.mso.application.Resources
import yankov.mso.application.converters.StringConverters
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel.Instrument
import yankov.mso.application.ui.controls.LabeledTextField

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
    dataManager.updateInstrument(Instrument(artifact.id, instrument.getValue))

  override def cleanup(): Unit =
    instrument.setValue("")

  override def artifactToString(artifact: Instrument): String =
    StringConverters.instrumentToString(artifact)

  override def getExistingArtifacts: List[Instrument] =
    dataManager.getInstruments

  override def onArtifactSelect(artifact: Instrument): Unit =
    if (artifact != null) instrument.setValue(artifact.name)

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children.add(instrument.getContainer)
  }

  override def artifactExists: Boolean =
    getExistingArtifacts.exists(x => x.name.equalsIgnoreCase(instrument.getValue))
}
