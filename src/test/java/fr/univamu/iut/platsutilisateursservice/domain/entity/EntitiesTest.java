package fr.univamu.iut.platsutilisateursservice.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntitiesTest {

    @Test
    void platConstructeurEtAccesseursFonctionnent() {
        Plat plat = new Plat(1L, "Aioli", "Poisson", new BigDecimal("13.50"));

        assertEquals(1L, plat.getId());
        assertEquals("Aioli", plat.getNom());
        assertEquals("Poisson", plat.getDescription());
        assertEquals(new BigDecimal("13.50"), plat.getPrix());

        plat.setId(2L);
        plat.setNom("Gratin");
        plat.setDescription("Pommes de terre");
        plat.setPrix(new BigDecimal("11.90"));

        assertEquals(2L, plat.getId());
        assertEquals("Gratin", plat.getNom());
        assertEquals("Pommes de terre", plat.getDescription());
        assertEquals(new BigDecimal("11.90"), plat.getPrix());
    }

    @Test
    void platConstructeurVideLaisseEtatNullParDefaut() {
        Plat plat = new Plat();

        assertNull(plat.getId());
        assertNull(plat.getNom());
        assertNull(plat.getDescription());
        assertNull(plat.getPrix());
    }

    @Test
    void utilisateurConstructeurEtAccesseursFonctionnent() {
        Utilisateur utilisateur = new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille");

        assertEquals(1L, utilisateur.getId());
        assertEquals("Dupont", utilisateur.getNom());
        assertEquals("Alice", utilisateur.getPrenom());
        assertEquals("alice@example.com", utilisateur.getEmail());
        assertEquals("Marseille", utilisateur.getAdresse());

        utilisateur.setId(2L);
        utilisateur.setNom("Martin");
        utilisateur.setPrenom("Bob");
        utilisateur.setEmail("bob@example.com");
        utilisateur.setAdresse("Aix-en-Provence");

        assertEquals(2L, utilisateur.getId());
        assertEquals("Martin", utilisateur.getNom());
        assertEquals("Bob", utilisateur.getPrenom());
        assertEquals("bob@example.com", utilisateur.getEmail());
        assertEquals("Aix-en-Provence", utilisateur.getAdresse());
    }

    @Test
    void utilisateurConstructeurVideLaisseEtatNullParDefaut() {
        Utilisateur utilisateur = new Utilisateur();

        assertNull(utilisateur.getId());
        assertNull(utilisateur.getNom());
        assertNull(utilisateur.getPrenom());
        assertNull(utilisateur.getEmail());
        assertNull(utilisateur.getAdresse());
    }
}
