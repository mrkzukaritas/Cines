package exceptions;

/**
 * Excepción para cuando los datos de un Usuario no cumplen
 * las reglas de formato/negocio (email inválido, password corto, etc.)
 */
public class ValidationException extends Exception {
    public ValidationException(String mensaje) {
        super(mensaje);
    }
}