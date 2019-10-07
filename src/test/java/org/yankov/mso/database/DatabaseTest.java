package org.yankov.mso.database;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.yankov.mso.application.ApplicationContext;
import org.yankov.mso.application.MediaStorageOrganizer;

public class DatabaseTest {

    public static final String[] ARGS = new String[] { "--test-mode", "--db-url",
        "jdbc:derby:memory:testdb;create=true" };

    static {
        MediaStorageOrganizer.arguments.parseValues(ARGS);
    }

    public DatabaseTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext.getInstance().initialize(MediaStorageOrganizer.arguments);

        ApplicationContext.getInstance().getDatabaseManager()
            .setOperationFailed(throwable -> Assert.fail(throwable.getMessage()));

        ApplicationContext.getInstance().getDatabaseManager().startServer();
        ApplicationContext.getInstance().getDatabaseManager().openSession();
    }

    @AfterClass
    public static void afterClass() {
        ApplicationContext.getInstance().getDatabaseManager().closeSession();
        ApplicationContext.getInstance().getDatabaseManager().stopServer();
    }

}
