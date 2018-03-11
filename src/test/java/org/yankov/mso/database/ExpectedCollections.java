package org.yankov.mso.database;

import org.yankov.mso.datamodel.*;

import java.beans.PropertyChangeSupport;
import java.util.*;

public class ExpectedCollections {

    private final Set<SourceType> sourceTypes;
    private final Set<Source> sources;
    private final Set<Instrument> instruments;
    private final Set<Artist> artists;
    private final Set<Album> albums;
    private final List<FolklorePiece> pieces;
    private final Set<EthnographicRegion> ethnographicRegions;

    public ExpectedCollections() {
        this.sourceTypes = new HashSet<>();
        this.sources = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
        this.albums = new HashSet<>();
        this.pieces = new ArrayList<>();
        this.ethnographicRegions = new HashSet<>();
    }

    public Set<SourceType> getSourceTypes() {
        return sourceTypes;
    }

    public Set<Source> getSources() {
        return sources;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public List<FolklorePiece> getPieces() {
        return pieces;
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return ethnographicRegions;
    }

    public Optional<Artist> findArtist(String name) {
        return artists.stream().filter(artist -> artist.getName().equals(name)).findFirst();
    }

    public void createExpectedCollections() {
        sourceTypes.addAll(FolkloreEntityCollections.getInstance().getSourceTypes());
        sources.addAll(FolkloreEntityCollections.getInstance().getSources());
        instruments.addAll(FolkloreEntityCollections.getInstance().getInstruments());
        artists.addAll(FolkloreEntityCollections.getInstance().getArtists());
        albums.addAll(FolkloreEntityCollections.getInstance().getAlbums());
        pieces.addAll(FolkloreEntityCollections.getInstance().getPieces());
        ethnographicRegions.addAll(FolkloreEntityCollections.getInstance().getEthnographicRegions());
    }

}
