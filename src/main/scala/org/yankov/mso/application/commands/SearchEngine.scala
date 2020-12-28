package org.yankov.mso.application.commands

import java.time.Duration

import org.yankov.mso.application.model.SearchModel.Filter

import scala.annotation.tailrec

object SearchEngine {

  implicit class StringAnalyzer(s: String) {
    def normalize(): String = s.toLowerCase

    def removePunctuation(): String = {
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

  def search[T](tracks: List[T], filters: List[Filter[T]], trackComparator: (T, T) => Boolean, getDuration: T => Duration): (List[T], Duration) = {
    val result = applyFilters(filters, tracks)
    (
      result.sortWith(trackComparator),
      tracks
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
      val searchValue = analyze(filter.value)
      val trackValue = analyze(filter.variable.valueProvider(x))
      filter.operator.predicate(trackValue, searchValue)
    })
  }

  private def analyze(str: String): String = {
    str
      .trim
      .normalize()
      .removePunctuation()
  }
}
