package org.yankov.mso.application.model

import java.time.Duration

import org.yankov.mso.application.converters.DurationConverter
import org.yankov.mso.application.model.DataModel.{FolkloreTrack, Source, emptyArtist, emptyEthnographicRegion}
import org.yankov.mso.application.ui.Resources
import scalafx.beans.property.StringProperty
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

  case class FolkloreTrackProperties(folkloreTrack: FolkloreTrack) {
    val id: StringProperty = StringProperty(folkloreTrack.id.toString)
    val title: StringProperty = StringProperty(folkloreTrack.title)
    val performer: StringProperty = StringProperty(folkloreTrack.performer.getOrElse(emptyArtist).name)
    val accompanimentPerformer: StringProperty = StringProperty(folkloreTrack.accompanimentPerformer.getOrElse(emptyArtist).name)
    val arrangementAuthor: StringProperty = StringProperty(folkloreTrack.arrangementAuthor.getOrElse(emptyArtist).name)
    val conductor: StringProperty = StringProperty(folkloreTrack.conductor.getOrElse(emptyArtist).name)
    val author: StringProperty = StringProperty(folkloreTrack.author.getOrElse(emptyArtist).name)
    val soloist: StringProperty = StringProperty(folkloreTrack.soloist.getOrElse(emptyArtist).name)
    val duration: StringProperty = StringProperty(printDuration(folkloreTrack.duration))
    val source: StringProperty = StringProperty(printSource(folkloreTrack.source))
    val ethnographicRegion: StringProperty = StringProperty(folkloreTrack.ethnographicRegion.getOrElse(emptyEthnographicRegion).name)
    val note: StringProperty = StringProperty(folkloreTrack.note.getOrElse(""))
    val file: StringProperty = StringProperty("")

    private def printDuration(duration: Duration): String =
      "%02d:%02d".format(DurationConverter.toMinutesPart(duration), DurationConverter.toSecondsPart(duration))

    private def printSource(source: Option[Source]): String = {
      if (source.isDefined) {
        val src = source.get
        if (src.signature.isDefined) src.sourceType.name + "/" + src.signature.get
        else src.sourceType.name
      }
      else ""
    }
  }

}
