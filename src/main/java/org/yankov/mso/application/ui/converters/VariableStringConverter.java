package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.datamodel.Variable;

public class VariableStringConverter extends StringConverter<Variable> {

    @Override
    public String toString(Variable object) {
        return object!= null ? object.getLabel() : "";
    }

    @Override
    public Variable fromString(String string) {
        return null;
    }

}
