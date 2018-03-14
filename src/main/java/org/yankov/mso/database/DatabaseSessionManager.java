package org.yankov.mso.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

public class DatabaseSessionManager {

    private Session session;

    public DatabaseSessionManager() {
    }

    public boolean openSession() {
        if (session != null && session.isOpen()) {
            return false;
        }

        Optional<SessionFactory> sessionFactory = createSessionFactory();
        if (sessionFactory.isPresent()) {
            session = sessionFactory.get().openSession();
            return true;
        } else {
            return false;
        }
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public Optional<Object> executeOperation(Function<Session, Object> operation, Consumer<Throwable> operationFailed) {
        Object result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (operation != null) {
                result = operation.apply(session);
            }
            transaction.commit();
            return result != null ? Optional.of(result) : Optional.empty();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            if (operationFailed != null) {
                operationFailed.accept(e);
            }
            return Optional.empty();
        }
    }

    private Optional<SessionFactory> createSessionFactory() {
        try {
            Map<String, String> settings = new HashMap<>();
            settings.put(Environment.DRIVER, "org.apache.derby.jdbc.ClientDriver");
            settings.put(Environment.URL, "jdbc:derby://localhost:1527/mso");
            settings.put(Environment.DEFAULT_SCHEMA, "admin");
            settings.put(Environment.USER, "admin");
            settings.put(Environment.PASS, "admin");

            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySettings(settings);

            StandardServiceRegistry registry = registryBuilder.build();

            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClass(SourceType.class);
            sources.addAnnotatedClass(Source.class);
            sources.addAnnotatedClass(Record.class);
            sources.addAnnotatedClass(Piece.class);
            sources.addAnnotatedClass(Instrument.class);
            sources.addAnnotatedClass(Album.class);
            sources.addAnnotatedClass(Artist.class);
            sources.addAnnotatedClass(FolklorePiece.class);
            sources.addAnnotatedClass(EthnographicRegion.class);
            sources.addAnnotatedClass(FolkloreEntityCollections.class);

            Metadata metadata = sources.getMetadataBuilder().build();

            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

            return Optional.of(sessionFactory);
        } catch (HibernateException e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
            return Optional.empty();
        }
    }

}
