package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.datamodel.Variable;

public class VariableStringConverter<T> extends StringConverter<Variable<T>> {

    @Override
    public String toString(Variable<T> object) {
        return object!= null ? object.getLabel() : "";
    }

    @Override
    public Variable<T> fromString(String string) {
        return null;
    }

}
