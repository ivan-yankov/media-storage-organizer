package org.yankov.mso.application;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private static final String MESSAGE_PATTERN_ARGUMENTS_COUNT = "Invalid number of arguments for command {0}. Expected {1}, actual {2}.";
    private static final String MESSAGE_PATTERN_ARGUMENT_TYPES = "Invalid argument type(s) for command {0}. Expected {1}, actual {2}";

    public abstract void execute(Object... commandArgs);

    private String name;
    private List<String> argumentTypeNames;
    private String errorMessage;

    public Command(String name, List<String> argumentTypeNames) {
        this.name = name;
        this.argumentTypeNames = argumentTypeNames;
    }

    public String getName() {
        return name;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public final boolean checkArguments(Object... commandArgs) {
        int expectedNumberOfArguments = commandArgs != null ? commandArgs.length : 0;
        int actualNumberOfArguments = argumentTypeNames != null ? argumentTypeNames.size() : 0;
        if (expectedNumberOfArguments != actualNumberOfArguments) {
            errorMessage = MessageFormat
                    .format(MESSAGE_PATTERN_ARGUMENTS_COUNT, name, Integer.toString(expectedNumberOfArguments),
                            Integer.toString(actualNumberOfArguments));
            return false;
        }

        for (int i = 0; i < actualNumberOfArguments; i++) {
            if (!argumentTypeNames.get(i).equals(commandArgs[i].getClass().getName())) {
                String expectedArguments = argumentTypeNames.stream()
                                                            .reduce("", (type1, type2) -> type1 + type2 + ", ");
                String actualArguments = Arrays.asList(commandArgs).stream().map(arg -> arg.getClass().getName())
                                               .reduce("", (type1, type2) -> type1 + type2 + ", ");
                errorMessage = MessageFormat.format(MESSAGE_PATTERN_ARGUMENT_TYPES, name,
                                                    expectedArguments.substring(0, expectedArguments.length() - 2),
                                                    actualArguments.substring(0, actualArguments.length() - 2));
                return false;
            }
        }

        return true;
    }

}
