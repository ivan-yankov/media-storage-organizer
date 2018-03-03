package org.yankov.mso.database;

import org.junit.Assert;
import org.junit.Test;
import org.yankov.mso.application.ApplicationContext;

public class DatabaseConnectionTest extends DatabaseTest {

    @Test
    public void testDatabaseConnection() {
        ApplicationContext.getInstance().getDatabaseSessionManager()
                          .executeOperation(null, throwable -> Assert.fail(throwable.getMessage()));
    }

}
