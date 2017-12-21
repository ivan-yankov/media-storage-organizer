package org.yankov.mso.application.generic;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.application.ApplicationArguments;

import java.util.Optional;

public class ApplicationArgumentsTest {

    @Test
    public void testGetArgument() {
        ApplicationArguments arguments = new ApplicationArguments(null);
        Optional<String> actual = arguments.getArgument("-k");
        Assert.assertFalse(actual.isPresent());

        arguments = new ApplicationArguments(new String[] {"-k1=value1", "-k2=value2"});
        actual = arguments.getArgument("-k3");
        Assert.assertFalse(actual.isPresent());

        arguments = new ApplicationArguments(new String[] {"-k1=value1", "-k2=value2"});
        actual = arguments.getArgument("-k2");
        Assert.assertTrue(actual.isPresent());
        Assert.assertEquals("value2", actual.get());
    }

}
