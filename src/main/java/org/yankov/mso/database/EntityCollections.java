package org.yankov.mso.database;

import org.yankov.mso.datamodel.*;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EntityCollections<T extends Piece> {

    void clear();

    void initialize();

    void loadFromDatabase();

    void saveToDatabase();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    Set<SourceType> getSourceTypes();

    Set<Source> getSources();

    Set<Instrument> getInstruments();

    Set<Artist> getArtists();

    Set<Album> getAlbums();

    List<T> getPieces();

    Optional<SourceType> getSourceType(String name);

    boolean addSourceType(SourceType sourceType);

    void addSourceTypes(Set<SourceType> sourceTypes);

    Optional<Source> getSource(String representation);

    boolean addSource(Source source);

    void addSources(Set<Source> sources);

    Optional<Instrument> getInstrument(String name);

    boolean addInstrument(Instrument instrument);

    void addInstruments(Set<Instrument> instruments);

    Optional<Artist> getArtist(String name);

    boolean addArtist(Artist artist);

    Optional<Album> getAlbum(String collectionSignature);

    boolean addAlbum(Album album);

    Optional<T> getPiece(int index);

    boolean addPiece(T piece);

}
