package org.yankov.mso.application.search

import org.scalatest.{FreeSpec, Matchers}

class TextAnalyzerTest extends FreeSpec with Matchers {
  "analyze text should succeed" in {
    val text = """ Момне ле"девойко", от хубава, по-хубава/най-хубава; """
    TextAnalyzer.analyze(text) shouldBe "момне ле девойко от хубава по хубава най хубава"
  }
}
