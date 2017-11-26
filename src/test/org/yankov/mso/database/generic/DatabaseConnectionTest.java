package org.yankov.mso.database.generic;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseConnectionTest extends DatabaseTest {

    @Test
    public void testDatabaseConnection() {
        databaseSessionManager.executeOperation(null, message -> Assert.fail(message));
    }

}
