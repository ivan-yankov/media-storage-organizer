package org.yankov.mso.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseConnectionTest {

    private static final String CONFIGURATION_FILE = "hibernate-test.cfg.xml";

    @Test
    public void testDatabaseConnection() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = SessionFactoryBuilder.createSessionFactory(CONFIGURATION_FILE).openSession();
            transaction = session.beginTransaction();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            Assert.fail(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
