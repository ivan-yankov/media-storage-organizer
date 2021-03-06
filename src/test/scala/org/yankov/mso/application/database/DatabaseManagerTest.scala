package org.yankov.mso.application.database

import java.sql.{Connection, Types}

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import org.yankov.mso.application.database.SqlModel._

class DatabaseManagerTest extends WordSpec with Matchers with BeforeAndAfterAll {
  override def beforeAll(): Unit = System.setSecurityManager(null)

  "create schema, create table and drop table should succeed" in {
    val connection = createDatabase("test-schema-table")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.notNull),
      ColumnDefinition("VAL", SqlTypes.double)
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val tables = connection.getMetaData.getTables(null, schema, null, Array("TABLE"))
    tables.next() shouldBe true
    tables.getString("TABLE_SCHEM") shouldBe schema
    tables.getString("TABLE_NAME") shouldBe table

    DatabaseManager.dropTable(connection, schema, table).isRight shouldBe true

    connection
      .getMetaData
      .getTables(null, schema, null, Array("TABLE"))
      .next() shouldBe false

    connection.close()
  }

  "add column should succeed" in {
    val connection = createDatabase("test-add-column")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.notNull),
      ColumnDefinition("VAL", SqlTypes.double)
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val tableColumns = connection.getMetaData.getColumns(null, schema, table, null)
    tableColumns.next() shouldBe true
    tableColumns.getString("COLUMN_NAME") shouldBe "ID"
    tableColumns.getInt("DATA_TYPE") shouldBe Types.INTEGER
    tableColumns.next() shouldBe true
    tableColumns.getString("COLUMN_NAME") shouldBe "VAL"
    tableColumns.getInt("DATA_TYPE") shouldBe Types.DOUBLE
    tableColumns.next() shouldBe false

    DatabaseManager.addColumn(
      connection,
      schema,
      table,
      ColumnDefinition("ADDED_COLUMN", SqlTypes.varchar())
    ).isRight shouldBe true

    val newTableColumns = connection.getMetaData.getColumns(null, schema, table, null)
    newTableColumns.next() shouldBe true
    newTableColumns.getString("COLUMN_NAME") shouldBe "ID"
    newTableColumns.getInt("DATA_TYPE") shouldBe Types.INTEGER
    newTableColumns.next() shouldBe true
    newTableColumns.getString("COLUMN_NAME") shouldBe "VAL"
    newTableColumns.getInt("DATA_TYPE") shouldBe Types.DOUBLE
    newTableColumns.next() shouldBe true
    newTableColumns.getString("COLUMN_NAME") shouldBe "ADDED_COLUMN"
    newTableColumns.getInt("DATA_TYPE") shouldBe Types.VARCHAR
    newTableColumns.next() shouldBe false

    connection.close()
  }

  "drop column should succeed" in {
    val connection = createDatabase("test-drop-column")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.notNull),
      ColumnDefinition("VAL", SqlTypes.double)
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val tableColumns = connection.getMetaData.getColumns(null, schema, table, null)
    tableColumns.next() shouldBe true
    tableColumns.getString("COLUMN_NAME") shouldBe "ID"
    tableColumns.getInt("DATA_TYPE") shouldBe Types.INTEGER
    tableColumns.next() shouldBe true
    tableColumns.getString("COLUMN_NAME") shouldBe "VAL"
    tableColumns.getInt("DATA_TYPE") shouldBe Types.DOUBLE
    tableColumns.next() shouldBe false

    DatabaseManager.dropColumn(connection, schema, table, "VAL").isRight shouldBe true

    val newTableColumns = connection.getMetaData.getColumns(null, schema, table, null)
    newTableColumns.next() shouldBe true
    newTableColumns.getString("COLUMN_NAME") shouldBe "ID"
    newTableColumns.getInt("DATA_TYPE") shouldBe Types.INTEGER
    newTableColumns.next() shouldBe false

    connection.close()
  }

  "insert and select all rows, all columns should succeed" in {
    val connection = createDatabase("test-insert-select-all-rows-all-columns")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("LONG_COL", SqlTypes.long),
      ColumnDefinition("DOUBLE_COL", SqlTypes.double),
      ColumnDefinition("BOOLEAN_COL", SqlTypes.boolean),
      ColumnDefinition("BYTES_COL", SqlTypes.bytes),
      ColumnDefinition("STRING_COL", SqlTypes.string),
      ColumnDefinition("VARCHAR_COL", SqlTypes.varchar())
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(
      IntSqlValue(Option(1)),
      LongSqlValue(Option(1)),
      DoubleSqlValue(Option(1.0)),
      BooleanSqlValue(Option(true)),
      BytesSqlValue(Option("bytes-1".getBytes.toList)),
      StringSqlValue(Option("string 1")),
      VarcharSqlValue(Option("string 11"))
    )
    val r2 = List(
      IntSqlValue(Option(2)),
      LongSqlValue(Option(2)),
      DoubleSqlValue(Option(2.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-2".getBytes.toList)),
      StringSqlValue(Option("string 2")),
      VarcharSqlValue(Option("string 22"))
    )
    val r3 = List(
      IntSqlValue(Option(3)),
      LongSqlValue(Option(3)),
      DoubleSqlValue(Option(3.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-3".getBytes.toList)),
      StringSqlValue(Option("string 3")),
      VarcharSqlValue(Option("string 33"))
    )

    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r3).isRight shouldBe true

    val result = DatabaseManager.select(connection, schema, table)
    result.isRight shouldBe true
    result.right.get shouldBe List(r1, r2, r3)
  }

  "insert and select some rows, all columns should succeed" in {
    val connection = createDatabase("test-insert-select-some-rows-all-columns")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("LONG_COL", SqlTypes.long),
      ColumnDefinition("DOUBLE_COL", SqlTypes.double),
      ColumnDefinition("BOOLEAN_COL", SqlTypes.boolean),
      ColumnDefinition("BYTES_COL", SqlTypes.bytes),
      ColumnDefinition("STRING_COL", SqlTypes.string),
      ColumnDefinition("VARCHAR_COL", SqlTypes.varchar())
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(
      IntSqlValue(Option(1)),
      LongSqlValue(Option(1)),
      DoubleSqlValue(Option(1.0)),
      BooleanSqlValue(Option(true)),
      BytesSqlValue(Option("bytes-1".getBytes.toList)),
      StringSqlValue(Option("string 1")),
      VarcharSqlValue(Option("string 11"))
    )
    val r2 = List(
      IntSqlValue(Option(2)),
      LongSqlValue(Option(2)),
      DoubleSqlValue(Option(2.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-2".getBytes.toList)),
      StringSqlValue(Option("string 2")),
      VarcharSqlValue(Option("string 22"))
    )
    val r3 = List(
      IntSqlValue(Option(3)),
      LongSqlValue(Option(3)),
      DoubleSqlValue(Option(3.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-3".getBytes.toList)),
      StringSqlValue(Option("string 3")),
      VarcharSqlValue(Option("string 33"))
    )

    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r3).isRight shouldBe true

    val result = DatabaseManager.select(
      connection,
      schemaName = schema,
      tableName = table,
      columns = List(),
      criteria = List(
        WhereClause("ID", "=", IntSqlValue(Option(1))),
        OrClause("ID", "=", IntSqlValue(Option(3)))
      )
    )
    result.isRight shouldBe true
    result.right.get shouldBe List(r1, r3)
  }

  "insert and select all rows, some columns should succeed" in {
    val connection = createDatabase("test-insert-select-all-rows-some-columns")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("LONG_COL", SqlTypes.long),
      ColumnDefinition("DOUBLE_COL", SqlTypes.double),
      ColumnDefinition("BOOLEAN_COL", SqlTypes.boolean),
      ColumnDefinition("BYTES_COL", SqlTypes.bytes),
      ColumnDefinition("STRING_COL", SqlTypes.string),
      ColumnDefinition("VARCHAR_COL", SqlTypes.varchar())
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(
      IntSqlValue(Option(1)),
      LongSqlValue(Option(1)),
      DoubleSqlValue(Option(1.0)),
      BooleanSqlValue(Option(true)),
      BytesSqlValue(Option("bytes-1".getBytes.toList)),
      StringSqlValue(Option("string 1")),
      VarcharSqlValue(Option("string 11"))
    )
    val r2 = List(
      IntSqlValue(Option(2)),
      LongSqlValue(Option(2)),
      DoubleSqlValue(Option(2.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-2".getBytes.toList)),
      StringSqlValue(Option("string 2")),
      VarcharSqlValue(Option("string 22"))
    )
    val r3 = List(
      IntSqlValue(Option(3)),
      LongSqlValue(Option(3)),
      DoubleSqlValue(Option(3.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-3".getBytes.toList)),
      StringSqlValue(Option("string 3")),
      VarcharSqlValue(Option("string 33"))
    )

    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r3).isRight shouldBe true

    val expectedData = List(
      List(
        LongSqlValue(Option(1)),
        DoubleSqlValue(Option(1.0))
      ),
      List(
        LongSqlValue(Option(2)),
        DoubleSqlValue(Option(2.0))
      ),
      List(
        LongSqlValue(Option(3)),
        DoubleSqlValue(Option(3.0))
      )
    )

    val result = DatabaseManager.select(
      connection,
      schemaName = schema,
      tableName = table,
      columns = List("LONG_COL", "DOUBLE_COL"),
      criteria = List()
    )
    result.isRight shouldBe true
    result.right.get shouldBe expectedData
  }

  "insert and select all rows, all columns with null values should succeed" in {
    val connection = createDatabase("test-insert-select-all-rows-all-columns-handle-null")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("INT_COL", SqlTypes.int),
      ColumnDefinition("LONG_COL", SqlTypes.long),
      ColumnDefinition("DOUBLE_COL", SqlTypes.double),
      ColumnDefinition("BOOLEAN_COL", SqlTypes.boolean),
      ColumnDefinition("BYTES_COL", SqlTypes.bytes),
      ColumnDefinition("STRING_COL", SqlTypes.string),
      ColumnDefinition("VARCHAR_COL", SqlTypes.varchar())
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(IntSqlValue(Option(1)))
    val r2 = List(
      IntSqlValue(Option(2)),
      IntSqlValue(Option.empty),
      LongSqlValue(Option.empty),
      DoubleSqlValue(Option.empty),
      BooleanSqlValue(Option.empty),
      BytesSqlValue(Option.empty),
      StringSqlValue(Option.empty),
      VarcharSqlValue(Option.empty)
    )

    DatabaseManager.insert(connection, schema, table, List("id"), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true

    val result = DatabaseManager.select(connection, schema, table)
    result.isRight shouldBe true
    result.getOrElse() shouldBe List(
      List(
        IntSqlValue(Option(1)),
        IntSqlValue(Option.empty),
        LongSqlValue(Option.empty),
        DoubleSqlValue(Option.empty),
        BooleanSqlValue(Option.empty),
        BytesSqlValue(Option.empty),
        StringSqlValue(Option.empty),
        VarcharSqlValue(Option.empty),
      ),
      List(
        IntSqlValue(Option(2)),
        IntSqlValue(Option.empty),
        LongSqlValue(Option.empty),
        DoubleSqlValue(Option.empty),
        BooleanSqlValue(Option.empty),
        BytesSqlValue(Option.empty),
        StringSqlValue(Option.empty),
        VarcharSqlValue(Option.empty),
      )
    )
  }

  "update should succeed" in {
    val connection = createDatabase("test-update")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("INT_COL", SqlTypes.int),
      ColumnDefinition("LONG_COL", SqlTypes.long),
      ColumnDefinition("DOUBLE_COL", SqlTypes.double),
      ColumnDefinition("BOOLEAN_COL", SqlTypes.boolean),
      ColumnDefinition("BYTES_COL", SqlTypes.bytes),
      ColumnDefinition("STRING_COL", SqlTypes.string),
      ColumnDefinition("VARCHAR_COL", SqlTypes.varchar())
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(
      IntSqlValue(Option(1)),
      IntSqlValue(Option(1)),
      LongSqlValue(Option(1)),
      DoubleSqlValue(Option(1.0)),
      BooleanSqlValue(Option(true)),
      BytesSqlValue(Option("bytes-1".getBytes.toList)),
      StringSqlValue(Option("string 1")),
      VarcharSqlValue(Option("string 11"))
    )
    val r2 = List(
      IntSqlValue(Option(2)),
      IntSqlValue(Option(2)),
      LongSqlValue(Option(2)),
      DoubleSqlValue(Option(2.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-2".getBytes.toList)),
      StringSqlValue(Option("string 2")),
      VarcharSqlValue(Option("string 22"))
    )
    val r3 = List(
      IntSqlValue(Option(3)),
      IntSqlValue(Option(3)),
      LongSqlValue(Option(3)),
      DoubleSqlValue(Option(3.0)),
      BooleanSqlValue(Option(false)),
      BytesSqlValue(Option("bytes-3".getBytes.toList)),
      StringSqlValue(Option("string 3")),
      VarcharSqlValue(Option("string 33"))
    )

    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r3).isRight shouldBe true

    DatabaseManager.update(
      connection,
      schema,
      table,
      columns.map(x => x.name).filter(x => !x.equals("ID")),
      List(
        IntSqlValue(Option(22)),
        LongSqlValue(Option(22)),
        DoubleSqlValue(Option(22.0)),
        BooleanSqlValue(Option(true)),
        BytesSqlValue(Option("bytes-22".getBytes.toList)),
        StringSqlValue(Option("string 22")),
        VarcharSqlValue(Option("string 222"))
      ),
      List(WhereClause("ID", "=", IntSqlValue(Option(2))))
    )
    DatabaseManager.update(
      connection,
      schema,
      table,
      columns.map(x => x.name).filter(x => !x.equals("ID")),
      List(
        IntSqlValue(Option.empty),
        LongSqlValue(Option.empty),
        DoubleSqlValue(Option.empty),
        BooleanSqlValue(Option.empty),
        BytesSqlValue(Option.empty),
        StringSqlValue(Option.empty),
        VarcharSqlValue(Option.empty)
      ),
      List(WhereClause("ID", "=", IntSqlValue(Option(3))))
    )

    val result = DatabaseManager.select(connection, schema, table)
    result.isRight shouldBe true
    result.right.get shouldBe List(
      r1,
      List(
        IntSqlValue(Option(2)),
        IntSqlValue(Option(22)),
        LongSqlValue(Option(22)),
        DoubleSqlValue(Option(22.0)),
        BooleanSqlValue(Option(true)),
        BytesSqlValue(Option("bytes-22".getBytes.toList)),
        StringSqlValue(Option("string 22")),
        VarcharSqlValue(Option("string 222"))
      ),
      List(
        IntSqlValue(Option(3)),
        IntSqlValue(Option.empty),
        LongSqlValue(Option.empty),
        DoubleSqlValue(Option.empty),
        BooleanSqlValue(Option.empty),
        BytesSqlValue(Option(List())),
        StringSqlValue(Option("")),
        VarcharSqlValue(Option.empty)
      )
    )
  }

  "delete should succeed" in {
    val connection = createDatabase("test-delete")

    val schema = "SCM"
    val table = "TBL"
    val columns = List(
      ColumnDefinition("ID", SqlTypes.int, DerbySqlConstraints.primaryKey),
      ColumnDefinition("VAL1", SqlTypes.varchar()),
      ColumnDefinition("VAL2", SqlTypes.double)
    )

    DatabaseManager.createSchema(connection, schema).isRight shouldBe true
    DatabaseManager.createTable(connection, schema, table, columns).isRight shouldBe true

    val r1 = List(
      IntSqlValue(Option(1)),
      VarcharSqlValue(Option("string 1")),
      DoubleSqlValue(Option(1.0))
    )
    val r2 = List(
      IntSqlValue(Option(2)),
      VarcharSqlValue(Option("string 2")),
      DoubleSqlValue(Option(2.0))
    )
    val r3 = List(
      IntSqlValue(Option(3)),
      VarcharSqlValue(Option("string 3")),
      DoubleSqlValue(Option(3.0))
    )

    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r1).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r2).isRight shouldBe true
    DatabaseManager.insert(connection, schema, table, columns.map(x => x.name), r3).isRight shouldBe true

    val expectedData = List(
      List(
        IntSqlValue(Option(1)),
        VarcharSqlValue(Option("string 1")),
        DoubleSqlValue(Option(1.0))
      ),
      List(
        IntSqlValue(Option(3)),
        VarcharSqlValue(Option("string 3")),
        DoubleSqlValue(Option(3.0))
      )
    )

    DatabaseManager.delete(connection, schema, table, List(WhereClause("ID", "=", IntSqlValue(Option(2)))))

    val result = DatabaseManager.select(connection, schema, table)
    result.isRight shouldBe true
    result.right.get shouldBe expectedData
  }

  private def createDatabase(name: String): Connection = {
    val connectionString = ConnectionStringFactory.createDerbyConnectionString(
      InMemoryDatabaseProtocol,
      name,
      Map("create" -> "true")
    )

    DatabaseConnection.connect(connectionString).getOrElse().asInstanceOf[Connection]
  }
}
