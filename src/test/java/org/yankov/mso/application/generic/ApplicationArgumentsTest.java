package org.yankov.mso.application.generic;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationArgumentsTest {

    private static final String DEFAULT = "default";

    @Test
    public void testGetArgument() {
        ApplicationArguments arguments = new ApplicationArguments(null);
        String actual = arguments.getArgument("-k", DEFAULT);
        Assert.assertEquals(DEFAULT, actual);

        arguments = new ApplicationArguments(new String[] {"-k1=value1", "-k2=value2"});
        actual = arguments.getArgument("-k3", DEFAULT);
        Assert.assertEquals(DEFAULT, actual);

        arguments = new ApplicationArguments(new String[] {"-k1=value1", "-k2=value2"});
        actual = arguments.getArgument("-k2", DEFAULT);
        Assert.assertEquals("value2", actual);
    }

}
