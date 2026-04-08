package fr.univamu.iut.platsutilisateursservice.application.port.out;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;

import java.util.List;
import java.util.Optional;

/**
 * Port de sortie pour la persistence des plats.
 */
public interface PlatGateway {
    /**
     * Retourne tous les plats.
     *
     * @return liste des plats.
     */
    List<Plat> findAll();

    /**
     * Recherche un plat par identifiant.
     *
     * @param id identifiant technique.
     * @return plat trouve, vide sinon.
     */
    Optional<Plat> findById(Long id);

    /**
     * Cree un plat.
     *
     * @param plat entite a persister.
     * @return entite creee avec identifiant.
     */
    Plat save(Plat plat);

    /**
     * Modifie un plat.
     *
     * @param id identifiant cible.
     * @param plat donnees a ecrire.
     * @return entite modifiee, vide si introuvable.
     */
    Optional<Plat> update(Long id, Plat plat);

    /**
     * Supprime un plat.
     *
     * @param id identifiant cible.
     * @return vrai si suppression effectuee.
     */
    boolean deleteById(Long id);
}
