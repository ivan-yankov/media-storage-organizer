package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application.Resources
import scalafx.scene.control.{Button, Tooltip}
import scalafx.scene.image.{Image, ImageView}

object ButtonIds {
  val btnAdd: String = "atn-add"
  val btnRemove: String = "btn-remove"
  val btnClone: String = "btn-clone"
  val btnCopyProperties: String = "btn-copy-properties"
  val btnApplyProperties: String = "btn-apply-properties"
  val btnImportFromClipboard: String = "btn-import-from-clipboard"
  val btnClear: String = "btn-clean"
  val btnLoadTracks: String = "btn-load-tracks"
  val btnEditTrack: String = "btn-edit-trac"
  val btnPlay: String = "btn-play"
  val btnUpload: String = "btn-upload"
  val btnUpdate: String = "btn-update"
  val btnExport: String = "btn-export"
}

case class ButtonUserData(atInputTab: Boolean)

case class ToolbarButtons(handlers: ToolbarButtonHandlers) {
  private val iconSize = 32

  val inputTabButtons: List[Button] = List(
    addButton(ButtonUserData(true)),
    removeButton(ButtonUserData(true)),
    cloneButton(ButtonUserData(true)),
    copyPropertiesButton(ButtonUserData(true)),
    applyPropertiesButton(ButtonUserData(true)),
    importFromClipboardButton(ButtonUserData(true)),
    clearButton(ButtonUserData(true)),
    loadTracksButton(ButtonUserData(true)),
    editButton(ButtonUserData(true)),
    playButton(ButtonUserData(true)),
    uploadButton(ButtonUserData(true))
  )

  val searchTabButtons: List[Button] = List(
    editButton(ButtonUserData(false)),
    playButton(ButtonUserData(false)),
    updateButton(ButtonUserData(false)),
    exportButton(ButtonUserData(false))
  )

  def atInputTabToolbar(button: Button): Boolean = button.getUserData.asInstanceOf[ButtonUserData].atInputTab

  private def editButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnEditTrack
    tooltip = new Tooltip(Resources.ToolbarButtons.editTrack)
    graphic = getIcon("edit")
    onAction = _ => handlers.editTrack(data.atInputTab)
    userData = data
  }

  private def playButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnPlay
    tooltip = new Tooltip(Resources.ToolbarButtons.play)
    graphic = getIcon("play")
    onAction = _ => handlers.playStop(data.atInputTab)
    userData = data
  }

  private def uploadButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnUpload
    tooltip = new Tooltip(Resources.ToolbarButtons.upload)
    graphic = getIcon("upload")
    onAction = _ => handlers.uploadItems(data.atInputTab)
    userData = data
  }

  private def addButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnAdd
    tooltip = new Tooltip(Resources.ToolbarButtons.add)
    graphic = getIcon("add")
    onAction = _ => handlers.addItem(data.atInputTab)
    userData = data
  }

  private def removeButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnRemove
    tooltip = new Tooltip(Resources.ToolbarButtons.remove)
    graphic = getIcon("remove")
    onAction = _ => handlers.removeItem(data.atInputTab)
    userData = data
  }

  private def cloneButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnClone
    tooltip = new Tooltip(Resources.ToolbarButtons.cloneItems)
    graphic = getIcon("clone")
    onAction = _ => handlers.cloneItem(data.atInputTab)
    userData = data
  }

  private def copyPropertiesButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnCopyProperties
    tooltip = new Tooltip(Resources.ToolbarButtons.copyProperties)
    graphic = getIcon("copy-properties")
    onAction = _ => handlers.copyProperties(data.atInputTab)
    userData = data
  }

  private def applyPropertiesButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnApplyProperties
    tooltip = new Tooltip(Resources.ToolbarButtons.applyProperties)
    graphic = getIcon("apply-properties")
    onAction = _ => handlers.applyProperties(data.atInputTab)
    userData = data
  }

  private def importFromClipboardButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnImportFromClipboard
    tooltip = new Tooltip(Resources.ToolbarButtons.importFromClipboard)
    graphic = getIcon("import")
    onAction = _ => handlers.importTitlesFromClipboard(data.atInputTab)
    userData = data
  }

  private def clearButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnClear
    tooltip = new Tooltip(Resources.ToolbarButtons.clear)
    graphic = getIcon("clear")
    onAction = _ => handlers.clearTable(data.atInputTab)
    userData = data
  }

  private def loadTracksButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnLoadTracks
    tooltip = new Tooltip(Resources.ToolbarButtons.loadTracks)
    graphic = getIcon("select-files")
    onAction = _ => handlers.loadTracks(data.atInputTab)
    userData = data
  }

  private def exportButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnExport
    tooltip = new Tooltip(Resources.ToolbarButtons.exportItems)
    graphic = getIcon("export")
    onAction = _ => handlers.exportItems(data.atInputTab)
    userData = data
  }

  private def updateButton(data: ButtonUserData): Button = new Button {
    id = ButtonIds.btnUpdate
    tooltip = new Tooltip(Resources.ToolbarButtons.update)
    graphic = getIcon("update")
    onAction = _ => handlers.updateItems(data.atInputTab)
    userData = data
  }

  private def getIcon(name: String): ImageView = {
    val url = getClass.getResource("/icons/buttons/" + name + ".png")
    val image = new Image(url.toString, iconSize, iconSize, true, true)
    new ImageView(image)
  }
}
