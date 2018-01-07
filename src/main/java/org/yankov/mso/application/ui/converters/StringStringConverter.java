package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;

public class StringStringConverter extends StringConverter<String> {

    @Override
    public String toString(String object) {
        return object;
    }

    @Override
    public String fromString(String string) {
        return string;
    }

}
