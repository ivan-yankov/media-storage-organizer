package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.generic.Album;

public class AlbumStringConverter extends StringConverter<Album> {

    @Override
    public String toString(Album object) {
        return object != null ? object.getCollectionSignature() : "";
    }

    @Override
    public Album fromString(String string) {
        return ApplicationContext.getInstance().getFolkloreEntityCollections().getAlbum(string).orElse(null);
    }

}
