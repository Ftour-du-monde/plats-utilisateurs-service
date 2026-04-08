package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.HealthResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Endpoint de sante pour verifier la disponibilite du service.
 */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    /**
     * Constructeur par defaut requis par CDI/JAX-RS.
     */
    public HealthController() {
    }

    /**
     * Retourne l'etat de disponibilite du service.
     *
     * @return reponse HTTP 200 avec statut logique {@code UP}.
     */
    @GET
    public Response health() {
        HealthResponse response = new HealthResponse("UP", OffsetDateTime.now(ZoneOffset.UTC).toString());
        return Response.ok(response).build();
    }
}
