package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.ui.controls.{FolkloreControlsFactory, LabeledTextField}
import scalafx.scene.control.{CheckBox, TitledPane}
import scalafx.scene.layout.{HBox, Pane, VBox}

case class ArtistControls() extends ArtifactControls[Artist] {
  private val name = LabeledTextField(Resources.Artists.artistName, "")

  private val note = LabeledTextField(Resources.Artists.note, "")

  private val instrument = FolkloreControlsFactory.createInstrument()

  private val missions: List[(ArtistMission, CheckBox)] = List(
    (Singer, new CheckBox(Resources.Artists.singer)),
    (InstrumentPlayer, new CheckBox(Resources.Artists.instrumentPlayer)),
    (Composer, new CheckBox(Resources.Artists.composer)),
    (Conductor, new CheckBox(Resources.Artists.conductor)),
    (Orchestra, new CheckBox(Resources.Artists.orchestra)),
    (Choir, new CheckBox(Resources.Artists.choir)),
    (Ensemble, new CheckBox(Resources.Artists.ensemble)),
    (ChamberGroup, new CheckBox(Resources.Artists.chamberGroup))
  ).map(x => {
    x._2.setOnAction(_ => enableInstrument())
    (x._1, x._2)
  })

  init()

  override def validateUserInput(): Boolean = {
    if (name.getValue.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Artists.artistNameUndefined)
      false
    }
    else if (selectedMissions.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Artists.noArtistMissionSelected)
      false
    }
    else if (selectedMissions.contains(InstrumentPlayer) && instrument.getValue.name.isEmpty) {
      console.writeMessageWithTimestamp(Resources.Artists.artistInstrumentUndefined)
      false
    }
    else true
  }

  override def createArtifact(): Boolean = {
    dataManager.insertArtist(
      Artist(
        id = DataModel.invalidId,
        name = name.getValue,
        instrument = instrument.getValue,
        note = note.getValue,
        missions = selectedMissions
      )
    )
  }

  override def updateArtifact(artifact: Artist): Boolean =
    dataManager.updateArtist(artifact)

  override def cleanup(): Unit = {
    name.setValue("")
    note.setValue("")
    missions.foreach(x => x._2.setSelected(false))
    instrument.clear()
    enableInstrument()
  }

  override def artifactToString(artifact: Artist): String =
    StringConverters.artistToString(artifact)

  override def getExistingArtifacts: List[Artist] =
    dataManager.getArtists

  override def onArtifactSelect(artifact: Artist): Unit = {
    name.setValue(artifact.name)
    note.setValue(artifact.note)
    missions.foreach(x => x._2.setSelected(artifact.missions.contains(x._1)))
    instrument.setValue(artifact.instrument)
    enableInstrument()
  }

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children = List(
      name.getContainer,
      note.getContainer,
      new TitledPane {
        text = Resources.Artists.missions
        content = new HBox {
          spacing = whiteSpace
          missions.map(x => x._2).foreach(x => children.add(x))
        }
        collapsible = false
      },
      instrument.getContainer
    )
  }

  override def artifactExists: Boolean =
    getExistingArtifacts.exists(x => x.name.equalsIgnoreCase(name.getValue))

  private def init(): Unit = {
    enableInstrument()
  }

  private def selectedMissions: List[ArtistMission] =
    missions.filter(x => x._2.isSelected).map(x => x._1)

  private def enableInstrument(): Unit =
    instrument.setDisable(selectedMissions.contains(InstrumentPlayer))
}
