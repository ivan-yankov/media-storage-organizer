package yankov.mso.application

import org.scalatest._

import java.io.File
import java.nio.file.{Files, Path}

object TestHelpers extends Matchers {
  def assertFilesEqual(actualFile: Path, reference: String): Unit = {
    val actual = Files.readAllBytes(actualFile)
    val expected = getClass.getResourceAsStream(reference).readAllBytes()
    actual shouldBe expected
  }

  def tempFile(fileName: String): File = {
    val file = File.createTempFile(fileName, "")
    file.deleteOnExit()
    file
  }
}
