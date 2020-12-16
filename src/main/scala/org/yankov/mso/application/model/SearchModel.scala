package org.yankov.mso.application.model


object SearchModel {
  case class Operator(label: String, predicate: (String, String) => Boolean)

  case class Variable[T](label: String, valueProvider: T => String)

  case class Filter[T](variable: Variable[T], operator: Operator, value: String)
}
