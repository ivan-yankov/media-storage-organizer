package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.generic.Instrument;

public class InstrumentStringConverter extends StringConverter<Instrument> {

    @Override
    public String toString(Instrument object) {
        return object != null ? object.getName() : "";
    }

    @Override
    public Instrument fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getInstrument(string).orElse(null);
    }

}
