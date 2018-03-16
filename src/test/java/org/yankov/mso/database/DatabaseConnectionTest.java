package org.yankov.mso.database;

import org.junit.Test;
import org.yankov.mso.application.ApplicationContext;

public class DatabaseConnectionTest extends DatabaseTest {

    @Test
    public void testDatabaseConnection() {
        ApplicationContext.getInstance().getDatabaseManager().executeOperation(null);
    }

}
