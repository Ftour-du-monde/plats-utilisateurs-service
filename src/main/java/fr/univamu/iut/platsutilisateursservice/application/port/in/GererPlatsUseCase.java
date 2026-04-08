package fr.univamu.iut.platsutilisateursservice.application.port.in;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Port d'entree pour les cas d'usage de gestion des plats.
 */
public interface GererPlatsUseCase {
    /**
     * Cree un plat.
     *
     * @param nom nom du plat.
     * @param description description du plat.
     * @param prix prix unitaire du plat.
     * @return plat cree avec son identifiant.
     */
    Plat creer(String nom, String description, BigDecimal prix);

    /**
     * Modifie un plat existant.
     *
     * @param id identifiant du plat.
     * @param nom nom du plat.
     * @param description description du plat.
     * @param prix prix unitaire du plat.
     * @return plat modifie, vide si introuvable.
     */
    Optional<Plat> modifier(Long id, String nom, String description, BigDecimal prix);

    /**
     * Supprime un plat.
     *
     * @param id identifiant du plat.
     * @return vrai si suppression effectuee.
     */
    boolean supprimer(Long id);
}
