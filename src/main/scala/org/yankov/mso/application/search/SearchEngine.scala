package org.yankov.mso.application.search

import org.yankov.mso.application.search.SearchModel.SearchParameters

import java.time.Duration
import scala.annotation.tailrec

object SearchEngine {
  def search[T](tracks: List[T], searchParameters: List[SearchParameters[T]], getDuration: T => Duration): (List[T], Duration) = {
    val result = applyFilters(searchParameters, tracks)
    (
      result,
      result
        .map(x => getDuration(x))
        .foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    )
  }

  @tailrec
  private def applyFilters[T](searchParameters: List[SearchParameters[T]], tracks: List[T]): List[T] = {
    if (searchParameters.isEmpty) tracks
    else applyFilters(searchParameters.tail, applyFilter(tracks, searchParameters.head))
  }

  private def applyFilter[T](tracks: List[T], searchParameters: SearchParameters[T]): List[T] =
    searchParameters.filter.execute(searchParameters.variable, searchParameters.value, tracks)
}
