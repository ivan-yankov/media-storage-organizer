package org.yankov.mso.application

import org.slf4j.LoggerFactory

import java.io.{FileOutputStream, PrintWriter}
import java.util.Scanner
import scala.annotation.tailrec

object FileUtils {
  private val log = LoggerFactory.getLogger(getClass)

  def readTextFile(path: String, acceptLine: String => Boolean = _ => true): Either[String, List[String]] = {
    tryOrException(() => new Scanner(getClass.getResourceAsStream(path))) match {
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

  def writeTextFile(lines: List[String], path: String, append: Boolean = false): Either[String, Unit] = {
    tryOrException(() => new PrintWriter(new FileOutputStream(path, append))) match {
      case Left(e) =>
        log.error("Fail to initialize text file for writing", e)
        Left(e.getMessage)
      case Right(writer) =>
        try {
          lines.foreach(x => writer.println(x))
          Right(())
        }
        catch {
          case e: Exception =>
            log.error("Fail to write text file", e)
            Left(e.getMessage)
        }
      finally {
        writer.close()
      }
    }
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
