package org.yankov.mso.application.ui.edit;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.generic.Artist;

public class ArtistStringConverter extends StringConverter<Artist> {

    @Override
    public String toString(Artist object) {
        return object != null ? object.getName() : "";
    }

    @Override
    public Artist fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getArtist(string).orElse(null);
    }

}
