package yankov.mso.application.search

import yankov.mso.application.Id
import yankov.mso.application.Resources.Filters._
import yankov.mso.application.Resources.Variables._
import yankov.mso.application.converters.StringConverters
import yankov.mso.application.model.DataModel.{Artist, FolkloreTrack}
import yankov.mso.application.search.TextAnalyzer._

object SearchModel {
  trait Variable {
    def label: String
  }

  object Variables {
    case object TitleVariable extends Variable {
      override def label: String = title
    }

    case object PerformerVariable extends Variable {
      override def label: String = performer
    }

    case object AccompanimentPerformerVariable extends Variable {
      override def label: String = accompanimentPerformer
    }

    case object ArrangementAuthorVariable extends Variable {
      override def label: String = arrangementAuthor
    }

    case object ConductorVariable extends Variable {
      override def label: String = conductor
    }

    case object InstrumentPerformanceVariable extends Variable {
      override def label: String = instrumentPerformance
    }

    case object InstrumentAccompanimentVariable extends Variable {
      override def label: String = instrumentAccompaniment
    }

    case object SoloistVariable extends Variable {
      override def label: String = soloist
    }

    case object AuthorVariable extends Variable {
      override def label: String = author
    }

    case object EthnographicRegionVariable extends Variable {
      override def label: String = ethnographicRegion
    }

    case object TrackNoteVariable extends Variable {
      override def label: String = trackNote
    }

    case object SourceVariable extends Variable {
      override def label: String = source
    }

    case object IdVariable extends Variable {
      override def label: String = id
    }

    def asList: List[Variable] = List(
      TitleVariable,
      PerformerVariable,
      AccompanimentPerformerVariable,
      ArrangementAuthorVariable,
      ConductorVariable,
      SourceVariable,
      IdVariable,
      TrackNoteVariable,
      InstrumentPerformanceVariable,
      InstrumentAccompanimentVariable,
      SoloistVariable,
      AuthorVariable,
      EthnographicRegionVariable
    )

    def getValue(variable: Variable, track: FolkloreTrack, artistExactMatch: Boolean): String = {
      def searchName(artist: Artist): String = {
        if (artistExactMatch) artist.name
        else (artist.name :: artist.members).mkString(",")
      }

      variable match {
        case TitleVariable => track.title
        case PerformerVariable => searchName(track.performer)
        case AccompanimentPerformerVariable => searchName(track.accompanimentPerformer)
        case ArrangementAuthorVariable => searchName(track.arrangementAuthor)
        case ConductorVariable => searchName(track.conductor)
        case InstrumentPerformanceVariable => getInstruments(track.performer).mkString(",")
        case InstrumentAccompanimentVariable => getInstruments(track.accompanimentPerformer).mkString(",")
        case SoloistVariable => searchName(track.soloist)
        case AuthorVariable => searchName(track.author)
        case EthnographicRegionVariable => track.ethnographicRegion.name
        case TrackNoteVariable => track.note
        case SourceVariable => StringConverters.sourceToString(track.source)
        case IdVariable => track.id
      }
    }

    private def getInstruments(artist: Artist): List[String] = {
      if (artist.members.nonEmpty) artist.members.flatMap(x => x.instruments.map(_.name))
      else artist.instruments.map(_.name)
    }
  }

  case class Filter[T](label: String, execute: (Variable, String, List[T]) => List[T])

  object Filters {
    import Variables._

    val filterEquals: Filter[FolkloreTrack] = Filter(
      equalsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filter(x => analyze(getValue(variable, x, artistExactMatch = true)).equalsIgnoreCase(analyze(value)))
        else tracks
      }
    )

    val filterNotEquals: Filter[FolkloreTrack] = Filter(
      notEqualsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filterNot(x => analyze(getValue(variable, x, artistExactMatch = true)).equalsIgnoreCase(analyze(value)))
        else tracks
      }
    )

    val filterContains: Filter[FolkloreTrack] = Filter(
      containsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filter(x => analyze(getValue(variable, x, artistExactMatch = false)).contains(analyze(value)))
        else tracks
      }
    )

    val filterNotContains: Filter[FolkloreTrack] = Filter(
      notContainsLabel,
      (variable, value, tracks) => {
        if (value.nonEmpty) tracks.filterNot(x => analyze(getValue(variable, x, artistExactMatch = false)).contains(analyze(value)))
        else tracks
      }
    )

    val filterEmpty: Filter[FolkloreTrack] = Filter(
      emptyLabel,
      (variable, _, tracks) => tracks.filter(x => getValue(variable, x, artistExactMatch = true).isBlank)
    )

    def asList: List[Filter[FolkloreTrack]] = List(
      filterContains,
      filterNotContains,
      filterEquals,
      filterNotEquals,
      filterEmpty
    )
  }

  case class SearchParameters[T](variable: Variable, filter: Filter[T], value: String)

  case class SearchIndexElement(term: String, ids: List[Id])
}
