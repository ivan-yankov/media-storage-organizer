package org.yankov.mso.application.database

import java.sql.Types

object SqlModel {

  type Bytes = List[Byte]

  case class SqlType(stringRepresentation: String, javaTypeCode: Int)

  object SqlTypes {
    val short: SqlType = SqlType("INTEGER", Types.INTEGER)
    val int: SqlType = SqlType("INTEGER", Types.INTEGER)
    val long: SqlType = SqlType("BIGINT", Types.BIGINT)
    val float: SqlType = SqlType("DOUBLE", Types.DOUBLE)
    val double: SqlType = SqlType("DOUBLE", Types.DOUBLE)
    val boolean: SqlType = SqlType("BOOLEAN", Types.BOOLEAN)
    val bytes: SqlType = SqlType("BLOB", Types.BLOB)
    val string: SqlType = SqlType("CLOB", Types.CLOB)

    def varchar(size: Int = 255): SqlType = SqlType(s"VARCHAR($size)", Types.VARCHAR)
  }

  object DerbySqlConstraints {
    val notNull: String = "NOT NULL"
    val primaryKey: String = "PRIMARY KEY"
    val unique: String = "UNIQUE"
    val foreignKey: String = "FOREIGN KEY"
    val check: String = "CHECK"
  }

  case class ColumnDefinition(name: String, sqlType: SqlType, constraint: String = "")

  trait Clause {
    def name: String

    def column: String

    def operator: String

    def value: SqlValue
  }

  case class WhereClause(column: String, operator: String, value: SqlValue) extends Clause {
    override def name: String = "WHERE"
  }

  case class AndClause(column: String, operator: String, value: SqlValue) extends Clause {
    override def name: String = "AND"
  }

  case class OrClause(column: String, operator: String, value: SqlValue) extends Clause {
    override def name: String = "OR"
  }

  trait SqlValue {
    def asIntOption: Option[Int] = this match {
      case IntSqlValue(value) => value
      case _ => Option.empty
    }

    def asLongOption: Option[Long] = this match {
      case LongSqlValue(value) => value
      case _ => Option.empty
    }

    def asDoubleOption: Option[Double] = this match {
      case DoubleSqlValue(value) => value
      case _ => Option.empty
    }

    def asBooleanOption: Option[Boolean] = this match {
      case BooleanSqlValue(value) => value
      case _ => Option.empty
    }

    def asBytesOption: Option[Bytes] = this match {
      case BytesSqlValue(value) => value
      case _ => Option.empty
    }

    def asStringOption: Option[String] = this match {
      case VarcharSqlValue(value) => value
      case StringSqlValue(value) => value
      case _ => Option.empty
    }

    def isEmpty: Boolean = {
      asIntOption.isEmpty &&
        asLongOption.isEmpty &&
        asDoubleOption.isEmpty &&
        asBooleanOption.isEmpty &&
        asBytesOption.isEmpty &&
        asStringOption.isEmpty
    }

    def nonEmpty: Boolean = !isEmpty

    def sqlType: SqlType
  }

  case class IntSqlValue(value: Option[Int]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.int
  }

  case class LongSqlValue(value: Option[Long]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.long
  }

  case class DoubleSqlValue(value: Option[Double]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.double
  }

  case class BooleanSqlValue(value: Option[Boolean]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.boolean
  }

  case class BytesSqlValue(value: Option[Bytes]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.bytes
  }

  case class StringSqlValue(value: Option[String]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.string
  }

  case class VarcharSqlValue(value: Option[String]) extends SqlValue {
    def sqlType: SqlType = SqlTypes.varchar()
  }

}
