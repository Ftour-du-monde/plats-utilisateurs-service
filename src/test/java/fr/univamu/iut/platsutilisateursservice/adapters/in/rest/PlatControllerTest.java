package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.ApiErrorResponse;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.PlatRequest;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.PlatResponse;
import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.in.GererPlatsUseCase;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatControllerTest {

    @Test
    void getAllRetourne200EtListeMappee() {
        FakeConsulterPlatsUseCase consulter = new FakeConsulterPlatsUseCase();
        consulter.all = List.of(new Plat(1L, "Aioli", "Poisson", new BigDecimal("13.50")));
        PlatController controller = newController(consulter, new FakeGererPlatsUseCase());

        Response response = controller.getAll();

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<PlatResponse> payload = (List<PlatResponse>) response.getEntity();
        assertEquals(1, payload.size());
        assertEquals("Aioli", payload.get(0).getNom());
    }

    @Test
    void getAllRetourne500SiErreurTechnique() {
        FakeConsulterPlatsUseCase consulter = new FakeConsulterPlatsUseCase();
        consulter.allException = new IllegalStateException("db down");
        PlatController controller = newController(consulter, new FakeGererPlatsUseCase());

        Response response = controller.getAll();

        assertEquals(500, response.getStatus());
    }

    @Test
    void getByIdRetourne404SiPlatAbsent() {
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), new FakeGererPlatsUseCase());

        Response response = controller.getById(999L);

        assertEquals(404, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Plat introuvable.", payload.getMessage());
    }

    @Test
    void getByIdRetourne400SiIdInvalide() {
        FakeConsulterPlatsUseCase consulter = new FakeConsulterPlatsUseCase();
        consulter.byIdException = new IllegalArgumentException("L'identifiant du plat est invalide.");
        PlatController controller = newController(consulter, new FakeGererPlatsUseCase());

        Response response = controller.getById(0L);

        assertEquals(400, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("L'identifiant du plat est invalide.", payload.getMessage());
    }

    @Test
    void createRetourne400SiCorpsAbsent() {
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), new FakeGererPlatsUseCase());

        Response response = controller.create(null);

        assertEquals(400, response.getStatus());
    }

    @Test
    void createRetourne201LocationEtPayload() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.createResult = new Plat(7L, "Gratin", "Pommes de terre", new BigDecimal("11.90"));
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);
        PlatRequest request = new PlatRequest();
        request.setNom("Gratin");
        request.setDescription("Pommes de terre");
        request.setPrix(new BigDecimal("11.90"));

        Response response = controller.create(request);

        assertEquals(201, response.getStatus());
        assertEquals(URI.create("http://localhost:8080/api/plats/7"), response.getLocation());
        PlatResponse payload = (PlatResponse) response.getEntity();
        assertEquals(7L, payload.getId());
        assertEquals("Gratin", payload.getNom());
    }

    @Test
    void createRetourne400SurErreurFonctionnelle() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.createException = new IllegalArgumentException("Le champ 'nom' est obligatoire.");
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);
        PlatRequest request = new PlatRequest();

        Response response = controller.create(request);

        assertEquals(400, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Le champ 'nom' est obligatoire.", payload.getMessage());
    }

    @Test
    void updateRetourne404SiPlatAbsent() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.updateResult = Optional.empty();
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);
        PlatRequest request = new PlatRequest();
        request.setNom("Aioli");
        request.setPrix(new BigDecimal("13.50"));

        Response response = controller.update(12L, request);

        assertEquals(404, response.getStatus());
    }

    @Test
    void deleteRetourne204SiSuppressionEffectuee() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.deleteResult = true;
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);

        Response response = controller.delete(4L);

        assertEquals(204, response.getStatus());
        assertEquals(4L, gerer.lastDeleteId);
    }

    @Test
    void deleteRetourne404SiPlatAbsent() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.deleteResult = false;
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);

        Response response = controller.delete(4L);

        assertEquals(404, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Plat introuvable.", payload.getMessage());
    }

    @Test
    void updateRetourne200SiModificationEffectuee() {
        FakeGererPlatsUseCase gerer = new FakeGererPlatsUseCase();
        gerer.updateResult = Optional.of(new Plat(3L, "Salade", "Fraiche", new BigDecimal("9.90")));
        PlatController controller = newController(new FakeConsulterPlatsUseCase(), gerer);
        PlatRequest request = new PlatRequest();
        request.setNom("Salade");
        request.setDescription("Fraiche");
        request.setPrix(new BigDecimal("9.90"));

        Response response = controller.update(3L, request);

        assertEquals(200, response.getStatus());
        PlatResponse payload = (PlatResponse) response.getEntity();
        assertEquals(3L, payload.getId());
        assertEquals("Salade", payload.getNom());
        assertTrue(gerer.lastUpdateId == 3L);
    }

    private PlatController newController(FakeConsulterPlatsUseCase consulter, FakeGererPlatsUseCase gerer) {
        PlatController controller = new PlatController();
        controller.consulterPlatsUseCase = consulter;
        controller.gererPlatsUseCase = gerer;
        controller.uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/plats");
        return controller;
    }

    private static class FakeConsulterPlatsUseCase implements ConsulterPlatsUseCase {
        private List<Plat> all = List.of();
        private Optional<Plat> byId = Optional.empty();
        private RuntimeException allException;
        private RuntimeException byIdException;

        @Override
        public List<Plat> tousLesPlats() {
            if (allException != null) {
                throw allException;
            }
            return all;
        }

        @Override
        public Optional<Plat> platParId(Long id) {
            if (byIdException != null) {
                throw byIdException;
            }
            return byId;
        }
    }

    private static class FakeGererPlatsUseCase implements GererPlatsUseCase {
        private Plat createResult = new Plat(1L, "Aioli", "Poisson", new BigDecimal("13.50"));
        private Optional<Plat> updateResult = Optional.of(new Plat(1L, "Aioli", "Poisson", new BigDecimal("13.50")));
        private boolean deleteResult;
        private RuntimeException createException;
        private RuntimeException updateException;
        private RuntimeException deleteException;
        private Long lastDeleteId;
        private Long lastUpdateId;

        @Override
        public Plat creer(String nom, String description, BigDecimal prix) {
            if (createException != null) {
                throw createException;
            }
            return createResult;
        }

        @Override
        public Optional<Plat> modifier(Long id, String nom, String description, BigDecimal prix) {
            if (updateException != null) {
                throw updateException;
            }
            lastUpdateId = id;
            return updateResult;
        }

        @Override
        public boolean supprimer(Long id) {
            if (deleteException != null) {
                throw deleteException;
            }
            lastDeleteId = id;
            return deleteResult;
        }
    }
}
