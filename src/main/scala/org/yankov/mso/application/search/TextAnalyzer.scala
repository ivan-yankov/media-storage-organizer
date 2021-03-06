package org.yankov.mso.application.search

import org.yankov.mso.application.IOUtils

import java.io.{BufferedReader, FileInputStream, InputStreamReader}
import java.nio.file.{Files, Paths}
import scala.annotation.tailrec
import scala.collection.JavaConverters._

object TextAnalyzer {

  implicit class Analyzer(string: String) {
    def normalize(): String = string.toLowerCase

    def removePunctuation(): String = {
      val symbols = List(",", ";", "-", "/", "\"")

      @tailrec
      def replace(input: String, replaceWhat: List[String], replaceWith: String): String = {
        if (replaceWhat.isEmpty) input
        else replace(input.replace(replaceWhat.head, replaceWith), replaceWhat.tail, replaceWith)
      }

      replace(string, symbols, " ")
        .trim
        // replace multiple spaces with a single space
        .replaceAll(" +", " ")
    }
  }

  private val stopWordsBg = IOUtils.readTextFile("/stop-words-bg.txt")

  def analyze(string: String): String = {
    string
      .normalize()
      .removePunctuation()
      .trim
  }

  def indexAnalyze(string: String): List[String] = {
    analyze(string)
      .split(" ")
      .toList
      .filterNot(x => stopWordsBg.contains(x))
  }

  def levenshteinDistance(s1: String, s2: String): Int = {
    val dist = Array.tabulate(s2.length + 1, s1.length + 1) { (j, i) => if (j == 0) i else if (i == 0) j else 0 }

    def minimum(i: Int*): Int = i.min

    for {j <- dist.indices.tail
         i <- dist(0).indices.tail} dist(j)(i) =
      if (s2(j - 1) == s1(i - 1)) dist(j - 1)(i - 1)
      else minimum(dist(j - 1)(i) + 1, dist(j)(i - 1) + 1, dist(j - 1)(i - 1) + 1)

    dist(s2.length)(s1.length)
  }
}
