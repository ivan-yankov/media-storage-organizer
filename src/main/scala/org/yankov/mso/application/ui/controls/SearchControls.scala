package org.yankov.mso.application.ui.controls

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, TitledPane}
import scalafx.scene.layout.HBox

abstract class SearchControls[T] {
  val btnSearch: Button = new Button {
    text = searchButtonText
    onAction = _ => doSearch()
  }

  def createSearchContainer: HBox = new HBox {
    padding = Insets(10.0, 20.0, 10.0, 20.0)
    spacing = 25.0
    alignment = Pos.BottomLeft
  }

  def searchButtonText: String

  def panels: List[TitledPane]

  def doSearch(): Unit
}
