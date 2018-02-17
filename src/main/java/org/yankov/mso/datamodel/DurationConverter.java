package org.yankov.mso.datamodel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {

    private static final String SEPARATOR = ":";
    private static final String FORMAT_TEMPLATE = "%02d" + SEPARATOR + "%02d" + SEPARATOR + "%02d";

    private static final int SECONDS_IN_MINUTE = 60;
    private static int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        return String
                .format(FORMAT_TEMPLATE, duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    @Override
    public Duration convertToEntityAttribute(String s) {
        String[] elements = s.split(SEPARATOR);

        int hoursPart = Integer.parseInt(elements[0]);
        int minutesPart = Integer.parseInt(elements[1]);
        int secondsPart = Integer.parseInt(elements[2]);

        long seconds = hoursPart * SECONDS_IN_HOUR + minutesPart * SECONDS_IN_MINUTE + secondsPart;

        return Duration.ofSeconds(seconds);
    }
}
