package org.yankov.mso.application.search

import scala.annotation.tailrec

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

  def analyze(string: String): String = {
    string
      .normalize()
      .removePunctuation()
      .trim
  }
}
