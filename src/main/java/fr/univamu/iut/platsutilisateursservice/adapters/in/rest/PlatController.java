package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.PlatRequest;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.PlatResponse;
import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.in.GererPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller REST pour le cycle de vie des plats.
 */
@Path("/plats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlatController {

    @Inject
    ConsulterPlatsUseCase consulterPlatsUseCase;

    @Inject
    GererPlatsUseCase gererPlatsUseCase;

    @Context
    UriInfo uriInfo;

    /**
     * Constructeur par defaut requis par CDI/JAX-RS.
     */
    public PlatController() {
    }

    /**
     * Liste l'ensemble des plats.
     *
     * @return reponse HTTP 200 avec collection JSON.
     */
    @GET
    public Response getAll() {
        try {
            List<PlatResponse> plats = consulterPlatsUseCase.tousLesPlats()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(plats).build();
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Retourne un plat par identifiant.
     *
     * @param id identifiant technique du plat.
     * @return HTTP 200 si trouve, 404 sinon, 400 si id invalide.
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            Optional<Plat> plat = consulterPlatsUseCase.platParId(id);
            if (plat.isEmpty()) {
                return ApiResponses.notFound("Plat introuvable.", uriInfo);
            }
            return Response.ok(toResponse(plat.get())).build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Cree un plat.
     *
     * @param request corps de requete JSON.
     * @return HTTP 201 avec entite creee et header {@code Location}.
     */
    @POST
    public Response create(PlatRequest request) {
        if (request == null) {
            return ApiResponses.badRequest("Le corps de la requete est obligatoire.", uriInfo);
        }

        try {
            Plat plat = gererPlatsUseCase.creer(request.getNom(), request.getDescription(), request.getPrix());
            PlatResponse response = toResponse(plat);
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(response.getId())).build();
            return Response.created(location).entity(response).build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Met a jour un plat existant.
     *
     * @param id identifiant du plat.
     * @param request corps de requete JSON.
     * @return HTTP 200 si modifie, 404 si absent, 400 si donnees invalides.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, PlatRequest request) {
        if (request == null) {
            return ApiResponses.badRequest("Le corps de la requete est obligatoire.", uriInfo);
        }

        try {
            Optional<Plat> plat = gererPlatsUseCase.modifier(id, request.getNom(), request.getDescription(), request.getPrix());
            if (plat.isEmpty()) {
                return ApiResponses.notFound("Plat introuvable.", uriInfo);
            }
            return Response.ok(toResponse(plat.get())).build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Supprime un plat.
     *
     * @param id identifiant du plat.
     * @return HTTP 204 si supprime, 404 si absent, 400 si id invalide.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            if (!gererPlatsUseCase.supprimer(id)) {
                return ApiResponses.notFound("Plat introuvable.", uriInfo);
            }
            return Response.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    private PlatResponse toResponse(Plat plat) {
        return new PlatResponse(plat.getId(), plat.getNom(), plat.getDescription(), plat.getPrix());
    }
}
