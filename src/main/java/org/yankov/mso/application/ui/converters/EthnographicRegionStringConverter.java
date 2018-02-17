package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.EthnographicRegion;

public class EthnographicRegionStringConverter extends StringConverter<EthnographicRegion> {

    @Override
    public String toString(EthnographicRegion object) {
        return object != null ? object.getName() : "";
    }

    @Override
    public EthnographicRegion fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getEthnographicRegion(string)
                                 .orElse(null);
    }

}
