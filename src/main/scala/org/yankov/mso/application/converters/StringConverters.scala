package org.yankov.mso.application.converters

import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.search.SearchModel.{Filter, Variable}

object StringConverters {
  def artistToString(x: Artist): String = x.displayName

  def ethnographicRegionToString(x: EthnographicRegion): String = x.name

  def instrumentToString(x: Instrument): String = x.name

  def sourceToString(x: Source): String = {
    if (x.signature.nonEmpty) x.sourceType.name + "/" + x.signature
    else x.sourceType.name
  }

  def sourceTypeToString(x: SourceType): String = x.name

  def filterToString(x: Filter[_]): String = x.label

  def variableToString(x: Variable[_]): String = x.label
}
