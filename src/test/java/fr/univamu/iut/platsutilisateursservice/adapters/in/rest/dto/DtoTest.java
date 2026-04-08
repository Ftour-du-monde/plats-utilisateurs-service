package fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DtoTest {

    @Test
    void platRequestExposeSesAccesseurs() {
        PlatRequest request = new PlatRequest();
        request.setNom("Aioli");
        request.setDescription("Poisson");
        request.setPrix(new BigDecimal("13.50"));

        assertEquals("Aioli", request.getNom());
        assertEquals("Poisson", request.getDescription());
        assertEquals(new BigDecimal("13.50"), request.getPrix());
    }

    @Test
    void platResponseExposeConstructeurEtAccesseurs() {
        PlatResponse response = new PlatResponse(1L, "Aioli", "Poisson", new BigDecimal("13.50"));

        assertEquals(1L, response.getId());
        assertEquals("Aioli", response.getNom());
        assertEquals("Poisson", response.getDescription());
        assertEquals(new BigDecimal("13.50"), response.getPrix());

        response.setId(2L);
        response.setNom("Gratin");
        response.setDescription("Pommes de terre");
        response.setPrix(new BigDecimal("11.90"));

        assertEquals(2L, response.getId());
        assertEquals("Gratin", response.getNom());
        assertEquals("Pommes de terre", response.getDescription());
        assertEquals(new BigDecimal("11.90"), response.getPrix());
    }

    @Test
    void utilisateurRequestExposeSesAccesseurs() {
        UtilisateurRequest request = new UtilisateurRequest();
        request.setNom("Dupont");
        request.setPrenom("Alice");
        request.setEmail("alice@example.com");
        request.setAdresse("Marseille");

        assertEquals("Dupont", request.getNom());
        assertEquals("Alice", request.getPrenom());
        assertEquals("alice@example.com", request.getEmail());
        assertEquals("Marseille", request.getAdresse());
    }

    @Test
    void utilisateurResponseExposeConstructeurEtAccesseurs() {
        UtilisateurResponse response = new UtilisateurResponse(1L, "Dupont", "Alice", "alice@example.com", "Marseille");

        assertEquals(1L, response.getId());
        assertEquals("Dupont", response.getNom());
        assertEquals("Alice", response.getPrenom());
        assertEquals("alice@example.com", response.getEmail());
        assertEquals("Marseille", response.getAdresse());

        response.setId(2L);
        response.setNom("Martin");
        response.setPrenom("Bob");
        response.setEmail("bob@example.com");
        response.setAdresse("Aix-en-Provence");

        assertEquals(2L, response.getId());
        assertEquals("Martin", response.getNom());
        assertEquals("Bob", response.getPrenom());
        assertEquals("bob@example.com", response.getEmail());
        assertEquals("Aix-en-Provence", response.getAdresse());
    }

    @Test
    void apiErrorResponseExposeConstructeurEtAccesseurs() {
        ApiErrorResponse error = new ApiErrorResponse(400, "Bad Request", "message", "/api/plats", "2026-04-08T10:15:30Z");

        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertEquals("message", error.getMessage());
        assertEquals("/api/plats", error.getPath());
        assertEquals("2026-04-08T10:15:30Z", error.getTimestamp());

        error.setStatus(404);
        error.setError("Not Found");
        error.setMessage("introuvable");
        error.setPath("/api/plats/999");
        error.setTimestamp("2026-04-08T10:15:31Z");

        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("introuvable", error.getMessage());
        assertEquals("/api/plats/999", error.getPath());
        assertEquals("2026-04-08T10:15:31Z", error.getTimestamp());
    }

    @Test
    void healthResponseExposeConstructeurEtAccesseurs() {
        HealthResponse health = new HealthResponse("UP", "2026-04-08T10:15:30Z");

        assertEquals("UP", health.getStatus());
        assertEquals("2026-04-08T10:15:30Z", health.getTimestamp());

        health.setStatus("DOWN");
        health.setTimestamp("2026-04-08T10:16:00Z");

        assertEquals("DOWN", health.getStatus());
        assertEquals("2026-04-08T10:16:00Z", health.getTimestamp());
    }

    @Test
    void constructeursVidesSontDisponibles() {
        assertNull(new PlatResponse().getId());
        assertNull(new UtilisateurResponse().getId());
        assertNull(new ApiErrorResponse().getError());
        assertNull(new HealthResponse().getStatus());
    }
}
