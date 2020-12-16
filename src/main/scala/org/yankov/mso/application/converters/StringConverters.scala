package org.yankov.mso.application.converters

import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.SearchModel.{Operator, Variable}

object StringConverters {
  def artistToString(x: Option[Artist]): String = if (x.isDefined) x.get.name else ""

  def ethnographicRegionToString(x: Option[EthnographicRegion]): String = if (x.isDefined) x.get.name else ""

  def instrumentToString(x: Option[Instrument]): String = if (x.isDefined) x.get.name else ""

  def sourceToString(x: Option[Source]): String = {
    if (x.isDefined) {
      if (x.get.signature.isDefined) sourceTypeToString(x.get.sourceType) + "/" + x.get.signature.get
      else sourceTypeToString(x.get.sourceType)
    }
    else ""
  }

  def sourceTypeToString(x: Option[SourceType]): String = if (x.isDefined) x.get.name else ""

  def operatorToString(x: Operator): String = x.label

  def variableToString(x: Variable[_]): String = x.label
}
