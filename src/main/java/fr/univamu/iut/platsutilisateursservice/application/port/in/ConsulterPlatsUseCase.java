package fr.univamu.iut.platsutilisateursservice.application.port.in;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;

import java.util.List;
import java.util.Optional;

/**
 * Port d'entree pour les cas d'usage de consultation des plats.
 */
public interface ConsulterPlatsUseCase {
    /**
     * Retourne la liste complete des plats.
     *
     * @return liste des plats.
     */
    List<Plat> tousLesPlats();

    /**
     * Recherche un plat par son identifiant technique.
     *
     * @param id identifiant du plat.
     * @return plat trouve, vide sinon.
     */
    Optional<Plat> platParId(Long id);
}
