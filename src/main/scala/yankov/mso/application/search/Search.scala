package yankov.mso.application.search

import yankov.mso.application.Resources
import yankov.mso.application.converters.StringConverters
import yankov.mso.application.media.AudioIndex
import yankov.mso.application.model.DataModel._
import yankov.mso.application.model.UiModel._
import yankov.mso.application.search.SearchModel._
import yankov.mso.application.ui.console.ApplicationConsole
import yankov.mso.application.ui.controls.UiTable
import yankov.mso.application.media.AudioIndex
import yankov.mso.application.model.DataModel.{AudioSearchSample, FolkloreTrack}
import yankov.mso.application.model.UiModel.TrackTableProperties
import yankov.mso.application.ui.console.ApplicationConsole
import yankov.mso.application.ui.controls.UiTable

import java.time.Duration

object Search {
  def metadataSearch(searchParameters: List[SearchParameters[FolkloreTrack]],
                     allTracks: List[FolkloreTrack],
                     resultTable: UiTable[TrackTableProperties]): Unit = {
    val tracks = {
      val searchResults = SearchEngine.metadataSearch[FolkloreTrack](allTracks, searchParameters)
      if (searchResults.size == allTracks.size) searchResults
      else searchResults.zipWithIndex.sortBy(x => (StringConverters.sourceToString(x._1.source), x._2)).map(x => x._1)
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
