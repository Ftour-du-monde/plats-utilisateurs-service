package fr.univamu.iut.platsutilisateursservice.application.port.out;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;

import java.util.List;
import java.util.Optional;

/**
 * Port de sortie pour la persistence des utilisateurs.
 */
public interface UtilisateurGateway {
    /**
     * Retourne tous les utilisateurs.
     *
     * @return liste des utilisateurs.
     */
    List<Utilisateur> findAll();

    /**
     * Recherche un utilisateur par identifiant.
     *
     * @param id identifiant technique.
     * @return utilisateur trouve, vide sinon.
     */
    Optional<Utilisateur> findById(Long id);

    /**
     * Cree un utilisateur.
     *
     * @param utilisateur entite a persister.
     * @return entite creee avec identifiant.
     */
    Utilisateur save(Utilisateur utilisateur);

    /**
     * Modifie un utilisateur.
     *
     * @param id identifiant cible.
     * @param utilisateur donnees a ecrire.
     * @return entite modifiee, vide si introuvable.
     */
    Optional<Utilisateur> update(Long id, Utilisateur utilisateur);

    /**
     * Supprime un utilisateur.
     *
     * @param id identifiant cible.
     * @return vrai si suppression effectuee.
     */
    boolean deleteById(Long id);
}
