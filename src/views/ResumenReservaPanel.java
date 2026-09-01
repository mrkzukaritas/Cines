package views;

import models.Cliente;
import models.Cine;
import models.DetalleReserva;
import models.Funcion;
import models.Pelicula;
import models.Reserva;
import models.Sala;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ResumenReservaPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final Reserva reserva;

    public ResumenReservaPanel(
            MainFrame frame,
            Cliente cliente,
            Reserva reserva
    ) {
        this.frame = frame;
        this.cliente = cliente;
        this.reserva = reserva;

        setLayout(new BorderLayout(20, 20));
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoResumen.png");
        add(header, BorderLayout.NORTH);

        add(construirDatos(), BorderLayout.CENTER);

        // ==========================================
        // BOTONES
        // ==========================================

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        Estilos.aplicarFondoFormulario(botones);

        botones.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_GRANDE, 0));

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));

        BotonRedondeado btnPagar = Estilos.crearBotonPrincipal("Pagar");
        btnPagar.addActionListener(e -> frame.mostrarPago(cliente, reserva));
        botones.add(btnVolver);
        botones.add(btnPagar);
        add(botones, BorderLayout.SOUTH);
    }

    private JPanel construirDatos() {

        Funcion funcion = reserva.getFuncion();
        Pelicula pelicula = funcion != null ? funcion.getPelicula() : null;
        Sala sala = funcion != null ? funcion.getSala() : null;
        Cine cine = sala != null ? sala.getCine() : null;

        String tituloPelicula = pelicula != null ? pelicula.getTitulo() : "Película desconocida";
        String nombreCine = cine != null ? cine.getNombre() : "Cine desconocido";
        String nombreSala = sala != null ? sala.getNombre() : "Sala desconocida";
        String fecha = funcion != null ? funcion.getFechaFuncion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
        String hora = funcion != null ? String.valueOf(funcion.getHoraInicio()) : "-";

        StringBuilder asientos = new StringBuilder();
        for (DetalleReserva d : reserva.getDetalles()) {
            if (d.getAsiento() != null) {
                asientos.append(d.getAsiento().getFila()).append(d.getAsiento().getNumero()).append("  ");
            }
        }
        if (asientos.isEmpty()) {
            asientos.append("Sin asientos");
        }

        JPanel datos = new JPanel(new GridLayout(7, 2, 10, 18));
        Estilos.aplicarFondoFormulario(datos);
        datos.setBorder(BorderFactory.createEmptyBorder(Estilos.PADDING_GRANDE, 150, Estilos.PADDING_GRANDE, 150));

        datos.add(crearEtiquetaClave("Película:"));
        datos.add(crearValor(tituloPelicula));

        datos.add(crearEtiquetaClave("Cine:"));
        datos.add(crearValor(nombreCine));

        datos.add(crearEtiquetaClave("Sala:"));
        datos.add(crearValor(nombreSala));

        datos.add(crearEtiquetaClave("Fecha:"));
        datos.add(crearValor(fecha));

        datos.add(crearEtiquetaClave("Hora:"));
        datos.add(crearValor(hora));

        datos.add(crearEtiquetaClave("Asientos:"));
        datos.add(crearValor(asientos.toString()));

        JLabel labelTotal = crearEtiquetaClave("Total:");
        JLabel valorTotal = crearValor("$" + reserva.calcularTotal());
        valorTotal.setForeground(Estilos.ROJO_PRINCIPAL);
        valorTotal.setFont(Estilos.FUENTE_TITULO.deriveFont(Font.BOLD, 20f));

        datos.add(labelTotal);
        datos.add(valorTotal);

        return datos;
    }

    private JLabel crearEtiquetaClave(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL);
        label.setForeground(Estilos.GRIS_TEXTO);
        return label;
    }

    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }
}