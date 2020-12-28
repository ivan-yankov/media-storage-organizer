package org.yankov.mso.application.ui.controls

import javafx.scene.input.KeyCode
import org.yankov.mso.application.Resources
import org.yankov.mso.application.model.SearchModel._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, TitledPane}
import scalafx.scene.layout.{HBox, Priority}

case class SearchFilterControls[T](variableCreator: () => LabeledComboBox[Variable[T]],
                                   operatorCreator: () => LabeledComboBox[Operator],
                                   search: List[Filter[T]] => Unit) {

  case class FilterControls(variable: LabeledComboBox[Variable[T]],
                            operator: LabeledComboBox[Operator],
                            value: LabeledTextField,
                            panel: TitledPane)

  private val btnSearch = new Button {
    text = Resources.Search.search
    onAction = _ => search(createFilters)
  }

  val controls: List[FilterControls] = List(
    createControls(1),
    createControls(2),
    createControls(3)
  )

  private def createControls(number: Int): FilterControls = {
    val variable = variableCreator()
    val operator = operatorCreator()
    val value = LabeledTextField(Resources.Search.value, "")
    value.setOnKeyReleased(x => if (x.getCode.equals(KeyCode.ENTER)) search(createFilters))
    HBox.setHgrow(value.getContainer, Priority.Always)

    val searchContainer = new HBox {
      padding = Insets(10.0, 20.0, 10.0, 20.0)
      spacing = 25.0
      alignment = Pos.BottomLeft
      children.add(variable.getContainer)
      children.add(operator.getContainer)
      children.add(value.getContainer)
    }

    if (number == 1) searchContainer.children.add(btnSearch)

    val panel = new TitledPane {
      text = Resources.Search.filter + " " + number.toString
      expanded = number == 1
      content = searchContainer
    }

    FilterControls(variable, operator, value, panel)
  }

  private def createFilters: List[Filter[T]] = {
    controls
      .map(
        x => Filter(
          variable = x.variable.getValue,
          operator = x.operator.getValue,
          value = x.value.getValue
        )
      )
  }
}
