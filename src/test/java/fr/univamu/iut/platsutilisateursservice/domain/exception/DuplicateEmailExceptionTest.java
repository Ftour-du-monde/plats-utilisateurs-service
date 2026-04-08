package fr.univamu.iut.platsutilisateursservice.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateEmailExceptionTest {

    @Test
    void conserveLeMessageFonctionnel() {
        DuplicateEmailException exception = new DuplicateEmailException("Email deja utilise.");

        assertEquals("Email deja utilise.", exception.getMessage());
    }
}
