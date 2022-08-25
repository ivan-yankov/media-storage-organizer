package yankov.mso.application

import Main.{createDataManager, getApplicationArgument}
import yankov.mso.application.model.DataModel.{AudioInput, AudioSearchSample, FolkloreTrack, SourceType}
import yankov.mso.application.ui.controls.AudioSearchControls
import yankov.mso.application.ui.toolbars.FolkloreToolbarButtonHandlers
import scalafx.scene.control.CheckBox
import yankov.mso.application.model.DataManager
import yankov.mso.application.model.DataModel.{FolkloreTrack, SourceType}
import yankov.mso.application.search.Search
import yankov.mso.application.ui.controls.{AudioSearchControls, AudioSearchTable, FolkloreControlsFactory, FolkloreTrackTable, LabeledComboBox, LabeledTextField, MetadataSearchControls, SearchControls}
import yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}

case class MainControls(dataManager: DataManager) {
  val buttonHandlers: FolkloreToolbarButtonHandlers = FolkloreToolbarButtonHandlers()

  val inputTable: FolkloreTrackTable = new FolkloreTrackTable(true, buttonHandlers)

  val searchTable: FolkloreTrackTable = new FolkloreTrackTable(false, buttonHandlers)

  val audioSearchTable: AudioSearchTable = new AudioSearchTable()

  val toolbarButtons: ToolbarButtons = ToolbarButtons(buttonHandlers)

  val metadataSearchControls: SearchControls[FolkloreTrack] = new MetadataSearchControls[FolkloreTrack](
    x => Search.metadataSearch(x, dataManager.getTracks, searchTable),
    () => FolkloreControlsFactory.createSearchVariable(),
    () => FolkloreControlsFactory.createSearchFilter()
  )

  val audioSearchControls: SearchControls[FolkloreTrack] = new AudioSearchControls(
    (files, correlation, crossCorrelationShift) => Search.audioSearch(
      files.map(y => AudioSearchSample(y.getName, AudioInput(y))),
      if (searchTable.nonEmpty) searchTable.getItems.map(x => x.track) else dataManager.getTracks,
      dataManager.audioIndex,
      audioSearchTable,
      correlation,
      crossCorrelationShift
    ),
    audioSearchTable
  )

  val extractDataFromFileNameRegex: LabeledTextField = LabeledTextField(
    labelText = Resources.MainForm.extractDataFromFileNameRegex,
    value = "(?<= - ).*"
  )

  val extractTitleFromFileNameCheckBox: CheckBox = new CheckBox {
    text = Resources.MainForm.extractTitleFromFileName
    selected = false
  }

  val sourceType: LabeledComboBox[SourceType] = FolkloreControlsFactory.createSourceTypeInput()

  val sourceLabel: LabeledTextField = LabeledTextField(Resources.Sources.label, "")

  val extractSourceSignatureFromFileNameCheckBox: CheckBox = new CheckBox {
    text = Resources.MainForm.extractSourceSignatureFromFileName
    selected = false
  }
}
