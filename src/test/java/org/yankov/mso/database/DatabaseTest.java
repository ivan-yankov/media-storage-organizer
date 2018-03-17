package org.yankov.mso.database;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.yankov.mso.application.ApplicationArguments;
import org.yankov.mso.application.ApplicationContext;

public class DatabaseTest {

    public static final ApplicationArguments TEST_ARGUMENTS = new ApplicationArguments(
            new String[] {"test", "bg", "folklore", "embedded", "./test-db-unit/mso", "host", "port"});

    public DatabaseTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext.getInstance().initialize(TEST_ARGUMENTS);

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