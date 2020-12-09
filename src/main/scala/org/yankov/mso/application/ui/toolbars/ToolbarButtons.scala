package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application.ui.Resources
import scalafx.scene.control.{Button, Tooltip}
import scalafx.scene.image.{Image, ImageView}

case class ToolbarButtons(handlers: ToolbarButtonHandlers) {
  private val iconSize = 32

  private val buttons: List[Button] = List(
    new Button {
      id = Resources.ControlIds.btnEditProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.editProperties)
      graphic = getIcon("edit")
      onAction = handlers.editTrack
    },
    new Button {
      id = Resources.ControlIds.btnPlay
      tooltip = new Tooltip(Resources.ToolbarButtons.play)
      graphic = getIcon("play")
      onAction = handlers.playStop
    },
    new Button {
      id = Resources.ControlIds.btnUpload
      tooltip = new Tooltip(Resources.ToolbarButtons.upload)
      graphic = getIcon("upload")
      onAction = handlers.upload
    },
    new Button {
      id = Resources.ControlIds.btnAdd
      tooltip = new Tooltip(Resources.ToolbarButtons.add)
      graphic = getIcon("add")
      onAction = handlers.add
    },
    new Button {
      id = Resources.ControlIds.btnRemove
      tooltip = new Tooltip(Resources.ToolbarButtons.remove)
      graphic = getIcon("remove")
      onAction = handlers.remove
    },
    new Button {
      id = Resources.ControlIds.btnClone
      tooltip = new Tooltip(Resources.ToolbarButtons._clone)
      graphic = getIcon("clone")
      onAction = handlers.cln
    },
    new Button {
      id = Resources.ControlIds.btnCopyProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.copyProperties)
      graphic = getIcon("copy-properties")
      onAction = handlers.copyProperties
    },
    new Button {
      id = Resources.ControlIds.btnApplyProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.applyProperties)
      graphic = getIcon("apply-properties")
      onAction = handlers.applyProperties
    },
    new Button {
      id = Resources.ControlIds.btnImportFromClipboard
      tooltip = new Tooltip(Resources.ToolbarButtons.importFromClipboard)
      graphic = getIcon("import")
      onAction = handlers.importFromClipboard
    },
    new Button {
      id = Resources.ControlIds.btnClear
      tooltip = new Tooltip(Resources.ToolbarButtons.clear)
      graphic = getIcon("clear")
      onAction = handlers.clear
    },
    new Button {
      id = Resources.ControlIds.btnLoadTracks
      tooltip = new Tooltip(Resources.ToolbarButtons.loadTracks)
      graphic = getIcon("select-files")
      onAction = handlers.loadTracks
    },
    new Button {
      id = Resources.ControlIds.btnExport
      tooltip = new Tooltip(Resources.ToolbarButtons.export)
      graphic = getIcon("export")
      onAction = handlers.export
    },
    new Button {
      id = Resources.ControlIds.btnUpdate
      tooltip = new Tooltip(Resources.ToolbarButtons.update)
      graphic = getIcon("update")
      onAction = handlers.update
    }
  )

  def inputTabButtons: List[Button] = {
    List(
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnAdd)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnRemove)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnClone)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnCopyProperties)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnApplyProperties)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnImportFromClipboard)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnClear)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnLoadTracks)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnEditProperties)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnPlay)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnUpload))
    ).map(x => x.get)
  }

  def searchTabButtons: List[Button] = {
    List(
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnClear)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnEditProperties)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnPlay)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnUpdate)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnExport)),
    ).map(x => x.get)
  }

  private def getIcon(name: String): ImageView = {
    val url = getClass.getResource("/icons/buttons/" + name + ".png")
    val image = new Image(url.toString, iconSize, iconSize, true, true)
    new ImageView(image)
  }
}
