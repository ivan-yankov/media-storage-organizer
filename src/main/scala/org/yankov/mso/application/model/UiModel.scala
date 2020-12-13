package org.yankov.mso.application.model

import java.time.Duration

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.{DurationConverter, StringConverters}
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.EmptyValues._
import scalafx.beans.property.StringProperty
import scalafx.scene.image.Image
import scalafx.scene.layout.Pane
import scalafx.stage.Screen

object UiModel {

  object ApplicationSettings {
    def isMaximized: Boolean = true

    def getIcon: Image = {
      val url = getClass.getResource("/icons/application.png")
      new Image(url.toString)
    }

    def getTitle: String = Resources.MainForm.title

    def getWindowWidth: Double = 1000.0

    def getWindowHeight: Double = 500.0

    def getX: Double = Screen.primary.visualBounds.minX

    def getY: Double = Screen.primary.visualBounds.minY
  }

  case class FolkloreTrackProperties(track: FolkloreTrack) {
    def id: StringProperty = StringProperty(track.id.toString)
    def title: StringProperty = StringProperty(track.title)
    def performer: StringProperty = StringProperty(track.performer.getOrElse(emptyArtist).name)
    def accompanimentPerformer: StringProperty = StringProperty(track.accompanimentPerformer.getOrElse(emptyArtist).name)
    def arrangementAuthor: StringProperty = StringProperty(track.arrangementAuthor.getOrElse(emptyArtist).name)
    def conductor: StringProperty = StringProperty(track.conductor.getOrElse(emptyArtist).name)
    def author: StringProperty = StringProperty(track.author.getOrElse(emptyArtist).name)
    def soloist: StringProperty = StringProperty(track.soloist.getOrElse(emptyArtist).name)
    def duration: StringProperty = StringProperty(printDuration(track.duration))
    def source: StringProperty = StringProperty(StringConverters.sourceToString(track.source))
    def ethnographicRegion: StringProperty = StringProperty(track.ethnographicRegion.getOrElse(emptyEthnographicRegion).name)
    def note: StringProperty = StringProperty(track.note)
    def file: StringProperty = StringProperty(if (track.file.isDefined) track.file.get.getName else "")

    private def printDuration(duration: Duration): String =
      "%02d:%02d".format(DurationConverter.toMinutesPart(duration), DurationConverter.toSecondsPart(duration))
  }

  trait Control[T] {
    def getContainer: Pane

    def getValue: T
  }

}
