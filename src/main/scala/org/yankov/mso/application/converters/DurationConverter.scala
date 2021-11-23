package org.yankov.mso.application.converters

import java.time.Duration

object DurationConverter {
  private val separator = ':'
  private val hhMMss = "%02d" + separator + "%02d" + separator + "%02d"
  private val minutesInHour = 60
  private val secondsInMinute = 60
  private val secondsInHour = 60 * secondsInMinute
  private val millisInSecond = 1000

  def toHourMinSecString(duration: Duration, trimLeadingZeros: Boolean = false): String = {
    val str = hhMMss.format(duration.toHours, toMinutesPart(duration), toSecondsPart(duration))
    if (trimLeadingZeros) str.foldLeft("")((acc, c) => if (acc.isEmpty && (c == '0' || c == separator)) acc else acc + c.toString)
    else str
  }

  def fromString(s: String): Duration = {
    if (s.isEmpty) Duration.ZERO
    else {
      val elements = s.split(separator.toString)

      val hoursPart = Integer.parseInt(elements(0))
      val minutesPart = Integer.parseInt(elements(1))
      val secondsPart = Integer.parseInt(elements(2))

      val seconds = hoursPart * secondsInHour + minutesPart * secondsInMinute + secondsPart

      Duration.ofSeconds(seconds)
    }
  }

  private def toMinutesPart(duration: Duration): Long = duration.toMinutes % minutesInHour

  private def toSecondsPart(duration: Duration): Long = (duration.toMillis / millisInSecond) % secondsInMinute
}
