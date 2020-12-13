package org.yankov.mso.application

import org.yankov.mso.application.model.UiModel.ApplicationSettings
import org.yankov.mso.application.ui.FxUtils
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.FolkloreTrackTable
import org.yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane, ToolBar}
import scalafx.scene.layout.{BorderPane, Priority, VBox}

object Main extends JFXApp {
  stage = new PrimaryStage {
    maximized = ApplicationSettings.isMaximized
    width = ApplicationSettings.getWindowWidth
    height = ApplicationSettings.getWindowHeight
    icons.add(ApplicationSettings.getIcon)
    title = ApplicationSettings.getTitle
    x = ApplicationSettings.getX
    y = ApplicationSettings.getY

    onCloseRequest = event => if (!FxUtils.confirmCloseApplication) event.consume()

    scene = new Scene {
      root = new BorderPane {
        center = tabPane
        bottom = ApplicationConsole.container
      }
    }
  }

  lazy val inputTable: FolkloreTrackTable = FolkloreTrackTable(true)
  lazy val searchTable: FolkloreTrackTable = FolkloreTrackTable(false)

  lazy val toolbarButtons: ToolbarButtons = ToolbarButtons(FolkloreToolbarButtonHandlers())

  private def tabPane: TabPane = {
    VBox.setVgrow(inputTable.getContainer, Priority.Always)

    val inputTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.inputTabButtons
        },
        inputTable.getContainer
      )
    }

//    val inputArtifactsTab = ???

//    val searchTab = ???

    new TabPane {
      tabs = Seq(
        new Tab {
          text = Resources.MainForm.inputTab
          closable = false
          content = inputTab
        },
        new Tab {
          text = Resources.MainForm.inputArtifactsTab
          closable = false
//          content = inputArtifactsTab
        },
        new Tab {
          text = Resources.MainForm.searchTab
          closable = false
//          content = searchTab
        }
      )
    }
  }
}
