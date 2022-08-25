package yankov.mso.application

import org.scalatest._

import java.nio.file.Paths

class FileUtilsTest extends FreeSpec with Matchers {
  private val basePath = "/file-utils"

  "read text file" - {
    val path = basePath + "/read-text-file.txt"

    "accept all lines" in {
      FileUtils.readTextFile(getClass.getResourceAsStream(path)) shouldBe Right(
        List(
          "First line",
          "Not acceptable line",
          "Second line",
          "Third line"
        )
      )
    }

    "filter lines" in {
      FileUtils.readTextFile(getClass.getResourceAsStream(path), x => !x.toLowerCase().startsWith("not")) shouldBe Right(
        List(
          "First line",
          "Second line",
          "Third line"
        )
      )
    }

    "write text file" - {
      def path(fileName: String): String = basePath + "/" + fileName
      val lines = List(
        "First line",
        "Second line",
        "",
        "Third line"
      )

      "create and rewrite" in {
        val fileName = "write-text-file.txt"
        val file = TestHelpers.tempFile(fileName)

        FileUtils.writeTextFile(lines, file.toPath)
        FileUtils.writeTextFile(lines, file.toPath)

        TestHelpers.assertFilesEqual(file.toPath, path(fileName))
      }

      "append" in {
        val fileName = "write-text-file-appended.txt"
        val file = TestHelpers.tempFile(fileName)

        FileUtils.writeTextFile(lines, file.toPath)
        FileUtils.writeTextFile(lines, file.toPath, append = true)

        TestHelpers.assertFilesEqual(file.toPath, path(fileName))
      }
    }
  }

  "file extensions" - {
    val fileWithExtensionLong = Paths.get("some", "path", "to", "the", "file.ext")
    val fileWithExtensionSimple = Paths.get("file.ext")

    val fileWithoutExtensionLong = Paths.get("some", "path", "to", "the", "file")
    val fileWithoutExtensionSimple = Paths.get("file")

    "file name extension" - {
      "file with extension" in {
        FileUtils.fileNameExtension(fileWithExtensionLong) shouldBe ".ext"
        FileUtils.fileNameExtension(fileWithExtensionSimple) shouldBe ".ext"
      }

      "file without extension" in {
        FileUtils.fileNameExtension(fileWithoutExtensionLong) shouldBe ""
        FileUtils.fileNameExtension(fileWithoutExtensionSimple) shouldBe ""
      }
    }

    "file name without extension" - {
      "file with extension" in {
        FileUtils.fileNameWithoutExtension(fileWithExtensionLong) shouldBe "file"
        FileUtils.fileNameWithoutExtension(fileWithExtensionSimple) shouldBe "file"
      }

      "file without extension" in {
        FileUtils.fileNameWithoutExtension(fileWithoutExtensionLong) shouldBe "file"
        FileUtils.fileNameWithoutExtension(fileWithoutExtensionSimple) shouldBe "file"
      }
    }
  }
}
