package org.yankov.mso.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import org.apache.derby.drda.NetworkServerControl;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.yankov.mso.application.ApplicationArguments;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.datamodel.Album;
import org.yankov.mso.datamodel.Artist;
import org.yankov.mso.datamodel.EthnographicRegion;
import org.yankov.mso.datamodel.FolklorePiece;
import org.yankov.mso.datamodel.Instrument;
import org.yankov.mso.datamodel.Piece;
import org.yankov.mso.datamodel.Record;
import org.yankov.mso.datamodel.Source;
import org.yankov.mso.datamodel.SourceType;

public class DatabaseManager {

    private NetworkServerControl server;
    private Session session;

    private Consumer<Throwable> operationFailed;

    public DatabaseManager() {
    }

    public void setOperationFailed(Consumer<Throwable> operationFailed) {
        this.operationFailed = operationFailed;
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

    public void startServer() {
        new Thread(() -> {
            try {
                server = new NetworkServerControl();
                server.start(null);
            } catch (Exception e) {
                if (operationFailed != null) {
                    operationFailed.accept(e);
                }
            }
        }).start();
    }

    public void stopServer() {
        try {
            if (server != null) {
                server.shutdown();
            }
        } catch (Exception e) {
            if (operationFailed != null) {
                operationFailed.accept(e);
            }
        }
    }

    public Optional<Object> executeOperation(Function<Session, Object> operation) {
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
            String url = ApplicationContext.getInstance().getApplicationArguments()
                .getArgument(ApplicationArguments.DB_URL_NAME);

            String driver = ApplicationContext.getInstance().getApplicationArguments()
                .getArgument(ApplicationArguments.DB_DRIVER_NAME).equalsIgnoreCase("embedded")
                    ? "org.apache.derby.jdbc.EmbeddedDriver"
                    : "org.apache.derby.jdbc.ClientDriver";

            Map<String, String> settings = new HashMap<>();
            settings.put(Environment.DRIVER, driver);
            settings.put(Environment.URL, url);
            settings.put(Environment.DEFAULT_SCHEMA, "admin");
            settings.put(Environment.USER, "admin");
            settings.put(Environment.PASS, "admin");
            settings.put(Environment.HBM2DDL_AUTO, "validate");

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
