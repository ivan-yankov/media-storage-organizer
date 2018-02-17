package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.datamodel.Operator;

public class OperatorStringConverter extends StringConverter<Operator> {

    @Override
    public String toString(Operator object) {
        return object != null ? object.getLabel() : "";
    }

    @Override
    public Operator fromString(String string) {
        return null;
    }

}
