package org.yankov.mso.database;

import org.hibernate.query.Query;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DatabaseOperations {

    public static FolkloreEntityCollections loadFromDatabase() {
        FolkloreEntityCollections collections = new FolkloreEntityCollections();
        collections.addSourceTypes(FolkloreEntityCollectionFactory.createSourceTypes());
        collections.addSources(loadCollectionFromDatabase(Source.class));
        collections.addInstruments(loadCollectionFromDatabase(Instrument.class));
        collections.addArtists(loadCollectionFromDatabase(Artist.class));
        collections.addAlbums(loadCollectionFromDatabase(Album.class));
        collections.addEthnographicRegions(loadCollectionFromDatabase(EthnographicRegion.class));
        collections.addPieces(loadCollectionFromDatabase(FolklorePiece.class));
        return collections;
    }

    public static void saveToDatabase(EntityCollections entityCollections) {
        FolkloreEntityCollections collections = (FolkloreEntityCollections) entityCollections;
        saveCollectionToDatabase(collections.getSourceTypes());
        saveCollectionToDatabase(collections.getSources());
        saveCollectionToDatabase(collections.getInstruments());
        saveCollectionToDatabase(collections.getArtists());
        saveCollectionToDatabase(collections.getAlbums());
        saveCollectionToDatabase(collections.getEthnographicRegions());
        saveCollectionToDatabase(collections.getPieces());
    }

    private static <CollectionType> List<CollectionType> loadCollectionFromDatabase(Class entityClass) {
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

    private static <CollectionType> void saveCollectionToDatabase(Collection<CollectionType> collection) {
        ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            collection.forEach(session::saveOrUpdate);
            return null;
        });
    }

}
