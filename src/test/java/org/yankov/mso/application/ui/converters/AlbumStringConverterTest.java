package org.yankov.mso.application.ui.converters;

import javafx.util.StringConverter;
import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.datamodel.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumStringConverterTest {

    private static final String TITLE = "Album Title";
    private static final String COLLECTION_SIGNATURE = "Collection Signature";

    private Album album;
    private List<Album> albums;

    public AlbumStringConverterTest() {
        this.album = new Album();
        this.album.setTitle(TITLE);
        this.album.setCollectionSignature(COLLECTION_SIGNATURE);
        this.albums = new ArrayList<>();
        this.albums.add(album);
        this.albums.add(new Album());
    }

    @Test
    public void testToString() {
        StringConverter<Album> converter = new AlbumStringConverter(albums, false);
        String result = converter.toString(album);
        Assert.assertEquals(COLLECTION_SIGNATURE, result);

        converter = new AlbumStringConverter(albums, true);
        result = converter.toString(album);
        Assert.assertEquals(COLLECTION_SIGNATURE + "/" + TITLE, result);
    }

    @Test
    public void testFromString() {
        StringConverter<Album> converter = new AlbumStringConverter(albums, false);
        Album result = converter.fromString(COLLECTION_SIGNATURE);
        Assert.assertEquals(album, result);

        converter = new AlbumStringConverter(albums, true);
        result = converter.fromString(COLLECTION_SIGNATURE + "/" + TITLE);
        Assert.assertEquals(album, result);
    }

}
