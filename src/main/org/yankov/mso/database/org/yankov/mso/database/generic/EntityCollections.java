package org.yankov.mso.database.org.yankov.mso.database.generic;

import org.hibernate.query.Query;
import org.yankov.mso.database.org.yankov.mso.database.folklore.FolkloreEntityCollectionFactory;
import org.yankov.mso.datamodel.generic.Artist;
import org.yankov.mso.datamodel.generic.Disc;
import org.yankov.mso.datamodel.generic.Instrument;
import org.yankov.mso.datamodel.generic.SourceType;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class EntityCollections {

    public abstract void initializeEntityCollections();

    public abstract void saveEntityCollections();

    private static final Logger LOGGER = Logger.getLogger(EntityCollections.class.getName());

    private Set<SourceType> sourceTypes;
    private Set<Instrument> instruments;
    private Set<Artist> artists;
    private Set<Disc> discs;

    public EntityCollections() {
        this.sourceTypes = new HashSet<>();
        this.instruments = new HashSet<>();
        this.artists = new HashSet<>();
        this.discs = new HashSet<>();
    }

    public Set<SourceType> getSourceTypes() {
        return sourceTypes;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public Set<Disc> getDiscs() {
        return discs;
    }

    public Optional<SourceType> getSourceType(String name) {
        return sourceTypes.stream().filter(entity -> entity.getName().toLowerCase().trim()
                .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addSourceType(String name) {
        return sourceTypes.add(new SourceType(name));
    }

    public Optional<Instrument> getInstrument(String name) {
        return instruments.stream().filter(entity -> entity.getName().toLowerCase().trim()
                .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addInstrument(String name) {
        return instruments.add(new Instrument(name));
    }

    public Optional<Artist> getArtist(String name) {
        return artists.stream().filter(entity -> entity.getName().toLowerCase().trim()
                .equals(name.toLowerCase().trim())).findFirst();
    }

    public boolean addArtist(String name) {
        return artists.add(new Artist(name));
    }

    public Optional<Disc> getDisc(String collectionSignature) {
        return discs.stream().filter(
                entity -> entity.getCollectionSignature().toLowerCase().trim()
                        .equals(collectionSignature.toLowerCase().trim())).findFirst();
    }

    public boolean addDisc(String collectionSignature) {
        return discs.add(new Disc(collectionSignature));
    }

    protected final <T> void initializeEntityCollection(Class entityClass, Collection<T> collection,
                                                Collection<T> defaultCollection) {
        collection.clear();

        List<T> dbCollection = loadCollectionFromDatabase(entityClass);
        if (!dbCollection.isEmpty()) {
            collection.addAll(dbCollection);
        } else {
            collection.addAll(defaultCollection);
        }
    }

    protected final void saveCollectionToDatabase(Collection<?> collection) {
        DatabaseSessionManager.getInstance().executeOperation(session -> {
            collection.forEach(entity -> session.saveOrUpdate(entity));
            return null;
        }, message -> LOGGER.log(Level.SEVERE, message));
    }

    private <T> List<T> loadCollectionFromDatabase(Class entityClass) {
        Optional optResult = DatabaseSessionManager.getInstance().executeOperation(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }, message -> LOGGER.log(Level.SEVERE, message));

        if (!optResult.isPresent()) {
            return Collections.emptyList();
        }

        return (List<T>) optResult.get();
    }

}
