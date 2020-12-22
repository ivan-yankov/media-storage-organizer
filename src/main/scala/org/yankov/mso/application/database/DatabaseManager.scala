package org.yankov.mso.application.database

import java.sql._

import org.yankov.mso.application.database.SqlModel._

import scala.annotation.tailrec

object DatabaseManager {
  private val columnName = "COLUMN_NAME"
  private val dataType = "DATA_TYPE"

  def createSchema(connection: Connection, name: String): Either[Throwable, Unit] = {
    try {
      connection.prepareStatement(s"CREATE SCHEMA $name").execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def createTable(connection: Connection, schemaName: String, tableName: String, columns: List[ColumnDefinition]): Either[Throwable, Unit] = {
    try {
      val fields = columns
        .map(x => columnToString(x))
        .mkString(", ")
      connection.prepareStatement(s"CREATE TABLE $schemaName.$tableName($fields)").execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def dropTable(connection: Connection, schemaName: String, tableName: String): Either[Throwable, Unit] = {
    try {
      connection.prepareStatement(s"DROP TABLE $schemaName.$tableName").execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def addColumn(connection: Connection, schemaName: String, tableName: String, column: ColumnDefinition): Either[Throwable, Unit] = {
    try {
      connection.prepareStatement(s"ALTER TABLE $schemaName.$tableName ADD ${columnToString(column)}").execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def dropColumn(connection: Connection, schemaName: String, tableName: String, columnName: String): Either[Throwable, Unit] = {
    try {
      connection.prepareStatement(s"ALTER TABLE $schemaName.$tableName DROP $columnName").execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def insert(connection: Connection, schemaName: String, tableName: String, columns: List[String], data: List[SqlValue]): Either[Throwable, Unit] = {
    try {
      val presented = columns
        .zip(data)
        .filter(x => x._2.nonEmpty)

      val s = connection.prepareStatement(insertQuery(schemaName, tableName, presented.map(x => x._1)))

      setRow(s, presented.map(x => x._2))

      val result = s.executeUpdate()
      if (result == 1) Right()
      else throw new SQLException("Insert was not successful.")
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def select(connection: Connection, schemaName: String, tableName: String, columns: List[String] = List(), criteria: List[Clause] = List()): Either[Throwable, List[List[SqlValue]]] = {
    try {
      val s = connection.prepareStatement(selectQuery(schemaName, tableName, columns, criteria))
      setStatementParameters(criteria.map(x => x.value), s, 1)
      val result = s.executeQuery()

      @tailrec
      def iterate(acc: List[List[SqlValue]]): List[List[SqlValue]] = {
        if (!result.next()) acc
        else {
          val row = getRow(connection, result, schemaName, tableName, columns)
          iterate(acc ++ List(row))
        }
      }

      val r = iterate(List())
      Right(r)
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def update(connection: Connection, schemaName: String, tableName: String, columns: List[String], data: List[SqlValue], criteria: List[Clause]): Either[Throwable, Unit] = {
    try {
      val s = connection.prepareStatement(updateQuery(schemaName, tableName, columns, criteria))
      setStatementParameters(data, s, 1)
      setStatementParameters(criteria.map(x => x.value), s, data.size + 1)
      s.executeUpdate()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  def delete(connection: Connection, schemaName: String, tableName: String, criteria: List[Clause]): Either[Throwable, Unit] = {
    try {
      val s = connection.prepareStatement(s"DELETE FROM $schemaName.$tableName${criteriaQuery(criteria)}")
      setStatementParameters(criteria.map(x => x.value), s, 1)
      s.execute()
      Right()
    } catch {
      case e: SQLException => Left(e)
    }
  }

  private def columnToString(column: ColumnDefinition): String = s"${column.name} ${column.sqlType.stringRepresentation} ${column.constraint}"

  private def insertQuery(schemaName: String, tableName: String, columns: List[String]): String = {
    val placeholders = columns.map(_ => "?").mkString(",")
    s"INSERT INTO $schemaName.$tableName(${columns.mkString(",")}) VALUES($placeholders)"
  }

  private def selectQuery(schemaName: String, tableName: String, columns: List[String], criteria: List[Clause]): String = {
    val s = if (columns.nonEmpty) columns.mkString(",") else "*"
    s"SELECT $s FROM $schemaName.$tableName${criteriaQuery(criteria)}"
  }

  private def updateQuery(schemaName: String, tableName: String, columns: List[String], criteria: List[Clause]): String = {
    val values = columns.map(x => s"$x=?").mkString(",")
    s"UPDATE $schemaName.$tableName SET $values${criteriaQuery(criteria)}"
  }

  private def criteriaQuery(criteria: List[Clause]): String =
    if (criteria.nonEmpty) criteria.map(x => s" ${x.name} ${x.column}${x.operator}?").mkString else ""

  private def setStatementValue(s: PreparedStatement, i: Int, x: SqlValue): Unit = {
    if (x.isEmpty) {
      x match {
        case BytesSqlValue(_) => s.setBytes(i, List().toArray)
        case StringSqlValue(_) => s.setString(i, "")
        case _ => s.setNull(i, x.sqlType.javaTypeCode)
      }
    }
    else {
      x match {
        case IntSqlValue(value) => s.setInt(i, value.get)
        case LongSqlValue(value) => s.setLong(i, value.get)
        case DoubleSqlValue(value) => s.setDouble(i, value.get)
        case BooleanSqlValue(value) => s.setBoolean(i, value.get)
        case BytesSqlValue(value) => s.setBytes(i, value.get.toArray)
        case VarcharSqlValue(value) => s.setString(i, value.get)
        case StringSqlValue(value) => s.setString(i, value.get)
      }
    }
  }

  private def getResultValue(result: ResultSet, columnName: String, columnType: Int): SqlValue = {
    def getInt: Option[Int] = {
      try {
        val r = result.getInt(columnName)
        if (result.wasNull()) Option.empty else Option(r)
      } catch {
        case _: NullPointerException => Option.empty
      }
    }
    def getLong: Option[Long] = {
      try {
        val r = result.getLong(columnName)
        if (result.wasNull()) Option.empty else Option(r)
      } catch {
        case _: NullPointerException => Option.empty
      }
    }
    def getDouble: Option[Double] = {
      try {
        val r = result.getDouble(columnName)
        if (result.wasNull()) Option.empty else Option(r)
      } catch {
        case _: NullPointerException => Option.empty
      }
    }
    def getBoolean: Option[Boolean] = {
      try {
        val r = result.getBoolean(columnName)
        if (result.wasNull()) Option.empty else Option(r)
      } catch {
        case _: NullPointerException => Option.empty
      }
    }
    def getBytes: Option[Bytes] = {
      try {
        Option(result.getBytes(columnName).toList)
      } catch {
        case _: NullPointerException => Option.empty
      }
    }
    def getString: Option[String] = {
      try {
        Option(result.getString(columnName))
      } catch {
        case _: NullPointerException => Option.empty
      }
    }

    columnType match {
      case Types.INTEGER => IntSqlValue(getInt)
      case Types.BIGINT => LongSqlValue(getLong)
      case Types.FLOAT => DoubleSqlValue(getDouble)
      case Types.DOUBLE => DoubleSqlValue(getDouble)
      case Types.BIT => BooleanSqlValue(getBoolean)
      case Types.BOOLEAN => BooleanSqlValue(getBoolean)
      case Types.BINARY => BytesSqlValue(getBytes)
      case Types.VARBINARY => BytesSqlValue(getBytes)
      case Types.LONGVARBINARY => BytesSqlValue(getBytes)
      case Types.BLOB => BytesSqlValue(getBytes)
      case Types.CHAR => VarcharSqlValue(getString)
      case Types.VARCHAR => VarcharSqlValue(getString)
      case Types.LONGVARCHAR => VarcharSqlValue(getString)
      case Types.CLOB => StringSqlValue(getString)
    }
  }

  private def setRow(s: PreparedStatement, values: List[SqlValue]): Unit = {
    values
      .indices
      .toList
      .map(x => x + 1)
      .zip(values)
      .foreach(x => setStatementValue(s, x._1, x._2))
  }

  private def getRow(connection: Connection, result: ResultSet, schemaName: String, tableName: String, columns: List[String]): List[SqlValue] = {
    val allTableColumnsResultSet = connection.getMetaData.getColumns(null, schemaName, tableName, null)

    @tailrec
    def getAllTableColumns(acc: List[(String, Int)]): List[(String, Int)] = {
      if (!allTableColumnsResultSet.next()) acc
      else getAllTableColumns(acc ++ List((allTableColumnsResultSet.getString(columnName), allTableColumnsResultSet.getInt(dataType))))
    }

    val allTableColumns = getAllTableColumns(List())

    val selectColumns = if (columns.nonEmpty) columns else allTableColumns.map(x => x._1)

    allTableColumns
      .filter(x => selectColumns.contains(x._1))
      .map(x => getResultValue(result, x._1, x._2))
  }

  private def setStatementParameters(parameters: List[SqlValue], statement: PreparedStatement, startIndex: Int): Unit = {
    parameters
      .indices
      .toList
      .map(x => x + startIndex)
      .zip(parameters)
      .foreach(x => setStatementValue(statement, x._1, x._2))
  }
}
