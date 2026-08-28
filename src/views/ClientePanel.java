package views;

import models.Cliente;

import javax.swing.*;
import java.awt.*;

public class ClientePanel extends JPanel {

    private MainFrame frame;
    private Cliente cliente;

    public ClientePanel(
            MainFrame frame,
            Cliente cliente
    ) {

        this.frame = frame;
        this.cliente = cliente;

        setLayout(new BorderLayout());

        // =================================================
        // BARRA SUPERIOR
        // =================================================

        JPanel barraSuperior =
                new JPanel(new BorderLayout());

        JLabel titulo =
                new JLabel("CINE");

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        barraSuperior.add(
                titulo,
                BorderLayout.WEST
        );

        JButton btnSalir =
                new JButton("Cerrar sesión");

        barraSuperior.add(
                btnSalir,
                BorderLayout.EAST
        );

        add(
                barraSuperior,
                BorderLayout.NORTH
        );

        // =================================================
        // CENTRO
        // =================================================

        JPanel contenido =
                new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel bienvenida =
                new JLabel(
                        "Bienvenido, " +
                                cliente.getNombre()
                );

        bienvenida.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        bienvenida.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        contenido.add(
                Box.createVerticalStrut(50)
        );

        contenido.add(
                bienvenida
        );

        contenido.add(
                Box.createVerticalStrut(40)
        );

        // =================================================
        // BOTONES
        // =================================================

        JButton btnPeliculas =
                new JButton("🎬 Películas");

        JButton btnReservas =
                new JButton("🎟 Mis reservas");

        JButton btnPerfil =
                new JButton("👤 Mi perfil");

        Dimension tamaño =
                new Dimension(
                        250,
                        50
                );

        btnPeliculas.setMaximumSize(tamaño);
        btnReservas.setMaximumSize(tamaño);
        btnPerfil.setMaximumSize(tamaño);

        btnPeliculas.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnReservas.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnPerfil.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        contenido.add(btnPeliculas);

        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(btnReservas);

        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(btnPerfil);

        add(
                contenido,
                BorderLayout.CENTER
        );

        // =================================================
        // EVENTOS
        // =================================================

        btnSalir.addActionListener(e ->
                frame.cerrarSesion()
        );

        btnPeliculas.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aquí veremos las películas."
            );

        });

        btnReservas.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aquí aparecerán tus reservas."
            );

        });

        btnPerfil.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aquí aparecerá tu información."
            );

        });
    }
}