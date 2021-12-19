package org.yankov.mso.application.search

import org.yankov.mso.application.Resources
import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.model.DataModel.FolkloreTrack
import org.yankov.mso.application.model.UiModel.TrackTableProperties
import org.yankov.mso.application.search.SearchModel._
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.UiTable

import java.time.Duration

object Search {
  def metadataSearch(searchParameters: List[MetadataSearchParameters[FolkloreTrack]],
                     allTracks: List[FolkloreTrack],
                     resultTable: UiTable[TrackTableProperties]): Unit = {
    val tracks = {
      if (searchParameters.forall(x => x.value.isBlank)) allTracks
      else {
        SearchEngine.metadataSearch[FolkloreTrack](
          allTracks,
          searchParameters
        ).sortBy(x => (StringConverters.sourceToString(x.source), x.note, x.title))
      }
    }

    resultTable.setItems(tracks.map(x => TrackTableProperties(x)))

    val totalDuration = tracks.map(x => x.duration).foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }

  def audioSearch(searchParameters: List[AudioSearchParameters]): Unit = {
    ???
  }
}
