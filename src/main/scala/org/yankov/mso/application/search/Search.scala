package org.yankov.mso.application.search


import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.media.AudioIndex
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel._
import org.yankov.mso.application.search.SearchModel._
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.UiTable

import java.time.Duration

object Search {
  def metadataSearch(searchParameters: List[SearchParameters[FolkloreTrack]],
                     allTracks: List[FolkloreTrack],
                     resultTable: UiTable[TrackTableProperties]): Unit = {
    val tracks = {
      if (searchParameters.forall(x => x.value.isBlank)) allTracks
      else {
        SearchEngine.metadataSearch[FolkloreTrack](
          allTracks,
          searchParameters
        ).zipWithIndex.sortBy(x => (StringConverters.sourceToString(x._1.source), x._2)).map(x => x._1)
      }
    }

    resultTable.setItems(tracks.map(x => TrackTableProperties(x)))

    val totalDuration = tracks.map(x => x.duration).foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }

  def audioSearch(inputs: List[AudioSearchSample],
                  tracks: List[FolkloreTrack],
                  audioIndex: Option[AudioIndex],
                  resultTable: UiTable[TrackTableProperties],
                  correlationThreshold: Double,
                  crossCorrelationShift: Int): Unit = {
    if (inputs.isEmpty || audioIndex.isEmpty) return

    val searchResults: List[List[TrackTableProperties]] = audioIndex.get
      .search(inputs, tracks.map(x => x.id), correlationThreshold, crossCorrelationShift)
      .map(
        x => {
          if (x._2.isEmpty) List(TrackTableProperties(FolkloreTrack(), Some(AudioSearchResult(x._1, None))))
          else x._2.map(y => TrackTableProperties(tracks.find(z => z.id.equals(y.matchDetails.get.matchId)).get, Some(y)))
        }
      )

    resultTable.setItems(
      searchResults.foldLeft(List[TrackTableProperties]())(
        (acc, x) => {
          if (acc.nonEmpty) acc ++ List(TrackTableProperties(FolkloreTrack(), None)) ++ x
          else x
        }
      )
    )
  }
}
