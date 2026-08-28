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

        // =====================================================
        // TITULO
        // =====================================================

        JLabel titulo = new JLabel(
                "Bienvenido, " + cliente.getNombre(),
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        add(titulo, BorderLayout.NORTH);

        // =====================================================
        // BOTONES
        // =====================================================

        JPanel botones = new JPanel();

        botones.setLayout(
                new GridLayout(4, 1, 15, 15)
        );

        botones.setBorder(
                BorderFactory.createEmptyBorder(
                        40, 200, 40, 200
                )
        );

        // VER PELICULAS

        JButton btnPeliculas =
                new JButton("Ver películas");

        // MIS RESERVAS

        JButton btnReservas =
                new JButton("Mis reservas");

        // MI PERFIL

        JButton btnPerfil =
                new JButton("Mi perfil");

        // CERRAR SESION

        JButton btnCerrarSesion =
                new JButton("Cerrar sesión");

        botones.add(btnPeliculas);
        botones.add(btnReservas);
        botones.add(btnPerfil);
        botones.add(btnCerrarSesion);

        add(
                botones,
                BorderLayout.CENTER
        );

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