package exceptions;

/**
 * Excepción para cuando falla el inicio de sesión
 * (usuario no existe o password incorrecta).
 */
public class AutenticacionException extends Exception {
    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}

