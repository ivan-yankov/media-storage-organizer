package org.yankov.mso.application.commands

import java.time.Duration

import org.yankov.mso.application.model.SearchModel.Filter

import scala.annotation.tailrec

case class SearchEngine[T](tracks: List[T],
                           trackComparator: (T, T) => Boolean,
                           getDuration: T => Duration) {
  def search(filters: List[Filter[T]]): (List[T], Duration) = {
    val result = applyFilters(filters, tracks)
    (
      result.sortWith(trackComparator),
      calculateTotalDuration(result)
    )
  }

  @tailrec
  private def applyFilters(filters: List[Filter[T]], tracks: List[T]): List[T] = {
    if (filters.isEmpty) tracks
    else applyFilters(filters.tail, applyFilter(tracks, filters.head))
  }

  private def applyFilter(tracks: List[T], filter: Filter[T]): List[T] = {
    if (filter.value.isEmpty) tracks
    else tracks.filter(x => {
      val searchValue = analyze(filter.value)
      val trackValue = analyze(filter.variable.valueProvider(x))
      filter.operator.predicate(searchValue, trackValue)
    })
  }

  private def calculateTotalDuration(tracks: List[T]): Duration = {
    tracks
      .map(x => getDuration(x))
      .foldLeft(Duration.ZERO)((x, y) => x.plus(y))
  }

  private def analyze(s: String): String = {
    removePunctuation(s)
  }

  private def removePunctuation(s: String): String = {
    val symbols = List(",", ";", "-", "/", "\"")

    @tailrec
    def replace(input: String, replaceWhat: List[String], replaceWith: String): String = {
      if (replaceWhat.isEmpty) input
      else replace(input.replace(replaceWhat.head, replaceWith), replaceWhat.tail, replaceWith)
    }

    replace(s, symbols, " ")
      .trim
      // replace multiple spaces with a single space
      .replaceAll(" +", " ")
  }
}
