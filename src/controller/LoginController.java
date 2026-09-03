package controller;

import models.IAuthService;
import models.Usuario;
import exceptions.ValidationException;
import exceptions.AutenticacionException;

public class LoginController {

    private final IAuthService authService;

    public LoginController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Registra un usuario. Relanza ValidationException para que la View
     * (RegistroPanel) pueda capturarla y mostrar el mensaje real al usuario.
     */
    public void manejarRegistro(Usuario usuario) throws ValidationException {
        authService.registrar(usuario);
    }

    /**
     * Intenta iniciar sesión. Relanza AutenticacionException para que
     * LoginPanel capture el error y lo muestre.
     */
    public Usuario manejarLogin(String email, String password) throws AutenticacionException {
        return authService.iniciarSesion(email, password);
    }
}