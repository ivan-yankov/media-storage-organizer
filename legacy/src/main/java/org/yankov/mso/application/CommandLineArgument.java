package org.yankov.mso.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLineArgument {

    private final String longName;
    private final Character shortName;
    private final String defaultValue;
    private final boolean required;
    private final boolean flag;
    private final List<String> supportedValues;
    private String value;

    @SafeVarargs
    public CommandLineArgument(String longName, Character shortName, String defaultValue, boolean required,
                               boolean flag, String... supportedValues) {
        this.longName = longName;
        this.shortName = shortName;
        this.defaultValue = defaultValue;
        this.required = required;
        this.flag = flag;
        this.supportedValues = Arrays.asList(supportedValues);
    }

    public String getLongName() {
        return longName;
    }

    public Character getShortName() {
        return shortName;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isFlag() {
        return flag;
    }

    public List<String> getSupportedValues() {
        return Collections.unmodifiableList(supportedValues);
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        if (supportedValues.isEmpty()) {
            this.value = value;
            return true;
        } else if (supportedValues.contains(value)) {
            this.value = value;
            return true;
        } else {
            return false;
        }
    }

    public void setDefaultValue() {
        value = defaultValue;
    }

}
