package org.yankov.mso.application

import org.slf4j.LoggerFactory
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.database.RealDatabase
import org.yankov.mso.application.media.{AudioIndex, MediaServer}
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel.{ApplicationSettings, FolkloreTrackProperties}
import org.yankov.mso.application.model.{DataManager, DatabasePaths}
import org.yankov.mso.application.search.SearchEngine
import org.yankov.mso.application.search.SearchModel.SearchParameters
import org.yankov.mso.application.ui.UiUtils
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.artifacts.ArtifactsTab
import org.yankov.mso.application.ui.controls.{FolkloreControlsFactory, FolkloreTrackTable, SearchFilterControls}
import org.yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, Priority, VBox}

import java.nio.file.Paths
import java.time.Duration

object Main extends JFXApp {
  stage = new PrimaryStage {
    maximized = ApplicationSettings.isMaximized
    width = ApplicationSettings.getWindowWidth
    height = ApplicationSettings.getWindowHeight
    icons.add(ApplicationSettings.getIcon)
    title = ApplicationSettings.getTitle
    x = ApplicationSettings.getX
    y = ApplicationSettings.getY

    onCloseRequest = event => {
      if (UiUtils.confirmCloseApplication) {
        MediaServer.stop()
      }
      else event.consume()
    }

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
    () => FolkloreControlsFactory.createSearchFilter(),
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
          log.error(s"Value for application argument [$argument] not found.")
          defaultValue
        }
      case None =>
        if (required) log.error(s"Missing application argument [$argument].")
        defaultValue
    }
  }

  private def onStart(): Unit = {
    new Thread(() => MediaServer.start()).start()
  }

  private def createDataManager: DataManager = {
    val dbDir = getApplicationArgument(Resources.ApplicationArgumentKeys.databaseDirectory)
    val dbPaths = DatabasePaths(Paths.get(dbDir))
    val db = RealDatabase()
    val audioIndex = AudioIndex(db, dbPaths)
//    audioIndex.buildIfNotExists()
    DataManager(db, dbPaths, Some(audioIndex))
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

  private def search(searchParameters: List[SearchParameters[FolkloreTrack]]): Unit = {
    val tracks = {
      if (searchParameters.forall(x => x.value.isBlank)) {
        dataManager.getTracks
      }
      else {
        SearchEngine.search[FolkloreTrack](
          dataManager.getTracks,
          searchParameters
        ).sortBy(x => (StringConverters.sourceToString(x.source), x.note, x.title))
      }
    }

    searchTable.getValue.getItems.clear()
    tracks.foreach(x => searchTable.getValue.getItems.add(FolkloreTrackProperties(x)))

    val totalDuration = tracks.map(x => x.duration).foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }
}
