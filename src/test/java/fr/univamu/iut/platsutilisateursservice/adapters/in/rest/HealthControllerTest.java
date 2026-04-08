package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.HealthResponse;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthControllerTest {

    @Test
    void retourneStatusUpEtTimestampUtc() {
        HealthController controller = new HealthController();

        Response response = controller.health();

        assertEquals(200, response.getStatus());
        HealthResponse entity = (HealthResponse) response.getEntity();
        assertEquals("UP", entity.getStatus());
        assertNotNull(OffsetDateTime.parse(entity.getTimestamp()));
    }
}
