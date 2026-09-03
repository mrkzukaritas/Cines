package controller;

import models.Usuario;
import exceptions.ValidationException;

public class PerfilController {

    private final LoginController loginController;

    public PerfilController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void guardarCambios(Usuario usuario, String nombre, String telefono, String password)
            throws ValidationException {
        loginController.actualizarPerfil(usuario, nombre, telefono, password);
    }
}