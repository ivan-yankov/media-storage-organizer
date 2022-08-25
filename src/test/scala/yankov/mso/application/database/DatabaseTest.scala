package yankov.mso.application.database

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.scalatest._
import yankov.mso.application.{Id, TestHelpers}
import yankov.mso.application.model.DatabaseModel.DbEntry
import yankov.mso.application.TestHelpers
import yankov.mso.application.model.DatabaseModel.DbEntry

import java.nio.file.{Path, Paths}

class DatabaseTest extends FreeSpec with Matchers {
  case class Entry(id: Id, k1: String, k2: String, k3: Int) extends DbEntry
  implicit val encoder: Encoder[Entry] = deriveEncoder[Entry]
  implicit val decoder: Decoder[Entry] = deriveDecoder[Entry]

  private val entries = List(
    Entry("3451ee65-18cd-4839-a9e7-82720c6e563e", "v11", "v12", 13),
    Entry("2df054de-dac6-4606-983b-f05387347d0a", "v21", "v22", 23),
    Entry("84b76cd7-460c-41ad-b2f6-93f65cacb240", "v31", "v32", 33)
  )

  private val basePath = "/database"

  private def path(fileName: String): Path = Paths.get(basePath, fileName)

  private def database: Database = RealDatabase()

  "insert succeed" in {
    val fileName = "insert.txt"
    val file = TestHelpers.tempFile(fileName)
    database.insert(entries, file.toPath)
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }

  "read succeed" in {
    val fileName = "read.txt"
    database.read[Entry](
      entries.map(x => x.id),
      path(fileName),
      x => getClass.getResourceAsStream(x.toString)
    ) shouldBe Right(entries.map(x => x))
  }

  "update succeed" in {
    val fileName = "update.txt"
    val file = TestHelpers.tempFile(fileName)
    database.insert(entries, file.toPath)
    val result = database.update(
      List(
        Entry(entries.tail.head.id, "v1", "v2", 3),
        Entry("2bcfde23-e63e-4678-8fca-078748b822c6", "v61", "v62", 63)
      ),
      file.toPath
    )
    result.isRight shouldBe true
    result.right.get shouldBe List(entries.tail.head.id)
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }

  "delete succeed" in {
    val fileName = "delete.txt"
    val file = TestHelpers.tempFile(fileName)
    database.insert(entries, file.toPath)
    val result = database.delete(
      List(
        "3451ee65-18cd-4839-a9e7-82720c6e563e",
        "2df054de-dac6-4606-983b-f05387347d0a",
        "581c91e0-41fc-451a-aa6d-d49f49b18596"
      ),
      file.toPath
    )
    result.isRight shouldBe true
    TestHelpers.assertFilesEqual(file.toPath, path(fileName).toString)
  }
}
