package org.yankov.mso.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationArguments {

    public static final String TEST_MODE_KEY = "test-mode";
    public static final String LANGUAGE_KEY = "lang";
    public static final String DB_URL_KEY = "db-url";
    public static final String DB_DRIVER_KEY = "db-driver";

    private List<ApplicationArgument> arguments;

    public ApplicationArguments() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new ApplicationArgument(TEST_MODE_KEY, "false", false, "true", "false"));
        this.arguments.add(new ApplicationArgument(LANGUAGE_KEY, "bg", false, "bg"));
        this.arguments.add(new ApplicationArgument(DB_URL_KEY, null, true));
        this.arguments.add(new ApplicationArgument(DB_DRIVER_KEY, "embedded", false, "embedded", "client"));
    }

    public String getArgument(String key) {
        Optional<ApplicationArgument> arg = findArgument(key);
        return arg.isPresent() ? arg.get().getValue() : "";
    }

    public Optional<String> parse(String[] args) {
        List<String> validKeys = arguments.stream().map(a -> a.getKey()).collect(Collectors.toList());
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                if (!validKeys.contains(key)) {
                    return Optional.of("Argument key '" + key + "' is not valid");
                }
            } else {
                if (i == 0) {
                    return Optional.of("There is no key specified for argument value '" + args[i] + "'");
                }
                String key = args[i - 1].substring(2);
                ApplicationArgument appArg = findArgument(key).get();
                if (!appArg.setValue(args[i])) {
                    return Optional.of("Argument value '" + args[i] + "' for key '" + key + "' is not supported");
                }
            }
        }

        for (ApplicationArgument argument : arguments) {
            if (argument.getValue() == null) {
                if (argument.isRequired()) {
                    return Optional.of("There is no provided value for required argument '" + argument.getKey() + "'");
                } else {
                    argument.setDefaultValue();
                }
            }
        }

        return Optional.empty();
    }

    private Optional<ApplicationArgument> findArgument(String key) {
        return arguments.stream().filter(a -> a.getKey().equalsIgnoreCase(key)).findFirst();
    }

}
