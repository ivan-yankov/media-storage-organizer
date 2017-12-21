package org.yankov.mso.database.generic;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.yankov.mso.application.ApplicationArguments;
import org.yankov.mso.application.ApplicationContext;

public class DatabaseTest {

    public DatabaseTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext.getInstance().initialize(
                new ApplicationArguments(new String[] {"-settings=folklore", "-language=bg", "-mode=test"}));
        ApplicationContext.getInstance().getDatabaseSessionManager().openSession();
    }

    @AfterClass
    public static void afterClass() {
        ApplicationContext.getInstance().getDatabaseSessionManager().closeSession();
    }

}
