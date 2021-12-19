package org.yankov.mso.application.ui.controls

import javafx.scene.input.KeyCode
import org.yankov.mso.application.Resources
import org.yankov.mso.application.search.SearchModel._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, TitledPane}
import scalafx.scene.layout.{HBox, Priority}

case class SearchControls[T](variableCreator: () => LabeledComboBox[Variable[T]],
                             filterCreator: () => LabeledComboBox[Filter[T]],
                             search: List[SearchParameters[T]] => Unit) {

  case class FilterControls(variable: LabeledComboBox[Variable[T]],
                            filter: LabeledComboBox[Filter[T]],
                            value: LabeledTextField,
                            panel: TitledPane)

  private val btnSearch = new Button {
    text = Resources.Search.search
    onAction = _ => doSearch()
  }

  val controls: List[FilterControls] = List(
    createControls(1),
    createControls(2),
    createControls(3)
  )

  private def createControls(number: Int): FilterControls = {
    val variable = variableCreator()
    val filter = filterCreator()
    val value = LabeledTextField(Resources.Search.value, "")
    value.setOnKeyReleased(x => if (x.getCode.equals(KeyCode.ENTER)) doSearch())
    HBox.setHgrow(value.getContainer, Priority.Always)

    val searchContainer = new HBox {
      padding = Insets(10.0, 20.0, 10.0, 20.0)
      spacing = 25.0
      alignment = Pos.BottomLeft
      children.add(variable.getContainer)
      children.add(filter.getContainer)
      children.add(value.getContainer)
    }

    if (number == 1) searchContainer.children.add(btnSearch)

    val panel = new TitledPane {
      text = Resources.Search.filter + " " + number.toString
      expanded = number == 1
      content = searchContainer
    }

    FilterControls(variable, filter, value, panel)
  }

  private def doSearch(): Unit =
    search(controls.map(x => SearchParameters(x.variable.getValue, x.filter.getValue, x.value.getValue)))
}
