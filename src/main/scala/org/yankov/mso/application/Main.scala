package org.yankov.mso.application

import org.slf4j.LoggerFactory
import org.yankov.mso.application.commands.SearchEngine
import org.yankov.mso.application.database._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.model.SearchModel.Filter
import org.yankov.mso.application.model.UiModel.{ApplicationSettings, FolkloreTrackProperties}
import org.yankov.mso.application.ui.Utils
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.artifacts.ArtifactsTab
import org.yankov.mso.application.ui.controls.{FolkloreControlsFactory, FolkloreTrackTable, SearchFilterControls}
import org.yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
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

    onCloseRequest = event => if (!Utils.confirmCloseApplication) event.consume()

    scene = new Scene {
      root = new BorderPane {
        center = tabPane
        bottom = ApplicationConsole.container
      }
    }
  }

  private lazy val log = LoggerFactory.getLogger(getClass)

  lazy val dataManager: DataManager = createDataManager
  lazy val inputTable: FolkloreTrackTable = FolkloreTrackTable(true)
  lazy val searchTable: FolkloreTrackTable = FolkloreTrackTable(false)
  lazy val toolbarButtons: ToolbarButtons = ToolbarButtons(FolkloreToolbarButtonHandlers())
  lazy val searchFilterControls: SearchFilterControls[FolkloreTrack] = SearchFilterControls(
    () => FolkloreControlsFactory.createSearchVariable(),
    () => FolkloreControlsFactory.createSearchOperator(),
    x => search(x)
  )

  private def createDataManager: DataManager = {
    val dbDir = getApplicationArgument(Resources.ApplicationArguments.databaseDirectory)
    val mediaDir = getApplicationArgument(Resources.ApplicationArguments.mediaDir)
    val connectionString = ConnectionStringFactory.createDerbyConnectionString(DirectoryDatabaseProtocol, dbDir, Map())
    DataManager(connectionString, mediaDir)
  }

  private def getApplicationArgument(argument: String, defaultValue: String = ""): String = {
    parameters
      .raw
      .find(x => x.startsWith(argument)) match {
      case Some(value) =>
        val items = value.split("=")
        if (items.length == 2) items(1)
        else {
          log.error(s"Missing application argument [$argument].")
          defaultValue
        }
      case None =>
        log.error(s"Missing application argument [$argument].")
        defaultValue
    }
  }

  private def tabPane: TabPane = {
    VBox.setVgrow(inputTable.getContainer, Priority.Always)
    VBox.setVgrow(searchTable.getContainer, Priority.Always)

    val inputTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.inputTabButtons
        },
        inputTable.getContainer
      )
    }

    val inputArtifactsTab = ArtifactsTab()

    val searchFilterPanels = searchFilterControls.controls.map(x => x.panel)
    val searchTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.searchTabButtons
        },
        searchFilterPanels.head,
        searchFilterPanels.tail.head,
        searchFilterPanels.tail.tail.head,
        searchTable.getContainer
      )
    }

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
          content = inputArtifactsTab.getContainer
        },
        new Tab {
          text = Resources.MainForm.searchTab
          closable = false
          content = searchTab
        }
      )
    }
  }

  private def search(filters: List[Filter[FolkloreTrack]]): Unit = {
    val searcher = SearchEngine[FolkloreTrack](
      dataManager.getTracks,
      (x, y) => x.id < y.id,
      x => x.duration
    )

    val (tracks, totalDuration) = searcher.search(filters)
    searchTable.getValue.getItems.clear()
    tracks.foreach(x => searchTable.getValue.getItems.add(FolkloreTrackProperties(x)))

    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }
}
