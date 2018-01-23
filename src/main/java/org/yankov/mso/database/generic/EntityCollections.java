package org.yankov.mso.database.generic;

import org.hibernate.query.Query;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.generic.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Level;

public abstract class EntityCollections<T extends Piece> {

    private static final String PROPERTY_SOURCE_TYPES = "sourceTypes";
    private static final String PROPERTY_SOURCES = "sources";
    private static final String PROPERTY_INSTRUMENTS = "instruments";
    private static final String PROPERTY_ARTISTS = "artists";
    private static final String PROPERTY_ALBUMS = "albums";
    private static final String PROPERTY_PIECES = "pieces";

    protected final PropertyChangeSupport propertyChangeSupport;

    protected final Set<SourceType> sourceTypes;
    protected final Set<Source> sources;
    protected final Set<Instrument> instruments;
    protected final Set<Artist> artists;
    protected final Set<Album> albums;
    protected final List<T> pieces;

    public abstract void initializeEntityCollections();

    public abstract void saveEntityCollections();

    public EntityCollections() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.sourceTypes = new HashSet<>();
        this.sources = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
        this.albums = new HashSet<>();
        this.pieces = new ArrayList<>();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Set<SourceType> getSourceTypes() {
        return Collections.unmodifiableSet(sourceTypes);
    }

    public Set<Source> getSources() {
        return Collections.unmodifiableSet(sources);
    }

    public Set<Instrument> getInstruments() {
        return Collections.unmodifiableSet(instruments);
    }

    public Set<Artist> getArtists() {
        return Collections.unmodifiableSet(artists);
    }

    public Set<Album> getAlbums() {
        return Collections.unmodifiableSet(albums);
    }

    public List<T> getPieces() {
        return Collections.unmodifiableList(pieces);
    }

    public Optional<SourceType> getSourceType(String name) {
        return sourceTypes.stream()
                          .filter(entity -> entity.toString().toLowerCase().trim().equals(name.toLowerCase().trim()))
                          .findFirst();
    }

    public boolean addSourceType(SourceType sourceType) {
        Set<SourceType> oldValue = new HashSet<>();
        oldValue.addAll(sourceTypes);
        boolean result = sourceTypes.add(sourceType);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, sourceTypes);
        return result;
    }

    public void addSourceTypes(Set<SourceType> sourceTypes) {
        Set<SourceType> oldValue = new HashSet<>();
        oldValue.addAll(this.sourceTypes);
        this.sourceTypes.addAll(sourceTypes);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCE_TYPES, oldValue, this.sourceTypes);
    }

    public Optional<Source> getSource(String representation) {
        return sources.stream().filter(entity -> entity.toString().toLowerCase().trim()
                                                       .equals(representation.toLowerCase().trim())).findFirst();
    }

    public boolean addSource(Source source) {
        Set<Source> oldValue = new HashSet<>();
        oldValue.addAll(sources);
        boolean result = sources.add(source);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, sources);
        return result;
    }

    public void addSources(Set<Source> sources) {
        Set<Source> oldValue = new HashSet<>();
        oldValue.addAll(this.sources);
        this.sources.addAll(sources);
        propertyChangeSupport.firePropertyChange(PROPERTY_SOURCES, oldValue, this.sources);
    }

    public Optional<Instrument> getInstrument(String name) {
        return instruments.stream()
                          .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
                          .findFirst();
    }

    public boolean addInstrument(Instrument instrument) {
        Set<Instrument> oldValue = new HashSet<>();
        oldValue.addAll(instruments);
        boolean result = instruments.add(instrument);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, instruments);
        return result;
    }

    public void addInstruments(Set<Instrument> instruments) {
        Set<Instrument> oldValue = new HashSet<>();
        oldValue.addAll(this.instruments);
        this.instruments.addAll(instruments);
        propertyChangeSupport.firePropertyChange(PROPERTY_INSTRUMENTS, oldValue, this.instruments);
    }

    public Optional<Artist> getArtist(String name) {
        return artists.stream()
                      .filter(entity -> entity.getName().toLowerCase().trim().equals(name.toLowerCase().trim()))
                      .findFirst();
    }

    public boolean addArtist(Artist artist) {
        Set<Artist> oldValue = new HashSet<>();
        oldValue.addAll(artists);
        boolean result = artists.add(artist);
        propertyChangeSupport.firePropertyChange(PROPERTY_ARTISTS, oldValue, artists);
        return result;
    }

    public Optional<Album> getAlbum(String collectionSignature) {
        return albums.stream().filter(entity -> entity.getCollectionSignature().toLowerCase().trim()
                                                      .equals(collectionSignature.toLowerCase().trim())).findFirst();
    }

    public boolean addAlbum(Album album) {
        Set<Album> oldValue = new HashSet<>();
        oldValue.addAll(albums);
        boolean result = albums.add(album);
        propertyChangeSupport.firePropertyChange(PROPERTY_ALBUMS, oldValue, albums);
        return result;
    }

    public Optional<T> getPiece(int index) {
        if (index < 0 || index >= pieces.size()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.get(index));
        }
    }

    public boolean addPiece(T piece) {
        List<T> oldValue = new ArrayList<>();
        oldValue.addAll(pieces);
        boolean result = pieces.add(piece);
        propertyChangeSupport.firePropertyChange(PROPERTY_PIECES, oldValue, pieces);
        return result;
    }

    protected final <CollectionType> void initializeEntityCollection(Class entityClass,
                                                                     Collection<CollectionType> collection,
                                                                     Collection<CollectionType> defaultCollection) {
        collection.clear();

        List<CollectionType> dbCollection = loadCollectionFromDatabase(entityClass);
        if (!dbCollection.isEmpty()) {
            collection.addAll(dbCollection);
        } else {
            collection.addAll(defaultCollection);
        }
    }

    protected final void saveCollectionToDatabase(Collection<?> collection) {
        ApplicationContext.getInstance().getDatabaseSessionManager().executeOperation(session -> {
            collection.forEach(session::saveOrUpdate);
            return null;
        }, message -> ApplicationContext.getInstance().getLogger().log(Level.SEVERE, message));
    }

    private <CollectionType> List<CollectionType> loadCollectionFromDatabase(Class entityClass) {
        Optional optResult = ApplicationContext.getInstance().getDatabaseSessionManager().executeOperation(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<CollectionType> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<CollectionType> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            Query<CollectionType> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }, message -> ApplicationContext.getInstance().getLogger().log(Level.SEVERE, message));

        if (!optResult.isPresent()) {
            return Collections.emptyList();
        }

        return (List<CollectionType>) optResult.get();
    }

}
