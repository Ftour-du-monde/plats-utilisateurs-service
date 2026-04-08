package fr.univamu.iut.platsutilisateursservice.domain.entity;

/**
 * Entite metier representant un utilisateur abonne.
 */
public class Utilisateur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public Utilisateur() {
    }

    /**
     * Construit un utilisateur.
     *
     * @param id identifiant technique.
     * @param nom nom de famille.
     * @param prenom prenom.
     * @param email email normalise.
     * @param adresse adresse optionnelle.
     */
    public Utilisateur(Long id, String nom, String prenom, String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }

    /**
     * Retourne l'identifiant technique.
     *
     * @return identifiant utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Definit l'identifiant technique.
     *
     * @param id identifiant utilisateur.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne le nom de famille.
     *
     * @return nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Definit le nom de famille.
     *
     * @param nom nom de l'utilisateur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prenom.
     *
     * @return prenom de l'utilisateur.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Definit le prenom.
     *
     * @param prenom prenom de l'utilisateur.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne l'email.
     *
     * @return email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Definit l'email.
     *
     * @param email email de l'utilisateur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne l'adresse.
     *
     * @return adresse optionnelle.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Definit l'adresse.
     *
     * @param adresse adresse optionnelle.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
