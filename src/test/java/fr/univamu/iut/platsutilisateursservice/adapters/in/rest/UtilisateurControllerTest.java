package fr.univamu.iut.platsutilisateursservice.adapters.in.rest;

import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.ApiErrorResponse;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.UtilisateurRequest;
import fr.univamu.iut.platsutilisateursservice.adapters.in.rest.dto.UtilisateurResponse;
import fr.univamu.iut.platsutilisateursservice.application.port.in.ConsulterUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.application.port.in.GererUtilisateursUseCase;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import fr.univamu.iut.platsutilisateursservice.domain.exception.DuplicateEmailException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilisateurControllerTest {

    @Test
    void getAllRetourne200EtListeMappee() {
        FakeConsulterUtilisateursUseCase consulter = new FakeConsulterUtilisateursUseCase();
        consulter.all = List.of(new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille"));
        UtilisateurController controller = newController(consulter, new FakeGererUtilisateursUseCase());

        Response response = controller.getAll();

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<UtilisateurResponse> payload = (List<UtilisateurResponse>) response.getEntity();
        assertEquals(1, payload.size());
        assertEquals("alice@example.com", payload.get(0).getEmail());
    }

    @Test
    void getByIdRetourne404SiUtilisateurAbsent() {
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), new FakeGererUtilisateursUseCase());

        Response response = controller.getById(999L);

        assertEquals(404, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Utilisateur introuvable.", payload.getMessage());
    }

    @Test
    void createRetourne201LocationEtPayload() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.createResult = new Utilisateur(5L, "Martin", "Bob", "bob@example.com", "Aix");
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);
        UtilisateurRequest request = new UtilisateurRequest();
        request.setNom("Martin");
        request.setPrenom("Bob");
        request.setEmail("bob@example.com");
        request.setAdresse("Aix");

        Response response = controller.create(request);

        assertEquals(201, response.getStatus());
        assertEquals(URI.create("http://localhost:8080/api/utilisateurs/5"), response.getLocation());
        UtilisateurResponse payload = (UtilisateurResponse) response.getEntity();
        assertEquals("bob@example.com", payload.getEmail());
    }

    @Test
    void createRetourne409SiEmailDuplique() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.createException = new DuplicateEmailException("Un utilisateur avec cet email existe deja.");
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);
        UtilisateurRequest request = new UtilisateurRequest();

        Response response = controller.create(request);

        assertEquals(409, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Un utilisateur avec cet email existe deja.", payload.getMessage());
    }

    @Test
    void updateRetourne409SiEmailDuplique() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.updateException = new DuplicateEmailException("Un utilisateur avec cet email existe deja.");
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);
        UtilisateurRequest request = new UtilisateurRequest();

        Response response = controller.update(1L, request);

        assertEquals(409, response.getStatus());
    }

    @Test
    void updateRetourne404SiUtilisateurAbsent() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.updateResult = Optional.empty();
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);
        UtilisateurRequest request = new UtilisateurRequest();
        request.setNom("Martin");
        request.setPrenom("Bob");
        request.setEmail("bob@example.com");

        Response response = controller.update(45L, request);

        assertEquals(404, response.getStatus());
    }

    @Test
    void deleteRetourne204SiSuppressionEffectuee() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.deleteResult = true;
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);

        Response response = controller.delete(2L);

        assertEquals(204, response.getStatus());
        assertEquals(2L, gerer.lastDeleteId);
    }

    @Test
    void deleteRetourne404SiUtilisateurAbsent() {
        FakeGererUtilisateursUseCase gerer = new FakeGererUtilisateursUseCase();
        gerer.deleteResult = false;
        UtilisateurController controller = newController(new FakeConsulterUtilisateursUseCase(), gerer);

        Response response = controller.delete(2L);

        assertEquals(404, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("Utilisateur introuvable.", payload.getMessage());
    }

    @Test
    void getByIdRetourne400SiIdInvalide() {
        FakeConsulterUtilisateursUseCase consulter = new FakeConsulterUtilisateursUseCase();
        consulter.byIdException = new IllegalArgumentException("L'identifiant de l'utilisateur est invalide.");
        UtilisateurController controller = newController(consulter, new FakeGererUtilisateursUseCase());

        Response response = controller.getById(0L);

        assertEquals(400, response.getStatus());
        ApiErrorResponse payload = (ApiErrorResponse) response.getEntity();
        assertEquals("L'identifiant de l'utilisateur est invalide.", payload.getMessage());
    }

    private UtilisateurController newController(
            FakeConsulterUtilisateursUseCase consulter,
            FakeGererUtilisateursUseCase gerer
    ) {
        UtilisateurController controller = new UtilisateurController();
        controller.consulterUtilisateursUseCase = consulter;
        controller.gererUtilisateursUseCase = gerer;
        controller.uriInfo = RestTestSupport.uriInfo("http://localhost:8080/api/utilisateurs");
        return controller;
    }

    private static class FakeConsulterUtilisateursUseCase implements ConsulterUtilisateursUseCase {
        private List<Utilisateur> all = List.of();
        private Optional<Utilisateur> byId = Optional.empty();
        private RuntimeException byIdException;

        @Override
        public List<Utilisateur> tousLesUtilisateurs() {
            return all;
        }

        @Override
        public Optional<Utilisateur> utilisateurParId(Long id) {
            if (byIdException != null) {
                throw byIdException;
            }
            return byId;
        }
    }

    private static class FakeGererUtilisateursUseCase implements GererUtilisateursUseCase {
        private Utilisateur createResult = new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille");
        private Optional<Utilisateur> updateResult = Optional.of(createResult);
        private boolean deleteResult;
        private RuntimeException createException;
        private RuntimeException updateException;
        private Long lastDeleteId;

        @Override
        public Utilisateur creer(String nom, String prenom, String email, String adresse) {
            if (createException != null) {
                throw createException;
            }
            return createResult;
        }

        @Override
        public Optional<Utilisateur> modifier(Long id, String nom, String prenom, String email, String adresse) {
            if (updateException != null) {
                throw updateException;
            }
            return updateResult;
        }

        @Override
        public boolean supprimer(Long id) {
            lastDeleteId = id;
            return deleteResult;
        }
    }
}
