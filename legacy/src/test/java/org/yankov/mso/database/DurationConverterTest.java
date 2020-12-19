package org.yankov.mso.database;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.datamodel.DurationConverter;

import java.time.Duration;

public class DurationConverterTest {

    private Duration duration = Duration.ofSeconds(3850);
    private String stringRepresentation = "01:04:10";

    @Test
    public void testConvertToDatabaseColumn() {
        DurationConverter converter = new DurationConverter();
        String actual = converter.convertToDatabaseColumn(duration);
        Assert.assertEquals(stringRepresentation, actual);
    }

    @Test
    public void testConvertToEntityAttribute() {
        DurationConverter converter = new DurationConverter();
        Duration actual = converter.convertToEntityAttribute(stringRepresentation);
        Assert.assertEquals(duration, actual);
    }

}
