package yankov.mso.application.converters

import org.scalatest.{FreeSpec, Matchers}

import java.time.Duration

class DurationConverterTest extends FreeSpec with Matchers {
  "to hour min sec string" - {
    "with leading zeros" in {
      DurationConverter.toHourMinSecString(Duration.ofSeconds(3850)) shouldBe "01:04:10"
      DurationConverter.toHourMinSecString(Duration.ofSeconds(300)) shouldBe "00:05:00"
    }

    "without leading zeros" in {
      DurationConverter.toHourMinSecString(Duration.ofSeconds(3850), trimLeadingZeros = true) shouldBe "1:04:10"
      DurationConverter.toHourMinSecString(Duration.ofSeconds(300), trimLeadingZeros = true) shouldBe "05:00"
    }
  }

  "from string should succeed" in {
    DurationConverter.fromString("01:04:10") shouldBe Duration.ofSeconds(3850)
  }
}
