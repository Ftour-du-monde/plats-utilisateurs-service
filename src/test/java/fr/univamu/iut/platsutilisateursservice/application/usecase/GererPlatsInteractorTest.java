package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.out.PlatGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GererPlatsInteractorTest {

    @Test
    void creerNormaliseNomDescriptionEtPrix() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        Plat created = interactor.creer("  Burger maison  ", "  boeuf et cheddar  ", new BigDecimal("12.50"));

        assertEquals("Burger maison", created.getNom());
        assertEquals("boeuf et cheddar", created.getDescription());
        assertEquals(new BigDecimal("12.50"), created.getPrix());
        assertEquals("Burger maison", gateway.lastSaved.getNom());
    }

    @Test
    void creerForceDeuxDecimalesSurPrixEntier() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        Plat created = interactor.creer("Aioli", "poisson", new BigDecimal("14"));

        assertEquals(new BigDecimal("14.00"), created.getPrix());
        assertEquals(2, created.getPrix().scale());
    }

    @Test
    void creerDescriptionVideDevientNull() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        Plat created = interactor.creer("Salade", "   ", new BigDecimal("9.90"));

        assertNull(created.getDescription());
    }

    @Test
    void creerRefuseNomNull() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer(null, "desc", new BigDecimal("9.99")));
    }

    @Test
    void creerRefuseNomVide() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("   ", "desc", new BigDecimal("9.99")));
    }

    @Test
    void creerRefuseNomTropLong() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);
        String nomTropLong = "x".repeat(151);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer(nomTropLong, "desc", new BigDecimal("9.99")));
    }

    @Test
    void creerRefuseDescriptionTropLongue() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);
        String descriptionTropLongue = "d".repeat(501);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Aioli", descriptionTropLongue, new BigDecimal("9.99")));
    }

    @Test
    void creerRefusePrixNull() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Aioli", "desc", null));
    }

    @Test
    void creerRefusePrixZero() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Aioli", "desc", BigDecimal.ZERO));
    }

    @Test
    void creerRefusePrixNegatif() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Aioli", "desc", new BigDecimal("-1.00")));
    }

    @Test
    void creerRefusePrixTropDeDecimales() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Salade", "desc", new BigDecimal("9.999")));
    }

    @Test
    void modifierMetAJourQuandIdValide() {
        FakePlatGateway gateway = new FakePlatGateway();
        gateway.updateResult = Optional.of(new Plat(2L, "Gratin", "pommes de terre", new BigDecimal("11.90")));
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        Optional<Plat> updated = interactor.modifier(2L, "  Gratin  ", "  pommes de terre  ", new BigDecimal("11.90"));

        assertTrue(updated.isPresent());
        assertEquals(2L, gateway.lastUpdatedId);
        assertEquals("Gratin", gateway.lastUpdatedPlat.getNom());
        assertEquals("pommes de terre", gateway.lastUpdatedPlat.getDescription());
    }

    @Test
    void modifierRetourneVideQuandGatewayNeTrouvePas() {
        FakePlatGateway gateway = new FakePlatGateway();
        gateway.updateResult = Optional.empty();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        Optional<Plat> updated = interactor.modifier(99L, "Gratin", "pommes", new BigDecimal("11.90"));

        assertTrue(updated.isEmpty());
    }

    @Test
    void modifierRefuseIdInvalide() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.modifier(0L, "Salade", "desc", new BigDecimal("9.99")));
    }

    @Test
    void supprimerRetourneResultatGateway() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        gateway.deleteResult = true;
        assertTrue(interactor.supprimer(4L));

        gateway.deleteResult = false;
        assertFalse(interactor.supprimer(4L));
        assertEquals(4L, gateway.lastDeletedId);
    }

    @Test
    void supprimerRefuseIdInvalide() {
        FakePlatGateway gateway = new FakePlatGateway();
        GererPlatsInteractor interactor = new GererPlatsInteractor(gateway);

        assertThrows(IllegalArgumentException.class, () -> interactor.supprimer(-10L));
    }

    private static class FakePlatGateway implements PlatGateway {
        private final List<Plat> plats = new ArrayList<>();
        private Plat lastSaved;
        private Long lastUpdatedId;
        private Plat lastUpdatedPlat;
        private Long lastDeletedId;
        private Optional<Plat> updateResult = Optional.of(new Plat(1L, "Aioli", "desc", new BigDecimal("12.50")));
        private boolean deleteResult;

        @Override
        public List<Plat> findAll() {
            return plats;
        }

        @Override
        public Optional<Plat> findById(Long id) {
            return plats.stream().filter(p -> p.getId().equals(id)).findFirst();
        }

        @Override
        public Plat save(Plat plat) {
            lastSaved = new Plat(1L, plat.getNom(), plat.getDescription(), plat.getPrix());
            plats.add(lastSaved);
            return lastSaved;
        }

        @Override
        public Optional<Plat> update(Long id, Plat plat) {
            lastUpdatedId = id;
            lastUpdatedPlat = plat;
            return updateResult;
        }

        @Override
        public boolean deleteById(Long id) {
            lastDeletedId = id;
            return deleteResult;
        }
    }
}
