package org.yankov.mso.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApplicationArguments {

    private static final String ARGUMENT_VALUE_SEPARATOR = "=";

    private Map<String, String> arguments;

    public ApplicationArguments(String[] args) {
        this.arguments = initialize(args);
    }

    public Optional<String> getArgument(String key) {
        String value = arguments.get(key);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    private Map<String, String> initialize(String[] args) {
        if (!argsPresented(args)) {
            return new HashMap<>();
        } else {
            Map<String, String> map = new HashMap<>();
            for (String arg : args) {
                String[] argVal = arg.split(ARGUMENT_VALUE_SEPARATOR);
                map.put(argVal[0], argVal[1]);
            }
            return map;
        }
    }

    private boolean argsPresented(String[] args) {
        return args != null && args.length > 0;
    }

}
