package org.yankov.mso.database.generic;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.yankov.mso.database.org.yankov.mso.database.generic.DatabaseSessionManager;

import java.io.IOException;
import java.nio.file.Paths;

public class DatabaseTest {

    private static final String CONFIGURATION_FILE = "hibernate-test.cfg.xml";

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
