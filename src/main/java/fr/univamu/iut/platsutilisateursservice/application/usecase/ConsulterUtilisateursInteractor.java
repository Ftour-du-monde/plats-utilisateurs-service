package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.out.UtilisateurGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * Interactor de consultation des utilisateurs.
 */
@ApplicationScoped
public class ConsulterUtilisateursInteractor implements ConsulterUtilisateursUseCase {

    private final UtilisateurGateway utilisateurGateway;

    /**
     * Cree l'interactor de consultation des utilisateurs.
     *
     * @param utilisateurGateway port de persistence des utilisateurs.
     */
    @Inject
    public ConsulterUtilisateursInteractor(UtilisateurGateway utilisateurGateway) {
        this.utilisateurGateway = utilisateurGateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Utilisateur> tousLesUtilisateurs() {
        return utilisateurGateway.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Utilisateur> utilisateurParId(Long id) {
        validateId(id);
        return utilisateurGateway.findById(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur est invalide.");
        }
    }
}
