package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

/**
 * DTO entrant pour la creation ou modification d'un utilisateur.
 */
public class UtilisateurRequest {
    private String nom;
    private String prenom;
    private String email;
    private String adresse;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public UtilisateurRequest() {
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
