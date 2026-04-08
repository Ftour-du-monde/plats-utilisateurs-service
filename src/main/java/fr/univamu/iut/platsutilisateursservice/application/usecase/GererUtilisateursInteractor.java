package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.in.GererUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.out.UtilisateurGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Interactor de gestion des utilisateurs.
 */
@ApplicationScoped
public class GererUtilisateursInteractor implements GererUtilisateursUseCase {

    private static final int NOM_MAX = 120;
    private static final int PRENOM_MAX = 120;
    private static final int EMAIL_MAX = 255;
    private static final int ADRESSE_MAX = 255;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UtilisateurGateway utilisateurGateway;

    /**
     * Cree l'interactor de gestion des utilisateurs.
     *
     * @param utilisateurGateway port de persistence des utilisateurs.
     */
    @Inject
    public GererUtilisateursInteractor(UtilisateurGateway utilisateurGateway) {
        this.utilisateurGateway = utilisateurGateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utilisateur creer(String nom, String prenom, String email, String adresse) {
        String nomNormalise = normalizeNom(nom);
        String prenomNormalise = normalizePrenom(prenom);
        String emailNormalise = normalizeEmail(email);
        String adresseNormalisee = normalizeAdresse(adresse);

        Utilisateur utilisateur = new Utilisateur(
                null,
                nomNormalise,
                prenomNormalise,
                emailNormalise,
                adresseNormalisee
        );
        return utilisateurGateway.save(utilisateur);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Utilisateur> modifier(Long id, String nom, String prenom, String email, String adresse) {
        validateId(id);
        String nomNormalise = normalizeNom(nom);
        String prenomNormalise = normalizePrenom(prenom);
        String emailNormalise = normalizeEmail(email);
        String adresseNormalisee = normalizeAdresse(adresse);

        Utilisateur utilisateur = new Utilisateur(
                id,
                nomNormalise,
                prenomNormalise,
                emailNormalise,
                adresseNormalisee
        );
        return utilisateurGateway.update(id, utilisateur);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supprimer(Long id) {
        validateId(id);
        return utilisateurGateway.deleteById(id);
    }

    private String normalizeNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le champ 'nom' est obligatoire.");
        }
        String nomNormalise = nom.trim();
        if (nomNormalise.length() > NOM_MAX) {
            throw new IllegalArgumentException("Le champ 'nom' depasse la taille maximale autorisee.");
        }
        return nomNormalise;
    }

    private String normalizePrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le champ 'prenom' est obligatoire.");
        }
        String prenomNormalise = prenom.trim();
        if (prenomNormalise.length() > PRENOM_MAX) {
            throw new IllegalArgumentException("Le champ 'prenom' depasse la taille maximale autorisee.");
        }
        return prenomNormalise;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Le champ 'email' est invalide.");
        }
        String emailNormalise = email.trim().toLowerCase(Locale.ROOT);
        if (emailNormalise.length() > EMAIL_MAX || !EMAIL_PATTERN.matcher(emailNormalise).matches()) {
            throw new IllegalArgumentException("Le champ 'email' est invalide.");
        }
        return emailNormalise;
    }

    private String normalizeAdresse(String adresse) {
        if (adresse == null || adresse.trim().isEmpty()) {
            return null;
        }
        String adresseNormalisee = adresse.trim();
        if (adresseNormalisee.length() > ADRESSE_MAX) {
            throw new IllegalArgumentException("Le champ 'adresse' depasse la taille maximale autorisee.");
        }
        return adresseNormalisee;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur est invalide.");
        }
    }
}
