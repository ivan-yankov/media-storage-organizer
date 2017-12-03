package org.yankov.mso.database.generic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.yankov.mso.database.folklore.FolkloreEntityCollections;
import org.yankov.mso.datamodel.folklore.EthnographicRegion;
import org.yankov.mso.datamodel.folklore.FolklorePiece;
import org.yankov.mso.datamodel.generic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseSessionManager {

    private static DatabaseSessionManager instance;

    private List<Class> annotatedClasses;

    private Session session;

    private DatabaseSessionManager() {
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

    public static DatabaseSessionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseSessionManager();
        }
        return instance;
    }

    public void openSession(String configurationFile) {
        if (session == null || !session.isOpen()) {
            session = createSessionFactory(configurationFile).openSession();
        }
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public Optional<Object> executeOperation(Function<Session, Object> operation, Consumer<String> operationFailed) {
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
                operationFailed.accept(e.getMessage());
            }
            return Optional.empty();
        }
    }

    private SessionFactory createSessionFactory(String configurationFile) {
        Configuration configuration = new Configuration();
        configuration.configure(configurationFile);

        annotatedClasses.forEach(configuration::addAnnotatedClass);

        return configuration.buildSessionFactory();
    }

}
