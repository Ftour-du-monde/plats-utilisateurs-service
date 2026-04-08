package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.ApiErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiResponsesTest {

    @Test
    void badRequestConstruitUneErreur400Normalisee() {
        UriInfo uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/plats");

        Response response = ApiResponses.badRequest("Requete invalide", uriInfo);

        assertEquals(400, response.getStatus());
        ApiErrorResponse entity = (ApiErrorResponse) response.getEntity();
        assertEquals(400, entity.getStatus());
        assertEquals("Bad Request", entity.getError());
        assertEquals("Requete invalide", entity.getMessage());
        assertEquals("/api/plats", entity.getPath());
        assertNotNull(OffsetDateTime.parse(entity.getTimestamp()));
    }

    @Test
    void notFoundConstruitUneErreur404Normalisee() {
        UriInfo uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/plats/99");

        Response response = ApiResponses.notFound("Introuvable", uriInfo);

        assertEquals(404, response.getStatus());
        ApiErrorResponse entity = (ApiErrorResponse) response.getEntity();
        assertEquals("Not Found", entity.getError());
        assertEquals("Introuvable", entity.getMessage());
        assertEquals("/api/plats/99", entity.getPath());
    }

    @Test
    void conflictConstruitUneErreur409Normalisee() {
        UriInfo uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/utilisateurs");

        Response response = ApiResponses.conflict("Conflit email", uriInfo);

        assertEquals(409, response.getStatus());
        ApiErrorResponse entity = (ApiErrorResponse) response.getEntity();
        assertEquals(409, entity.getStatus());
        assertEquals("Conflict", entity.getError());
        assertEquals("Conflit email", entity.getMessage());
    }

    @Test
    void internalServerErrorExposeMessageGenerique() {
        UriInfo uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/plats");

        Response response = ApiResponses.internalServerError(uriInfo);

        assertEquals(500, response.getStatus());
        ApiErrorResponse entity = (ApiErrorResponse) response.getEntity();
        assertEquals("Internal Server Error", entity.getError());
        assertEquals("Erreur interne du service.", entity.getMessage());
    }

    @Test
    void erreurAccepteUriInfoNull() {
        Response response = ApiResponses.badRequest("Requete invalide", null);

        ApiErrorResponse entity = (ApiErrorResponse) response.getEntity();
        assertNull(entity.getPath());
    }
}
