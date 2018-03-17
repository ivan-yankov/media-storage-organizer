package org.yankov.mso.database;

import org.yankov.mso.datamodel.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EntityCollections <T extends Piece> {

    void clear();

    void initialize();

    Set<SourceType> getSourceTypes();

    void addSourceTypes(Collection<SourceType> sourceTypes);

    Optional<SourceType> getSourceType(String name);

    boolean addSourceType(SourceType sourceType);

    Set<Source> getSources();

    void addSources(Collection<Source> sources);

    Optional<Source> getSource(String representation);

    boolean addSource(Source source);

    Set<Instrument> getInstruments();

    void addInstruments(Collection<Instrument> instruments);

    Optional<Instrument> getInstrument(String name);

    boolean addInstrument(Instrument instrument);

    Set<Artist> getArtists();

    void addArtists(Collection<Artist> artists);

    Optional<Artist> getArtist(String name);

    boolean addArtist(Artist artist);

    Set<Album> getAlbums();

    void addAlbums(Collection<Album> albums);

    Optional<Album> getAlbum(String collectionSignature);

    boolean addAlbum(Album album);

    List<T> getPieces();

    void addPieces(Collection<T> pieces);

    Optional<T> getPiece(int index);

    boolean addPiece(T piece);
}
