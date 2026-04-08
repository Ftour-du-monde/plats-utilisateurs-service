package fr.univamu.iut.platsutilisateursservice.application.port.in;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;

import java.util.Optional;

/**
 * Port d'entree pour les cas d'usage de gestion des utilisateurs.
 */
public interface GererUtilisateursUseCase {
    /**
     * Cree un utilisateur.
     *
     * @param nom nom de famille.
     * @param prenom prenom.
     * @param email email unique.
     * @param adresse adresse de livraison.
     * @return utilisateur cree.
     */
    Utilisateur creer(String nom, String prenom, String email, String adresse);

    /**
     * Met a jour un utilisateur.
     *
     * @param id identifiant utilisateur.
     * @param nom nom de famille.
     * @param prenom prenom.
     * @param email email unique.
     * @param adresse adresse de livraison.
     * @return utilisateur modifie, vide si introuvable.
     */
    Optional<Utilisateur> modifier(Long id, String nom, String prenom, String email, String adresse);

    /**
     * Supprime un utilisateur.
     *
     * @param id identifiant utilisateur.
     * @return vrai si suppression effectuee.
     */
    boolean supprimer(Long id);
}
