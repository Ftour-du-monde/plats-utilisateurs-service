package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

import java.math.BigDecimal;

/**
 * DTO entrant pour la creation ou modification d'un plat.
 */
public class PlatRequest {
    private String nom;
    private String description;
    private BigDecimal prix;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public PlatRequest() {
    }

    /**
     * Retourne le nom du plat.
     *
     * @return nom du plat.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Definit le nom du plat.
     *
     * @param nom nom du plat.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la description du plat.
     *
     * @return description optionnelle.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Definit la description du plat.
     *
     * @param description description optionnelle.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne le prix unitaire.
     *
     * @return prix du plat.
     */
    public BigDecimal getPrix() {
        return prix;
    }

    /**
     * Definit le prix unitaire.
     *
     * @param prix prix du plat.
     */
    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
}
