package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CorsFilterTest {

    @Test
    void preflightOptionsEstInterceptee() throws IOException {
        CorsFilter filter = new CorsFilter();
        AtomicReference<Response> aborted = new AtomicReference<>();
        ContainerRequestContext requestContext = RestTestSupport.requestContext("OPTIONS", aborted);

        filter.filter(requestContext);

        assertNotNull(aborted.get());
        assertEquals(200, aborted.get().getStatus());
    }

    @Test
    void requeteNonOptionsNEstPasInterceptee() throws IOException {
        CorsFilter filter = new CorsFilter();
        AtomicReference<Response> aborted = new AtomicReference<>();
        ContainerRequestContext requestContext = RestTestSupport.requestContext("GET", aborted);

        filter.filter(requestContext);

        assertNull(aborted.get());
    }

    @Test
    void ajouteLesEntetesCorsSurChaqueReponse() {
        CorsFilter filter = new CorsFilter();
        AtomicReference<Response> aborted = new AtomicReference<>();
        ContainerRequestContext requestContext = RestTestSupport.requestContext("GET", aborted);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        ContainerResponseContext responseContext = RestTestSupport.responseContext(headers);

        filter.filter(requestContext, responseContext);

        assertEquals("3600", headers.getFirst("Access-Control-Max-Age"));
        assertEquals("Location", headers.getFirst("Access-Control-Expose-Headers"));
        assertEquals("Origin", headers.getFirst("Vary"));
        assertNotNull(headers.getFirst("Access-Control-Allow-Origin"));
        assertNotNull(headers.getFirst("Access-Control-Allow-Headers"));
        assertNotNull(headers.getFirst("Access-Control-Allow-Methods"));
    }
}
