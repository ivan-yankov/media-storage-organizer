package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.Resources
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.ui.UiUtils._
import scalafx.scene.control.TitledPane

import java.io.File

class AudioSearchControls(search: (List[File], Double, Int) => Unit) extends SearchControls[FolkloreTrack] {
  private val correlation = LabeledTextField(Resources.Search.correlation, "0.9")
  private val crossCorrelationShift = LabeledTextField(Resources.Search.crossCorrelationShift, "50")

  val panel: TitledPane = {
    val searchContainer = createSearchContainer

    searchContainer.children.add(correlation.getContainer)
    searchContainer.children.add(crossCorrelationShift.getContainer)
    searchContainer.children.add(btnSearch)

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
}
