package fr.univamu.iut.platsutilisateursservice;

import jakarta.ws.rs.ApplicationPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestApplicationTest {

    @Test
    void declarePrefixeApi() {
        ApplicationPath annotation = RestApplication.class.getAnnotation(ApplicationPath.class);

        assertNotNull(annotation);
        assertEquals("/api", annotation.value());
    }
}
