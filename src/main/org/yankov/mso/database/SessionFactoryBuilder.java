package org.yankov.mso.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryBuilder {

    public static SessionFactory createSessionFactory(String configurationFile) {
        Configuration configuration = new Configuration();
        configuration.configure(configurationFile);
        return configuration.buildSessionFactory();
    }

}
