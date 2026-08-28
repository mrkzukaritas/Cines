package views;

import controller.LoginController;
import models.Cliente;
import exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {

    private MainFrame frame;
    private LoginController loginController;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JPasswordField txtPassword;

    public RegistroPanel(
            MainFrame frame,
            LoginController loginController
    ) {

        this.frame = frame;
        this.loginController = loginController;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ==============================
        // TITULO
        // ==============================

        JLabel titulo =
                new JLabel(
                        "CREAR CUENTA",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(titulo, gbc);

        // ==============================
        // NOMBRE
        // ==============================

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        add(
                new JLabel("Nombre:"),
                gbc
        );

        txtNombre =
                new JTextField(20);

        gbc.gridx = 1;

        add(
                txtNombre,
                gbc
        );

        // ==============================
        // EMAIL
        // ==============================

        gbc.gridx = 0;
        gbc.gridy = 2;

        add(
                new JLabel("Email:"),
                gbc
        );

        txtEmail =
                new JTextField(20);

        gbc.gridx = 1;

        add(
                txtEmail,
                gbc
        );

        // ==============================
        // PASSWORD
        // ==============================

        gbc.gridx = 0;
        gbc.gridy = 3;

        add(
                new JLabel("Contraseña:"),
                gbc
        );

        txtPassword =
                new JPasswordField(20);

        gbc.gridx = 1;

        add(
                txtPassword,
                gbc
        );

        // ==============================
        // TELEFONO
        // ==============================

        gbc.gridx = 0;
        gbc.gridy = 4;

        add(
                new JLabel("Teléfono:"),
                gbc
        );

        txtTelefono =
                new JTextField(20);

        gbc.gridx = 1;

        add(
                txtTelefono,
                gbc
        );

        // ==============================
        // REGISTRAR
        // ==============================

        JButton btnRegistrar =
                new JButton("Registrarse");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        add(
                btnRegistrar,
                gbc
        );

        // ==============================
        // VOLVER
        // ==============================

        JButton btnVolver =
                new JButton("Volver al login");

        gbc.gridy = 6;

        add(
                btnVolver,
                gbc
        );

        // ==============================
        // EVENTOS
        // ==============================

        btnRegistrar.addActionListener(
                e -> registrar()
        );

        btnVolver.addActionListener(
                e -> frame.mostrarLogin()
        );
    }

    private void registrar() {

        String nombre =
                txtNombre.getText().trim();

        String email =
                txtEmail.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        String telefono =
                txtTelefono.getText().trim();

        // ID temporal.
        // Después podemos manejarlo automáticamente.
        Cliente cliente =
                new Cliente(
                        (int) (Math.random() * 100000),
                        nombre,
                        email,
                        password,
                        telefono
                );

        try {

            loginController.manejarRegistro(
                    cliente
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente."
            );

            limpiarCampos();

            frame.mostrarLogin();

        } catch (ValidationException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {

        txtNombre.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtTelefono.setText("");
    }
}