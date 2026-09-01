package views;

import models.Cliente;

import javax.swing.*;
import java.awt.*;

public class ClientePanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;

    public ClientePanel(
            MainFrame frame,
            Cliente cliente
    ) {

        this.frame = frame;
        this.cliente = cliente;

        setLayout(new BorderLayout(20, 20));
        Estilos.aplicarFondoFormulario(this);

        // =====================================================
        // HEADER
        // =====================================================

        HeaderPanel header = new HeaderPanel(
                "src/images/encabezadoCine.png",
                "Bienvenido, " + cliente.getNombre());

        add(header, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(contenido);
        contenido.setBorder(BorderFactory.createEmptyBorder(Estilos.PADDING_GRANDE,0,0,0));

        // =====================================================
        // BOTONES
        // =====================================================

        JPanel botones = new JPanel();

        botones.setLayout(
                new BoxLayout(botones, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(botones);

        botones.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 200,
                        Estilos.PADDING_GRANDE,200
                ));

        // VER PELICULAS

        BotonRedondeado btnPeliculas =
                Estilos.crearBotonPrincipal("Ver películas");

        // MIS RESERVAS

        BotonRedondeado btnReservas =
                Estilos.crearBotonPrincipal("Mis reservas");

        // MI PERFIL

        BotonRedondeado btnPerfil =
                Estilos.crearBotonPrincipal("Mi perfil");

        // CERRAR SESION

        BotonRedondeado btnCerrarSesion =
                Estilos.crearBotonSecundario("Cerrar sesión");

        for (BotonRedondeado b : new BotonRedondeado[]{
                btnPeliculas, btnReservas, btnPerfil, btnCerrarSesion}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        }

        botones.add(btnPeliculas);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnReservas);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnPerfil);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnCerrarSesion);

        contenido.add(botones);

        add(contenido, BorderLayout.CENTER);

        // =====================================================
        // EVENTOS
        // =====================================================

        btnPeliculas.addActionListener(e ->
                frame.mostrarPeliculas(cliente)
        );

        btnReservas.addActionListener(e ->
                frame.mostrarReservas(cliente)
        );

        btnPerfil.addActionListener(e ->
                frame.mostrarPerfil(cliente)
        );

        btnCerrarSesion.addActionListener(e -> {

            int opcion =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Está seguro de cerrar sesión?",
                            "Cerrar sesión",
                            JOptionPane.YES_NO_OPTION
                    );

            if (opcion == JOptionPane.YES_OPTION) {

                cliente.cerrarSesion();

                frame.mostrarLogin();
            }
        });
    }
}