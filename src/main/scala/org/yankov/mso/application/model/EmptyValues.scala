package org.yankov.mso.application.model

import java.time.Duration

import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.SearchModel._

object EmptyValues {
  def emptyInstrument: Instrument = Instrument(-1, "")

  def emptyEthnographicRegion: EthnographicRegion = EthnographicRegion(-1, "")

  def emptySourceType: SourceType = SourceType(-1, "")

  def emptySource: Source = Source(-1, Option.empty, Option.empty)

  def emptyArtist: Artist = Artist(-1, "", Option.empty, Option.empty, List())

  def emptyFolkloreTrack: FolkloreTrack = FolkloreTrack(
    id = -1,
    title = "",
    performer = Option.empty,
    accompanimentPerformer = Option.empty,
    author = Option.empty,
    arrangementAuthor = Option.empty,
    conductor = Option.empty,
    soloist = Option.empty,
    duration = Duration.ZERO,
    note = "",
    source = Option.empty,
    ethnographicRegion = Option.empty,
    file = Option.empty,
    recordFormat = "FLAC"
  )

  def emptyOperator: Operator = Operator("", (_, _) => false)

  def emptyVariable[T]: Variable[T] = Variable("", _ => "")
}
