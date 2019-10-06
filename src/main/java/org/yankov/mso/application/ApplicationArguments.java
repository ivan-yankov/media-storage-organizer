package org.yankov.mso.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationArguments {

    public static final String TEST_MODE_NAME = "test-mode";
    public static final String LANGUAGE_NAME = "lang";
    public static final String DB_URL_NAME = "db-url";
    public static final String DB_DRIVER_NAME = "db-driver";

    private static final String SHORT_NAME_PREFIX = "-";
    private static final String LONG_NAME_PREFIX = "--";

    private List<ApplicationArgument> arguments;

    public ApplicationArguments() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new ApplicationArgument(TEST_MODE_NAME, null, "false", false, true, "true", "false"));
        this.arguments.add(new ApplicationArgument(LANGUAGE_NAME, null, "bg", false, false, "bg"));
        this.arguments.add(new ApplicationArgument(DB_URL_NAME, null, null, true, false));
        this.arguments
            .add(new ApplicationArgument(DB_DRIVER_NAME, null, "embedded", false, false, "embedded", "client"));
    }

    public String getArgument(String name) {
        Optional<ApplicationArgument> arg = findArgument(name);
        return arg.isPresent() ? arg.get().getValue() : "";
    }

    public Optional<String> parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String name;
            if (args[i].startsWith(LONG_NAME_PREFIX)) {
                name = args[i].substring(LONG_NAME_PREFIX.length());
            } else if (args[i].startsWith(SHORT_NAME_PREFIX)) {
                name = args[i].substring(SHORT_NAME_PREFIX.length());
            } else {
                continue;
            }

            Optional<ApplicationArgument> argument = findArgument(name);
            if (argument.isPresent()) {
                String value = argument.get().isFlag() ? "true" : getArgumentValueAt(args, i + 1);
                if (!argument.get().setValue(value)) {
                    return Optional.of("Argument value '" + value + "' for name '" + name + "' is not supported");
                }
            } else {
                return Optional.of("Argument name '" + name + "' is not valid");
            }
        }

        for (ApplicationArgument argument : arguments) {
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

    private Optional<ApplicationArgument> findArgument(String name) {
        if (name.length() == 1) {
            return arguments.stream().filter(a -> a.getShortName().equals(name.charAt(0))).findFirst();
        } else {
            return arguments.stream().filter(a -> a.getLongName().equals(name)).findFirst();
        }
    }

}
