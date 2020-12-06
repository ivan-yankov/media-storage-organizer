package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.SourceType;

public class SourceTypeStringConverter extends StringConverter<SourceType> {

    @Override
    public String toString(SourceType object) {
        return object != null ? object.getName() : "";
    }

    @Override
    public SourceType fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getSourceType(string).orElse(null);
    }

}
