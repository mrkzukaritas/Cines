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
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoFunciones.png",
                pelicula.getTitulo());

        add(header, BorderLayout.NORTH);

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

        Estilos.aplicarFondoFormulario(listaFunciones);
        listaFunciones.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE, 60,
                Estilos.PADDING_GRANDE, 60
        ));

        JScrollPane scroll = new JScrollPane(listaFunciones);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        add(scroll, BorderLayout.CENTER);

        // ==========================================
        // BOTON VOLVER
        // ==========================================

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver a películas");

        btnVolver.addActionListener(e ->
                frame.mostrarPeliculas(cliente)
        );

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Estilos.aplicarFondoFormulario(panelVolver);
        panelVolver.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_MEDIO, 0
        ));
        panelVolver.add(btnVolver);
        add(
                panelVolver,
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

            mensaje.setFont(Estilos.FUENTE_LABEL);
            mensaje.setForeground(Estilos.GRIS_TEXTO);
            mensaje.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            listaFunciones.add(
                    Box.createVerticalStrut(30)
            );

            listaFunciones.add(mensaje);

        } else {

            for (Funcion funcion : funciones) {

                JPanel tarjeta = crearTarjetaFuncion(funcion);
                tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

                listaFunciones.add(tarjeta);

                listaFunciones.add(
                        Box.createVerticalStrut(15)
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
                        new GridLayout(1, 6, 10, 10)
                );

        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 218, 205)
                        ),
                        BorderFactory.createEmptyBorder(
                                14, 20, 14, 20
                        )
                ));

        // ==========================================
        // CINE
        // ==========================================
        String nombreCine = "Cine desconocido";
        String direccionCine = "";

        if (funcion.getSala() != null) {
            Cine cine = funcionController.buscarCineDeSala(funcion.getSala());
            if (cine != null) {
                nombreCine = cine.getNombre();
                direccionCine = cine.getDireccion() != null ? cine.getDireccion() : "";
            }
        }

        String textoCine = direccionCine.isEmpty() ? nombreCine : nombreCine + " - " + direccionCine;
        JLabel cine = crearEtiquetaDato("Cine / Sede", textoCine);
        // ==========================================
        // SALA
        // ==========================================

        String nombreSala =
                funcion.getSala() != null
                        ? funcion.getSala().getNombre()
                        : "Sin sala";

        JLabel sala = crearEtiquetaDato("Sala", nombreSala);

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

        JLabel fechaLabel = crearEtiquetaDato("Fecha", fecha);

        // ==========================================
        // HORA Y PRECIO
        // ==========================================

        JLabel hora = crearEtiquetaDato("Hora", String.valueOf(funcion.getHoraInicio()));

        JLabel precio = crearEtiquetaDato("Precio", "$" + funcion.getPrecio());

        tarjeta.add(cine);
        tarjeta.add(sala);
        tarjeta.add(fechaLabel);
        tarjeta.add(hora);
        tarjeta.add(precio);

        // ==========================================
        // BOTON
        // ==========================================

        BotonRedondeado seleccionar = Estilos.crearBotonPrincipal("Seleccionar");

        JPanel contenedorBoton = new JPanel(new BorderLayout());
        contenedorBoton.setOpaque(false);
        contenedorBoton.add(seleccionar, BorderLayout.CENTER);

        tarjeta.add(contenedorBoton);

        seleccionar.addActionListener(e ->
                frame.mostrarAsientos(cliente, funcion)
        );

        return tarjeta;

    }

    private JLabel crearEtiquetaDato(String titulo, String valor) {
        JLabel label = new JLabel(
                "<html><b style='color:#8B1E2B;'>" + titulo + "</b><br>"
                        + "<span style='color:#333333;'>" + valor + "</span></html>"
        );
        label.setFont(Estilos.FUENTE_LABEL);
        return label;
    }

}