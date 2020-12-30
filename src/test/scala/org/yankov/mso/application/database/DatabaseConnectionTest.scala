package org.yankov.mso.application.database

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class DatabaseConnectionTest extends WordSpec with Matchers with BeforeAndAfterAll {
 override def beforeAll(): Unit = System.setSecurityManager(null)

  "connect should succeed" in {
    val connectionString = ConnectionStringFactory.createDerbyConnectionString(
      InMemoryDatabaseProtocol,
      "test",
      Map("create" -> "true")
    )

    DatabaseConnection.connect(connectionString).isRight shouldBe true
  }
}
