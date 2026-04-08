package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.UtilisateurRequest;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.UtilisateurResponse;
import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.in.GererUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import fr.univamu.iut.platsutilisateursservice.domain.exception.DuplicateEmailException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller REST pour le cycle de vie des utilisateurs.
 */
@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurController {

    @Inject
    ConsulterUtilisateursUseCase consulterUtilisateursUseCase;

    @Inject
    GererUtilisateursUseCase gererUtilisateursUseCase;

    @Context
    UriInfo uriInfo;

    /**
     * Constructeur par defaut requis par CDI/JAX-RS.
     */
    public UtilisateurController() {
    }

    /**
     * Liste l'ensemble des utilisateurs.
     *
     * @return reponse HTTP 200 avec collection JSON.
     */
    @GET
    public Response getAll() {
        try {
            List<UtilisateurResponse> utilisateurs = consulterUtilisateursUseCase.tousLesUtilisateurs()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(utilisateurs).build();
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Retourne un utilisateur par identifiant.
     *
     * @param id identifiant technique utilisateur.
     * @return HTTP 200 si trouve, 404 sinon, 400 si id invalide.
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            Optional<Utilisateur> utilisateur = consulterUtilisateursUseCase.utilisateurParId(id);
            if (utilisateur.isEmpty()) {
                return ApiResponses.notFound("Utilisateur introuvable.", uriInfo);
            }
            return Response.ok(toResponse(utilisateur.get())).build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Cree un utilisateur.
     *
     * @param request corps de requete JSON.
     * @return HTTP 201 avec entite creee et header {@code Location}.
     */
    @POST
    public Response create(UtilisateurRequest request) {
        if (request == null) {
            return ApiResponses.badRequest("Le corps de la requete est obligatoire.", uriInfo);
        }

        try {
            Utilisateur utilisateur = gererUtilisateursUseCase.creer(
                    request.getNom(),
                    request.getPrenom(),
                    request.getEmail(),
                    request.getAdresse()
            );
            UtilisateurResponse response = toResponse(utilisateur);
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(response.getId())).build();
            return Response.created(location).entity(response).build();
        } catch (DuplicateEmailException exception) {
            return ApiResponses.conflict(exception.getMessage(), uriInfo);
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Met a jour un utilisateur existant.
     *
     * @param id identifiant utilisateur.
     * @param request corps de requete JSON.
     * @return HTTP 200 si modifie, 404 si absent, 400 si donnees invalides.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UtilisateurRequest request) {
        if (request == null) {
            return ApiResponses.badRequest("Le corps de la requete est obligatoire.", uriInfo);
        }

        try {
            Optional<Utilisateur> utilisateur = gererUtilisateursUseCase.modifier(
                    id,
                    request.getNom(),
                    request.getPrenom(),
                    request.getEmail(),
                    request.getAdresse()
            );
            if (utilisateur.isEmpty()) {
                return ApiResponses.notFound("Utilisateur introuvable.", uriInfo);
            }
            return Response.ok(toResponse(utilisateur.get())).build();
        } catch (DuplicateEmailException exception) {
            return ApiResponses.conflict(exception.getMessage(), uriInfo);
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    /**
     * Supprime un utilisateur.
     *
     * @param id identifiant utilisateur.
     * @return HTTP 204 si supprime, 404 si absent, 400 si id invalide.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            if (!gererUtilisateursUseCase.supprimer(id)) {
                return ApiResponses.notFound("Utilisateur introuvable.", uriInfo);
            }
            return Response.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ApiResponses.badRequest(exception.getMessage(), uriInfo);
        } catch (IllegalStateException exception) {
            return ApiResponses.internalServerError(uriInfo);
        }
    }

    private UtilisateurResponse toResponse(Utilisateur utilisateur) {
        return new UtilisateurResponse(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getAdresse()
        );
    }
}
