package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.out.UtilisateurGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GererUtilisateursInteractorTest {

    @Test
    void creerNormaliseNomPrenomEmailEtAdresse() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        Utilisateur created = interactor.creer(
                "  Dupont  ",
                " Alice ",
                "  Alice.DUPONT@Example.com  ",
                " 12 rue de Provence "
        );

        assertEquals("Dupont", created.getNom());
        assertEquals("Alice", created.getPrenom());
        assertEquals("alice.dupont@example.com", created.getEmail());
        assertEquals("12 rue de Provence", created.getAdresse());
    }

    @Test
    void creerAdresseVideDevientNull() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        Utilisateur created = interactor.creer("Dupont", "Alice", "alice@example.com", "   ");

        assertNull(created.getAdresse());
    }

    @Test
    void creerRefuseNomNullOuVide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer(null, "Alice", "alice@example.com", "adresse"));
        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("   ", "Alice", "alice@example.com", "adresse"));
    }

    @Test
    void creerRefuseNomTropLong() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);
        String nomTropLong = "x".repeat(121);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer(nomTropLong, "Alice", "alice@example.com", "adresse"));
    }

    @Test
    void creerRefusePrenomNullOuVide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", null, "alice@example.com", "adresse"));
        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "   ", "alice@example.com", "adresse"));
    }

    @Test
    void creerRefusePrenomTropLong() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);
        String prenomTropLong = "x".repeat(121);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", prenomTropLong, "alice@example.com", "adresse"));
    }

    @Test
    void creerRefuseEmailNullOuVide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "Alice", null, "adresse"));
        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "Alice", "   ", "adresse"));
    }

    @Test
    void creerRefuseEmailInvalide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "Alice", "alice.example.com", "adresse"));
    }

    @Test
    void creerRefuseEmailTropLong() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);
        String emailTropLong = "a".repeat(250) + "@x.com";

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "Alice", emailTropLong, "adresse"));
    }

    @Test
    void creerRefuseAdresseTropLongue() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);
        String adresseTropLongue = "x".repeat(256);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.creer("Dupont", "Alice", "alice@example.com", adresseTropLongue));
    }

    @Test
    void modifierNormaliseEtRetourneUtilisateur() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        gateway.updateResult = Optional.of(new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", null));
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        Optional<Utilisateur> updated = interactor.modifier(
                1L,
                "  Dupont  ",
                "  Alice  ",
                "  Alice@Example.com ",
                "   "
        );

        assertTrue(updated.isPresent());
        assertEquals(1L, gateway.lastUpdatedId);
        assertEquals("Dupont", gateway.lastUpdated.getNom());
        assertEquals("Alice", gateway.lastUpdated.getPrenom());
        assertEquals("alice@example.com", gateway.lastUpdated.getEmail());
        assertNull(gateway.lastUpdated.getAdresse());
    }

    @Test
    void modifierRetourneVideQuandUtilisateurAbsent() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        gateway.updateResult = Optional.empty();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        Optional<Utilisateur> updated = interactor.modifier(99L, "Dupont", "Alice", "alice@example.com", "adresse");

        assertTrue(updated.isEmpty());
    }

    @Test
    void modifierRefuseIdInvalide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class,
                () -> interactor.modifier(0L, "Dupont", "Alice", "alice@example.com", "adresse"));
    }

    @Test
    void supprimerRetourneResultatGateway() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        gateway.deleteResult = true;
        assertTrue(interactor.supprimer(1L));

        gateway.deleteResult = false;
        assertFalse(interactor.supprimer(1L));
        assertEquals(1L, gateway.lastDeletedId);
    }

    @Test
    void supprimerRefuseIdInvalide() {
        FakeUtilisateurGateway gateway = new FakeUtilisateurGateway();
        GererUtilisateursInteractor interactor = new GererUtilisateursInteractor(gateway);

        assertThrows(IllegalArgumentException.class, () -> interactor.supprimer(0L));
    }

    private static class FakeUtilisateurGateway implements UtilisateurGateway {
        private final List<Utilisateur> utilisateurs = new ArrayList<>();
        private Utilisateur lastUpdated;
        private Long lastUpdatedId;
        private Long lastDeletedId;
        private Optional<Utilisateur> updateResult = Optional.of(new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille"));
        private boolean deleteResult;

        @Override
        public List<Utilisateur> findAll() {
            return utilisateurs;
        }

        @Override
        public Optional<Utilisateur> findById(Long id) {
            return utilisateurs.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public Utilisateur save(Utilisateur utilisateur) {
            Utilisateur created = new Utilisateur(
                    1L,
                    utilisateur.getNom(),
                    utilisateur.getPrenom(),
                    utilisateur.getEmail(),
                    utilisateur.getAdresse()
            );
            utilisateurs.add(created);
            return created;
        }

        @Override
        public Optional<Utilisateur> update(Long id, Utilisateur utilisateur) {
            lastUpdatedId = id;
            lastUpdated = utilisateur;
            return updateResult;
        }

        @Override
        public boolean deleteById(Long id) {
            lastDeletedId = id;
            return deleteResult;
        }
    }
}
