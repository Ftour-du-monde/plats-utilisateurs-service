package fr.univamu.iut.platsutilisateursservice.framework.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DataSourceProducerTest {

    @Test
    void dataSourceUtiliseFallbackMysqlQuandJndiIndisponible() {
        String previousJndi = System.getProperty("plats.users.db.jndi");
        try {
            System.setProperty("plats.users.db.jndi", "java:comp/env/jdbc/inexistant");

            DataSource dataSource = new DataSourceProducer().dataSource();

            assertInstanceOf(MysqlDataSource.class, dataSource);
        } finally {
            restoreProperty("plats.users.db.jndi", previousJndi);
        }
    }

    @Test
    void dataSourceLitLaConfigurationDepuisProprietesSysteme() {
        String previousJndi = System.getProperty("plats.users.db.jndi");
        String previousUrl = System.getProperty("plats.users.db.url");
        String previousUser = System.getProperty("plats.users.db.user");
        String previousPassword = System.getProperty("plats.users.db.password");

        String configuredUrl = "jdbc:mysql://127.0.0.1:3306/tests_db";
        String configuredUser = "tests_user";
        String configuredPassword = "tests_pwd";

        try {
            System.setProperty("plats.users.db.jndi", "java:comp/env/jdbc/inexistant");
            System.setProperty("plats.users.db.url", configuredUrl);
            System.setProperty("plats.users.db.user", configuredUser);
            System.setProperty("plats.users.db.password", configuredPassword);

            MysqlDataSource dataSource = (MysqlDataSource) new DataSourceProducer().dataSource();

            assertEquals(envOrDefault("PLATS_USERS_DB_URL", configuredUrl), dataSource.getURL());
            assertEquals(envOrDefault("PLATS_USERS_DB_USER", configuredUser), dataSource.getUser());
            assertEquals(envOrDefault("PLATS_USERS_DB_PASSWORD", configuredPassword), dataSource.getPassword());
        } finally {
            restoreProperty("plats.users.db.jndi", previousJndi);
            restoreProperty("plats.users.db.url", previousUrl);
            restoreProperty("plats.users.db.user", previousUser);
            restoreProperty("plats.users.db.password", previousPassword);
        }
    }

    private static String envOrDefault(String envKey, String fallback) {
        String env = System.getenv(envKey);
        return env == null || env.isBlank() ? fallback : env;
    }

    private static void restoreProperty(String key, String value) {
        if (value == null) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, value);
        }
    }
}
