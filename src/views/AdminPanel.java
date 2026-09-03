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
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoAdmin.png");
        add(header, BorderLayout.NORTH);

        // ==========================================
        // BOTONES
        // ==========================================

        JPanel botones = new JPanel();
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(botones);
        botones.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE, 200, Estilos.PADDING_GRANDE, 200));

        BotonRedondeado btnPeliculas = Estilos.crearBotonPrincipal("Gestionar películas");
        BotonRedondeado btnCines = Estilos.crearBotonPrincipal("Gestionar cines y salas");
        BotonRedondeado btnFunciones = Estilos.crearBotonPrincipal("Gestionar funciones");
        BotonRedondeado btnCerrarSesion = Estilos.crearBotonSecundario("Cerrar sesión");

        for (BotonRedondeado b : new BotonRedondeado[]{
                btnPeliculas, btnCines, btnFunciones, btnCerrarSesion}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        }

        botones.add(btnPeliculas);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnCines);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnFunciones);
        botones.add(Box.createVerticalStrut(15));
        botones.add(btnCerrarSesion);

        add(botones, BorderLayout.CENTER);

        // ==========================================
        // EVENTOS
        // ==========================================

        btnPeliculas.addActionListener(e -> frame.mostrarAdminPeliculas(administrador));
        btnCines.addActionListener(e -> frame.mostrarAdminCines(administrador));
        btnFunciones.addActionListener(e -> frame.mostrarAdminFunciones(administrador));

        btnCerrarSesion.addActionListener(e -> {

            boolean confirmar =
                    DialogoEstilizado.confirmar(
                            this,
                            "Cerrar sesión",
                            "¿Está seguro de cerrar sesión?"
                    );

            if (confirmar) {

                administrador.cerrarSesion();

                frame.mostrarLogin();
            }
        });
    }
}