package org.yankov.mso.application.converters

import java.time.Duration

import org.scalatest.{FreeSpec, Matchers}

class DurationConverterTest extends FreeSpec with Matchers {
  private val duration = Duration.ofSeconds(3850)

  "to hour min sec string" - {
    "with leading zero should succeed" in {
      DurationConverter.toHourMinSecString(duration, withLeadingZero = true) shouldBe "01:04:10"
    }

    "without leading zero should succeed" in {
      DurationConverter.toHourMinSecString(duration, withLeadingZero = false) shouldBe "1:04:10"
    }
  }

  "from string should succeed" in {
    DurationConverter.fromString("01:04:10") shouldBe duration
  }
}
