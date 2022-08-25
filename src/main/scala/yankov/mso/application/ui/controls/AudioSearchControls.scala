package yankov.mso.application.ui.controls

import scalafx.scene.control.{Button, TitledPane}
import yankov.mso.application.Main.dataManager
import yankov.mso.application.Resources.Search
import yankov.mso.application.media.Player
import yankov.mso.application.model.DataModel.FolkloreTrack
import yankov.mso.application.ui.UiUtils._

import java.io.File

class AudioSearchControls(search: (List[File], Double, Int) => Unit,
                          audioSearchTable: AudioSearchTable) extends SearchControls[FolkloreTrack] {
  private val correlation = LabeledTextField(Search.correlation, "0.9")
  private val crossCorrelationShift = LabeledTextField(yankov.mso.application.Resources.Search.crossCorrelationShift, "50")

  val panel: TitledPane = {
    val searchContainer = createSearchContainer

    val btnClear: Button = new Button {
      text = yankov.mso.application.Resources.Search.audioSearchClearButton
      onAction = _ => audioSearchTable.clear()
    }
    val btnPlayLeft: Button = new Button {
      text = yankov.mso.application.Resources.Search.audioSearchPlayLeftButton
      onAction = _ => playLeft()
    }
    val btnPlayRight: Button = new Button {
      text = yankov.mso.application.Resources.Search.audioSearchPlayRightButton
      onAction = _ => playRight()
    }

    searchContainer.children.add(correlation.getContainer)
    searchContainer.children.add(crossCorrelationShift.getContainer)
    searchContainer.children.add(btnSearch)
    searchContainer.children.add(btnPlayLeft)
    searchContainer.children.add(btnPlayRight)
    searchContainer.children.add(btnClear)

    new TitledPane {
      text = yankov.mso.application.Resources.Search.audioSearchPane
      expanded = true
      content = searchContainer
    }
  }

  override def searchButtonText: String = yankov.mso.application.Resources.Search.audioSearchButton

  override def panels: List[TitledPane] = List(panel)

  override def doSearch(): Unit = {
    selectFlacFiles(false) match {
      case Some(files) =>
        longOperation(
          () => search(files, correlation.getValue.toDouble, crossCorrelationShift.getValue.toInt)
        ).inThread.start()
      case None => ()
    }
  }

  private def playLeft(): Unit = {
    val item = audioSearchTable.pure.getSelectionModel.getSelectedItem
    if (item != null && item.audioSearchResult.isDefined) {
      Player.play(List(item.audioSearchResult.get.sample.audioInput.input.left.get))
    }
  }

  private def playRight(): Unit = {
    val item = audioSearchTable.pure.getSelectionModel.getSelectedItem
    if (item != null && item.audioSearchResult.isDefined) {
      Player.play(List(dataManager.databasePaths.mediaFile(item.track.id)))
    }
  }
}
