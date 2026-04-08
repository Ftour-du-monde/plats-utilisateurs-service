package fr.univamu.iut.platsutilisateursservice.application.port.in;

import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;

import java.util.List;
import java.util.Optional;

/**
 * Port d'entree pour les cas d'usage de consultation des utilisateurs.
 */
public interface ConsulterUtilisateursUseCase {
    /**
     * Retourne la liste complete des utilisateurs.
     *
     * @return liste des utilisateurs.
     */
    List<Utilisateur> tousLesUtilisateurs();

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant utilisateur.
     * @return utilisateur trouve, vide sinon.
     */
    Optional<Utilisateur> utilisateurParId(Long id);
}
