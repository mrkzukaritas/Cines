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
        Estilos.aplicarFondoFormulario(this);

        HeaderPanel header = new HeaderPanel("src/images/encabezadoReservas.png");
        add(header, BorderLayout.NORTH);

        listaReservas = new JPanel();
        listaReservas.setLayout(new BoxLayout(listaReservas, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(listaReservas);
        listaReservas.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE, 100, Estilos.PADDING_GRANDE, 100));

        JScrollPane scroll = new JScrollPane(listaReservas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        add(scroll, BorderLayout.CENTER);

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Estilos.aplicarFondoFormulario(panelVolver);
        panelVolver.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_MEDIO, 0));
        panelVolver.add(btnVolver);

        add(panelVolver, BorderLayout.SOUTH);

        cargarReservas();

    }

    private void cargarReservas() {

        listaReservas.removeAll();

        List<Reserva> reservas = reservaController.listarReservasPorCliente(cliente);

        if (reservas.isEmpty()) {
            JLabel mensaje = new JLabel("Aún no tienes reservas.");
            mensaje.setFont(Estilos.FUENTE_LABEL);
            mensaje.setForeground(Estilos.GRIS_TEXTO);
            mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaReservas.add(Box.createVerticalStrut(30));
            listaReservas.add(mensaje);
        } else {
            for (Reserva reserva : reservas) {
                JPanel tarjeta = crearTarjetaReserva(reserva);
                tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, tarjeta.getPreferredSize().height));
                listaReservas.add(tarjeta);
                listaReservas.add(Box.createVerticalStrut(15));
            }
        }

        listaReservas.revalidate();
        listaReservas.repaint();
    }

    private JPanel crearTarjetaReserva(Reserva reserva) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 218, 205)),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
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
        if (asientos.isEmpty()) {
            asientos.append("Sin asientos");
        }

        JLabel linea1 = new JLabel("Reserva #" + reserva.getId() + " - " + tituloPelicula);
        linea1.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 16f));
        linea1.setForeground(Estilos.ROJO_PRINCIPAL);

        JLabel linea2 = new JLabel(nombreCine + " - " + nombreSala + " | " + fechaFuncion + " " + horaFuncion);
        linea2.setFont(Estilos.FUENTE_CAMPO);
        linea2.setForeground(Estilos.GRIS_TEXTO);

        JLabel linea3 = new JLabel("Asientos: " + asientos);
        linea3.setFont(Estilos.FUENTE_CAMPO);
        linea3.setForeground(Estilos.GRIS_TEXTO);

        JLabel linea4 = new JLabel("Total: $" + reserva.getTotal() + "   |   Estado: " + reserva.getEstado());
        linea4.setFont(Estilos.FUENTE_LABEL);
        linea4.setForeground(Color.DARK_GRAY);

        tarjeta.add(linea1);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(linea2);
        tarjeta.add(linea3);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(linea4);

        return tarjeta;
    }
}