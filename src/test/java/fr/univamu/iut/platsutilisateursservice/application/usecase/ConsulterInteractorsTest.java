package fr.univamu.iut.platsutilisateursservice.application.usecase;

import fr.univamu.iut.platsutilisateursservice.application.port.out.PlatGateway;
import fr.univamu.iut.platsutilisateursservice.application.port.out.UtilisateurGateway;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Plat;
import fr.univamu.iut.platsutilisateursservice.domain.entity.Utilisateur;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsulterInteractorsTest {

    @Test
    void consulterPlatsRefuseIdInvalide() {
        ConsulterPlatsInteractor interactor = new ConsulterPlatsInteractor(new PlatGatewayStub());

        assertThrows(IllegalArgumentException.class, () -> interactor.platParId(0L));
    }

    @Test
    void consulterUtilisateursRefuseIdInvalide() {
        ConsulterUtilisateursInteractor interactor = new ConsulterUtilisateursInteractor(new UtilisateurGatewayStub());

        assertThrows(IllegalArgumentException.class, () -> interactor.utilisateurParId(null));
    }

    @Test
    void consulterPlatsAvecIdValideRetourneLePlat() {
        ConsulterPlatsInteractor interactor = new ConsulterPlatsInteractor(new PlatGatewayStub());

        Optional<Plat> plat = interactor.platParId(1L);

        assertTrue(plat.isPresent());
        assertEquals("Aioli", plat.get().getNom());
    }

    @Test
    void consulterUtilisateursAvecIdValideRetourneLUtilisateur() {
        ConsulterUtilisateursInteractor interactor = new ConsulterUtilisateursInteractor(new UtilisateurGatewayStub());

        Optional<Utilisateur> utilisateur = interactor.utilisateurParId(1L);

        assertTrue(utilisateur.isPresent());
        assertEquals("alice@example.com", utilisateur.get().getEmail());
    }

    @Test
    void tousLesPlatsRetourneLaListeComplete() {
        ConsulterPlatsInteractor interactor = new ConsulterPlatsInteractor(new PlatGatewayStub());

        List<Plat> plats = interactor.tousLesPlats();

        assertEquals(1, plats.size());
        assertEquals("Aioli", plats.get(0).getNom());
    }

    @Test
    void tousLesUtilisateursRetourneLaListeComplete() {
        ConsulterUtilisateursInteractor interactor = new ConsulterUtilisateursInteractor(new UtilisateurGatewayStub());

        List<Utilisateur> utilisateurs = interactor.tousLesUtilisateurs();

        assertEquals(1, utilisateurs.size());
        assertEquals("Dupont", utilisateurs.get(0).getNom());
    }

    @Test
    void platParIdIntrouvableRetourneVide() {
        ConsulterPlatsInteractor interactor = new ConsulterPlatsInteractor(new PlatGatewayStub());

        Optional<Plat> plat = interactor.platParId(99L);

        assertTrue(plat.isEmpty());
    }

    @Test
    void utilisateurParIdIntrouvableRetourneVide() {
        ConsulterUtilisateursInteractor interactor = new ConsulterUtilisateursInteractor(new UtilisateurGatewayStub());

        Optional<Utilisateur> utilisateur = interactor.utilisateurParId(99L);

        assertTrue(utilisateur.isEmpty());
    }

    private static class PlatGatewayStub implements PlatGateway {

        @Override
        public List<Plat> findAll() {
            return List.of(new Plat(1L, "Aioli", "Poisson et legumes", new BigDecimal("13.50")));
        }

        @Override
        public Optional<Plat> findById(Long id) {
            if (id == 1L) {
                return Optional.of(new Plat(1L, "Aioli", "Poisson et legumes", new BigDecimal("13.50")));
            }
            return Optional.empty();
        }

        @Override
        public Plat save(Plat plat) {
            return plat;
        }

        @Override
        public Optional<Plat> update(Long id, Plat plat) {
            return Optional.of(plat);
        }

        @Override
        public boolean deleteById(Long id) {
            return true;
        }
    }

    private static class UtilisateurGatewayStub implements UtilisateurGateway {

        @Override
        public List<Utilisateur> findAll() {
            return List.of(new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille"));
        }

        @Override
        public Optional<Utilisateur> findById(Long id) {
            if (id != null && id == 1L) {
                return Optional.of(new Utilisateur(1L, "Dupont", "Alice", "alice@example.com", "Marseille"));
            }
            return Optional.empty();
        }

        @Override
        public Utilisateur save(Utilisateur utilisateur) {
            return utilisateur;
        }

        @Override
        public Optional<Utilisateur> update(Long id, Utilisateur utilisateur) {
            return Optional.of(utilisateur);
        }

        @Override
        public boolean deleteById(Long id) {
            return true;
        }
    }
}
