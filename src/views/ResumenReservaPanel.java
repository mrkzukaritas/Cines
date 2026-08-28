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

        JLabel titulo = new JLabel("Resumen de tu reserva", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        add(titulo, BorderLayout.NORTH);

        add(construirDatos(), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));

        JButton btnPagar = new JButton("Pagar");
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
        if (asientos.length() == 0) {
            asientos.append("Sin asientos");
        }

        JPanel datos = new JPanel(new GridLayout(7, 2, 10, 12));
        datos.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        datos.add(new JLabel("Película:"));
        datos.add(new JLabel(tituloPelicula));

        datos.add(new JLabel("Cine:"));
        datos.add(new JLabel(nombreCine));

        datos.add(new JLabel("Sala:"));
        datos.add(new JLabel(nombreSala));

        datos.add(new JLabel("Fecha:"));
        datos.add(new JLabel(fecha));

        datos.add(new JLabel("Hora:"));
        datos.add(new JLabel(hora));

        datos.add(new JLabel("Asientos:"));
        datos.add(new JLabel(asientos.toString()));

        JLabel labelTotal = new JLabel("Total:");
        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD));
        JLabel valorTotal = new JLabel("$" + reserva.calcularTotal());
        valorTotal.setFont(valorTotal.getFont().deriveFont(Font.BOLD));

        datos.add(labelTotal);
        datos.add(valorTotal);

        return datos;
    }
}