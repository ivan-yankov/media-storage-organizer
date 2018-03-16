package org.yankov.mso.database;

import org.hibernate.query.Query;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Level;

public class FolkloreEntityCollections implements EntityCollections<FolklorePiece> {

    private static final String PROPERTY_ETHNOGRAPHIC_REGIONS = "ethnographicRegions";

    private static final String PROPERTY_SOURCE_TYPES = "sourceTypes";
    private static final String PROPERTY_SOURCES = "sources";
    private static final String PROPERTY_INSTRUMENTS = "instruments";
    private static final String PROPERTY_ARTISTS = "artists";
    private static final String PROPERTY_ALBUMS = "albums";
    private static final String PROPERTY_PIECES = "pieces";

    private static FolkloreEntityCollections instance;

    private final PropertyChangeSupport propertyChangeSupport;

    private final Set<SourceType> sourceTypes;
    private final Set<Source> sources;
    private final Set<Instrument> instruments;
    private final Set<Artist> artists;
    private final Set<Album> albums;
    private final Set<EthnographicRegion> ethnographicRegions;
    private final List<FolklorePiece> pieces;

    private FolkloreEntityCollections() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.sourceTypes = new HashSet<>();
        this.sources = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
        this.albums = new HashSet<>();
        this.ethnographicRegions = new HashSet<>();
        this.pieces = new ArrayList<>();
    }

    public static FolkloreEntityCollections getInstance() {
        if (instance == null) {
            instance = new FolkloreEntityCollections();
        }
        return instance;
    }

    @Override
    public void clear() {
        getInstance().sourceTypes.clear();
        getInstance().sources.clear();
        getInstance().instruments.clear();
        getInstance().artists.clear();
        getInstance().albums.clear();
        getInstance().ethnographicRegions.clear();
        getInstance().pieces.clear();
    }

    @Override
    public void initialize() {
        if (getInstance().sourceTypes.isEmpty()) {
            getInstance().sourceTypes.addAll(FolkloreEntityCollectionFactory.createSourceTypes());
        }
        if (getInstance().sources.isEmpty()) {
            getInstance().sources.addAll(FolkloreEntityCollectionFactory.createSources(sourceTypes));
        }
        if (getInstance().instruments.isEmpty()) {
            getInstance().instruments.addAll(FolkloreEntityCollectionFactory.createInstruments());
        }
        if (getInstance().ethnographicRegions.isEmpty()) {
            getInstance().ethnographicRegions.addAll(FolkloreEntityCollectionFactory.createEthnographicRegions());
        }
    }

    @Override
    public void loadFromDatabase() {
        clear();
        getInstance().sourceTypes.addAll(FolkloreEntityCollectionFactory.createSourceTypes());
        getInstance().sources.addAll(loadCollectionFromDatabase(Source.class));
        getInstance().instruments.addAll(loadCollectionFromDatabase(Instrument.class));
        getInstance().artists.addAll(loadCollectionFromDatabase(Artist.class));
        getInstance().albums.addAll(loadCollectionFromDatabase(Album.class));
        getInstance().ethnographicRegions.addAll(loadCollectionFromDatabase(EthnographicRegion.class));
        getInstance().pieces.addAll(loadCollectionFromDatabase(FolklorePiece.class));
    }

    @Override
    public void saveToDatabase() {
        saveCollectionToDatabase(getInstance().sourceTypes);
        saveCollectionToDatabase(getInstance().sources);
        saveCollectionToDatabase(getInstance().instruments);
        saveCollectionToDatabase(getInstance().artists);
        saveCollectionToDatabase(getInstance().albums);
        saveCollectionToDatabase(getInstance().ethnographicRegions);
        saveCollectionToDatabase(getInstance().pieces);
    }

    private <CollectionType> List<CollectionType> loadCollectionFromDatabase(Class entityClass) {
        Optional optResult = ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<CollectionType> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<CollectionType> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            Query<CollectionType> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        });

        if (!optResult.isPresent()) {
            return Collections.emptyList();
        }

        return (List<CollectionType>) optResult.get();
    }

    private <CollectionType> void saveCollectionToDatabase(Collection<CollectionType> collection) {
        ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            collection.forEach(session::saveOrUpdate);
            return null;
        });
    }

    public Set<EthnographicRegion> getEthnographicRegions() {
        return Collections.unmodifiableSet(ethnographicRegions);
    }

    public Optional<EthnographicRegion> getEthnographicRegion(String name) {
        return ethnographicRegions.stream().filter(entity -> entity.getName().toLowerCase().trim()
                                                                   .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addEthnographicRegion(EthnographicRegion ethnographicRegion) {
        Set<EthnographicRegion> oldValue = new HashSet<>();
        oldValue.addAll(ethnographicRegions);
        boolean result = ethnographicRegions.add(ethnographicRegion);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, ethnographicRegions);
        return result;
    }

    public void addEthnographicRegions(Set<EthnographicRegion> ethnographicRegions) {
        Set<EthnographicRegion> oldValue = new HashSet<>();
        oldValue.addAll(this.ethnographicRegions);
        this.ethnographicRegions.addAll(ethnographicRegions);
        propertyChangeSupport.firePropertyChange(PROPERTY_ETHNOGRAPHIC_REGIONS, oldValue, this.ethnographicRegions);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public Set<SourceType> getSourceTypes() {
        return Collections.unmodifiableSet(sourceTypes);
    }

    @Override
    public Set<Source> getSources() {
        return Collections.unmodifiableSet(sources);
    }

    @Override
    public Set<Instrument> getInstruments() {
        return Collections.unmodifiableSet(instruments);
    }

    @Override
    public Set<Artist> getArtists() {
        return Collections.unmodifiableSet(artists);
    }

    @Override
    public Set<Album> getAlbums() {
        return Collections.unmodifiableSet(albums);
    }

    @Override
    public List<FolklorePiece> getPieces() {
        return Collections.unmodifiableList(pieces);
    }

    @Override
    public Optional<SourceType> getSourceType(String name) {
        return sourceTypes.stream()
                          .filter(entity -> entity.toString().toLowerCase().trim().equals(name.toLowerCase().trim()))
                          .findFirst();
    }

    @Override
    public boolean addSourceType(SourceType sourceType) {
        Set<SourceType> oldValue = new HashSet<>();
        oldValue.addAll(sourceTypes);
        boolean result = sourceTypes.add(sourceType);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, sourceTypes);
        return result;
    }

    @Override
    public void addSourceTypes(Set<SourceType> sourceTypes) {
        Set<SourceType> oldValue = new HashSet<>();
        oldValue.addAll(this.sourceTypes);
        this.sourceTypes.addAll(sourceTypes);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, this.sourceTypes);
    }

    @Override
    public Optional<Source> getSource(String representation) {
        return sources.stream().filter(entity -> entity.toString().toLowerCase().trim()
                                                       .equals(representation.toLowerCase().trim())).findFirst();
    }

    @Override
    public boolean addSource(Source source) {
        Set<Source> oldValue = new HashSet<>();
        oldValue.addAll(sources);
        boolean result = sources.add(source);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, sources);
        return result;
    }

    @Override
    public void addSources(Set<Source> sources) {
        Set<Source> oldValue = new HashSet<>();
        oldValue.addAll(this.sources);
        this.sources.addAll(sources);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, this.sources);
    }

    @Override
    public Optional<Instrument> getInstrument(String name) {
        return instruments.stream()
                          .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
                          .findFirst();
    }

    @Override
    public boolean addInstrument(Instrument instrument) {
        Set<Instrument> oldValue = new HashSet<>();
        oldValue.addAll(instruments);
        boolean result = instruments.add(instrument);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, instruments);
        return result;
    }

    @Override
    public void addInstruments(Set<Instrument> instruments) {
        Set<Instrument> oldValue = new HashSet<>();
        oldValue.addAll(this.instruments);
        this.instruments.addAll(instruments);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, this.instruments);
    }

    @Override
    public Optional<Artist> getArtist(String name) {
        return artists.stream()
                      .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
                      .findFirst();
    }

    @Override
    public boolean addArtist(Artist artist) {
        Set<Artist> oldValue = new HashSet<>();
        oldValue.addAll(artists);
        boolean result = artists.add(artist);
        propertyChangeSupport.firePropertyChange(PROPERTY_ARTISTS, oldValue, artists);
        return result;
    }

    @Override
    public Optional<Album> getAlbum(String collectionSignature) {
        return albums.stream().filter(entity -> entity.getCollectionSignature().toLowerCase().trim()
                                                      .equals(collectionSignature.toLowerCase().trim())).findFirst();
    }

    @Override
    public boolean addAlbum(Album album) {
        Set<Album> oldValue = new HashSet<>();
        oldValue.addAll(albums);
        boolean result = albums.add(album);
        propertyChangeSupport.firePropertyChange(PROPERTY_ALBUMS, oldValue, albums);
        return result;
    }

    @Override
    public Optional<FolklorePiece> getPiece(int index) {
        if (index < 0 || index >= pieces.size()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.get(index));
        }
    }

    @Override
    public boolean addPiece(FolklorePiece piece) {
        List<FolklorePiece> oldValue = new ArrayList<>();
        oldValue.addAll(pieces);
        boolean result = pieces.add(piece);
        propertyChangeSupport.firePropertyChange(PROPERTY_PIECES, oldValue, pieces);
        return result;
    }

}
