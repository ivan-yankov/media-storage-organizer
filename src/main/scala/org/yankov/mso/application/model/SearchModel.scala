package org.yankov.mso.application.model

import org.yankov.mso.application.Resources.Variables._
import org.yankov.mso.application.Resources.Operators._
import org.yankov.mso.application.model.DataModel.FolkloreTrack

object SearchModel {

  case class Operator(label: String, predicate: (String, String) => Boolean)

  case class Variable[T](label: String, valueProvider: T => String)

  case class Filter[T](variable: Variable[T], operator: Operator, value: String)

  object Variables {
    val varTitle: Variable[FolkloreTrack] = Variable[FolkloreTrack](title, x => x.title)
    val varPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](performer, x => x.performer.name)
    val varAccompanimentPerformer: Variable[FolkloreTrack] = Variable[FolkloreTrack](accompanimentPerformer, x => x.accompanimentPerformer.name)
    val varArrangementAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](arrangementAuthor, x => x.arrangementAuthor.name)
    val varConductor: Variable[FolkloreTrack] = Variable[FolkloreTrack](conductor, x => x.conductor.name)
    val varInstrumentPerformance: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentPerformance, x => x.performer.instrument.name)
    val varInstrumentAccompaniment: Variable[FolkloreTrack] = Variable[FolkloreTrack](instrumentAccompaniment, x => x.accompanimentPerformer.instrument.name)
    val varSoloist: Variable[FolkloreTrack] = Variable[FolkloreTrack](soloist, x => x.soloist.name)
    val varAuthor: Variable[FolkloreTrack] = Variable[FolkloreTrack](author, x => x.author.name)
    val varEthnographicRegion: Variable[FolkloreTrack] = Variable[FolkloreTrack](ethnographicRegion, x => x.ethnographicRegion.name)
    val varTrackNote: Variable[FolkloreTrack] = Variable[FolkloreTrack](trackNote, x => x.note)
    val varSourceType: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceType, x => x.source.sourceType.name)
    val varSourceSignature: Variable[FolkloreTrack] = Variable[FolkloreTrack](sourceSignature, x => x.source.signature)

    def asList: List[Variable[FolkloreTrack]] = List(
      varTitle,
      varPerformer,
      varAccompanimentPerformer,
      varArrangementAuthor,
      varConductor,
      varInstrumentPerformance,
      varInstrumentAccompaniment,
      varSoloist,
      varAuthor,
      varEthnographicRegion,
      varTrackNote,
      varSourceType,
      varSourceSignature
    )
  }

  object Operators {
    val opEquals: Operator = Operator(equalsLabel, (x, y) => x.equalsIgnoreCase(y))
    val opNotEquals: Operator = Operator(notEqualsLabel, (x, y) => !x.equalsIgnoreCase(y))
    val opContains: Operator = Operator(containsLabel, (x, y) => x.contains(y))
    val opNotContains: Operator = Operator(notContainsLabel, (x, y) => !x.contains(y))

    def asList: List[Operator] = List(
      opContains,
      opNotContains,
      opEquals,
      opNotEquals
    )
  }

}
