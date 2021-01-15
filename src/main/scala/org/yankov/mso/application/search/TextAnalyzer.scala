package org.yankov.mso.application.search

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

  private val stopWordsBgPath = Paths.get(getClass.getResource("/stop-words-bg.txt").toURI)
  private val stopWordsBg = Files.readAllLines(stopWordsBgPath).asScala.toList.filter(x => x.nonEmpty)

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

  def levenshteinDistance(a: String, b: String): Int = {
    def lev(x: String, y: String): Int = {
      if (y.isEmpty) x.length
      else if (x.isEmpty) y.length
      else if (x.head.equals(y.head)) lev(x.tail, y.tail)
      else List(lev(x.tail, y), lev(x, y.tail), lev(x.tail, y.tail)).min + 1
    }
    if (a.equals(b)) 0
    else lev(a, b)
  }
}
