package fr.univamu.iut.platsutilisateursservice.domain.exception;

/**
 * Exception metier levee lorsqu'un email utilisateur existe deja.
 */
public class DuplicateEmailException extends RuntimeException {

    /**
     * Construit l'exception de conflit email.
     *
     * @param message message fonctionnel.
     */
    public DuplicateEmailException(String message) {
        super(message);
    }
}
