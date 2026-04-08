package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

/**
 * DTO standardise pour les erreurs REST.
 */
public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String timestamp;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public ApiErrorResponse() {
    }

    /**
     * Construit une erreur API complete.
     *
     * @param status code HTTP numerique.
     * @param error libelle HTTP.
     * @param message message fonctionnel.
     * @param path chemin de la requete.
     * @param timestamp date ISO-8601 UTC.
     */
    public ApiErrorResponse(int status, String error, String message, String path, String timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    /**
     * Retourne le code HTTP numerique.
     *
     * @return code HTTP.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Definit le code HTTP numerique.
     *
     * @param status code HTTP.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Retourne le libelle HTTP.
     *
     * @return libelle HTTP.
     */
    public String getError() {
        return error;
    }

    /**
     * Definit le libelle HTTP.
     *
     * @param error libelle HTTP.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Retourne le message fonctionnel.
     *
     * @return message fonctionnel.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Definit le message fonctionnel.
     *
     * @param message message fonctionnel.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retourne le chemin de la requete.
     *
     * @return chemin de requete.
     */
    public String getPath() {
        return path;
    }

    /**
     * Definit le chemin de la requete.
     *
     * @param path chemin de requete.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Retourne l'horodatage ISO-8601 UTC.
     *
     * @return horodatage.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Definit l'horodatage ISO-8601 UTC.
     *
     * @param timestamp horodatage.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
