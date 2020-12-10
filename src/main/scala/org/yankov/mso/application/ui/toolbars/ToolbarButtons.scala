package org.yankov.mso.application.ui.toolbars

import org.yankov.mso.application.ui.Resources
import scalafx.scene.control.{Button, Tooltip}
import scalafx.scene.image.{Image, ImageView}

case class ToolbarButtons(handlers: ToolbarButtonHandlers) {
  private val iconSize = 32
  private val atInputTabToolbar: String = "at-input-tab-toolbar"
  private val atOutputTabToolbar: String = "at-output-tab-toolbar"

  val inputTabButtons: List[Button] = {
    val buttons = createButtons
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
    )
      .map(x => x.get)
      .map(x => {
        x.setUserData(atInputTabToolbar)
        x
      })
  }

  val searchTabButtons: List[Button] = {
    val buttons = createButtons
    List(
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnEditProperties)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnPlay)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnUpdate)),
      buttons.find(x => x.id.getValue.equals(Resources.ControlIds.btnExport)),
    )
      .map(x => x.get)
      .map(x => {
        x.setUserData(atOutputTabToolbar)
        x
      })
  }

  def atInputTabToolbar(button: Button): Boolean = button.getUserData.asInstanceOf[String].equals(atInputTabToolbar)

  private def createButtons: List[Button] = List(
    new Button {
      id = Resources.ControlIds.btnEditProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.editProperties)
      graphic = getIcon("edit")
      onAction = x => handlers.editTrack(x)
    },
    new Button {
      id = Resources.ControlIds.btnPlay
      tooltip = new Tooltip(Resources.ToolbarButtons.play)
      graphic = getIcon("play")
      onAction = x => handlers.playStop(x)
    },
    new Button {
      id = Resources.ControlIds.btnUpload
      tooltip = new Tooltip(Resources.ToolbarButtons.upload)
      graphic = getIcon("upload")
      onAction = x => handlers.upload(x)
    },
    new Button {
      id = Resources.ControlIds.btnAdd
      tooltip = new Tooltip(Resources.ToolbarButtons.add)
      graphic = getIcon("add")
      onAction = x => handlers.addItem(x)
    },
    new Button {
      id = Resources.ControlIds.btnRemove
      tooltip = new Tooltip(Resources.ToolbarButtons.remove)
      graphic = getIcon("remove")
      onAction = x => handlers.removeItem(x)
    },
    new Button {
      id = Resources.ControlIds.btnClone
      tooltip = new Tooltip(Resources.ToolbarButtons.cloneItems)
      graphic = getIcon("clone")
      onAction = x => handlers.cloneItem(x)
    },
    new Button {
      id = Resources.ControlIds.btnCopyProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.copyProperties)
      graphic = getIcon("copy-properties")
      onAction = x => handlers.copyProperties(x)
    },
    new Button {
      id = Resources.ControlIds.btnApplyProperties
      tooltip = new Tooltip(Resources.ToolbarButtons.applyProperties)
      graphic = getIcon("apply-properties")
      onAction = x => handlers.applyProperties(x)
    },
    new Button {
      id = Resources.ControlIds.btnImportFromClipboard
      tooltip = new Tooltip(Resources.ToolbarButtons.importFromClipboard)
      graphic = getIcon("import")
      onAction = x => handlers.importFromClipboard(x)
    },
    new Button {
      id = Resources.ControlIds.btnClear
      tooltip = new Tooltip(Resources.ToolbarButtons.clear)
      graphic = getIcon("clear")
      onAction = x => handlers.clearTable(x)
    },
    new Button {
      id = Resources.ControlIds.btnLoadTracks
      tooltip = new Tooltip(Resources.ToolbarButtons.loadTracks)
      graphic = getIcon("select-files")
      onAction = x => handlers.loadTracks(x)
    },
    new Button {
      id = Resources.ControlIds.btnExport
      tooltip = new Tooltip(Resources.ToolbarButtons.exportItems)
      graphic = getIcon("export")
      onAction = x => handlers.exportItems(x)
    },
    new Button {
      id = Resources.ControlIds.btnUpdate
      tooltip = new Tooltip(Resources.ToolbarButtons.update)
      graphic = getIcon("update")
      onAction = x => handlers.updateDatabase(x)
    }
  )

  private def getIcon(name: String): ImageView = {
    val url = getClass.getResource("/icons/buttons/" + name + ".png")
    val image = new Image(url.toString, iconSize, iconSize, true, true)
    new ImageView(image)
  }
}
