package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.out.PlatGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * Interactor de consultation des plats.
 */
@ApplicationScoped
public class ConsulterPlatsInteractor implements ConsulterPlatsUseCase {

    private final PlatGateway platGateway;

    /**
     * Cree l'interactor de consultation des plats.
     *
     * @param platGateway port de persistence des plats.
     */
    @Inject
    public ConsulterPlatsInteractor(PlatGateway platGateway) {
        this.platGateway = platGateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Plat> tousLesPlats() {
        return platGateway.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Plat> platParId(Long id) {
        validateId(id);
        return platGateway.findById(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant du plat est invalide.");
        }
    }
}
