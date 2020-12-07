package org.yankov.mso.application.ui.toolbars

import javafx.event.{ActionEvent, EventHandler}

case class FolkloreToolbarButtonHandlers() extends ToolbarButtonHandlers {
  override def update: EventHandler[ActionEvent] = ???

  override def `export`: EventHandler[ActionEvent] = ???

  override def loadTracks: EventHandler[ActionEvent] = ???

  override def clear: EventHandler[ActionEvent] = ???

  override def importFromClipboard: EventHandler[ActionEvent] = ???

  override def applyProperties: EventHandler[ActionEvent] = ???

  override def copyProperties: EventHandler[ActionEvent] = ???

  override def cln: EventHandler[ActionEvent] = ???

  override def remove: EventHandler[ActionEvent] = ???

  override def add: EventHandler[ActionEvent] = ???

  override def upload: EventHandler[ActionEvent] = ???

  override def playStop: EventHandler[ActionEvent] = ???

  override def editTrack: EventHandler[ActionEvent] = ???
}
