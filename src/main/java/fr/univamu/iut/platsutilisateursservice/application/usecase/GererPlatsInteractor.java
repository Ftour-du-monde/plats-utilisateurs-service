package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.in.GererPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.out.PlatGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * Interactor de gestion des plats.
 */
@ApplicationScoped
public class GererPlatsInteractor implements GererPlatsUseCase {

    private static final int NOM_MAX = 150;
    private static final int DESCRIPTION_MAX = 500;

    private final PlatGateway platGateway;

    /**
     * Cree l'interactor de gestion des plats.
     *
     * @param platGateway port de persistence des plats.
     */
    @Inject
    public GererPlatsInteractor(PlatGateway platGateway) {
        this.platGateway = platGateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plat creer(String nom, String description, BigDecimal prix) {
        String nomNormalise = normalizeNom(nom);
        String descriptionNormalisee = normalizeDescription(description);
        BigDecimal prixNormalise = normalizePrix(prix);
        Plat plat = new Plat(null, nomNormalise, descriptionNormalisee, prixNormalise);
        return platGateway.save(plat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Plat> modifier(Long id, String nom, String description, BigDecimal prix) {
        validateId(id);
        String nomNormalise = normalizeNom(nom);
        String descriptionNormalisee = normalizeDescription(description);
        BigDecimal prixNormalise = normalizePrix(prix);
        Plat plat = new Plat(id, nomNormalise, descriptionNormalisee, prixNormalise);
        return platGateway.update(id, plat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supprimer(Long id) {
        validateId(id);
        return platGateway.deleteById(id);
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

    private String normalizeDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }
        String descriptionNormalisee = description.trim();
        if (descriptionNormalisee.length() > DESCRIPTION_MAX) {
            throw new IllegalArgumentException("Le champ 'description' depasse la taille maximale autorisee.");
        }
        return descriptionNormalisee;
    }

    private BigDecimal normalizePrix(BigDecimal prix) {
        if (prix == null) {
            throw new IllegalArgumentException("Le champ 'prix' est obligatoire.");
        }
        if (prix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le champ 'prix' doit etre strictement positif.");
        }
        if (prix.scale() > 2) {
            throw new IllegalArgumentException("Le champ 'prix' ne peut pas avoir plus de 2 decimales.");
        }
        return prix.setScale(2, RoundingMode.UNNECESSARY);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant du plat est invalide.");
        }
    }
}
