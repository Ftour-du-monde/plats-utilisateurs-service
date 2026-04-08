package fr.univamu.iut.platsutilisateursservice.adapters.out.persistence.mysql;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import fr.univamu.iut.platsutilisateursservice.domain.exception.DuplicateEmailException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MySqlUtilisateurGatewayTest {

    private static final String SQL_SELECT_ONE =
            "SELECT id, nom, prenom, email, adresse FROM utilisateurs WHERE id = ?";
    private static final String SQL_INSERT =
            "INSERT INTO utilisateurs(nom, prenom, email, adresse) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, adresse = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM utilisateurs WHERE id = ?";

    @Test
    void findByIdRetourneUtilisateurQuandPresent() {
        JdbcProxySupport.StatementScript selectOne = new JdbcProxySupport.StatementScript();
        selectOne.queryResult = JdbcProxySupport.resultSet(List.of(
                JdbcProxySupport.row(
                        "id", 1L,
                        "nom", "Dupont",
                        "prenom", "Alice",
                        "email", "alice@example.com",
                        "adresse", "Marseille"
                )
        ));
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_SELECT_ONE, JdbcProxySupport.preparedStatement(selectOne)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        Optional<Utilisateur> utilisateur = gateway.findById(1L);

        assertTrue(utilisateur.isPresent());
        assertEquals("Dupont", utilisateur.get().getNom());
        assertEquals("alice@example.com", utilisateur.get().getEmail());
    }

    @Test
    void saveRetourneEntiteAvecIdGenere() {
        JdbcProxySupport.StatementScript insert = new JdbcProxySupport.StatementScript();
        insert.generatedKeys = JdbcProxySupport.resultSet(List.of(JdbcProxySupport.row(1, 5L)));
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_INSERT, JdbcProxySupport.preparedStatement(insert)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        Utilisateur created = gateway.save(new Utilisateur(null, "Martin", "Bob", "bob@example.com", "Aix"));

        assertEquals(5L, created.getId());
        assertEquals("Martin", insert.parameters.get(1));
        assertEquals("Bob", insert.parameters.get(2));
        assertEquals("bob@example.com", insert.parameters.get(3));
        assertEquals("Aix", insert.parameters.get(4));
    }

    @Test
    void saveLanceDuplicateEmailExceptionSiContrainteUnique() {
        JdbcProxySupport.StatementScript insert = new JdbcProxySupport.StatementScript();
        insert.executeUpdateException = new SQLException("duplicate", "23000");
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_INSERT, JdbcProxySupport.preparedStatement(insert)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class,
                () -> gateway.save(new Utilisateur(null, "Martin", "Bob", "bob@example.com", "Aix")));

        assertTrue(exception.getMessage().contains("email existe deja"));
    }

    @Test
    void saveLanceIllegalStateSiErreurSqlNonMetier() {
        JdbcProxySupport.StatementScript insert = new JdbcProxySupport.StatementScript();
        insert.executeUpdateException = new SQLException("generic", "42000");
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_INSERT, JdbcProxySupport.preparedStatement(insert)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> gateway.save(new Utilisateur(null, "Martin", "Bob", "bob@example.com", "Aix")));

        assertTrue(exception.getMessage().contains("Impossible de creer l'utilisateur"));
    }

    @Test
    void updateLanceDuplicateEmailExceptionSiContrainteUnique() {
        JdbcProxySupport.StatementScript update = new JdbcProxySupport.StatementScript();
        update.executeUpdateException = new SQLException("duplicate", "23505");
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_UPDATE, JdbcProxySupport.preparedStatement(update)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class,
                () -> gateway.update(8L, new Utilisateur(8L, "Martin", "Bob", "bob@example.com", "Aix")));

        assertTrue(exception.getMessage().contains("email existe deja"));
    }

    @Test
    void deleteByIdRetourneFalseQuandAucuneLigneSupprimee() {
        JdbcProxySupport.StatementScript delete = new JdbcProxySupport.StatementScript();
        delete.executeUpdateResult = 0;
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_DELETE, JdbcProxySupport.preparedStatement(delete)));

        MySqlUtilisateurGateway gateway = new MySqlUtilisateurGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        boolean deleted = gateway.deleteById(99L);

        assertTrue(!deleted);
        assertEquals(99L, delete.parameters.get(1));
    }
}
