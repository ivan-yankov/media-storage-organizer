package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.Main.dataManager
import org.yankov.mso.application.Resources
import org.yankov.mso.application.media.Player
import org.yankov.mso.application.media.decode.FlacDecoder
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.ui.UiUtils._
import scalafx.scene.control.{Button, TitledPane}

import java.io.File
import java.nio.file.Files

class AudioSearchControls(search: (List[File], Double, Int) => Unit,
                          audioSearchTable: AudioSearchTable) extends SearchControls[FolkloreTrack] {
  private val correlation = LabeledTextField(Resources.Search.correlation, "0.9")
  private val crossCorrelationShift = LabeledTextField(Resources.Search.crossCorrelationShift, "50")

  val panel: TitledPane = {
    val searchContainer = createSearchContainer

    val btnClear: Button = new Button {
      text = Resources.Search.audioSearchClearButton
      onAction = _ => audioSearchTable.clear()
    }
    val btnPlayLeft: Button = new Button {
      text = Resources.Search.audioSearchPlayLeftButton
      onAction = _ => playLeft()
    }
    val btnPlayRight: Button = new Button {
      text = Resources.Search.audioSearchPlayRightButton
      onAction = _ => playRight()
    }

    searchContainer.children.add(correlation.getContainer)
    searchContainer.children.add(crossCorrelationShift.getContainer)
    searchContainer.children.add(btnSearch)
    searchContainer.children.add(btnPlayLeft)
    searchContainer.children.add(btnPlayRight)
    searchContainer.children.add(btnClear)

    new TitledPane {
      text = Resources.Search.audioSearchPane
      expanded = true
      content = searchContainer
    }
  }

  override def searchButtonText: String = Resources.Search.audioSearchButton

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
      val data = FlacDecoder.decode(item.audioSearchResult.get.sample.audioData).getOrElse(Array())
      Player.play(List(item.audioSearchResult.get.sample.id.replace(" ", "_") -> data))
    }
  }

  private def playRight(): Unit = {
    val item = audioSearchTable.pure.getSelectionModel.getSelectedItem
    if (item != null && item.audioSearchResult.isDefined) {
      val path = dataManager.databasePaths.mediaFile(item.track.id).toPath
      val data = FlacDecoder.decode(Files.readAllBytes(path)).getOrElse(Array())
      Player.play(List(item.track.id -> data))
    }
  }
}
