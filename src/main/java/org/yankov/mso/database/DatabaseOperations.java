package org.yankov.mso.database;

import org.hibernate.query.Query;
import org.yankov.mso.application.ApplicationContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Supplier;

public class DatabaseOperations {

    public static EntityCollections loadFromDatabase(Supplier<EntityCollections> creator) {
        EntityCollections entityCollections = creator.get();
        Map<Class, Collection> collections = entityCollections.getCollections();
        for (Class c : collections.keySet()) {
            entityCollections.addEntities(c, loadCollectionFromDatabase(c));
        }
        return entityCollections;
    }

    public static void saveToDatabase(EntityCollections entityCollections) {
        Map<Class, Collection> collections = entityCollections.getCollections();
        for (Class c : collections.keySet()) {
            saveCollectionToDatabase(collections.get(c));
        }
    }

    private static <T> List<T> loadCollectionFromDatabase(Class entityClass) {
        Optional optResult = ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        });

        if (!optResult.isPresent()) {
            return Collections.emptyList();
        }

        return (List<T>) optResult.get();
    }

    private static <T> void saveCollectionToDatabase(Collection<T> collection) {
        ApplicationContext.getInstance().getDatabaseManager().executeOperation(session -> {
            collection.forEach(session::saveOrUpdate);
            return null;
        });
    }

}
