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
        Estilos.aplicarFondoFormulario(this);

        HeaderPanel header = new HeaderPanel(
                "src/images/encabezadoPeliculas.png");
        add(header,BorderLayout.NORTH);


        listaPeliculas = new JPanel();
        listaPeliculas.setLayout(new WrapLayout
                (FlowLayout.CENTER, 20, 20));

        Estilos.aplicarFondoFormulario(listaPeliculas);
        listaPeliculas.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE,150,
                Estilos.PADDING_GRANDE,150
        ));

        JScrollPane scroll =
                new JScrollPane(listaPeliculas);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        add(scroll, BorderLayout.CENTER);


        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver");

        btnVolver.addActionListener(e ->
                frame.mostrarCliente(cliente)
        );

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Estilos.aplicarFondoFormulario(panelVolver);
        panelVolver.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_MEDIO, 0));
        panelVolver.add(btnVolver);

        add(panelVolver, BorderLayout.SOUTH);
    }

    private void cargarPeliculas() {

        listaPeliculas.removeAll();

        List<Pelicula> peliculas =
                peliculaController.listarPeliculas();

        if (peliculas.isEmpty()) {

            JLabel mensaje =
                    new JLabel("No hay películas disponibles.");

            mensaje.setFont(Estilos.FUENTE_LABEL);
            mensaje.setForeground(Estilos.GRIS_TEXTO);

            mensaje.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            listaPeliculas.add(Box.createVerticalStrut(Estilos.PADDING_GRANDE));
            listaPeliculas.add(mensaje);

        } else {

            for (Pelicula pelicula : peliculas) {

                TarjetaPelicula tarjeta = new TarjetaPelicula(
                        pelicula.getTitulo(),
                        pelicula.getRutaImagen()
                );

                tarjeta.addActionListener(() ->
                        frame.mostrarFunciones(
                                cliente,
                                pelicula
                        )
                );

                listaPeliculas.add(tarjeta);
            }

        }

        listaPeliculas.revalidate();
        listaPeliculas.repaint();

    }
}