package org.yankov.mso.application.search

import org.yankov.mso.application.search.SearchModel._

import scala.annotation.tailrec

object SearchEngine {
  def metadataSearch[T](tracks: List[T], searchParameters: List[MetadataSearchParameters[T]]): List[T] = {
    applyFilters(searchParameters, tracks)
  }

  @tailrec
  private def applyFilters[T](searchParameters: List[MetadataSearchParameters[T]], tracks: List[T]): List[T] = {
    if (searchParameters.isEmpty) tracks
    else applyFilters(searchParameters.tail, applyFilter(tracks, searchParameters.head))
  }

  private def applyFilter[T](tracks: List[T], searchParameters: MetadataSearchParameters[T]): List[T] =
    searchParameters.filter.execute(searchParameters.variable, searchParameters.value, tracks)
}
