package org.yankov.mso.application.command;

import java.text.MessageFormat;

public class InvalidCommandArgumentsException extends Exception {

    private static final String MESSAGE_PATTERN = "Invalid arguments for command. Expected number of arguments: {0}. Expected types of arguments: {1}";

    public InvalidCommandArgumentsException(int expectedNumberOfArguments, String expectedTypesOfArguments) {
        super(MessageFormat.format(MESSAGE_PATTERN, expectedNumberOfArguments, expectedTypesOfArguments));
    }

}
