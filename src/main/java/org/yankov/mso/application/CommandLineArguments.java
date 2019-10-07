package org.yankov.mso.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandLineArguments {

    private static final String SHORT_NAME_PREFIX = "-";
    private static final String LONG_NAME_PREFIX = "--";

    private final List<CommandLineArgument> arguments;

    public CommandLineArguments() {
        this.arguments = new ArrayList<>();
    }

    public void add(CommandLineArgument argument) {
        this.arguments.add(argument);
    }

    public String getValue(String name) {
        Optional<CommandLineArgument> arg = findArgument(name);
        return arg.isPresent() ? arg.get().getValue() : "";
    }

    public Optional<String> parseValues(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String name;
            if (args[i].startsWith(LONG_NAME_PREFIX)) {
                name = args[i].substring(LONG_NAME_PREFIX.length());
            } else if (args[i].startsWith(SHORT_NAME_PREFIX)) {
                name = args[i].substring(SHORT_NAME_PREFIX.length());
            } else {
                continue;
            }

            Optional<CommandLineArgument> argument = findArgument(name);
            if (argument.isPresent()) {
                String value = argument.get().isFlag() ? "true" : getArgumentValueAt(args, i + 1);
                if (!argument.get().setValue(value)) {
                    return Optional.of("Argument value '" + value + "' for name '" + name + "' is not supported");
                }
            } else {
                return Optional.of("Argument name '" + name + "' is not valid");
            }
        }

        for (CommandLineArgument argument : arguments) {
            if (argument.getValue() == null) {
                if (argument.isRequired()) {
                    return Optional
                        .of("There is no provided value for required argument '" + argument.getLongName() + "'");
                } else {
                    argument.setDefaultValue();
                }
            }
        }

        return Optional.empty();
    }

    private String getArgumentValueAt(String[] args, int i) {
        if (i < 0 || i >= args.length) {
            return null;
        }

        if (args[i].startsWith(SHORT_NAME_PREFIX) || args[i].startsWith(LONG_NAME_PREFIX)) {
            return null;
        }

        return args[i];
    }

    private Optional<CommandLineArgument> findArgument(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }
        if (name.length() == 1) {
            return arguments.stream().filter(a -> a.getShortName() != null && a.getShortName().equals(name.charAt(0))).findFirst();
        } else {
            return arguments.stream().filter(a -> name.equals(a.getLongName())).findFirst();
        }
    }

}
