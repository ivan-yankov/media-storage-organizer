package org.yankov.mso.application.ui.controls

import javafx.scene.input.KeyCode
import org.yankov.mso.application.Resources
import org.yankov.mso.application.search.SearchModel._
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.{HBox, Priority}

class MetadataSearchControls[T](search: List[SearchParameters[T]] => Unit,
                                createVariables: () => LabeledComboBox[Variable],
                                createFilters: () => LabeledComboBox[Filter[T]]) extends SearchControls[T] {

  case class Controls(variable: LabeledComboBox[Variable],
                      filter: LabeledComboBox[Filter[T]],
                      value: LabeledTextField,
                      panel: TitledPane)

  private val controls: List[Controls] = List(
    createControls(1),
    createControls(2),
    createControls(3)
  )

  private def createControls(number: Int): Controls = {
    val variables = createVariables()
    val filters = createFilters()
    val value = createValue
    value.setOnKeyReleased(x => if (x.getCode.equals(KeyCode.ENTER)) doSearch())
    HBox.setHgrow(value.getContainer, Priority.Always)

    val searchContainer = createSearchContainer

    searchContainer.children.add(variables.getContainer)
    searchContainer.children.add(filters.getContainer)
    searchContainer.children.add(value.getContainer)

    if (number == 1) searchContainer.children.add(btnSearch)

    val panel = new TitledPane {
      text = Resources.Search.filter + " " + number.toString
      expanded = number == 1
      content = searchContainer
    }

    Controls(variables, filters, value, panel)
  }

  private def createValue: LabeledTextField = LabeledTextField(Resources.Search.value, "")

  override def searchButtonText: String = Resources.Search.search

  override def panels: List[TitledPane] = controls.map(x => x.panel)

  override def doSearch(): Unit =
    search(controls.map(x => SearchParameters(x.variable.getValue, x.filter.getValue, x.value.getValue)))
}
