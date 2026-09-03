package views;

import controller.LoginController;
import models.Rol;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private MainFrame frame;
    private LoginController loginController;

    private CampoConIcono campoEmail;
    private CampoConIcono campoPassword;

    public LoginPanel(
            MainFrame frame,
            LoginController loginController
    ) {

        this.frame = frame;
        this.loginController = loginController;

        setLayout(new BorderLayout());
        Estilos.aplicarFondoFormulario(this);

        // ==============================
        // HEADER
        // ==============================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoIniciarSesion.png");
        add(header, BorderLayout.NORTH);

        // ==============================
        // FORMULARIO
        // ==============================

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(formulario);
        formulario.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE, Estilos.PADDING_GRANDE,
                Estilos.PADDING_GRANDE, Estilos.PADDING_GRANDE));

        campoEmail = new CampoConIcono("Correo electrónico", "✉");
        campoPassword = new CampoConIcono("Contraseña", "🔒", true);

        campoEmail.setAlignmentX(CENTER_ALIGNMENT);
        campoPassword.setAlignmentX(CENTER_ALIGNMENT);

        formulario.add(Box.createVerticalStrut(40));
        formulario.add(campoEmail);
        formulario.add(campoPassword);
        formulario.add(Box.createVerticalGlue());

        add(formulario, BorderLayout.CENTER);

        // ==============================
        // BOTONES
        // ==============================

        BotonRedondeado btnLogin = Estilos.crearBotonPrincipal("Iniciar sesión");
        BotonRedondeado btnRegistro = Estilos.crearBotonSecundario("Crear cuenta");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        Estilos.aplicarFondoFormulario(panelBotones);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_GRANDE, Estilos.PADDING_GRANDE));

        panelBotones.add(btnRegistro);
        panelBotones.add(btnLogin);

        add(panelBotones, BorderLayout.SOUTH);

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

        String email = campoEmail.getTexto();
        String password = campoPassword.getTexto();

        if (email.isEmpty() || password.isEmpty()) {

            DialogoEstilizado.mostrarAdvertencia(
                    this,
                    "Campos incompletos",
                    "Complete todos los campos."
            );

            return;
        }

        try {

            Usuario usuario = loginController.manejarLogin(email, password);

            DialogoEstilizado.mostrarExito(
                    this,
                    "Inicio de sesión exitoso",
                    "Bienvenido, " + usuario.getNombre()
            );

            frame.iniciarSesion(usuario);

        } catch (Exception e) {

            DialogoEstilizado.mostrarError(
                    this,
                    "Error de autenticación",
                    e.getMessage()
            );
        }
    }
}