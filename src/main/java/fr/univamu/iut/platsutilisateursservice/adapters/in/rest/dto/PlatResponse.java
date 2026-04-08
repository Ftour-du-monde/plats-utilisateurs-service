package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

import java.math.BigDecimal;

/**
 * DTO sortant representant un plat expose par l'API.
 */
public class PlatResponse {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public PlatResponse() {
    }

    /**
     * Construit une representation API de plat.
     *
     * @param id identifiant technique.
     * @param nom nom du plat.
     * @param description description optionnelle.
     * @param prix prix unitaire.
     */
    public PlatResponse(Long id, String nom, String description, BigDecimal prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    /**
     * Retourne l'identifiant technique.
     *
     * @return identifiant du plat.
     */
    public Long getId() {
        return id;
    }

    /**
     * Definit l'identifiant technique.
     *
     * @param id identifiant du plat.
     */
    public void setId(Long id) {
        this.id = id;
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
