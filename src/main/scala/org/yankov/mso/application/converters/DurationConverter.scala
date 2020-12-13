package org.yankov.mso.application.converters

import java.time.Duration

object DurationConverter {
  private val separator = ":"
  private val formatTemplate = "%02d" + separator + "%02d" + separator + "%02d"
  private val hoursInDay = 24
  private val minutesInHour = 60
  private val secondsInMinute = 60
  private val secondsInHour = 60 * secondsInMinute
  private val millisInSecond = 1000

  def toString(duration: Duration): String =
    formatTemplate.format(toHoursPart(duration), toMinutesPart(duration), toSecondsPart(duration))

  def fromString(s: String): Duration = {
    if (s.isEmpty) Duration.ZERO
    else {
      val elements = s.split(separator)

      val hoursPart = Integer.parseInt(elements(0))
      val minutesPart = Integer.parseInt(elements(1))
      val secondsPart = Integer.parseInt(elements(2))

      val seconds = hoursPart * secondsInHour + minutesPart * secondsInMinute + secondsPart

      Duration.ofSeconds(seconds)
    }
  }

  def toHoursPart(duration: Duration): Long = duration.toHours % hoursInDay

  def toMinutesPart(duration: Duration): Long = duration.toMinutes % minutesInHour

  def toSecondsPart(duration: Duration): Long = (duration.toMillis / millisInSecond) % secondsInMinute
}
