package fr.univamu.iut.platsutilisateursservice.adapters.out.persistence.mysql;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MySqlPlatGatewayTest {

    private static final String SQL_SELECT_ALL = "SELECT id, nom, description, prix FROM plats ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT id, nom, description, prix FROM plats WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO plats(nom, description, prix) VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE plats SET nom = ?, description = ?, prix = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM plats WHERE id = ?";

    @Test
    void findAllMappeLesLignesSqlEnEntites() {
        JdbcProxySupport.StatementScript selectAll = new JdbcProxySupport.StatementScript();
        selectAll.queryResult = JdbcProxySupport.resultSet(List.of(
                JdbcProxySupport.row("id", 1L, "nom", "Aioli", "description", "Poisson", "prix", new BigDecimal("13.50")),
                JdbcProxySupport.row("id", 2L, "nom", "Gratin", "description", "Pommes de terre", "prix", new BigDecimal("11.90"))
        ));
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_SELECT_ALL, JdbcProxySupport.preparedStatement(selectAll)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        List<Plat> plats = gateway.findAll();

        assertEquals(2, plats.size());
        assertEquals("Aioli", plats.get(0).getNom());
        assertEquals(new BigDecimal("11.90"), plats.get(1).getPrix());
    }

    @Test
    void findByIdRetourneVideQuandAucuneLigne() {
        JdbcProxySupport.StatementScript selectOne = new JdbcProxySupport.StatementScript();
        selectOne.queryResult = JdbcProxySupport.resultSet(List.of());
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_SELECT_ONE, JdbcProxySupport.preparedStatement(selectOne)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        Optional<Plat> result = gateway.findById(99L);

        assertTrue(result.isEmpty());
        assertEquals(99L, selectOne.parameters.get(1));
    }

    @Test
    void saveRetourneEntiteAvecIdGenereEtParametresSql() {
        JdbcProxySupport.StatementScript insert = new JdbcProxySupport.StatementScript();
        insert.generatedKeys = JdbcProxySupport.resultSet(List.of(JdbcProxySupport.row(1, 7L)));
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_INSERT, JdbcProxySupport.preparedStatement(insert)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        Plat created = gateway.save(new Plat(null, "Salade", "Fraiche", new BigDecimal("9.90")));

        assertEquals(7L, created.getId());
        assertEquals("Salade", created.getNom());
        assertEquals("Salade", insert.parameters.get(1));
        assertEquals("Fraiche", insert.parameters.get(2));
        assertEquals(new BigDecimal("9.90"), insert.parameters.get(3));
    }

    @Test
    void saveLanceIllegalStateSiIdNonGenere() {
        JdbcProxySupport.StatementScript insert = new JdbcProxySupport.StatementScript();
        insert.generatedKeys = JdbcProxySupport.resultSet(List.of());
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_INSERT, JdbcProxySupport.preparedStatement(insert)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> gateway.save(new Plat(null, "Salade", "Fraiche", new BigDecimal("9.90"))));

        assertTrue(exception.getMessage().contains("id non genere"));
    }

    @Test
    void updateRetourneVideQuandAucuneLigneNEstModifiee() {
        JdbcProxySupport.StatementScript update = new JdbcProxySupport.StatementScript();
        update.executeUpdateResult = 0;
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_UPDATE, JdbcProxySupport.preparedStatement(update)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        Optional<Plat> result = gateway.update(8L, new Plat(8L, "Soupe", "Legumes", new BigDecimal("8.50")));

        assertTrue(result.isEmpty());
        assertEquals(8L, update.parameters.get(4));
    }

    @Test
    void deleteByIdRetourneTrueQuandUneLigneEstSupprimee() {
        JdbcProxySupport.StatementScript delete = new JdbcProxySupport.StatementScript();
        delete.executeUpdateResult = 1;
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_DELETE, JdbcProxySupport.preparedStatement(delete)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        boolean deleted = gateway.deleteById(4L);

        assertTrue(deleted);
        assertEquals(4L, delete.parameters.get(1));
    }

    @Test
    void findAllEnveloppeSQLExceptionDansIllegalState() {
        JdbcProxySupport.StatementScript selectAll = new JdbcProxySupport.StatementScript();
        selectAll.executeQueryException = new SQLException("boom");
        Connection connection = JdbcProxySupport.connection(Map.of(SQL_SELECT_ALL, JdbcProxySupport.preparedStatement(selectAll)));

        MySqlPlatGateway gateway = new MySqlPlatGateway();
        gateway.dataSource = JdbcProxySupport.dataSource(connection);

        IllegalStateException exception = assertThrows(IllegalStateException.class, gateway::findAll);

        assertTrue(exception.getMessage().contains("Impossible de lire les plats"));
    }
}
