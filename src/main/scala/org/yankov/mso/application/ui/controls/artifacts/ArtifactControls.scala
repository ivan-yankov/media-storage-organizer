package org.yankov.mso.application.ui.controls.artifacts

import org.yankov.mso.application.Main
import org.yankov.mso.application.model.DataManager
import org.yankov.mso.application.ui.console.{ApplicationConsole, ConsoleService}
import scalafx.scene.layout.Pane

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
}
