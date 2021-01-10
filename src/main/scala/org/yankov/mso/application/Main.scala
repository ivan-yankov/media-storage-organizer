package org.yankov.mso.application

import org.slf4j.LoggerFactory
import org.yankov.mso.application.database._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.{DataManager, DatabaseCache}
import org.yankov.mso.application.search.SearchModel.Filter
import org.yankov.mso.application.model.UiModel.{ApplicationSettings, FolkloreTrackProperties}
import org.yankov.mso.application.search.{SearchEngine, TextAnalyzer}
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

  onStart()

  def getApplicationArgument(argument: String, defaultValue: String = "", required: Boolean = true): String = {
    parameters
      .raw
      .find(x => x.startsWith(argument)) match {
      case Some(value) =>
        val items = value.split("=")
        if (items.length == 2) items(1)
        else {
          log.error(s"Value for application argument [$argument] no found.")
          defaultValue
        }
      case None =>
        if (required) log.error(s"Missing application argument [$argument].")
        defaultValue
    }
  }

  private def onStart(): Unit = {
    getApplicationArgument(Resources.ApplicationArgumentKeys.findDuplicates, required = false) match {
      case Resources.ApplicationArgumentValues.findDuplicatesExact =>
        setOutput(findDuplicates(x => TextAnalyzer.analyze(x.title) + "|" + x.performer + "|" + x.duration.getSeconds.toString))
      case Resources.ApplicationArgumentValues.findDuplicatesTitlePerformer =>
        setOutput(findDuplicates(x => TextAnalyzer.analyze(x.title) + "|" + x.performer))
      case _ => ()
    }
  }

  private def createDataManager: DataManager = {
    System.setSecurityManager(null)
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver")

    val dbDir = getApplicationArgument(Resources.ApplicationArgumentKeys.databaseDirectory)
    val mediaDir = getApplicationArgument(Resources.ApplicationArgumentKeys.mediaDir)
    val connectionString = ConnectionStringFactory.createDerbyConnectionString(DirectoryDatabaseProtocol, dbDir, Map())
    DataManager(connectionString, mediaDir, DatabaseCache(connectionString))
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
    val (tracks, totalDuration) = SearchEngine.search[FolkloreTrack](
      dataManager.getTracks,
      filters,
      (x, y) => x.id < y.id,
      x => x.duration
    )
    searchTable.getValue.getItems.clear()
    tracks.foreach(x => searchTable.getValue.getItems.add(FolkloreTrackProperties(x)))

    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }

  private def findDuplicates(key: FolkloreTrack => String): List[FolkloreTrack] = {
    dataManager
      .getTracks
      .groupBy(x => key(x))
      .filter(x => x._2.size > 1)
      .values
      .toList
      .flatten
  }

  private def setOutput(items: List[FolkloreTrack]): Unit = {
    searchTable.getValue.getItems.clear()
    items
      .map(x => FolkloreTrackProperties(x))
      .foreach(x => searchTable.getValue.getItems.add(x))
  }
}
