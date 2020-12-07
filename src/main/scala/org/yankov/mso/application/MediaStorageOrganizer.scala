package org.yankov.mso.application

import org.yankov.mso.application.ui.UiModel.ApplicationSettings
import org.yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}
import org.yankov.mso.application.ui.{FolkloreTrackTable, FxUtils, Resources}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane, ToolBar}
import scalafx.scene.layout.{BorderPane, Pane, Priority, VBox}

object MediaStorageOrganizer extends JFXApp {
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
        padding = Insets(25)
        center = tabPane
        bottom = console
      }
    }
  }

  private def console: Pane = {
    ApplicationConsole.getInstance.layout
    ApplicationConsole.getInstance.getContainer
  }

  private def tabPane: TabPane = {
    val table = FolkloreTrackTable(true)
    VBox.setVgrow(table.container, Priority.Always)

    val inputTab = new VBox {
      children = Seq(
        new ToolBar {
          items = ToolbarButtons(FolkloreToolbarButtonHandlers()).inputTabButtons
        },
        table.container
      )
    }

    val inputArtifactsTab = new FolkloreInputArtifactsTab();
    inputArtifactsTab.layout();

    val searchTab = new FolkloreSearchTab();
    searchTab.layout();

    new TabPane {
      tabs = Seq(
        new Tab {
          id = Resources.ControlIds.inputTab
          text = Resources.MainForm.inputTab
          closable = false
          content = inputTab
        },
        new Tab {
          id = Resources.ControlIds.inputArtifactsTab
          text = Resources.MainForm.inputArtifactsTab
          closable = false
          content = inputArtifactsTab
        },
        new Tab {
          id = Resources.ControlIds.searchTab
          text = Resources.MainForm.searchTab
          closable = false
          content = searchTab
        }
      )
    }
  }
}
