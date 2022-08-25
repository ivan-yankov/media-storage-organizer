package yankov.mso.application.ui.toolbars

trait ToolbarButtonHandlers {
  def updateItems(targetInputTab: Boolean): Unit

  def exportItems(targetInputTab: Boolean): Unit

  def loadTracks(targetInputTab: Boolean): Unit

  def clearTable(targetInputTab: Boolean): Unit

  def importTitlesFromClipboard(targetInputTab: Boolean): Unit

  def applyProperties(targetInputTab: Boolean): Unit

  def copyProperties(targetInputTab: Boolean): Unit

  def cloneItem(targetInputTab: Boolean): Unit

  def removeItems(targetInputTab: Boolean): Unit

  def deleteItems(targetInputTab: Boolean): Unit

  def addItem(targetInputTab: Boolean): Unit

  def uploadItems(targetInputTab: Boolean): Unit

  def play(targetInputTab: Boolean): Unit

  def editTrack(targetInputTab: Boolean): Unit
}
