package org.yankov.mso.application

import org.slf4j.LoggerFactory
import org.yankov.mso.application.database.RealDatabase
import org.yankov.mso.application.media.{AudioIndex, MediaServer}
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel._
import org.yankov.mso.application.model.{DataManager, DatabasePaths}
import org.yankov.mso.application.search._
import org.yankov.mso.application.ui.UiUtils
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls._
import org.yankov.mso.application.ui.controls.artifacts.ArtifactsTab
import org.yankov.mso.application.ui.toolbars.{FolkloreToolbarButtonHandlers, ToolbarButtons}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, Priority, VBox}

import java.io.FileInputStream
import java.nio.file.{Files, Paths}

object Main extends JFXApp {
  private lazy val log = LoggerFactory.getLogger(getClass)

  override def main(args: Array[String]): Unit = {
    if (getApplicationArgumentFlag(Resources.ApplicationArgumentKeys.buildAudioIndex, args.toSeq)) {
      val dataManager = createDataManager(
        getApplicationArgument(
          argument = Resources.ApplicationArgumentKeys.databaseDirectory,
          arguments = args.toSeq)
      )
      Files.deleteIfExists(dataManager.databasePaths.audioIndex)
      Files.createFile(dataManager.databasePaths.audioIndex)
      dataManager.audioIndex.get.build()
    }
    else if (getApplicationArgumentFlag(Resources.ApplicationArgumentKeys.importDatabase, args.toSeq)) {
      ImportDatabase.run(
        getApplicationArgument(
          Resources.ApplicationArgumentKeys.databaseDirectory,
          arguments = args.toSeq
        )
      )
    }
    else {
      super.main(args)
    }
  }

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

  lazy val dataManager: DataManager = createDataManager(
    getApplicationArgument(Resources.ApplicationArgumentKeys.databaseDirectory)
  )
  lazy val inputTable: FolkloreTrackTable = new FolkloreTrackTable(true)
  lazy val searchTable: FolkloreTrackTable = new FolkloreTrackTable(false)
  lazy val audioSearchTable: AudioSearchTable = new AudioSearchTable()
  lazy val toolbarButtons: ToolbarButtons = ToolbarButtons(FolkloreToolbarButtonHandlers())
  lazy val metadataSearchControls: SearchControls[FolkloreTrack] = new MetadataSearchControls[FolkloreTrack](
    x => Search.metadataSearch(x, dataManager.getTracks, searchTable),
    () => FolkloreControlsFactory.createSearchVariable(),
    () => FolkloreControlsFactory.createSearchFilter()
  )
  lazy val audioSearchControls: SearchControls[FolkloreTrack] = new AudioSearchControls(
    (files, correlation, crossCorrelationShift) => Search.audioSearch(
      files.map(y => y.getName -> new FileInputStream(y)).toMap,
      dataManager.getTracks,
      dataManager.audioIndex,
      audioSearchTable,
      correlation,
      crossCorrelationShift
    ),
    () => audioSearchTable.clear()
  )

  onStart()

  def getApplicationArgumentFlag(argument: String, arguments: Seq[String] = parameters.raw): Boolean =
    arguments.exists(x => x.equals(argument))

  def getApplicationArgument(argument: String,
                             defaultValue: String = "",
                             required: Boolean = true,
                             arguments: Seq[String] = parameters.raw): String = {
    arguments.find(x => x.startsWith(argument)) match {
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

  private def createDataManager(dbDir: String): DataManager = {
    val dbPaths = DatabasePaths(Paths.get(dbDir))
    val db = RealDatabase()
    val audioIndex = AudioIndex(db, dbPaths)
    DataManager(db, dbPaths, Some(audioIndex))
  }

  private def tabPane: TabPane = {
    VBox.setVgrow(inputTable.getContainer, Priority.Always)
    VBox.setVgrow(searchTable.getContainer, Priority.Always)
    VBox.setVgrow(audioSearchTable.getContainer, Priority.Always)

    val inputTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.inputTabButtons
        },
        inputTable.getContainer
      )
    }

    val searchPanels = metadataSearchControls.panels
    val searchTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.searchTabButtons
        },
        searchPanels.head,
        searchPanels.tail.head,
        searchPanels.tail.tail.head,
        searchTable.getContainer
      )
    }

    val audioSearchTab = new VBox {
      children = Seq(
        audioSearchControls.panels.head,
        audioSearchTable.getContainer
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
          content = ArtifactsTab().getContainer
        },
        new Tab {
          text = Resources.MainForm.searchTab
          closable = false
          content = searchTab
        },
        new Tab {
          text = Resources.MainForm.audioSearchTab
          closable = false
          content = audioSearchTab
        }
      )
    }
  }
}
