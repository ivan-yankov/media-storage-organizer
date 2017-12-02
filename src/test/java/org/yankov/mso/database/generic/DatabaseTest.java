package org.yankov.mso.database.generic;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DatabaseTest {

    private static final String CONFIGURATION_FILE = "hibernate.cfg.xml";

    protected DatabaseSessionManager databaseSessionManager;

    public DatabaseTest() {
        databaseSessionManager = DatabaseSessionManager.getInstance();
    }

    @BeforeClass
    public static void beforeClass() {
        DatabaseSessionManager.getInstance().openSession(CONFIGURATION_FILE);
    }

    @AfterClass
    public static void afterClass() {
        DatabaseSessionManager.getInstance().closeSession();
    }

}
