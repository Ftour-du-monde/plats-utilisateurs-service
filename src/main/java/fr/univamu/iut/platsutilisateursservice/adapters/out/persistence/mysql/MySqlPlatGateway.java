package fr.univamu.iut.platsutilisateursservice.adapters.out.persistence.mysql;

import fr.univamu.iut.platsutilisateursservice.application.port.out.PlatGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Adaptateur MySQL du port {@link PlatGateway}.
 */
@ApplicationScoped
public class MySqlPlatGateway implements PlatGateway {

    private static final Logger LOGGER = Logger.getLogger(MySqlPlatGateway.class.getName());

    private static final String SQL_SELECT_ALL = "SELECT id, nom, description, prix FROM plats ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT id, nom, description, prix FROM plats WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO plats(nom, description, prix) VALUES(?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE plats SET nom = ?, description = ?, prix = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM plats WHERE id = ?";

    @Inject
    DataSource dataSource;

    /**
     * Constructeur par defaut requis par CDI.
     */
    public MySqlPlatGateway() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Plat> findAll() {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            List<Plat> plats = new ArrayList<>();
            while (resultSet.next()) {
                plats.add(mapRow(resultSet));
            }
            return plats;
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la lecture de tous les plats.", exception);
            throw new IllegalStateException("Impossible de lire les plats depuis la base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Plat> findById(Long id) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ONE)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(resultSet));
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la lecture d'un plat par id.", exception);
            throw new IllegalStateException("Impossible de lire le plat depuis la base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plat save(Plat plat) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, plat.getNom());
            statement.setString(2, plat.getDescription());
            statement.setBigDecimal(3, plat.getPrix());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new IllegalStateException("Creation du plat impossible: id non genere.");
                }
                long id = generatedKeys.getLong(1);
                return new Plat(id, plat.getNom(), plat.getDescription(), plat.getPrix());
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la creation d'un plat.", exception);
            throw new IllegalStateException("Impossible de creer le plat en base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Plat> update(Long id, Plat plat) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, plat.getNom());
            statement.setString(2, plat.getDescription());
            statement.setBigDecimal(3, plat.getPrix());
            statement.setLong(4, id);
            int updated = statement.executeUpdate();
            if (updated == 0) {
                return Optional.empty();
            }
            return Optional.of(new Plat(id, plat.getNom(), plat.getDescription(), plat.getPrix()));
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la mise a jour d'un plat.", exception);
            throw new IllegalStateException("Impossible de modifier le plat en base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la suppression d'un plat.", exception);
            throw new IllegalStateException("Impossible de supprimer le plat en base.", exception);
        }
    }

    private Plat mapRow(ResultSet resultSet) throws SQLException {
        return new Plat(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getString("description"),
                resultSet.getBigDecimal("prix")
        );
    }

    private Connection openConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
