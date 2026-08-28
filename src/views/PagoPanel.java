package views;

import controller.PagoController;
import models.Cliente;
import models.MetodoPago;
import models.Pago;
import models.Reserva;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PagoPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;
    private final Reserva reserva;
    private final PagoController pagoController;

    private JComboBox<String> comboMetodo;

    public PagoPanel(
            MainFrame frame,
            Cliente cliente,
            Reserva reserva,
            PagoController pagoController
    ) {
        this.frame = frame;
        this.cliente = cliente;
        this.reserva = reserva;
        this.pagoController = pagoController;

        setLayout(new BorderLayout(20, 20));

        JLabel titulo = new JLabel("Método de pago", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel labelTotal = new JLabel("Total a pagar: $" + reserva.calcularTotal());
        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD, 16f));
        labelTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filaCombo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaCombo.add(new JLabel("Selecciona un método:"));
        comboMetodo = new JComboBox<>(new String[]{"Tarjeta", "Efectivo", "PSE"});
        filaCombo.add(comboMetodo);
        filaCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(Box.createVerticalStrut(20));
        contenido.add(labelTotal);
        contenido.add(Box.createVerticalStrut(20));
        contenido.add(filaCombo);

        centro.add(contenido);
        add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnVolver = new JButton("Volver al resumen");
        JButton btnConfirmar = new JButton("Confirmar pago");

        btnVolver.addActionListener(e -> frame.mostrarResumen(cliente, reserva));
        btnConfirmar.addActionListener(e -> confirmarPago());

        botones.add(btnVolver);
        botones.add(btnConfirmar);
        add(botones, BorderLayout.SOUTH);
    }

    private void confirmarPago() {

        String tipo = (String) comboMetodo.getSelectedItem();
        MetodoPago metodoPago = new MetodoPago(comboMetodo.getSelectedIndex() + 1, tipo);

        Pago pago = pagoController.procesarPago(reserva, metodoPago);

        if (pago == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo procesar el pago. Verifica que la reserva tenga asientos\ny que el método de pago sea válido.",
                    "Error de pago",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "¡Pago aprobado!\n"
                        + "Pago #" + pago.getId() + "\n"
                        + "Monto: $" + pago.getMonto() + "\n"
                        + "Fecha: " + pago.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n"
                        + "Método: " + metodoPago.getTipo() + "\n\n"
                        + "Reserva #" + reserva.getId() + " confirmada.",
                "Reserva confirmada",
                JOptionPane.INFORMATION_MESSAGE
        );

        frame.mostrarCliente(cliente);
    }
}