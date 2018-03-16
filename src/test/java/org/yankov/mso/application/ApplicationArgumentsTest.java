package org.yankov.mso.application;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationArgumentsTest {

    @Test
    public void testGetArgument() {
        ApplicationArguments arguments = new ApplicationArguments(null);
        Assert.assertTrue(arguments.getArgument(ApplicationArguments.Argument.LANGUAGE).isEmpty());

        arguments = new ApplicationArguments(new String[] {"", "bg"});
        // number of arguments is invalid
        Assert.assertTrue(arguments.getArgument(ApplicationArguments.Argument.LANGUAGE).isEmpty());

        String[] args = new String[] {
                "mode", "lang", "settings-type", "db-embedded-mode", "db-name", "db-host", "db-port"
        };
        arguments = new ApplicationArguments(args);
        Assert.assertEquals(args[0], arguments.getArgument(ApplicationArguments.Argument.APPLICATION_MODE));
        Assert.assertEquals(args[1], arguments.getArgument(ApplicationArguments.Argument.LANGUAGE));
        Assert.assertEquals(args[2], arguments.getArgument(ApplicationArguments.Argument.SETTINGS));
        Assert.assertEquals(args[3], arguments.getArgument(ApplicationArguments.Argument.DB_EMBEDDED_MODE));
        Assert.assertEquals(args[4], arguments.getArgument(ApplicationArguments.Argument.DB_NAME));
        Assert.assertEquals(args[5], arguments.getArgument(ApplicationArguments.Argument.DB_HOST));
        Assert.assertEquals(args[6], arguments.getArgument(ApplicationArguments.Argument.DB_PORT));
    }

}
