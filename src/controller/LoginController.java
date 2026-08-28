package controller;

import models.AuthService;
import models.Usuario;
import exceptions.ValidationException;
import exceptions.AutenticacionException;

/**
 * CONTROLLER
 *
 * Conecta las interfaces Swing con AuthService.
 * No contiene la lógica de validación.
 */
public class LoginController {

    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registra un usuario.
     *
     * @throws ValidationException si los datos no son válidos
     */
    public void manejarRegistro(Usuario usuario)
            throws ValidationException {

        authService.registrar(usuario);
    }

    /**
     * Realiza el login.
     *
     * @return Usuario autenticado
     * @throws AutenticacionException si las credenciales son incorrectas
     */
    public Usuario manejarLogin(
            String email,
            String password
    ) throws AutenticacionException {

        return authService.iniciarSesion(email, password);
    }
}