package org.yankov.mso.datamodel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {

    private static final String SEPARATOR = ":";
    private static final String FORMAT_TEMPLATE = "%02d" + SEPARATOR + "%02d" + SEPARATOR + "%02d";

    private static final long HOURS_IN_DAY = 24;
    private static final long MINUTES_IN_HOUR = 60;
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    private static final long MILLIS_IN_SECOND = 1000;

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        return duration != null ? String
            .format(FORMAT_TEMPLATE, toHoursPart(duration), toMinutesPart(duration), toSecondsPart(duration)) : "";
    }

    @Override
    public Duration convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        String[] elements = s.split(SEPARATOR);

        int hoursPart = Integer.parseInt(elements[0]);
        int minutesPart = Integer.parseInt(elements[1]);
        int secondsPart = Integer.parseInt(elements[2]);

        long seconds = hoursPart * SECONDS_IN_HOUR + minutesPart * SECONDS_IN_MINUTE + secondsPart;

        return Duration.ofSeconds(seconds);
    }

    public static long toHoursPart(Duration duration) {
        return duration.toHours() % HOURS_IN_DAY;
    }

    public static long toMinutesPart(Duration duration) {
        return duration.toMinutes() % MINUTES_IN_HOUR;
    }

    public static long toSecondsPart(Duration duration) {
        return (duration.toMillis() / MILLIS_IN_SECOND) % SECONDS_IN_MINUTE;
    }

}
