package views;

import controller.ReservaController;
import models.Asiento;
import models.Cliente;
import models.Funcion;
import models.Reserva;
import models.Sala;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AsientosPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final Funcion funcion;
    private final ReservaController reservaController;

    private final List<Asiento> seleccionados = new ArrayList<>();
    private JPanel gridAsientos;
    private JLabel labelSeleccion;

    public AsientosPanel(
            MainFrame frame,
            Cliente cliente,
            Funcion funcion,
            ReservaController reservaController
    ) {
        this.frame = frame;
        this.cliente = cliente;
        this.funcion = funcion;
        this.reservaController = reservaController;

        setLayout(new BorderLayout(10, 10));
        Estilos.aplicarFondoFormulario(this);

        HeaderPanel header = new HeaderPanel("src/images/encabezadoAsientos.png");
        add(header, BorderLayout.NORTH);

        gridAsientos = new JPanel();
        Estilos.aplicarFondoFormulario(gridAsientos);

        JPanel contenidoAsientos = new JPanel();
        contenidoAsientos.setLayout(new BoxLayout(contenidoAsientos, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(contenidoAsientos);
        contenidoAsientos.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_CHICO, 0, 0, 0));

        PantallaPanel pantalla = new PantallaPanel();
        pantalla.setAlignmentX(CENTER_ALIGNMENT);

        gridAsientos.setAlignmentX(CENTER_ALIGNMENT);

        contenidoAsientos.add(pantalla);
        contenidoAsientos.add(Box.createVerticalStrut(20));
        contenidoAsientos.add(gridAsientos);

        JScrollPane scroll = new JScrollPane(contenidoAsientos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        add(scroll, BorderLayout.CENTER);


        JPanel sur = new JPanel(new BorderLayout(0, 10));

        Estilos.aplicarFondoFormulario(sur);
        sur.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_MEDIO, 0, Estilos.PADDING_GRANDE, 0));

        labelSeleccion = new JLabel("Asientos elegidos: ninguno", SwingConstants.CENTER);
        labelSeleccion.setFont(Estilos.FUENTE_LABEL);
        labelSeleccion.setForeground(Estilos.GRIS_TEXTO);
        sur.add(labelSeleccion, BorderLayout.NORTH);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        Estilos.aplicarFondoFormulario(botones);

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver a funciones");
        BotonRedondeado btnConfirmar = Estilos.crearBotonPrincipal("Confirmar selección");

        botones.add(btnVolver);
        botones.add(btnConfirmar);
        sur.add(botones, BorderLayout.SOUTH);

        add(sur, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                frame.mostrarFunciones(cliente, funcion.getPelicula())
        );

        btnConfirmar.addActionListener(e -> confirmarSeleccion());

        cargarAsientos();
    }

    private void cargarAsientos() {

        Sala sala = funcion.getSala();

        if (sala == null) {
            JLabel mensaje = new JLabel("Esta función no tiene sala asignada.");
            mensaje.setFont(Estilos.FUENTE_LABEL);
            gridAsientos.add(mensaje);
            return;
        }

        List<Asiento> asientos = sala.consultarAsientos();

        LinkedHashMap<String, List<Asiento>> porFila = new LinkedHashMap<>();
        for (Asiento a : asientos) {
            porFila.computeIfAbsent(a.getFila(), k -> new ArrayList<>()).add(a);
        }

        gridAsientos.setLayout(new GridLayout(Math.max(porFila.size(), 1), 0, 6, 6));

        for (var entrada : porFila.entrySet()) {

            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
            filaPanel.setOpaque(false);

            for (Asiento asiento : entrada.getValue()) {

                boolean ocupado = "OCUPADA".equals(asiento.getEstado());

                AsientoBoton boton = new AsientoBoton(
                        asiento.getFila() + String.valueOf(asiento.getNumero()),
                        ocupado
                );

                boton.addActionListener(e -> {
                    if (boton.isSelected()) {
                        seleccionados.add(asiento);
                    } else {
                        seleccionados.remove(asiento);
                    }
                    actualizarLabelSeleccion();
                });

                asiento.agregarObserver(a -> {
                    boolean ahoraOcupado = "OCUPADA".equals(a.getEstado());
                    boton.setEnabled(!ahoraOcupado);
                    if (ahoraOcupado) {
                        seleccionados.remove(a);
                        boton.setSelected(false);
                        actualizarLabelSeleccion();
                    }
                });

                filaPanel.add(boton);
            }

            gridAsientos.add(filaPanel);

        }
    }

    private void actualizarLabelSeleccion() {
        if (seleccionados.isEmpty()) {
            labelSeleccion.setText("Asientos elegidos: ninguno");
            return;
        }
        StringBuilder sb = new StringBuilder("Asientos elegidos: ");
        for (Asiento a : seleccionados) {
            sb.append(a.getFila()).append(a.getNumero()).append("  ");
        }
        labelSeleccion.setText(sb.toString());
    }

    private void confirmarSeleccion() {

        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona al menos un asiento.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Reserva reserva = reservaController.crearReserva(cliente, funcion);

        // ConcurrentModificationException.
        for (Asiento asiento : new ArrayList<>(seleccionados)) {
            boolean agregado = reservaController.agregarAsiento(reserva, asiento);
            if (!agregado) {
                JOptionPane.showMessageDialog(
                        this,
                        "El asiento " + asiento.getFila() + asiento.getNumero()
                                + " ya no está disponible. Intenta de nuevo.",
                        "Asiento no disponible",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        frame.mostrarResumen(cliente, reserva);
    }
}