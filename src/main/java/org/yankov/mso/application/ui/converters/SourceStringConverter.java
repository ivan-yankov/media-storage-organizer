package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.generic.Source;

public class SourceStringConverter extends StringConverter<Source> {

    @Override
    public String toString(Source object) {
        return object != null ? object.toString() : "";
    }

    @Override
    public Source fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getSource(string).orElse(null);
    }

}
