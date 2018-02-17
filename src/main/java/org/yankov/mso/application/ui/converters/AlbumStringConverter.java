package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.yankov.mso.datamodel.Album;

import java.util.Collection;

public class AlbumStringConverter extends StringConverter<Album> {

    private static final String SEPARATOR = "/";

    private final boolean showTitle;
    private final Collection<Album> albums;

    public AlbumStringConverter(Collection<Album> albums, boolean showTitle) {
        this.showTitle = showTitle;
        this.albums = albums;
    }

    @Override
    public String toString(Album object) {
        return object != null ? composeLabel(object) : "";
    }

    @Override
    public Album fromString(String string) {
        String signature = getCollectionSignature(string);
        return albums.stream().filter(album -> album.getCollectionSignature().equalsIgnoreCase(signature)).findFirst()
                     .orElse(null);
    }

    private String composeLabel(Album object) {
        StringBuilder label = new StringBuilder();
        label.append(object.getCollectionSignature());
        if (showTitle) {
            label.append(SEPARATOR);
            label.append(object.getTitle());
        }
        return label.toString();
    }

    private String getCollectionSignature(String label) {
        String[] fragments = label.split(SEPARATOR);
        return fragments[0];
    }

}
