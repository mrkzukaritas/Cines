package views;

import controller.FuncionController;
import models.Cliente;
import models.Funcion;
import models.Pelicula;
import models.Cine;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FuncionesPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final Pelicula pelicula;
    private FuncionController funcionController;

    private JPanel listaFunciones;

    public FuncionesPanel(
            MainFrame frame,
            Cliente cliente,
            Pelicula pelicula,
            FuncionController funcionController
    ) {

        this.frame = frame;
        this.cliente = cliente;
        this.pelicula = pelicula;
        this.funcionController = funcionController;

        setLayout(new BorderLayout(10, 10));

        // ==========================================
        // TITULO
        // ==========================================

        JLabel titulo = new JLabel(
                "Funciones de: " + pelicula.getTitulo(),
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titulo, BorderLayout.NORTH);

        // ==========================================
        // LISTA DE FUNCIONES
        // ==========================================

        listaFunciones = new JPanel();

        listaFunciones.setLayout(
                new BoxLayout(
                        listaFunciones,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scroll = new JScrollPane(
                listaFunciones
        );

        add(scroll, BorderLayout.CENTER);

        // ==========================================
        // BOTON VOLVER
        // ==========================================

        JButton btnVolver =
                new JButton("Volver a películas");

        btnVolver.addActionListener(e ->
                frame.mostrarPeliculas(cliente)
        );

        add(
                btnVolver,
                BorderLayout.SOUTH
        );

        cargarFunciones();
    }

    // ==========================================
    // CARGAR FUNCIONES
    // ==========================================

    private void cargarFunciones() {

        listaFunciones.removeAll();

        List<Funcion> funciones =
                funcionController.listarPorPelicula(
                        pelicula
                );

        if (funciones.isEmpty()) {

            JLabel mensaje =
                    new JLabel(
                            "No hay funciones disponibles para esta película."
                    );

            mensaje.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            listaFunciones.add(
                    Box.createVerticalStrut(30)
            );

            listaFunciones.add(mensaje);

        } else {

            for (Funcion funcion : funciones) {

                listaFunciones.add(
                        crearTarjetaFuncion(funcion)
                );

                listaFunciones.add(
                        Box.createVerticalStrut(10)
                );
            }
        }

        listaFunciones.revalidate();
        listaFunciones.repaint();
    }

    // ==========================================
    // TARJETA DE FUNCIÓN
    // ==========================================

    private JPanel crearTarjetaFuncion(
            Funcion funcion
    ) {

        JPanel tarjeta =
                new JPanel(
                        new GridLayout(1, 5, 10, 10)
                );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        // ==========================================
        // CINE
        // ==========================================

        String nombreCine = "Cine desconocido";

        if (funcion.getSala() != null) {

            Cine cine =
                    funcionController.buscarCineDeSala(
                            funcion.getSala()
                    );

            if (cine != null) {
                nombreCine = cine.getNombre();
            }
        }

        JLabel cine =
                new JLabel(
                        "<html><b>Cine</b><br>"
                                + nombreCine
                                + "</html>"
                );

        // ==========================================
        // SALA
        // ==========================================

        String nombreSala =
                funcion.getSala() != null
                        ? funcion.getSala().getNombre()
                        : "Sin sala";

        JLabel sala =
                new JLabel(
                        "<html><b>Sala</b><br>"
                                + nombreSala
                                + "</html>"
                );

        // ==========================================
        // FECHA
        // ==========================================

        String fecha =
                funcion.getFechaFuncion()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                )
                        );

        JLabel fechaLabel =
                new JLabel(
                        "<html><b>Fecha</b><br>"
                                + fecha
                                + "</html>"
                );

        // ==========================================
        // HORA Y PRECIO
        // ==========================================

        JLabel hora =
                new JLabel(
                        "<html><b>Hora</b><br>"
                                + funcion.getHoraInicio()
                                + "</html>"
                );

        JLabel precio =
                new JLabel(
                        "<html><b>Precio</b><br>$"
                                + funcion.getPrecio()
                                + "</html>"
                );

        tarjeta.add(cine);
        tarjeta.add(sala);
        tarjeta.add(fechaLabel);
        tarjeta.add(hora);
        tarjeta.add(precio);

        // ==========================================
        // BOTON
        // ==========================================

        JButton seleccionar =
                new JButton("Seleccionar");

        JPanel contenedorBoton =
                new JPanel(new BorderLayout());

        contenedorBoton.add(
                seleccionar,
                BorderLayout.CENTER
        );

        // Como el GridLayout necesita otro elemento,
        // agregamos el botón reemplazando el precio
        tarjeta.remove(precio);

        tarjeta.add(contenedorBoton);

        seleccionar.addActionListener(e ->
                frame.mostrarAsientos(cliente, funcion)
        );

        return tarjeta;
    }

}