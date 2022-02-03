package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.converters.StringConverters.artistToString
import org.yankov.mso.application.model.DataModel
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.ui.controls.{LabeledComboBox, LabeledTextField}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Node
import scalafx.scene.control.{Button, CheckBox, TitledPane}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}

case class ArtistControls() extends ArtifactControls[Artist] {
  private val name = LabeledTextField(Resources.Artists.artistName, "")

  private val displayName = LabeledTextField(Resources.Artists.artistDisplayName, "", readOnly = true)

  private val artists = {
    LabeledComboBox[Artist](
      labelText = Resources.ArtifactsTab.artist,
      cbItems = getExistingArtifacts,
      value = Artist(),
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  private val note = LabeledTextField(Resources.Artists.note, "")

  private val instruments = dataManager.getInstruments.map(x => x.id -> new CheckBox(x.name))

  private val missions: List[(ArtistMission, CheckBox)] = List(
    (Singer, new CheckBox(Resources.Artists.singer)),
    (InstrumentPlayer, new CheckBox(Resources.Artists.instrumentPlayer)),
    (Composer, new CheckBox(Resources.Artists.composer)),
    (Conductor, new CheckBox(Resources.Artists.conductor)),
    (Orchestra, new CheckBox(Resources.Artists.orchestra)),
    (Choir, new CheckBox(Resources.Artists.choir)),
    (Ensemble, new CheckBox(Resources.Artists.ensemble)),
    (ChamberGroup, new CheckBox(Resources.Artists.chamberGroup))
  ).map(
    x => {
      x._2.setOnAction(_ => enableInstruments())
      (x._1, x._2)
    }
  )

  private var members: List[Artist] = List()

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
    else if (selectedMissions.contains(InstrumentPlayer) && instruments.forall(x => !x._2.isSelected)) {
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
        instruments = selectedInstruments,
        note = note.getValue,
        missions = selectedMissions,
        members = members
      )
    )
  }

  override def updateArtifact(artifact: Artist): Boolean = {
    dataManager.updateArtist(
      Artist(
        id = artifact.id,
        name = name.getValue,
        instruments = selectedInstruments,
        note = note.getValue,
        missions = selectedMissions,
        members = members
      )
    )
  }

  override def cleanup(): Unit = {
    name.setValue("")
    displayName.setValue("")
    note.setValue("")
    missions.foreach(x => x._2.setSelected(false))
    instruments.foreach(x => x._2.setSelected(false))
    enableInstruments()
  }

  override def artifactToString(artifact: Artist): String = StringConverters.artistToString(artifact)

  override def getExistingArtifacts: List[Artist] = dataManager.getArtists

  override def onArtifactSelect(artifact: Artist): Unit = {
    if (artifact != null) {
      name.setValue(artifact.name)
      displayName.setValue(artifact.displayName)
      note.setValue(artifact.note)
      missions.foreach(x => x._2.setSelected(artifact.missions.contains(x._1)))
      instruments.foreach(x => x._2.setSelected(artifact.instruments.map(_.id).contains(x._1)))
      enableInstruments()
    }
  }

  override def createControls(): Pane = new VBox {
    spacing = whiteSpace
    children = List(
      name.getContainer,
      displayName.getContainer,
      new TitledPane {
        text = Resources.Artists.members
        content = gridPane(List(artists.getContainer, btnAddMember), 2)
        collapsible = false
      },
      note.getContainer,
      new TitledPane {
        text = Resources.Artists.missions
        content = gridPane(missions.map(x => x._2), missions.size)
        collapsible = false
      },
      new TitledPane {
        text = Resources.Artists.instruments
        content = gridPane(instruments.map(x => x._2).sortBy(_.text.value), 10)
        collapsible = true
      }
    )
  }

  override def artifactExists: Boolean =
    getExistingArtifacts.exists(x => x.displayName.equalsIgnoreCase(name.getValue))

  private def init(): Unit = {
    enableInstruments()
  }

  private def selectedMissions: List[ArtistMission] =
    missions.filter(x => x._2.isSelected).map(x => x._1)

  private def enableInstruments(): Unit =
    instruments.foreach(x => x._2.setDisable(!selectedMissions.contains(InstrumentPlayer)))

  private def btnAddMember: Button = {
    new Button {
      text = Resources.Artists.addMember
      onAction = _ => {
        addMember(artists.getValue)
        displayName.setValue(Artist(members = members).displayName)
      }
    }
  }

  private def addMember(artist: Artist): Unit = members = artist :: members

  private def selectedInstruments: List[Instrument] = {
    dataManager
      .getInstruments
      .filter(x => instruments.filter(y => y._2.isSelected).map(y => y._1).contains(x.id))
  }

  private def gridPane(items: List[Node], numberOfColumns: Int): Pane = {
    val gp = new GridPane()
    items
      .sliding(numberOfColumns, numberOfColumns)
      .zipWithIndex
      .foreach(x => x._1.zipWithIndex.foreach(y => gp.add(y._1, y._2, x._2)))

    gp.setPadding(Insets(whiteSpace))
    gp.setHgap(whiteSpace)
    gp.setVgap(whiteSpace)

    gp
  }
}
