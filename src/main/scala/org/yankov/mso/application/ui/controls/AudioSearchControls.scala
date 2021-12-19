package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.Resources
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.search.SearchModel.AudioSearchParameters
import scalafx.scene.control.TitledPane

class AudioSearchControls(search: List[AudioSearchParameters] => Unit) extends SearchControls[FolkloreTrack] {
  val panel: TitledPane = {
    val searchContainer = createSearchContainer

    searchContainer.children.add(btnSearch)

    new TitledPane {
      text = Resources.Search.audioSearchPane
      expanded = true
      content = searchContainer
    }
  }

  override def searchButtonText: String = Resources.Search.audioSearchButton

  override def panels: List[TitledPane] = List(panel)

  override def doSearch(): Unit = ???
}
