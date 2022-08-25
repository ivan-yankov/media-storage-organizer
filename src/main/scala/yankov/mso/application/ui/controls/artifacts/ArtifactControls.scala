package yankov.mso.application.ui.controls.artifacts

import scalafx.scene.layout.Pane
import yankov.mso.application.Main
import yankov.mso.application.model.DataManager
import yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}

trait ArtifactControls[T] {
  protected val whiteSpace: Double = 20.0
  protected val console: ConsoleService = ApplicationConsole
  protected val dataManager: DataManager = Main.dataManager

  def validateUserInput(): Boolean

  def createArtifact(): Boolean

  def updateArtifact(artifact: T): Boolean

  def cleanup(): Unit

  def artifactToString(artifact: T): String

  def getExistingArtifacts: List[T]

  def onArtifactSelect(artifact: T): Unit

  def createControls(): Pane

  def artifactExists: Boolean

  def refresh(): Unit = {}
}
