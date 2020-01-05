package org.yankov.mso.database;

import org.yankov.mso.datamodel.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class FolkloreEntityCollections implements EntityCollections<FolklorePiece> {

    private static final String PROPERTY_ETHNOGRAPHIC_REGIONS = "ethnographicRegions";

    private static final String PROPERTY_SOURCE_TYPES = "sourceTypes";
    private static final String PROPERTY_SOURCES = "sources";
    private static final String PROPERTY_INSTRUMENTS = "instruments";
    private static final String PROPERTY_ARTISTS = "artists";
    private static final String PROPERTY_ALBUMS = "albums";
    private static final String PROPERTY_PIECES = "pieces";

    private final PropertyChangeSupport propertyChangeSupport;

    private final Set<SourceType> sourceTypes;
    private final Set<Source> sources;
    private final Set<Instrument> instruments;
    private final Set<Artist> artists;
    private final Set<Album> albums;
    private final Set<EthnographicRegion> ethnographicRegions;
    private final List<FolklorePiece> pieces;

    private Map<Class, Collection> collections = new HashMap<>();

    public FolkloreEntityCollections() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.sourceTypes = new HashSet<>();
        this.sources = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
        this.albums = new HashSet<>();
        this.ethnographicRegions = new HashSet<>();
        this.pieces = new ArrayList<>();

        this.collections = new HashMap<>();
        this.collections.put(SourceType.class, sourceTypes);
        this.collections.put(Source.class, sources);
        this.collections.put(Instrument.class, instruments);
        this.collections.put(Artist.class, artists);
        this.collections.put(Album.class, albums);
        this.collections.put(EthnographicRegion.class, ethnographicRegions);
        this.collections.put(FolklorePiece.class, pieces);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void clear() {
        sourceTypes.clear();
        sources.clear();
        instruments.clear();
        artists.clear();
        albums.clear();
        ethnographicRegions.clear();
        pieces.clear();
    }

    @Override
    public void initialize() {
        if (sourceTypes.isEmpty()) {
            sourceTypes.addAll(FolkloreEntityCollectionFactory.createSourceTypes());
        }
        if (sources.isEmpty()) {
            sources.addAll(FolkloreEntityCollectionFactory.createSources(sourceTypes));
        }
        if (instruments.isEmpty()) {
            instruments.addAll(FolkloreEntityCollectionFactory.createInstruments());
        }
        if (ethnographicRegions.isEmpty()) {
            ethnographicRegions.addAll(FolkloreEntityCollectionFactory.createEthnographicRegions());
        }
    }

    @Override
    public Map<Class, Collection> getCollections() {
        return collections;
    }

    @Override
    public void addEntities(Class c, Collection entities) {
        collections.get(c).addAll(entities);
    }

    public Set<SourceType> getSourceTypes() {
        return Collections.unmodifiableSet(sourceTypes);
    }

    public void addSourceTypes(Collection<SourceType> sourceTypes) {
        Set<SourceType> oldValue = new HashSet<>(this.sourceTypes);
        this.sourceTypes.addAll(sourceTypes);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, this.sourceTypes);
    }

    public Optional<SourceType> getSourceType(String name) {
        return sourceTypes.stream()
            .filter(entity -> entity.toString().toLowerCase().trim().equals(name.toLowerCase().trim()))
            .findFirst();
    }

    public boolean addSourceType(SourceType sourceType) {
        Set<SourceType> oldValue = new HashSet<>(sourceTypes);
        boolean result = sourceTypes.add(sourceType);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, sourceTypes);
        return result;
    }

    public Set<Source> getSources() {
        return Collections.unmodifiableSet(sources);
    }

    public void addSources(Collection<Source> sources) {
        Set<Source> oldValue = new HashSet<>(this.sources);
        this.sources.addAll(sources);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, this.sources);
    }

    public Optional<Source> getSource(String representation) {
        return sources.stream().filter(entity -> entity.toString().toLowerCase().trim()
            .equals(representation.toLowerCase().trim())).findFirst();
    }

    public boolean addSource(Source source) {
        Set<Source> oldValue = new HashSet<>(sources);
        boolean result = sources.add(source);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, sources);
        return result;
    }

    public Set<Instrument> getInstruments() {
        return Collections.unmodifiableSet(instruments);
    }

    public void addInstruments(Collection<Instrument> instruments) {
        Set<Instrument> oldValue = new HashSet<>(this.instruments);
        this.instruments.addAll(instruments);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, this.instruments);
    }

    public Optional<Instrument> getInstrument(String name) {
        return instruments.stream()
            .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
            .findFirst();
    }

    public boolean addInstrument(Instrument instrument) {
        Set<Instrument> oldValue = new HashSet<>(instruments);
        boolean result = instruments.add(instrument);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, instruments);
        return result;
    }

    public Set<Artist> getArtists() {
        return Collections.unmodifiableSet(artists);
    }

    public void addArtists(Collection<Artist> artists) {
        Set<Artist> oldValue = new HashSet<>(this.artists);
        this.artists.addAll(artists);
        propertyChangeSupport.firePropertyChange(PROPERTY_ARTISTS, oldValue, this.artists);
    }

    public Optional<Artist> getArtist(String name) {
        return artists.stream()
            .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
            .findFirst();
    }

    public boolean addArtist(Artist artist) {
        Set<Artist> oldValue = new HashSet<>(artists);
        boolean result = artists.add(artist);
        propertyChangeSupport.firePropertyChange(PROPERTY_ARTISTS, oldValue, artists);
        return result;
    }

    public Set<Album> getAlbums() {
        return Collections.unmodifiableSet(albums);
    }

    public void addAlbums(Collection<Album> albums) {
        Set<Album> oldValue = new HashSet<>(this.albums);
        this.albums.addAll(albums);
        propertyChangeSupport.firePropertyChange(PROPERTY_ALBUMS, oldValue, this.albums);
    }

    public Optional<Album> getAlbum(String collectionSignature) {
        return albums.stream().filter(entity -> entity.getCollectionSignature().toLowerCase().trim()
            .equals(collectionSignature.toLowerCase().trim())).findFirst();
    }

    public boolean addAlbum(Album album) {
        Set<Album> oldValue = new HashSet<>(albums);
        boolean result = albums.add(album);
        propertyChangeSupport.firePropertyChange(PROPERTY_ALBUMS, oldValue, albums);
        return result;
    }

    @Override
    public List<FolklorePiece> getPieces() {
        return Collections.unmodifiableList(pieces);
    }

    public void addPieces(Collection<FolklorePiece> pieces) {
        List<FolklorePiece> oldValue = new ArrayList<>(this.pieces);
        this.pieces.addAll(pieces);
        propertyChangeSupport.firePropertyChange(PROPERTY_PIECES, oldValue, this.pieces);
    }

    public Optional<FolklorePiece> getPiece(int index) {
        if (index < 0 || index >= pieces.size()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.get(index));
        }
    }

    @Override
    public boolean addPiece(FolklorePiece piece) {
        List<FolklorePiece> oldValue = new ArrayList<>(pieces);
        boolean result = pieces.add(piece);
        propertyChangeSupport.firePropertyChange(PROPERTY_PIECES, oldValue, pieces);
        return result;
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return Collections.unmodifiableSet(ethnographicRegions);
    }

    public void addEthnographicRegions(Collection<EthnographicRegion> ethnographicRegions) {
        Set<EthnographicRegion> oldValue = new HashSet<>(this.ethnographicRegions);
        this.ethnographicRegions.addAll(ethnographicRegions);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, this.ethnographicRegions);
    }

    public Optional<EthnographicRegion> getEthnographicRegion(String name) {
        return ethnographicRegions.stream().filter(entity -> entity.getName().toLowerCase().trim()
            .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addEthnographicRegion(EthnographicRegion ethnographicRegion) {
        Set<EthnographicRegion> oldValue = new HashSet<>(ethnographicRegions);
        boolean result = ethnographicRegions.add(ethnographicRegion);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, ethnographicRegions);
        return result;
    }

}
