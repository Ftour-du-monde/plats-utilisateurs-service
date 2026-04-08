package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

/**
 * Reponse de sante du service.
 */
public class HealthResponse {
    private String status;
    private String timestamp;

    /**
     * Constructeur vide requis par les frameworks de serialisation.
     */
    public HealthResponse() {
    }

    /**
     * Construit une reponse de sante.
     *
     * @param status statut logique du service.
     * @param timestamp horodatage ISO-8601 UTC.
     */
    public HealthResponse(String status, String timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    /**
     * Retourne l'etat logique du service.
     *
     * @return statut de sante.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Definit l'etat logique du service.
     *
     * @param status statut de sante.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retourne l'horodatage ISO-8601 UTC.
     *
     * @return horodatage de la reponse.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Definit l'horodatage ISO-8601 UTC.
     *
     * @param timestamp horodatage de la reponse.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
