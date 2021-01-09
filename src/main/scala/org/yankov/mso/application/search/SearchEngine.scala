package org.yankov.mso.application.search

import org.yankov.mso.application.search.SearchModel.Filter

import java.time.Duration
import scala.annotation.tailrec

object SearchEngine {

  def search[T](tracks: List[T], filters: List[Filter[T]], trackComparator: (T, T) => Boolean, getDuration: T => Duration): (List[T], Duration) = {
    val result = applyFilters(filters, tracks)
    (
      result.sortWith(trackComparator),
      result
        .map(x => getDuration(x))
        .foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    )
  }

  @tailrec
  private def applyFilters[T](filters: List[Filter[T]], tracks: List[T]): List[T] = {
    if (filters.isEmpty) tracks
    else applyFilters(filters.tail, applyFilter(tracks, filters.head))
  }

  private def applyFilter[T](tracks: List[T], filter: Filter[T]): List[T] = {
    if (filter.value.isEmpty) tracks
    else tracks.filter(x => {
      val searchValue = TextAnalyzer.analyze(filter.value)
      val trackValue = TextAnalyzer.analyze(filter.variable.valueProvider(x))
      filter.operator.predicate(trackValue, searchValue)
    })
  }
}
