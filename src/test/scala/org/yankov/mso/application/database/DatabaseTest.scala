package org.yankov.mso.application.database

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.scalatest._
import org.yankov.mso.application.TestHelpers

import java.nio.file.{Path, Paths}

class DatabaseTest extends FreeSpec with Matchers {
  case class Entry(k1: String, k2: String, k3: Int)
  implicit val encoder: Encoder[Entry] = deriveEncoder[Entry]
  implicit val decoder: Decoder[Entry] = deriveDecoder[Entry]

  private val basePath = "/database"

  private val entries = List(
    ("3451ee65-18cd-4839-a9e7-82720c6e563e", Entry("v11", "v12", 13)),
    ("2df054de-dac6-4606-983b-f05387347d0a", Entry("v21", "v22", 23)),
    ("84b76cd7-460c-41ad-b2f6-93f65cacb240", Entry("v31", "v32", 33))
  )

  private def path(fileName: String): Path = Paths.get(basePath, fileName)

  "insert succeed" in {
    val fileName = "insert.txt"
    val file = TestHelpers.tempFile(fileName)
    Database.insert(entries, file.toPath)
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }

  "read succeed" in {
    val fileName = "read.txt"
    Database.read[Entry](
      entries.map(x => x._1),
      path(fileName),
      x => getClass.getResourceAsStream(x.toString)
    ) shouldBe Right(entries.map(x => x._2))
  }

  "update succeed" in {
    val fileName = "update.txt"
    val file = TestHelpers.tempFile(fileName)
    Database.insert(entries, file.toPath)
    val result = Database.update(
      List(
        (entries.tail.head._1, Entry("v1", "v2", 3)),
        ("2bcfde23-e63e-4678-8fca-078748b822c6", Entry("v61", "v62", 63))
      ).toMap,
      file.toPath
    )
    result.isRight shouldBe true
    result.right.get shouldBe List(entries.tail.head._1)
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }

  "delete succeed" in {
    val fileName = "delete.txt"
    val file = TestHelpers.tempFile(fileName)
    Database.insert(entries, file.toPath)
    val result = Database.delete(
      List(
        "3451ee65-18cd-4839-a9e7-82720c6e563e",
        "2df054de-dac6-4606-983b-f05387347d0a",
        "581c91e0-41fc-451a-aa6d-d49f49b18596"
      ),
      file.toPath
    )
    result.isRight shouldBe true
    result.right.get shouldBe 2
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }
}
