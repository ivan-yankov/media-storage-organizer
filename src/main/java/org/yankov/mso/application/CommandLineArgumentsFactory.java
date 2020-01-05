package org.yankov.mso.application;


public class CommandLineArgumentsFactory {

    public static final String TEST_MODE_NAME = "test-mode";
    public static final String LANGUAGE_NAME = "lang";
    public static final String DB_URL_NAME = "db-url";
    public static final String DB_DRIVER_NAME = "db-driver";

    public static final CommandLineArguments createCommandLineArguments() {
        CommandLineArguments arguments = new CommandLineArguments();

        arguments.add(new CommandLineArgument(TEST_MODE_NAME, null, "false", false, true, "true", "false"));
        arguments.add(new CommandLineArgument(LANGUAGE_NAME, null, "bg", false, false, "bg"));
        arguments.add(new CommandLineArgument(DB_URL_NAME, null, null, true, false));
        arguments.add(new CommandLineArgument(DB_DRIVER_NAME, null, "embedded", false, false, "embedded", "client"));

        return arguments;
    }

}
