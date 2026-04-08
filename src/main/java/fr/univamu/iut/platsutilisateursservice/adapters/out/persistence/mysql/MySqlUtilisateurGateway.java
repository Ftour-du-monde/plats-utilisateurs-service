package fr.univamu.iut.platsutilisateursservice.adapters.out.persistence.mysql;

import fr.univamu.iut.platsutilisateursservice.application.port.out.UtilisateurGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import fr.univamu.iut.platsutilisateursservice.domain.exception.DuplicateEmailException;
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
 * Adaptateur MySQL du port {@link UtilisateurGateway}.
 */
@ApplicationScoped
public class MySqlUtilisateurGateway implements UtilisateurGateway {

    private static final Logger LOGGER = Logger.getLogger(MySqlUtilisateurGateway.class.getName());

    private static final String SQL_SELECT_ALL =
            "SELECT id, nom, prenom, email, adresse FROM utilisateurs ORDER BY id";
    private static final String SQL_SELECT_ONE =
            "SELECT id, nom, prenom, email, adresse FROM utilisateurs WHERE id = ?";
    private static final String SQL_INSERT =
            "INSERT INTO utilisateurs(nom, prenom, email, adresse) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, adresse = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM utilisateurs WHERE id = ?";

    @Inject
    DataSource dataSource;

    /**
     * Constructeur par defaut requis par CDI.
     */
    public MySqlUtilisateurGateway() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Utilisateur> findAll() {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            List<Utilisateur> utilisateurs = new ArrayList<>();
            while (resultSet.next()) {
                utilisateurs.add(mapRow(resultSet));
            }
            return utilisateurs;
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la lecture de tous les utilisateurs.", exception);
            throw new IllegalStateException("Impossible de lire les utilisateurs depuis la base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Utilisateur> findById(Long id) {
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
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la lecture d'un utilisateur par id.", exception);
            throw new IllegalStateException("Impossible de lire l'utilisateur depuis la base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setString(4, utilisateur.getAdresse());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new IllegalStateException("Creation de l'utilisateur impossible: id non genere.");
                }
                long id = generatedKeys.getLong(1);
                return new Utilisateur(
                        id,
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getEmail(),
                        utilisateur.getAdresse()
                );
            }
        } catch (SQLException exception) {
            if (isUniqueConstraint(exception)) {
                throw new DuplicateEmailException("Un utilisateur avec cet email existe deja.");
            }
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la creation d'un utilisateur.", exception);
            throw new IllegalStateException("Impossible de creer l'utilisateur en base.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Utilisateur> update(Long id, Utilisateur utilisateur) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setString(4, utilisateur.getAdresse());
            statement.setLong(5, id);
            int updated = statement.executeUpdate();
            if (updated == 0) {
                return Optional.empty();
            }
            return Optional.of(new Utilisateur(
                    id,
                    utilisateur.getNom(),
                    utilisateur.getPrenom(),
                    utilisateur.getEmail(),
                    utilisateur.getAdresse()
            ));
        } catch (SQLException exception) {
            if (isUniqueConstraint(exception)) {
                throw new DuplicateEmailException("Un utilisateur avec cet email existe deja.");
            }
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la mise a jour d'un utilisateur.", exception);
            throw new IllegalStateException("Impossible de modifier l'utilisateur en base.", exception);
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
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de la suppression d'un utilisateur.", exception);
            throw new IllegalStateException("Impossible de supprimer l'utilisateur en base.", exception);
        }
    }

    private Utilisateur mapRow(ResultSet resultSet) throws SQLException {
        return new Utilisateur(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("email"),
                resultSet.getString("adresse")
        );
    }

    private Connection openConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private boolean isUniqueConstraint(SQLException exception) {
        return exception.getSQLState() != null && exception.getSQLState().startsWith("23");
    }
}
