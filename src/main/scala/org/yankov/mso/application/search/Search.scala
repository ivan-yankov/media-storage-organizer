package org.yankov.mso.application.search


import org.yankov.mso.application.converters.StringConverters
import org.yankov.mso.application.media.AudioIndex
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.UiModel._
import org.yankov.mso.application.search.SearchModel._
import org.yankov.mso.application.ui.console.ApplicationConsole
import org.yankov.mso.application.ui.controls.UiTable
import org.yankov.mso.application.{Id, Resources}

import java.io.InputStream
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
        ).sortBy(x => (StringConverters.sourceToString(x.source), x.note, x.title))
      }
    }

    resultTable.setItems(tracks.map(x => TrackTableProperties(x)))

    val totalDuration = tracks.map(x => x.duration).foldLeft(Duration.ZERO)((x, y) => x.plus(y))
    val message = Resources.Search.totalItemsFound(tracks.size, totalDuration)
    ApplicationConsole.writeMessageWithTimestamp(message)
  }

  def audioSearch(inputs: Map[Id, InputStream],
                  allTracks: List[FolkloreTrack],
                  audioIndex: Option[AudioIndex],
                  resultTable: UiTable[TrackTableProperties],
                  correlationThreshold: Double,
                  crossCorrelationShift: Int): Unit = {
    if (inputs.isEmpty || audioIndex.isEmpty) return

    val searchResults = audioIndex.get.search(inputs, correlationThreshold, crossCorrelationShift)

    def collectResults(onCollection: AudioSearchResult => List[Id], identical: Boolean): List[TrackTableProperties] = {
      searchResults
        .flatMap(
          x =>
            List
              .fill(onCollection(x).size)(x.sampleId)
              .zip(onCollection(x))
              .map(
                y =>
                  TrackTableProperties(
                    allTracks.find(z => z.id.equals(y._2)).getOrElse(FolkloreTrack()),
                    Some(AudioSearchMatch(y._1, identical = identical))
                  )
              )
        ).filter(x => isValidId(x.track.id))
    }

    val exactMatchResults = collectResults(x => x.exactMatches, identical = true)
    val similarMatchResults = collectResults(x => x.similarMatches, identical = false)
    resultTable.setItems(exactMatchResults ++ similarMatchResults)
  }
}
