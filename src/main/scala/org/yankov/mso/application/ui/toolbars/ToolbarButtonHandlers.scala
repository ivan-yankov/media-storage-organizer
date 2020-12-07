package org.yankov.mso.application.ui.toolbars

import javafx.event.{ActionEvent, EventHandler}

trait ToolbarButtonHandlers {
  def update: EventHandler[ActionEvent]

  def export: EventHandler[ActionEvent]

  def loadTracks: EventHandler[ActionEvent]

  def clear: EventHandler[ActionEvent]

  def importFromClipboard: EventHandler[ActionEvent]

  def applyProperties: EventHandler[ActionEvent]

  def copyProperties: EventHandler[ActionEvent]

  def cln: EventHandler[ActionEvent]

  def remove: EventHandler[ActionEvent]

  def add: EventHandler[ActionEvent]

  def upload: EventHandler[ActionEvent]

  def playStop: EventHandler[ActionEvent]

  def editTrack: EventHandler[ActionEvent]
}
