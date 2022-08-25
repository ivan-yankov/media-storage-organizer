package yankov.mso.application.ui.controls.artifacts

import yankov.mso.application.Resources
import yankov.mso.application.converters.StringConverters.artistToString
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel._
import yankov.mso.application.ui.controls.{LabeledComboBox, LabeledTextField}
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.{Button, CheckBox, TitledPane}
import scalafx.scene.layout.{GridPane, Pane, VBox}
import yankov.mso.application.model.DataModel
import yankov.mso.application.model.DataModel.{Artist, ArtistMission, ChamberGroup, Choir, Composer, ConcertHost, Conductor, Ensemble, Instrument, InstrumentPlayer, Orchestra, Singer}

case class ArtistControls() extends ArtifactControls[Artist] {
  private val name = LabeledTextField(Resources.Artists.artistName, "")

  private val displayName = LabeledTextField(Resources.Artists.artistDisplayName, "", readOnly = true)

  private val artists = {
    LabeledComboBox[Artist](
      labelText = Resources.ArtifactsTab.artist,
      cbItems = () => getExistingArtifacts,
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
    (ChamberGroup, new CheckBox(Resources.Artists.chamberGroup)),
    (ConcertHost, new CheckBox(Resources.Artists.concertHost))
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
    members = List()
    enableInstruments()
  }

  override def artifactToString(artifact: Artist): String = artistToString(artifact)

  override def getExistingArtifacts: List[Artist] = dataManager.getArtists

  override def onArtifactSelect(artifact: Artist): Unit = {
    if (artifact != null) {
      name.setValue(artifact.name)
      displayName.setValue(artifact.composedName)
      note.setValue(artifact.note)
      missions.foreach(x => x._2.setSelected(artifact.missions.contains(x._1)))
      instruments.foreach(x => x._2.setSelected(artifact.instruments.map(_.id).contains(x._1)))
      setMembers(artifact.members)
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
        content = gridPane(List(artists.getContainer, btnAddMember, btnClearMembers), 3)
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
    getExistingArtifacts.exists(x => x.name.equalsIgnoreCase(name.getValue))

  override def refresh(): Unit = artists.refresh()

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
      onAction = _ => setMembers(members ++ List(artists.getValue))
    }
  }

  private def btnClearMembers: Button = {
    new Button {
      text = Resources.Artists.clearMembers
      onAction = _ => setMembers(List())
    }
  }

  private def selectedInstruments: List[Instrument] = {
    if (missions.find(x => x._1 == InstrumentPlayer).get._2.isSelected) {
      dataManager
        .getInstruments
        .filter(x => instruments.filter(y => y._2.isSelected).map(y => y._1).contains(x.id))
    }
    else List()
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

  private def setMembers(members: List[Artist]): Unit = {
    this.members = members
    displayName.setValue(Artist(members = this.members).composedName)
  }
}
