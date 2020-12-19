package org.yankov.mso.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class CommandLineArgumentsTest {

    private CommandLineArguments arguments;

    @Before
    public void beforeTest() {
        arguments = new CommandLineArguments();
        arguments.add(new CommandLineArgument("flag", null, "false", false, true, "true", "false"));
        arguments.add(new CommandLineArgument("required", null, null, true, false));
        arguments.add(new CommandLineArgument("not-required", null, "value1", false, false, "value1", "value2"));
        arguments.add(new CommandLineArgument("short", 's', null, false, false));
    }

    @Test
    public void testParse_NoRequiredArgumentsProvided_ShouldFail() {
        Optional<String> errors = arguments.parseValues(new String[]{});
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("There is no provided value for required argument"));
    }

    @Test
    public void testParse_InvalidKey_ShouldFail() {
        Optional<String> errors = arguments.parseValues(new String[]{"--invalid-name", "value"});
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("Argument name"));
    }

    @Test
    public void testParse_UnsupportedArgumentValue_ShouldFail() {
        Optional<String> errors = arguments.parseValues(new String[]{"--not-required", "invalid-value"});
        Assert.assertTrue(errors.isPresent());
        Assert.assertTrue(errors.get().startsWith("Argument value"));
    }

    @Test
    public void testParse_FlagArgument_ShouldPass() {
        Optional<String> errors = arguments.parseValues(new String[]{"--flag", "--required", "required-value"});
        Assert.assertFalse(errors.isPresent());
        Assert.assertEquals("true", arguments.getValue("flag"));
    }

    @Test
    public void testParse_ShouldPass() {
        Optional<String> errors = arguments.parseValues(new String[]{"--required", "required-value", "-s", "short-value"});
        Assert.assertFalse(errors.isPresent());
        Assert.assertNotNull(arguments.getValue("flag"));
        Assert.assertNotNull(arguments.getValue("required"));
        Assert.assertNotNull(arguments.getValue("not-required"));
        Assert.assertNotNull(arguments.getValue("s"));
    }

    @Test
    public void testGetArgument() {
        Optional<String> errors = arguments.parseValues(new String[]{"--required", "required-value", "-s", "short-value"});
        Assert.assertFalse(errors.isPresent());
        Assert.assertEquals("required-value", arguments.getValue("required"));
        Assert.assertEquals("short-value", arguments.getValue("s"));
    }

}
