package fr.univamu.iut.platsutilisateursservice.framework.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Produit le {@link DataSource} technique pour les adapters de persistence.
 */
@ApplicationScoped
public class DataSourceProducer {

    private static final String DEFAULT_JNDI = "java:comp/env/jdbc/platsUsersDS";
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/dashmed_plats_utilisateurs_service?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";

    /**
     * Constructeur par defaut du producteur CDI.
     */
    public DataSourceProducer() {
    }

    /**
     * Produit un DataSource en priorisant JNDI puis la configuration locale.
     *
     * @return source de donnees exploitable par les gateways MySQL.
     */
    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        DataSource jndiDataSource = loadFromJndi();
        if (jndiDataSource != null) {
            return jndiDataSource;
        }

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(read("PLATS_USERS_DB_URL", "plats.users.db.url", DEFAULT_URL));
        dataSource.setUser(read("PLATS_USERS_DB_USER", "plats.users.db.user", DEFAULT_USER));
        dataSource.setPassword(read("PLATS_USERS_DB_PASSWORD", "plats.users.db.password", ""));
        return dataSource;
    }

    private DataSource loadFromJndi() {
        String jndiName = read("PLATS_USERS_DB_JNDI", "plats.users.db.jndi", DEFAULT_JNDI);
        try {
            Object lookup = new InitialContext().lookup(jndiName);
            return lookup instanceof DataSource ? (DataSource) lookup : null;
        } catch (NamingException exception) {
            return null;
        }
    }

    private String read(String envKey, String propertyKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String propertyValue = System.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        return defaultValue;
    }
}
