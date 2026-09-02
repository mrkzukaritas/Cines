import controller.LoginController;
import models.Administrador;
import models.AuthService;
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
            // CONTROLLER
            // ==============================

            LoginController loginController =
                    new LoginController(authService);

            // ==============================
            // VENTANA PRINCIPAL
            // ==============================

            MainFrame ventana =
                    new MainFrame(loginController);
            ventana.precargarDatosDemo();

            ventana.setVisible(true);
        });

    }
}