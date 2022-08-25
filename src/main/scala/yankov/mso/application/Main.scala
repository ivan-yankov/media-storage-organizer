package yankov.mso.application

import org.slf4j.LoggerFactory
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.{Node, Scene}
import yankov.mso.application.database.RealDatabase
import yankov.mso.application.media.{AudioIndex, MediaServer}
import yankov.mso.application.model.{DataManager, DatabasePaths}
import yankov.mso.application.model.UiModel.ApplicationSettings
import yankov.mso.application.ui.UiUtils
import yankov.mso.application.ui.console.ApplicationConsole
import yankov.mso.application.ui.controls.UiTable
import yankov.mso.application.ui.controls.artifacts.ArtifactsTab

import java.nio.file.{Files, Paths}

object Main extends JFXApp {
  private lazy val log = LoggerFactory.getLogger(getClass)

  override def main(args: Array[String]): Unit = {
    if (getApplicationArgumentFlag(Resources.ApplicationArgumentKeys.buildAudioIndex, args.toSeq)) {
      val dataManager = createDataManager(
        getApplicationArgument(
          argument = Resources.ApplicationArgumentKeys.databaseDirectory,
          arguments = args.toSeq
        )
      )
      Files.deleteIfExists(dataManager.databasePaths.audioIndex)
      Files.createFile(dataManager.databasePaths.audioIndex)
      dataManager.audioIndex.get.build()
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

  lazy val mainControls: MainControls = MainControls(dataManager)
  import mainControls._

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
    yankov.mso.application.model.DataManager(db, dbPaths, Some(audioIndex))
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
        settings,
        columnSelector(inputTable),
        inputTable.getContainer
      )
    }

    val searchPanels = metadataSearchControls.panels
    val searchTab = new VBox {
      children = Seq(
        new ToolBar {
          items = toolbarButtons.searchTabButtons
        },
        columnSelector(searchTable),
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

  private def settings: TitledPane = {
    new TitledPane {
      text = Resources.MainForm.settings
      expanded = false
      content = new VBox {
        children = List(
          extractDataFromFileNameRegex.getContainer,
          extractTitleFromFileNameCheckBox,
          extractSourceSignatureFromFileNameCheckBox,
          new HBox {
            children = List(
              sourceType.getContainer,
              sourceLabel.getContainer,
            )
            spacing = 20.0
          }
        )
        spacing = 20.0
      }
    }
  }

  private def columnSelector(table: UiTable[_]): TitledPane = {
    val numberOfColumns = 10
    val gp = new GridPane()
    val items: List[Node] = table.pure.columns.map(
      x => {
        new CheckBox {
          text = x.getText
          selected = x.isVisible
          userData = x
          onAction = _ => x.setVisible(!x.isVisible)
        }
      }
    ).toList
    items
      .sliding(numberOfColumns, numberOfColumns)
      .zipWithIndex
      .foreach(x => x._1.zipWithIndex.foreach(y => gp.add(y._1, y._2, x._2)))

    val whiteSpace = 20.0
    gp.setPadding(Insets(whiteSpace))
    gp.setHgap(whiteSpace)
    gp.setVgap(whiteSpace)

    new TitledPane {
      text = Resources.TableColumns.tableColumnSelector
      expanded = false
      content = gp
    }
  }
}
