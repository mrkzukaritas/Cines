package views;

import controller.ReservaController;
import models.Cliente;
import models.DetalleReserva;
import models.Funcion;
import models.Reserva;
import models.Sala;
import models.Cine;
import models.Pelicula;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservasPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final ReservaController reservaController;

    private JPanel listaReservas;

    public ReservasPanel(
            MainFrame frame,
            Cliente cliente,
            ReservaController reservaController
    ) {
        this.frame = frame;
        this.cliente = cliente;
        this.reservaController = reservaController;

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Mis reservas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        listaReservas = new JPanel();
        listaReservas.setLayout(new BoxLayout(listaReservas, BoxLayout.Y_AXIS));
        add(new JScrollPane(listaReservas), BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));
        add(btnVolver, BorderLayout.SOUTH);

        cargarReservas();
    }

    private void cargarReservas() {

        listaReservas.removeAll();

        List<Reserva> reservas = reservaController.listarReservasPorCliente(cliente);

        if (reservas.isEmpty()) {
            JLabel mensaje = new JLabel("Aún no tienes reservas.");
            mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaReservas.add(Box.createVerticalStrut(30));
            listaReservas.add(mensaje);
        } else {
            for (Reserva reserva : reservas) {
                listaReservas.add(crearTarjetaReserva(reserva));
                listaReservas.add(Box.createVerticalStrut(10));
            }
        }

        listaReservas.revalidate();
        listaReservas.repaint();
    }

    private JPanel crearTarjetaReserva(Reserva reserva) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        Funcion funcion = reserva.getFuncion();
        Pelicula pelicula = funcion != null ? funcion.getPelicula() : null;
        Sala sala = funcion != null ? funcion.getSala() : null;
        Cine cine = sala != null ? sala.getCine() : null;

        String tituloPelicula = pelicula != null ? pelicula.getTitulo() : "Película desconocida";
        String nombreCine = cine != null ? cine.getNombre() : "Cine desconocido";
        String nombreSala = sala != null ? sala.getNombre() : "Sala desconocida";
        String fechaFuncion = funcion != null ? funcion.getFechaFuncion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
        String horaFuncion = funcion != null ? String.valueOf(funcion.getHoraInicio()) : "-";

        StringBuilder asientos = new StringBuilder();
        for (DetalleReserva d : reserva.getDetalles()) {
            if (d.getAsiento() != null) {
                asientos.append(d.getAsiento().getFila()).append(d.getAsiento().getNumero()).append("  ");
            }
        }
        if (asientos.length() == 0) {
            asientos.append("Sin asientos");
        }

        JLabel linea1 = new JLabel("Reserva #" + reserva.getId() + " - " + tituloPelicula);
        linea1.setFont(linea1.getFont().deriveFont(Font.BOLD, 14f));

        JLabel linea2 = new JLabel(nombreCine + " - " + nombreSala + " | " + fechaFuncion + " " + horaFuncion);
        JLabel linea3 = new JLabel("Asientos: " + asientos);
        JLabel linea4 = new JLabel("Total: $" + reserva.getTotal() + " | Estado: " + reserva.getEstado());

        tarjeta.add(linea1);
        tarjeta.add(linea2);
        tarjeta.add(linea3);
        tarjeta.add(linea4);

        return tarjeta;
    }
}