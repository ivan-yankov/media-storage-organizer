package org.yankov.mso.database;

import org.yankov.mso.datamodel.*;

import java.util.*;

public class ExpectedCollections {

    private final Set<SourceType> sourceTypes;
    private final Set<Source> sources;
    private final Set<Instrument> instruments;
    private final Set<Artist> artists;
    private final List<FolklorePiece> pieces;
    private final Set<EthnographicRegion> ethnographicRegions;

    public ExpectedCollections() {
        this.sourceTypes = new HashSet<>();
        this.sources = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
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

    public List<FolklorePiece> getPieces() {
        return pieces;
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return ethnographicRegions;
    }

    public Optional<Artist> findArtist(String name) {
        return artists.stream().filter(artist -> artist.getName().equals(name)).findFirst();
    }

    public void createExpectedCollections(FolkloreEntityCollections collections) {
        sourceTypes.addAll(collections.getSourceTypes());
        sources.addAll(collections.getSources());
        instruments.addAll(collections.getInstruments());
        artists.addAll(collections.getArtists());
        pieces.addAll(collections.getPieces());
        ethnographicRegions.addAll(collections.getEthnographicRegions());
    }

}
