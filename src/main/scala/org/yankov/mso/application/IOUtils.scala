package org.yankov.mso.application

import java.util.Scanner
import scala.annotation.tailrec

object IOUtils {
  def readTextFile(resourceFileName: String): List[String] = {
    val scanner = new Scanner(getClass.getResourceAsStream(resourceFileName))

    @tailrec
    def read(lines: List[String]): List[String] = {
      if (scanner.hasNext) read(scanner.nextLine() :: lines)
      else lines
    }

    val result = read(List()).filter(x => x.nonEmpty).reverse
    scanner.close()
    result
  }
}
