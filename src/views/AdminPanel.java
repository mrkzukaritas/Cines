package views;

import models.Administrador;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final MainFrame frame;
    private final Administrador administrador;

    public AdminPanel(MainFrame frame, Administrador administrador) {
        this.frame = frame;
        this.administrador = administrador;

        setLayout(new BorderLayout(20, 20));

        JLabel titulo = new JLabel(
                "Panel administrador - " + administrador.getNombre(),
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        add(titulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(4, 1, 15, 15));
        botones.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        JButton btnPeliculas = new JButton("Gestionar películas");
        JButton btnCines = new JButton("Gestionar cines y salas");
        JButton btnFunciones = new JButton("Gestionar funciones");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        botones.add(btnPeliculas);
        botones.add(btnCines);
        botones.add(btnFunciones);
        botones.add(btnCerrarSesion);

        add(botones, BorderLayout.CENTER);

        btnPeliculas.addActionListener(e -> frame.mostrarAdminPeliculas(administrador));
        btnCines.addActionListener(e -> frame.mostrarAdminCines(administrador));
        btnFunciones.addActionListener(e -> frame.mostrarAdminFunciones(administrador));

        btnCerrarSesion.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de cerrar sesión?",
                    "Cerrar sesión",
                    JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                administrador.cerrarSesion();
                frame.mostrarLogin();
            }
        });
    }
}