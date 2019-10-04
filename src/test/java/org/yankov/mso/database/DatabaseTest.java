package org.yankov.mso.database;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.yankov.mso.application.ApplicationArguments;
import org.yankov.mso.application.ApplicationContext;

public class DatabaseTest {

    public static final String[] ARGS = new String[] { "--test-mode", "true", "--db-url",
        "jdbc:derby:memory:testdb;create=true" };
    public static final ApplicationArguments TEST_ARGUMENTS;

    static {
        TEST_ARGUMENTS = new ApplicationArguments();
        TEST_ARGUMENTS.parse(ARGS);
    }

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
