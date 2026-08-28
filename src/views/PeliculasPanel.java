package views;

import controller.FuncionController;
import controller.PeliculaController;
import models.Cliente;
import models.Pelicula;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class PeliculasPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;

    private final PeliculaController peliculaController;
    private final FuncionController funcionController;

    private JPanel listaPeliculas;

    public PeliculasPanel(
            MainFrame frame,
            Cliente cliente,
            PeliculaController peliculaController,
            FuncionController funcionController
    ) {

        this.frame = frame;
        this.cliente = cliente;
        this.peliculaController = peliculaController;
        this.funcionController = funcionController;

        construirInterfaz();
        cargarPeliculas();
    }

    private void construirInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Películas disponibles",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titulo, BorderLayout.NORTH);

        listaPeliculas = new JPanel();

        listaPeliculas.setLayout(
                new BoxLayout(
                        listaPeliculas,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scroll =
                new JScrollPane(listaPeliculas);

        add(scroll, BorderLayout.CENTER);

        JButton btnVolver =
                new JButton("Volver");

        btnVolver.addActionListener(e ->
                frame.mostrarCliente(cliente)
        );

        add(btnVolver, BorderLayout.SOUTH);
    }
    private void cargarPeliculas() {

        listaPeliculas.removeAll();

        List<Pelicula> peliculas =
                peliculaController.listarPeliculas();

        if (peliculas.isEmpty()) {

            JLabel mensaje =
                    new JLabel("No hay películas disponibles.");

            mensaje.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            listaPeliculas.add(mensaje);

        } else {

            for (Pelicula pelicula : peliculas) {

                JButton boton =
                        new JButton(
                                pelicula.getTitulo()
                        );

                boton.setAlignmentX(
                        Component.CENTER_ALIGNMENT
                );

                boton.addActionListener(e ->
                        frame.mostrarFunciones(
                                cliente,
                                pelicula
                        )
                );

                listaPeliculas.add(boton);

                listaPeliculas.add(
                        Box.createVerticalStrut(10)
                );
            }
        }

        listaPeliculas.revalidate();
        listaPeliculas.repaint();
    }
}