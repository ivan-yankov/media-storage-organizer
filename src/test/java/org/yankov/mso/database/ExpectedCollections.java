package org.yankov.mso.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.yankov.mso.datamodel.Album;
import org.yankov.mso.datamodel.Artist;
import org.yankov.mso.datamodel.EthnographicRegion;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.Instrument;
import org.yankov.mso.datamodel.Source;
import org.yankov.mso.datamodel.SourceType;

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

    public void createExpectedCollections(FolkloreEntityCollections collections) {
        sourceTypes.addAll(collections.getSourceTypes());
        sources.addAll(collections.getSources());
        instruments.addAll(collections.getInstruments());
        artists.addAll(collections.getArtists());
        albums.addAll(collections.getAlbums());
        pieces.addAll(collections.getPieces());
        ethnographicRegions.addAll(collections.getEthnographicRegions());
    }

}
