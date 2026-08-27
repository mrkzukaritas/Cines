package controller;
import models.AuthService;
import models.Usuario;
import models.Rol;
import exceptions.ValidationException;
import exceptions.AutenticacionException;
/**
 * CONTROLLER (capa Controller del MVC)
 *
 * Recibe las acciones (por ejemplo, desde una View/formulario),
 * llama al Service correspondiente y decide qué hacer según el resultado.
 *
 * Aquí es donde se resuelve "¿es admin o no?" usando usuario.getRol(),
 * en vez de usar instanceof, gracias al polimorfismo definido en Usuario.
 */
public class LoginController {

    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public void manejarRegistro(Usuario usuario) {
        try {
            authService.registrar(usuario);
            System.out.println(">> Registro exitoso.\n");
        } catch (ValidationException e) {
            System.out.println(">> Error de validación: " + e.getMessage() + "\n");
        }
    }

    public void manejarLogin(String email, String password) {
        try {
            Usuario usuario = authService.iniciarSesion(email, password);

            // Aquí se decide el flujo según el rol, sin instanceof:
            if (usuario.getRol() == Rol.ADMINISTRADOR) {
                System.out.println(">> Acceso concedido: redirigiendo a PANEL DE ADMINISTRADOR.\n");
            } else {
                System.out.println(">> Acceso concedido: redirigiendo a PANEL DE CLIENTE.\n");
            }

        } catch (AutenticacionException e) {
            System.out.println(">> Error de autenticación: " + e.getMessage() + "\n");
        }
    }
}