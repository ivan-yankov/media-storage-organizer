package org.yankov.mso.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApplicationArgument {

    private final String key;
    private final String defaultValue;
    private final boolean required;
    private final List<String> supportedValues;
    private String value;

    public ApplicationArgument(String key, String defaultValue, boolean required, String... supportedValues) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.required = required;
        this.supportedValues = Arrays.asList(supportedValues);
    }

    public String getKey() {
        return key;
    }

    public boolean isRequired() {
        return required;
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
