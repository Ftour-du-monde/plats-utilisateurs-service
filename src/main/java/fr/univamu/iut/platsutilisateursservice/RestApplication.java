package fr.univamu.iut.platsutilisateursservice;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Point d'entree JAX-RS du microservice.
 *
 * <p>Toutes les ressources REST sont exposees sous le prefixe {@code /api}.</p>
 */
@ApplicationPath("/api")
public class RestApplication extends Application {

    /**
     * Constructeur par defaut du bootstrap JAX-RS.
     */
    public RestApplication() {
    }
}
