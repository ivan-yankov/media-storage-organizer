package yankov.mso.application

import org.slf4j.LoggerFactory

import java.io.{File, InputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, StandardOpenOption}
import java.util.Scanner
import scala.annotation.tailrec
import scala.collection.JavaConverters._

object FileUtils {
  private val log = LoggerFactory.getLogger(getClass)
  private val charset = StandardCharsets.UTF_8

  def readTextFile(inputStream: InputStream, acceptLine: String => Boolean = _ => true): Either[String, List[String]] = {
    tryOrException(() => new Scanner(inputStream)) match {
      case Left(e) =>
        log.error("Fail to initialize text file for reading", e)
        Left(e.getMessage)
      case Right(scanner) =>
        try {
          @tailrec
          def collectLines(lines: List[String]): List[String] = {
            if (scanner.hasNext) {
              val nextLine = scanner.nextLine()
              if (acceptLine(nextLine)) collectLines(nextLine :: lines)
              else collectLines(lines)
            }
            else lines
          }

          val result = collectLines(List()).filter(x => x.nonEmpty).reverse
          Right(result)
        }
        catch {
          case e: Exception =>
            log.error("Fail to read text file", e)
            Left(e.getMessage)
        }
        finally {
          scanner.close()
        }
    }
  }

  def writeTextFile(lines: List[String], path: Path, append: Boolean = false): Either[String, Unit] = {
    try {
      if (!append) Files.deleteIfExists(path)
      Files.write(
        path,
        lines.asJava,
        charset,
        if (append) StandardOpenOption.APPEND else StandardOpenOption.CREATE
      )
      Right(())
    }
    catch {
      case e: Exception =>
        log.error("Fail to write text file", e)
        Left(e.getMessage)
    }
  }

  def readBinaryFile(file: File): Either[String, Array[Byte]] = {
    try {
      Right(Files.readAllBytes(file.toPath))
    }
    catch {
      case e: Exception =>
        log.error("Fail to read binary file", e)
        Left(e.getMessage)
    }
  }

  def writeBinaryFile(file: File, bytes: Array[Byte]): Boolean = {
    try {
      Files.write(file.toPath, bytes)
      true
    }
    catch {
      case e: Exception =>
        log.error("Fail to write binary file", e)
        false
    }
  }

  def deleteFile(file: File): Boolean = Files.deleteIfExists(file.toPath)

  def getFiles(path: Path): List[Path] = Files.walk(path).iterator.asScala.toList.filter(x => Files.isRegularFile(x))

  def fileNameExtension(path: Path): String = {
    val fileName = path.getFileName.toString
    if (fileName.contains(".")) fileName.substring(fileName.lastIndexOf("."))
    else ""
  }

  def fileNameWithoutExtension(path: Path): String = {
    val fileName = path.getFileName.toString
    if (fileName.contains(".")) fileName.substring(0, fileName.lastIndexOf("."))
    else fileName
  }

  private def tryOrException[T](f: () => T): Either[Throwable, T] = {
    try {
      Right(f())
    }
    catch {
      case e: Exception => Left(e)
    }
  }
}
