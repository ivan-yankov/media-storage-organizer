package org.yankov.mso.application.ui

import scalafx.scene.image.Image
import scalafx.stage.Screen

object UiModel {

  object ApplicationSettings {
    def isMaximized: Boolean = true

    def getIcon: Image = {
      val url = getClass.getResource("/icons/application.png")
      new Image(url.toString)
    }

    def getTitle: String = Resources.Stage.title

    def getWindowWidth: Double = 1000.0

    def getWindowHeight: Double = 500.0

    def getX: Double = Screen.primary.visualBounds.minX

    def getY: Double = Screen.primary.visualBounds.minY
  }

}
