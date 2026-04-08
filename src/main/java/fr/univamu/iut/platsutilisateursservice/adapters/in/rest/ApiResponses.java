package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.ApiErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Fabrique de reponses HTTP d'erreur uniformisees pour l'API REST.
 */
public final class ApiResponses {

    private ApiResponses() {
    }

    /**
     * Construit une reponse HTTP 400.
     *
     * @param message message fonctionnel a exposer au client.
     * @param uriInfo contexte URI courant.
     * @return reponse normalisee d'erreur.
     */
    public static Response badRequest(String message, UriInfo uriInfo) {
        return error(Response.Status.BAD_REQUEST, message, uriInfo);
    }

    /**
     * Construit une reponse HTTP 404.
     *
     * @param message message fonctionnel a exposer au client.
     * @param uriInfo contexte URI courant.
     * @return reponse normalisee d'erreur.
     */
    public static Response notFound(String message, UriInfo uriInfo) {
        return error(Response.Status.NOT_FOUND, message, uriInfo);
    }

    /**
     * Construit une reponse HTTP 409.
     *
     * @param message message fonctionnel a exposer au client.
     * @param uriInfo contexte URI courant.
     * @return reponse normalisee d'erreur.
     */
    public static Response conflict(String message, UriInfo uriInfo) {
        return error(Response.Status.CONFLICT, message, uriInfo);
    }

    /**
     * Construit une reponse HTTP 500 avec message generique.
     *
     * @param uriInfo contexte URI courant.
     * @return reponse normalisee d'erreur.
     */
    public static Response internalServerError(UriInfo uriInfo) {
        return error(Response.Status.INTERNAL_SERVER_ERROR, "Erreur interne du service.", uriInfo);
    }

    private static Response error(Response.Status status, String message, UriInfo uriInfo) {
        ApiErrorResponse response = new ApiErrorResponse(
                status.getStatusCode(),
                status.getReasonPhrase(),
                message,
                requestPath(uriInfo),
                OffsetDateTime.now(ZoneOffset.UTC).toString()
        );
        return Response.status(status).entity(response).build();
    }

    private static String requestPath(UriInfo uriInfo) {
        return uriInfo == null ? null : uriInfo.getRequestUri().getPath();
    }
}
