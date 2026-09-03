package views;

import controller.LoginController;
import models.Cliente;
import exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {

    private MainFrame frame;
    private LoginController loginController;

    private CampoConIcono campoNombre;
    private CampoConIcono campoEmail;
    private CampoConIcono campoPassword;
    private CampoConIcono campoTelefono;

    public RegistroPanel(
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

        HeaderPanel header = new HeaderPanel("src/images/encabezadoRegistro.png");
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

        campoNombre = new CampoConIcono("Nombre","👤");
        campoEmail = new CampoConIcono("Correo electrónico","✉");
        campoPassword = new CampoConIcono("Contraseña","🔒", true);
        campoTelefono = new CampoConIcono("Teléfono","\uD83D\uDCDE");

        campoNombre.setAlignmentX(CENTER_ALIGNMENT);
        campoEmail.setAlignmentX(CENTER_ALIGNMENT);
        campoPassword.setAlignmentX(CENTER_ALIGNMENT);
        campoTelefono.setAlignmentX(CENTER_ALIGNMENT);

        formulario.add(campoNombre);
        formulario.add(campoEmail);
        formulario.add(campoPassword);
        formulario.add(campoTelefono);
        formulario.add(Box.createVerticalGlue());

        add(formulario, BorderLayout.CENTER);

        // ==============================
        // BOTONES
        // ==============================

        BotonRedondeado btnRegistrar = Estilos.crearBotonPrincipal("Registrarse");
        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver al login");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        Estilos.aplicarFondoFormulario(panelBotones);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_GRANDE, Estilos.PADDING_GRANDE));

        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);

        add(panelBotones, BorderLayout.SOUTH);

        // ==============================
        // EVENTOS
        // ==============================

        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> frame.mostrarLogin());
    }

    private void registrar() {

        String nombre = campoNombre.getTexto();
        String email = campoEmail.getTexto();
        String password = campoPassword.getTexto();
        String telefono = campoTelefono.getTexto();

        Cliente cliente = new Cliente(
                (int) (Math.random() * 100000),
                nombre,
                email,
                password,
                telefono
        );

        try {

            loginController.manejarRegistro(cliente);

            DialogoEstilizado.mostrarExito(
                    this,
                    "Registro exitoso",
                    "Usuario registrado correctamente."
            );

            limpiarCampos();

            frame.mostrarLogin();

        } catch (ValidationException e) {

            DialogoEstilizado.mostrarError(
                    this,
                    "Error de validación",
                    e.getMessage()
            );
        }
    }

    private void limpiarCampos() {

        campoNombre.setTexto("");
        campoEmail.setTexto("");
        campoPassword.setTexto("");
        campoTelefono.setTexto("");
    }
}