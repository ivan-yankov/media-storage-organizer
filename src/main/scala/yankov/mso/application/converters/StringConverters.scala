package yankov.mso.application.converters

import yankov.mso.application.model.DataModel._
import yankov.mso.application.search.SearchModel.{Filter, Variable}
import yankov.mso.application.model.DataModel.{Artist, EthnographicRegion, Instrument, Source, SourceType}

object StringConverters {
  def artistToString(x: Artist): String = x.name

  def ethnographicRegionToString(x: EthnographicRegion): String = x.name

  def instrumentToString(x: Instrument): String = x.name

  def sourceToString(x: Source): String = List(x.sourceType.name, x.label, x.signature).filter(_.nonEmpty).mkString("/")

  def sourceTypeToString(x: SourceType): String = x.name

  def filterToString(x: Filter[_]): String = x.label

  def variableToString(x: Variable): String = x.label
}
