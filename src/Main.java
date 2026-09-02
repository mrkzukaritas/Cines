import controller.LoginController;
import models.Administrador;
import models.AuthService;
import models.Cliente;
import views.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // ==============================
            // SERVICIO DE AUTENTICACIÓN
            // ==============================

            AuthService authService = new AuthService();

            // ==============================
            // ADMINISTRADOR INICIAL
            // ==============================

            Administrador admin = new Administrador(
                    1,
                    "Administrador",
                    "admin@cine.com",
                    "Admin123",
                    "3001234567"
            );
            authService.agregarUsuarioInicial(admin);

            // ==============================
            // CLIENTE DE PRUEBA
            // (para no tener que registrarte cada vez que abres la app)
            // ==============================

            Cliente clienteDemo = new Cliente(
                    2,
                    "Cliente Demo",
                    "cliente@cine.com",
                    "Cliente123",
                    "3009876543"
            );
            authService.agregarUsuarioInicial(clienteDemo);

            // ==============================
            // CONTROLLER
            // ==============================

            LoginController loginController =
                    new LoginController(authService);

            // ==============================
            // VENTANA PRINCIPAL
            // ==============================

            MainFrame ventana =
                    new MainFrame(loginController);

            // ==============================
            // DATOS DE PRUEBA (cines, películas, funciones)
            // ==============================

            ventana.precargarDatosDemo();

            ventana.setVisible(true);
        });
    }
}