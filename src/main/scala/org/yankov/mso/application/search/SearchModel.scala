package org.yankov.mso.application.search

import org.yankov.mso.application.Id
import org.yankov.mso.application.Resources.Filters._
import org.yankov.mso.application.Resources.Variables._
import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.search.TextAnalyzer._

import java.io.File

object SearchModel {
  case class Variable[T](label: String, valueProvider: T => String)

  object Variables {
    val varTitle: Variable[FolkloreTrack] = Variable[FolkloreTrack](title, x => x.title)
    val varPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](performer, x => x.performer.name)
    val varAccompanimentPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](accompanimentPerformer, x => x.accompanimentPerformer.name)
    val varArrangementAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](arrangementAuthor, x => x.arrangementAuthor.name)
    val varConductor: Variable[FolkloreTrack] = Variable[FolkloreTrack](conductor, x => x.conductor.name)
    val varInstrumentPerformance: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentPerformance, x => x.performer.instrument.name)
    val varInstrumentAccompaniment: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentAccompaniment, x => x.accompanimentPerformer.instrument.name)
    val varSoloist: Variable[FolkloreTrack] = Variable[FolkloreTrack](soloist, x => x.soloist.name)
    val varAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](author, x => x.author.name)
    val varEthnographicRegion: Variable[FolkloreTrack] = Variable[FolkloreTrack](ethnographicRegion, x => x.ethnographicRegion.name)
    val varTrackNote: Variable[FolkloreTrack] = Variable[FolkloreTrack](trackNote, x => x.note)
    val varSourceType: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceType, x => x.source.sourceType.name)
    val varSourceSignature: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceSignature, x => x.source.signature)
    val varId: Variable[FolkloreTrack] = Variable[FolkloreTrack](id, x => x.id)

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
        if (value.nonEmpty)
          tracks.filter(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
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

    def asList: List[Filter[FolkloreTrack]] = List(
      filterContains,
      filterNotContains,
      filterEquals,
      filterNotEquals
    )
  }

  case class SearchParameters[T](variable: Variable[T], filter: Filter[T], value: String)

  case class SearchIndexElement(term: String, ids: List[Id])
}
