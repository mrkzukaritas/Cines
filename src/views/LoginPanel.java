package views;

import controller.LoginController;
import models.Rol;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private MainFrame frame;
    private LoginController loginController;

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginPanel(
            MainFrame frame,
            LoginController loginController
    ) {

        this.frame = frame;
        this.loginController = loginController;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==============================
        // TITULO
        // ==============================

        JLabel titulo = new JLabel(
                "INICIAR SESIÓN",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(titulo, gbc);

        // ==============================
        // EMAIL
        // ==============================

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);

        gbc.gridx = 1;

        add(txtEmail, gbc);

        // ==============================
        // PASSWORD
        // ==============================

        gbc.gridx = 0;
        gbc.gridy = 2;

        add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(20);

        gbc.gridx = 1;

        add(txtPassword, gbc);

        // ==============================
        // BOTÓN LOGIN
        // ==============================

        JButton btnLogin =
                new JButton("Iniciar sesión");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        add(btnLogin, gbc);

        // ==============================
        // BOTÓN REGISTRO
        // ==============================

        JButton btnRegistro =
                new JButton("Crear cuenta");

        gbc.gridy = 4;

        add(btnRegistro, gbc);

        // ==============================
        // EVENTOS
        // ==============================

        btnLogin.addActionListener(e ->
                iniciarSesion()
        );

        btnRegistro.addActionListener(e ->
                frame.mostrarRegistro()
        );
    }

    private void iniciarSesion() {

        String email =
                txtEmail.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            Usuario usuario =
                    loginController.manejarLogin(
                            email,
                            password
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido, " +
                            usuario.getNombre()
            );

            // MainFrame decide si es Cliente o Administrador
            frame.iniciarSesion(usuario);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}