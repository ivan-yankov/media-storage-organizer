package org.yankov.mso.application

import org.scalatest._

import java.io.File
import java.nio.file.Files

class FileUtilsTest extends FreeSpec with Matchers {
  private val basePath = "/file-utils"

  "read text file" - {
    val path = basePath + "/read-text-file.txt"

    "accept all lines" in {
      FileUtils.readTextFile(path) shouldBe Right(
        List(
          "First line",
          "Not acceptable line",
          "Second line",
          "Third line"
        )
      )
    }

    "filter lines" in {
      FileUtils.readTextFile(path, x => !x.toLowerCase().startsWith("not")) shouldBe Right(
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
        val file = File.createTempFile(fileName, "")
        file.deleteOnExit()

        FileUtils.writeTextFile(lines, file.getPath)
        FileUtils.writeTextFile(lines, file.getPath)

        val actual = Files.readAllBytes(file.toPath)
        val expected = getClass.getResourceAsStream(path(fileName)).readAllBytes()
        actual shouldBe expected
      }

      "append" in {
        val fileName = "write-text-file-appended.txt"
        val file = File.createTempFile(fileName, "")
        file.deleteOnExit()

        FileUtils.writeTextFile(lines, file.getPath)
        FileUtils.writeTextFile(lines, file.getPath, append = true)

        val actual = Files.readAllBytes(file.toPath)
        val expected = getClass.getResourceAsStream(path(fileName)).readAllBytes()
        actual shouldBe expected
      }
    }
  }
}
