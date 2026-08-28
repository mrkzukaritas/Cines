package models;

import java.util.ArrayList;
import java.util.List;
import exceptions.ValidationException;
import exceptions.AutenticacionException;
/**
 * SERVICE (capa intermedia entre Controller y Model)
 *
 * No aparece en el diagrama de clases UML, pero en la práctica
 * es donde se coloca la lógica de:
 *   - Registrar usuarios (validando antes de guardar)
 *   - Iniciar sesión (verificar credenciales)
 *
 * Aquí simulamos una "base de datos" con una lista en memoria.
 * En un proyecto real, esto llamaría a un Repository/DAO conectado
 * a una base de datos real.
 */
public class AuthService {

    private List<Usuario> usuarios = new ArrayList<>();

    /**
     * Registra un nuevo usuario (Cliente o Administrador).
     * Primero valida los datos, luego llama al método registrarse()
     * propio de la subclase (polimorfismo).
     */
    public void registrar(Usuario nuevoUsuario) throws ValidationException {
        UsuarioValidator.validar(nuevoUsuario); // valida formato de datos

        boolean existe = usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(nuevoUsuario.getEmail()));
        if (existe) {
            throw new ValidationException("Ya existe un usuario registrado con ese email.");
        }

        usuarios.add(nuevoUsuario);
        nuevoUsuario.registrarse(); // ejecuta la versión de Cliente o Administrador
    }

    /**
     * Verifica credenciales y devuelve el Usuario autenticado.
     * El Controller usará usuario.getRol() para decidir qué hacer,
     * SIN necesidad de instanceof.
     */
    public Usuario iniciarSesion(String email, String password) throws AutenticacionException {
        Usuario encontrado = usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (encontrado == null || !encontrado.getPassword().equals(password)) {
            throw new AutenticacionException("Email o contraseña incorrectos.");
        }

        encontrado.iniciarSesion(); // ejecuta la versión de Cliente o Administrador
        return encontrado;
    }
    public void agregarUsuarioInicial(Usuario usuario) {
        usuarios.add(usuario);
    }
}