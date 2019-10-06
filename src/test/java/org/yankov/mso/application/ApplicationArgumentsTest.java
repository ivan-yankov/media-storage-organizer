package org.yankov.mso.application;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationArgumentsTest {

    @Test
    public void testParse_NoRequiredArgumentsProvided_ShouldFail() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] {});
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("There is no provided value for required argument"));
    }

    @Test
    public void testParse_InvalidKey_ShouldFail() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] { "--invalid-name", "value" });
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("Argument name"));
    }

    @Test
    public void testParse_UnsupportedArgumentValue_ShouldFail() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] { "--lang", "invalid-lang" });
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("Argument value"));
    }

    @Test
    public void testParse_FlagArgument_ShouldPass() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] { "--test-mode", "--db-url", "url" });
        Assert.assertFalse(errors.isPresent());
        Assert.assertEquals("true", arguments.getArgument("test-mode"));
    }

    @Test
    public void testParse_ShouldPass() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] { "--db-url", "url" });
        Assert.assertFalse(errors.isPresent());
        Assert.assertNotNull(arguments.getArgument("driver-name"));
        Assert.assertNotNull(arguments.getArgument("db-url"));
        Assert.assertNotNull(arguments.getArgument("lang"));
        Assert.assertNotNull(arguments.getArgument("test-mode"));
    }

    @Test
    public void testGetArgument() {
        ApplicationArguments arguments = new ApplicationArguments();
        Optional<String> errors = arguments.parse(new String[] { "--db-url", "url" });
        Assert.assertFalse(errors.isPresent());
        Assert.assertEquals("url", arguments.getArgument(ApplicationArguments.DB_URL_NAME));
    }

}
