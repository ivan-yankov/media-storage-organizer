package org.yankov.mso.application.search

import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.search.SearchModel.SearchIndexElement

case class SearchIndexes(tracks: List[FolkloreTrack]) {
  val titleIndex: List[SearchIndexElement] = buildIndex(x => x.title)
  val performerIndex: List[SearchIndexElement] = buildIndex(x => x.performer.name)
  val accompanimentPerformerIndex: List[SearchIndexElement] = buildIndex(x => x.accompanimentPerformer.name)
  val arrangementAuthorIndex: List[SearchIndexElement] = buildIndex(x => x.arrangementAuthor.name)
  val conductorIndex: List[SearchIndexElement] = buildIndex(x => x.conductor.name)
  val instrumentPerformanceIndex: List[SearchIndexElement] = buildIndex(x => x.performer.instrument.name)
  val instrumentAccompanimentIndex: List[SearchIndexElement] = buildIndex(x => x.accompanimentPerformer.instrument.name)
  val soloistIndex: List[SearchIndexElement] = buildIndex(x => x.soloist.name)
  val authorIndex: List[SearchIndexElement] = buildIndex(x => x.author.name)
  val ethnographicRegionIndex: List[SearchIndexElement] = buildIndex(x => x.ethnographicRegion.name)
  val noteIndex: List[SearchIndexElement] = buildIndex(x => x.note)
  val sourceTypeIndex: List[SearchIndexElement] = buildIndex(x => x.source.sourceType.name)
  val sourceSignatureIndex: List[SearchIndexElement] = buildIndex(x => x.source.signature)

  private def buildIndex(valueProvider: FolkloreTrack => String): List[SearchIndexElement] = {
    def findIds(term: String): List[Int] = {
      tracks
        .filter(x => valueProvider(x).contains(term))
        .map(x => x.id)
    }

    val result = tracks
      .map(x => valueProvider(x))
      .filter(x => x.nonEmpty)
      .distinct
      .flatMap(x => TextAnalyzer.indexAnalyze(x))
      .map(x => SearchIndexElement(x, findIds(x)))
    result
  }
}

object SearchIndexesInstance {
  private var instance: SearchIndexes = SearchIndexes(List())

  def getInstance: SearchIndexes = instance

  def setInstance(instance: SearchIndexes): Unit = this.instance = instance
}
