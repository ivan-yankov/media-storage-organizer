package org.yankov.mso.application.ui.toolbars

import javafx.event.ActionEvent

trait ToolbarButtonHandlers {
  def updateDatabase(event: ActionEvent): Unit

  def exportItems(event: ActionEvent): Unit

  def loadTracks(event: ActionEvent): Unit

  def clearTable(event: ActionEvent): Unit

  def importFromClipboard(event: ActionEvent): Unit

  def applyProperties(event: ActionEvent): Unit

  def copyProperties(event: ActionEvent): Unit

  def cloneItem(event: ActionEvent): Unit

  def removeItem(event: ActionEvent): Unit

  def addItem(event: ActionEvent): Unit

  def upload(event: ActionEvent): Unit

  def playStop(event: ActionEvent): Unit

  def editTrack(event: ActionEvent): Unit
}
