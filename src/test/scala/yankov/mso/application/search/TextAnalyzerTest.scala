package yankov.mso.application.search

import org.scalatest.{FreeSpec, Matchers}
import TextAnalyzer._

class TextAnalyzerTest extends FreeSpec with Matchers {
  "analyze text should succeed" in {
    val text = """ Момне ле"девойко", от хубава, по-хубава/най-хубава; """
    analyze(text) shouldBe "момне ле девойко от хубава по хубава най хубава"
  }

  "levenshtein distance is correct" in {
    levenshteinDistance("", "") shouldBe 0
    levenshteinDistance("abc", "abc") shouldBe 0
    levenshteinDistance("", "abc") shouldBe 3
    levenshteinDistance("abc", "") shouldBe 3
    levenshteinDistance("abc", "ade") shouldBe 2
    levenshteinDistance("abc", "def") shouldBe 3
    levenshteinDistance("abc", "avc") shouldBe 1
    levenshteinDistance("abc", "cba") shouldBe 2
    levenshteinDistance("abc", "acb") shouldBe 2
    levenshteinDistance("abcdef", "abcd") shouldBe 2
    levenshteinDistance("abc", "abcd") shouldBe 1
    levenshteinDistance("abd", "abcd") shouldBe 1
    levenshteinDistance("abcdef", "avcd") shouldBe 3
    levenshteinDistance("чичковите червенотиквеничковчета", "чичковите червенотиквеничковчита") shouldBe 1
  }
}
