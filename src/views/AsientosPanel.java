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

        JLabel titulo = new JLabel(
                "Elige tus asientos",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        gridAsientos = new JPanel();
        add(new JScrollPane(gridAsientos), BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(0, 10));

        labelSeleccion = new JLabel("Asientos elegidos: ninguno", SwingConstants.CENTER);
        sur.add(labelSeleccion, BorderLayout.NORTH);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver a funciones");
        JButton btnConfirmar = new JButton("Confirmar selección");
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
            gridAsientos.add(new JLabel("Esta función no tiene sala asignada."));
            return;
        }

        List<Asiento> asientos = sala.consultarAsientos();

        LinkedHashMap<String, List<Asiento>> porFila = new LinkedHashMap<>();
        for (Asiento a : asientos) {
            porFila.computeIfAbsent(a.getFila(), k -> new ArrayList<>()).add(a);
        }

        gridAsientos.setLayout(new GridLayout(Math.max(porFila.size(), 1), 0, 6, 6));

        for (var entrada : porFila.entrySet()) {

            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));

            for (Asiento asiento : entrada.getValue()) {

                JToggleButton boton = new JToggleButton(
                        asiento.getFila() + String.valueOf(asiento.getNumero())
                );
                boton.setPreferredSize(new Dimension(55, 35));

                boolean ocupado = "OCUPADA".equals(asiento.getEstado());
                boton.setEnabled(!ocupado);
                boton.setBackground(ocupado ? new Color(220, 90, 90) : new Color(120, 200, 120));
                boton.setOpaque(true);

                boton.addActionListener(e -> {
                    if (boton.isSelected()) {
                        seleccionados.add(asiento);
                        boton.setBackground(new Color(90, 140, 220));
                    } else {
                        seleccionados.remove(asiento);
                        boton.setBackground(new Color(120, 200, 120));
                    }
                    actualizarLabelSeleccion();
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

        for (Asiento asiento : seleccionados) {
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