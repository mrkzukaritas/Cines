package models;
import exceptions.ValidationException;
import exceptions.AutenticacionException;
/**
 * VALIDATOR (parte del Model, pero separado de las entidades)
 *
 * Aquí van las validaciones de FORMATO / REGLAS DE NEGOCIO.
 * No van dentro de Usuario/Cliente/Administrador para no ensuciar
 * el modelo con lógica que no es su responsabilidad (Single Responsibility).
 *
 * Se usa un método estático porque no necesita guardar estado,
 * solo recibe un Usuario y valida sus datos.
 */
public class UsuarioValidator {

    public static void validar(Usuario u) throws ValidationException {

        // Validar nombre
        if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre no puede estar vacío.");
        }

        // Validar email con una expresión regular simple
        if (u.getEmail() == null || !u.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("El email no tiene un formato válido.");
        }

        // Validar password (mínimo 8 caracteres)
        if (u.getPassword() == null || u.getPassword().length() < 8) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres.");
        }

        // Validar teléfono (solo números, 7 a 10 dígitos)
        if (u.getTelefono() == null || !u.getTelefono().matches("^[0-9]{7,10}$")) {
            throw new ValidationException("El teléfono debe tener entre 7 y 10 dígitos numéricos.");
        }
    }
}