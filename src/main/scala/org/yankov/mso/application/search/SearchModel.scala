package org.yankov.mso.application.search

import org.yankov.mso.application.Id
import org.yankov.mso.application.Resources.Filters._
import org.yankov.mso.application.Resources.Variables._
import org.yankov.mso.application.model.DataModel.{Artist, FolkloreTrack}
import org.yankov.mso.application.search.TextAnalyzer._

object SearchModel {
  case class Variable[T](label: String, valueProvider: T => String)

  object Variables {
    val varTitle: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](title, x => x.title)
    val varPerformer: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](performer, x => x.performer.composedName)
    val varAccompanimentPerformer: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](accompanimentPerformer, x => x.accompanimentPerformer.composedName)
    val varArrangementAuthor: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](arrangementAuthor, x => x.arrangementAuthor.composedName)
    val varConductor: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](conductor, x => x.conductor.composedName)
    val varInstrumentPerformance: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](instrumentPerformance, x => getInstruments(x.performer).mkString(","))
    val varInstrumentAccompaniment: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](instrumentAccompaniment, x => getInstruments(x.accompanimentPerformer).mkString(","))
    val varSoloist: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](soloist, x => x.soloist.composedName)
    val varAuthor: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](author, x => x.author.composedName)
    val varEthnographicRegion: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](ethnographicRegion, x => x.ethnographicRegion.name)
    val varTrackNote: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](trackNote, x => x.note)
    val varSourceType: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](sourceType, x => x.source.sourceType.name)
    val varSourceSignature: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](sourceSignature, x => x.source.signature)
    val varId: Variable[FolkloreTrack] =
      Variable[FolkloreTrack](id, x => x.id)

    def asList: List[Variable[FolkloreTrack]] = List(
      varTitle,
      varPerformer,
      varAccompanimentPerformer,
      varArrangementAuthor,
      varConductor,
      varSourceType,
      varSourceSignature,
      varId,
      varTrackNote,
      varInstrumentPerformance,
      varInstrumentAccompaniment,
      varSoloist,
      varAuthor,
      varEthnographicRegion
    )

    private def getInstruments(artist: Artist): List[String] = {
      if (artist.members.nonEmpty) artist.members.flatMap(x => x.instruments.map(_.name))
      else artist.instruments.map(_.name)
    }
  }

  case class Filter[T](label: String, execute: (Variable[T], String, List[T]) => List[T])

  object Filters {
    val filterEquals: Filter[FolkloreTrack] = Filter(
      equalsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filter(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
        else tracks
      }
    )

    val filterNotEquals: Filter[FolkloreTrack] = Filter(
      notEqualsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filterNot(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
        else tracks
      }
    )

    val filterContains: Filter[FolkloreTrack] = Filter(
      containsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filter(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
        else tracks
      }
    )

    val filterNotContains: Filter[FolkloreTrack] = Filter(
      notContainsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filterNot(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
        else tracks
      }
    )

    val filterEmpty: Filter[FolkloreTrack] = Filter(
      emptyLabel,
      (variable, _, tracks) => tracks.filter(x => variable.valueProvider(x).isBlank)
    )

    def asList: List[Filter[FolkloreTrack]] = List(
      filterContains,
      filterNotContains,
      filterEquals,
      filterNotEquals,
      filterEmpty
    )
  }

  case class SearchParameters[T](variable: Variable[T], filter: Filter[T], value: String)

  case class SearchIndexElement(term: String, ids: List[Id])
}
