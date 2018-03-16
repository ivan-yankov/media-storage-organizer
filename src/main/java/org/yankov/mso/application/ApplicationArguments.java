package org.yankov.mso.application;

import java.util.HashMap;
import java.util.Map;

public class ApplicationArguments {

    public enum Argument {
        APPLICATION_MODE,
        LANGUAGE,
        SETTINGS,
        DB_EMBEDDED_MODE,
        DB_NAME,
        DB_HOST,
        DB_PORT
    }

    private Map<Argument, String> arguments;

    public ApplicationArguments(String[] args) {
        this.arguments = initialize(args);
    }

    public String getArgument(Argument key) {
        String value = arguments.get(key);
        return value != null ? value : "";
    }

    private Map<Argument, String> initialize(String[] args) {
        if (argsValid(args)) {
            Map<Argument, String> map = new HashMap<>();
            map.put(Argument.APPLICATION_MODE, args[0]);
            map.put(Argument.LANGUAGE, args[1]);
            map.put(Argument.SETTINGS, args[2]);
            map.put(Argument.DB_EMBEDDED_MODE, args[3]);
            map.put(Argument.DB_NAME, args[4]);
            map.put(Argument.DB_HOST, args[5]);
            map.put(Argument.DB_PORT, args[6]);
            return map;
        } else {
            return new HashMap<>();
        }
    }

    private boolean argsValid(String[] args) {
        return args != null && args.length == Argument.values().length;
    }

}
