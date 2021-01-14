package org.yankov.mso.application.search

import org.yankov.mso.application.Resources.Operators.{containsLabel, equalsLabel, notContainsLabel, notEqualsLabel}
import org.yankov.mso.application.Resources.Variables._
import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.search.TextAnalyzer._

object SearchModel {

  case class Variable[T](label: String, valueProvider: T => String, searchIndex: List[SearchIndexElement])

  object Variables {
    val varTitle: Variable[FolkloreTrack] = Variable[FolkloreTrack](title, x => x.title, getSearchIndexes.titleIndex)
    val varPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](performer, x => x.performer.name, getSearchIndexes.performerIndex)
    val varAccompanimentPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](accompanimentPerformer, x => x.accompanimentPerformer.name, getSearchIndexes.accompanimentPerformerIndex)
    val varArrangementAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](arrangementAuthor, x => x.arrangementAuthor.name, getSearchIndexes.arrangementAuthorIndex)
    val varConductor: Variable[FolkloreTrack] = Variable[FolkloreTrack](conductor, x => x.conductor.name, getSearchIndexes.conductorIndex)
    val varInstrumentPerformance: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentPerformance, x => x.performer.instrument.name, getSearchIndexes.instrumentPerformanceIndex)
    val varInstrumentAccompaniment: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentAccompaniment, x => x.accompanimentPerformer.instrument.name, getSearchIndexes.instrumentAccompanimentIndex)
    val varSoloist: Variable[FolkloreTrack] = Variable[FolkloreTrack](soloist, x => x.soloist.name, getSearchIndexes.soloistIndex)
    val varAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](author, x => x.author.name, getSearchIndexes.authorIndex)
    val varEthnographicRegion: Variable[FolkloreTrack] = Variable[FolkloreTrack](ethnographicRegion, x => x.ethnographicRegion.name, getSearchIndexes.ethnographicRegionIndex)
    val varTrackNote: Variable[FolkloreTrack] = Variable[FolkloreTrack](trackNote, x => x.note, getSearchIndexes.noteIndex)
    val varSourceType: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceType, x => x.source.sourceType.name, getSearchIndexes.sourceTypeIndex)
    val varSourceSignature: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceSignature, x => x.source.signature, getSearchIndexes.sourceSignatureIndex)

    def asList: List[Variable[FolkloreTrack]] = List(
      varTitle,
      varPerformer,
      varAccompanimentPerformer,
      varArrangementAuthor,
      varConductor,
      varInstrumentPerformance,
      varInstrumentAccompaniment,
      varSoloist,
      varAuthor,
      varEthnographicRegion,
      varTrackNote,
      varSourceType,
      varSourceSignature
    )

    private def getSearchIndexes: SearchIndexes = SearchIndexesInstance.getInstance
  }

  case class Filter[T](label: String, execute: (Variable[T], String, List[T]) => List[T])

  object Filters {
    val filterEquals: Filter[FolkloreTrack] = Filter(
      equalsLabel,
      (variable, value, tracks) => tracks.filter(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
    )

    val filterNotEquals: Filter[FolkloreTrack] = Filter(
      notEqualsLabel,
      (variable, value, tracks) => tracks.filterNot(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
    )

    val filterContains: Filter[FolkloreTrack] = Filter(
      containsLabel,
      (variable, value, tracks) => tracks.filter(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
    )

    val filterNotContains: Filter[FolkloreTrack] = Filter(
      notContainsLabel,
      (variable, value, tracks) => tracks.filterNot(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
    )

    def asList: List[Filter[FolkloreTrack]] = List(
      filterContains,
      filterNotContains,
      filterEquals,
      filterNotEquals
    )
  }

  case class SearchParameters[T](variable: Variable[T], filter: Filter[T], value: String)

  case class SearchIndexElement(term: String, ids: List[Int])

}
