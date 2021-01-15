package org.yankov.mso.application.search

import org.yankov.mso.application.Resources.Filters._
import org.yankov.mso.application.Resources.Variables._
import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.search.TextAnalyzer._

object SearchModel {

  private val maxFuzzySearchResults = 30

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
      (variable, value, tracks) =>
        tracks
          .filter(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
          .sortWith(idComparator)
    )

    val filterNotEquals: Filter[FolkloreTrack] = Filter(
      notEqualsLabel,
      (variable, value, tracks) =>
        tracks
          .filterNot(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyze(value)))
          .sortWith(idComparator)
    )

    val filterContains: Filter[FolkloreTrack] = Filter(
      containsLabel,
      (variable, value, tracks) =>
        tracks
          .filter(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
          .sortWith(idComparator)
    )

    val filterNotContains: Filter[FolkloreTrack] = Filter(
      notContainsLabel,
      (variable, value, tracks) =>
        tracks
          .filterNot(x => analyze(variable.valueProvider(x)).contains(analyze(value)))
          .sortWith(idComparator)
    )

    val filterFuzzySearch: Filter[FolkloreTrack] = Filter(
      fuzzySearchLabel,
      (variable, value, tracks) => {
        def levenshteinMatch(a: String, b: String): Boolean = {
          val d = {
            if (a.split(" ").length > 1 && b.split(" ").length > 1) 2
            else 1
          }
          levenshteinDistance(a, b) <= d
        }

        val analyzedValue = analyze(value)
        val exactMatches = tracks
          .filter(x => analyze(variable.valueProvider(x)).equalsIgnoreCase(analyzedValue))
        val containsMatches = tracks
          .diff(exactMatches)
          .filter(x => analyze(variable.valueProvider(x)).contains(analyzedValue))
        val levenshteinMatches = tracks
          .diff(exactMatches)
          .diff(containsMatches)
          .filter(x => levenshteinMatch(analyze(variable.valueProvider(x)), analyzedValue))

        val indexAnalyzedValue = indexAnalyze(value)

        val ids = variable.searchIndex.filter(x => indexAnalyzedValue.contains(x.term)).map(x => x.ids)
        val idsFound = ids
          .flatten
          .distinct
          .map(x => (x, ids.count(y => y.contains(x))))
          .sortWith((x, y) => x._2 > y._2)
          .map(x => x._1)

        val fuzzyMatches = tracks
          .diff(exactMatches)
          .diff(containsMatches)
          .diff(levenshteinMatches)
          .filter(x => idsFound.contains(x.id))

        (exactMatches ++ containsMatches ++ levenshteinMatches ++ fuzzyMatches).take(maxFuzzySearchResults)
      }
    )

    def asList: List[Filter[FolkloreTrack]] = List(
      filterFuzzySearch,
      filterContains,
      filterNotContains,
      filterEquals,
      filterNotEquals
    )

    private def idComparator: (FolkloreTrack, FolkloreTrack) => Boolean = (x, y) => x.id < y.id
  }

  case class SearchParameters[T](variable: Variable[T], filter: Filter[T], value: String)

  case class SearchIndexElement(term: String, ids: List[Int])

}
