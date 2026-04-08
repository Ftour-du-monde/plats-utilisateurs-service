package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Active CORS pour permettre l'integration de l'IHM PHP.
 */
@Provider
@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String DEFAULT_ALLOWED_ORIGINS = "*";
    private static final String DEFAULT_ALLOWED_HEADERS = "Origin, Content-Type, Accept, Authorization";
    private static final String DEFAULT_ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";

    private final String allowedOrigins = read(
            "PLATS_USERS_CORS_ALLOWED_ORIGINS",
            "plats.users.cors.allowed.origins",
            DEFAULT_ALLOWED_ORIGINS
    );
    private final String allowedHeaders = read(
            "PLATS_USERS_CORS_ALLOWED_HEADERS",
            "plats.users.cors.allowed.headers",
            DEFAULT_ALLOWED_HEADERS
    );
    private final String allowedMethods = read(
            "PLATS_USERS_CORS_ALLOWED_METHODS",
            "plats.users.cors.allowed.methods",
            DEFAULT_ALLOWED_METHODS
    );

    /**
     * Constructeur par defaut requis par CDI/JAX-RS.
     */
    public CorsFilter() {
    }

    /**
     * Gere les requetes preflight.
     *
     * @param requestContext contexte HTTP entrant.
     * @throws IOException en cas d'erreur de filtrage.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            requestContext.abortWith(Response.ok().build());
        }
    }

    /**
     * Ajoute les en-tetes CORS a chaque reponse.
     *
     * @param requestContext contexte HTTP entrant.
     * @param responseContext contexte HTTP sortant.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", allowedOrigins);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", allowedHeaders);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", allowedMethods);
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "3600");
        responseContext.getHeaders().putSingle("Access-Control-Expose-Headers", "Location");
        responseContext.getHeaders().putSingle("Vary", "Origin");
    }

    private String read(String envKey, String propertyKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String propertyValue = System.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        return defaultValue;
    }
}
