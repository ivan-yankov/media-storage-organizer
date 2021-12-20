package org.yankov.mso.application.model

import org.yankov.mso.application.{Id, Resources}
import org.yankov.mso.application.converters.{DurationConverter, StringConverters}
import org.yankov.mso.application.model.DataModel._
import scalafx.beans.property.StringProperty
import scalafx.scene.image.Image
import scalafx.stage.Screen

import java.io.File

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

  case class AudioSearchMatch(id: Id, identical: Boolean)

  case class TrackTableProperties(track: FolkloreTrack, audioSearchMatch: Option[AudioSearchMatch] = None) {
    def id: StringProperty = StringProperty(track.id)

    def title: StringProperty = StringProperty(track.title)

    def performer: StringProperty = StringProperty(track.performer.name)

    def accompanimentPerformer: StringProperty = StringProperty(track.accompanimentPerformer.name)

    def arrangementAuthor: StringProperty = StringProperty(track.arrangementAuthor.name)

    def conductor: StringProperty = StringProperty(track.conductor.name)

    def author: StringProperty = StringProperty(track.author.name)

    def soloist: StringProperty = StringProperty(track.soloist.name)

    def duration: StringProperty = StringProperty(DurationConverter.toHourMinSecString(track.duration, trimLeadingZeros = true))

    def source: StringProperty = StringProperty(StringConverters.sourceToString(track.source))

    def ethnographicRegion: StringProperty = StringProperty(track.ethnographicRegion.name)

    def note: StringProperty = StringProperty(track.note)

    def file: StringProperty = StringProperty(if (track.file.isDefined) track.file.get.getName else "")

    def sampleId: StringProperty = {
      audioSearchMatch match {
        case Some(x) => StringProperty(x.id)
        case None => StringProperty("")
      }
    }

    def matchType: StringProperty = {
      audioSearchMatch match {
        case Some(x) =>
          StringProperty(
            if (x.identical) Resources.Search.audioSearchIdentical
            else Resources.Search.audioSearchSimilar
          )
        case None => StringProperty("")
      }
    }
  }
}
