package org.yankov.mso.application

import org.scalatest._

import java.io.File

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
}
