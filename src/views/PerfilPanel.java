package views;

import controller.PerfilController;
import models.Cliente;
import exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;

public class PerfilPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final PerfilController controller;

    private CampoConIcono campoNombre;
    private CampoConIcono campoEmail;
    private CampoConIcono campoPassword;
    private CampoConIcono campoTelefono;

    public PerfilPanel(
            MainFrame frame,
            Cliente cliente
    ) {
        this.frame = frame;
        this.cliente = cliente;
        this.controller = new PerfilController(frame.getLoginController());

        setLayout(new BorderLayout());
        Estilos.aplicarFondoFormulario(this);

        // ==============================
        // HEADER
        // ==============================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoPerfil.png");
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

        campoNombre = new CampoConIcono("Nombre", "👤");
        campoEmail = new CampoConIcono("Correo electrónico", "✉");
        campoPassword = new CampoConIcono("Contraseña", "🔒", true);
        campoTelefono = new CampoConIcono("Teléfono", "\uD83D\uDCDE");

        campoNombre.setTexto(cliente.getNombre());
        campoEmail.setTexto(cliente.getEmail());
        campoPassword.setTexto(cliente.getPassword());
        campoTelefono.setTexto(cliente.getTelefono());

        campoEmail.getCampoInterno().setEditable(false);

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

        BotonRedondeado btnGuardar = Estilos.crearBotonPrincipal("Guardar cambios");
        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        Estilos.aplicarFondoFormulario(panelBotones);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_GRANDE, Estilos.PADDING_GRANDE));

        panelBotones.add(btnVolver);
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);

        // ==============================
        // EVENTOS
        // ==============================

        btnGuardar.addActionListener(e -> guardarCambios());
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));
    }

    private void guardarCambios() {

        String nombre = campoNombre.getTexto();
        String telefono = campoTelefono.getTexto();
        String password = campoPassword.getTexto();

        try {

            controller.guardarCambios(cliente, nombre, telefono, password);

            DialogoEstilizado.mostrarExito(
                    this,
                    "Perfil actualizado",
                    "Tus datos se actualizaron correctamente."
            );

            frame.mostrarPerfil(cliente);

        } catch (ValidationException e) {

            DialogoEstilizado.mostrarError(
                    this,
                    "Error de validación",
                    e.getMessage()
            );
        }
    }
}