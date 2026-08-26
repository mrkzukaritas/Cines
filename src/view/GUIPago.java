package view;

import controller.PagoController;
import controller.ReservaController;
import models.EstadoReserva;
import models.MetodoPago;
import models.Pago;
import models.Reserva;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIPago extends JPanel implements IGUIEstilos {

    private final GUIPrincipal principal;
    private final ReservaController reservaController;
    private final PagoController pagoController;

    private JLabel labelReserva;
    private JLabel labelCliente;
    private JLabel labelPelicula;
    private JLabel labelAsientos;
    private JLabel labelTotal;

    private JComboBox<String> comboMetodoPago;

    private JButton btnVolver;
    private JButton btnPagar;


    public GUIPago(
            GUIPrincipal principal,
            ReservaController reservaController,
            PagoController pagoController) {

        this.principal = principal;
        this.reservaController = reservaController;
        this.pagoController = pagoController;

        construirGUI();
        cargarInformacion();
    }

    private void construirGUI() {

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        setBackground(
                COLOR_FONDO
        );


        JLabel titulo =
                new JLabel(
                        "Realizar pago",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                FUENTE_TITULO
        );

        titulo.setForeground(
                COLOR_TEXTO
        );


        add(
                titulo,
                BorderLayout.NORTH
        );


        JPanel panelCentral =
                new JPanel(
                        new GridBagLayout()
                );

        panelCentral.setBackground(
                COLOR_BLANCO
        );

        panelCentral.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;



        labelReserva =
                crearLabelValor();

        agregarFila(
                panelCentral,
                gbc,
                0,
                "Reserva:",
                labelReserva
        );


        labelCliente =
                crearLabelValor();

        agregarFila(
                panelCentral,
                gbc,
                1,
                "Cliente:",
                labelCliente
        );

        labelPelicula =
                crearLabelValor();

        agregarFila(
                panelCentral,
                gbc,
                2,
                "Película:",
                labelPelicula
        );

        labelAsientos =
                crearLabelValor();

        agregarFila(
                panelCentral,
                gbc,
                3,
                "Asientos:",
                labelAsientos
        );


           labelTotal =
                crearLabelValor();

        labelTotal.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        labelTotal.setForeground(
                COLOR_PRIMARIO
        );

        agregarFila(
                panelCentral,
                gbc,
                4,
                "Total:",
                labelTotal
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;

        JLabel labelMetodo =
                new JLabel(
                        "Método de pago:"
                );

        labelMetodo.setFont(
                FUENTE_NEGRITA
        );


        panelCentral.add(
                labelMetodo,
                gbc
        );


        comboMetodoPago =
                new JComboBox<>(
                        new String[]{
                                "Tarjeta",
                                "PSE",
                                "Efectivo"
                        }
                );

        comboMetodoPago.setFont(
                FUENTE_NORMAL
        );


        gbc.gridx = 1;
        gbc.weightx = 1;

        panelCentral.add(
                comboMetodoPago,
                gbc
        );


        add(
                panelCentral,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        panelBotones.setBackground(
                COLOR_FONDO
        );


        btnVolver =
                new JButton(
                        "<- Volver"
                );


        btnPagar =
                new JButton(
                        "Realizar pago"
                );

        btnPagar.setBackground(
                COLOR_PRIMARIO
        );

        btnPagar.setForeground(
                COLOR_BLANCO
        );


        panelBotones.add(
                btnVolver
        );

        panelBotones.add(
                btnPagar
        );


        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        btnVolver.addActionListener(
                e -> volverAReserva()
        );


        btnPagar.addActionListener(
                e -> realizarPago()
        );
    }

    private JLabel crearLabelValor() {

        JLabel label =
                new JLabel("-");

        label.setFont(
                FUENTE_NORMAL
        );

        label.setForeground(
                COLOR_TEXTO
        );

        return label;
    }

    private void agregarFila(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String titulo,
            JLabel valor) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        JLabel labelTitulo =
                new JLabel(
                        titulo
                );

        labelTitulo.setFont(
                FUENTE_NEGRITA
        );


        panel.add(
                labelTitulo,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                valor,
                gbc
        );
    }

    private void cargarInformacion() {

        Reserva reserva =
                principal.getReservaActual();


        if (reserva == null) {

            mostrarReservaVacia();

            return;
        }

        labelReserva.setText(
                String.valueOf(
                        reserva.getId()
                )
        );


        if (
                reserva.getCliente() != null
        ) {

            labelCliente.setText(
                    reserva
                            .getCliente()
                            .getNombre()
            );

        } else {

            labelCliente.setText(
                    "No disponible"
            );
        }


        if (
                reserva.getFuncion() != null
                        &&
                        reserva
                                .getFuncion()
                                .getPelicula() != null
        ) {

            labelPelicula.setText(
                    reserva
                            .getFuncion()
                            .getPelicula()
                            .getTitulo()
            );

        } else {

            labelPelicula.setText(
                    "No disponible"
            );
        }

       cargarAsientos(
                reserva
        );


        double total =
                reserva.calcularTotal();


        labelTotal.setText(
                "$" + total
        );
    }


    private void cargarAsientos(
            Reserva reserva) {

        if (
                reserva.getDetalles() == null
                        ||
                        reserva.getDetalles().isEmpty()
        ) {

            labelAsientos.setText(
                    "Ninguno"
            );

            return;
        }


        StringBuilder texto =
                new StringBuilder();


        for (
                int i = 0;
                i < reserva
                        .getDetalles()
                        .size();
                i++
        ) {

            if (
                    reserva
                            .getDetalles()
                            .get(i)
                            .getAsiento() != null
            ) {

                texto.append(
                        reserva
                                .getDetalles()
                                .get(i)
                                .getAsiento()
                                .getFila()
                );

                texto.append(
                        reserva
                                .getDetalles()
                                .get(i)
                                .getAsiento()
                                .getNumero()
                );
            }


            if (
                    i <
                            reserva
                                    .getDetalles()
                                    .size() - 1
            ) {

                texto.append(
                        ", "
                );
            }
        }


        labelAsientos.setText(
                texto.toString()
        );
    }



    private void mostrarReservaVacia() {

        labelReserva.setText(
                "No disponible"
        );

        labelCliente.setText(
                "No disponible"
        );

        labelPelicula.setText(
                "No disponible"
        );

        labelAsientos.setText(
                "Ninguno"
        );

        labelTotal.setText(
                "$0"
        );

        btnPagar.setEnabled(
                false
        );
    }

    private void volverAReserva() {

        principal.mostrarPantalla(
                GUIPrincipal.RESERVA
        );
    }


    private void realizarPago() {

        Reserva reserva =
                principal.getReservaActual();


        if (reserva == null) {

            JOptionPane.showMessageDialog(
                    principal,
                    "No existe una reserva.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (
                reserva.getDetalles() == null
                        ||
                        reserva.getDetalles().isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "La reserva no tiene asientos.",
                    "Reserva vacía",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        reservaController.confirmarReserva(
                reserva
        );


        String tipo =
                comboMetodoPago
                        .getSelectedItem()
                        .toString();


        MetodoPago metodoPago =
                new MetodoPago(
                        1,
                        tipo
                );


        Pago pago =
                pagoController.procesarPago(
                        reserva,
                        metodoPago
                );


        if (pago == null) {

            JOptionPane.showMessageDialog(
                    principal,
                    "No se pudo procesar el pago.",
                    "Pago rechazado",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                principal,
                "Pago realizado correctamente.\n"
                        + "Reserva #"
                        + reserva.getId()
                        + "\n"
                        + "Monto: $"
                        + pago.getMonto(),
                "Pago exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );

        principal.limpiarDatos();

        principal.mostrarPantalla(
                GUIPrincipal.REGISTRO
        );
    }

    public void actualizarInformacion() {

        cargarInformacion();
    }
}