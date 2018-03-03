package org.yankov.mso.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

public class DatabaseSessionManager {

    private String configurationFile;
    private List<Class> annotatedClasses;
    private Session session;

    public DatabaseSessionManager(String configurationFile) {
        this.configurationFile = configurationFile;

        annotatedClasses = new ArrayList<>();
        annotatedClasses.add(SourceType.class);
        annotatedClasses.add(Source.class);
        annotatedClasses.add(Record.class);
        annotatedClasses.add(Piece.class);
        annotatedClasses.add(Instrument.class);
        annotatedClasses.add(Album.class);
        annotatedClasses.add(Artist.class);
        annotatedClasses.add(FolklorePiece.class);
        annotatedClasses.add(EthnographicRegion.class);
        annotatedClasses.add(EntityCollections.class);
        annotatedClasses.add(FolkloreEntityCollections.class);
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
        Configuration configuration = new Configuration();
        configuration.configure(configurationFile);

        annotatedClasses.forEach(configuration::addAnnotatedClass);

        try {
            return Optional.of(configuration.buildSessionFactory());
        } catch (HibernateException e) {
            ApplicationContext.getInstance().getLogger().log(Level.SEVERE, e.getMessage(), e);
            return Optional.empty();
        }
    }

}
