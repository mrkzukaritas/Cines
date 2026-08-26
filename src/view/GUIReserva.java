package view;

import models.Asiento;
import models.DetalleReserva;
import models.Funcion;
import models.Reserva;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;


public class GUIReserva extends JPanel implements IGUIEstilos {


    private final GUIPrincipal principal;
    private final Reserva reserva;

    private JLabel labelCliente;
    private JLabel labelPelicula;
    private JLabel labelSala;
    private JLabel labelFecha;
    private JLabel labelHora;
    private JLabel labelAsientos;
    private JLabel labelTotal;

    private JButton btnVolver;
    private JButton btnContinuarPago;


    public GUIReserva(
            GUIPrincipal principal,
            Reserva reserva) {

        this.principal = principal;
        this.reserva = reserva;

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
                        "Resumen de tu reserva",
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


        JPanel panelInformacion =
                new JPanel(
                        new GridBagLayout()
                );

        panelInformacion.setBackground(
                COLOR_BLANCO
        );

        panelInformacion.setBorder(
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


        labelCliente =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                0,
                "Cliente:",
                labelCliente
        );



        labelPelicula =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                1,
                "Película:",
                labelPelicula
        );


        labelSala =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                2,
                "Sala:",
                labelSala
        );


        labelFecha =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                3,
                "Fecha:",
                labelFecha
        );


        labelHora =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                4,
                "Hora:",
                labelHora
        );


        labelAsientos =
                crearLabelValor();

        agregarFila(
                panelInformacion,
                gbc,
                5,
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
                panelInformacion,
                gbc,
                6,
                "Total:",
                labelTotal
        );


        add(
                panelInformacion,
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
                        "<- Volver a asientos"
                );

        btnVolver.setFont(
                FUENTE_NORMAL
        );


        btnContinuarPago =
                new JButton(
                        "Continuar al pago"
                );

        btnContinuarPago.setFont(
                FUENTE_NEGRITA
        );

        btnContinuarPago.setBackground(
                COLOR_PRIMARIO
        );

        btnContinuarPago.setForeground(
                COLOR_BLANCO
        );

        btnContinuarPago.setFocusPainted(
                false
        );


        panelBotones.add(
                btnVolver
        );

        panelBotones.add(
                btnContinuarPago
        );


        add(
                panelBotones,
                BorderLayout.SOUTH
        );


        btnVolver.addActionListener(
                e -> volverAAsientos()
        );


        btnContinuarPago.addActionListener(
                e -> continuarAlPago()
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

        labelTitulo.setForeground(
                COLOR_TEXTO
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

        if (reserva == null) {

            mostrarReservaVacia();

            return;
        }


        if (reserva.getCliente() != null) {

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


        Funcion funcion =
                reserva.getFuncion();


        if (funcion == null) {

            labelPelicula.setText(
                    "No disponible"
            );

            labelSala.setText(
                    "No disponible"
            );

            labelFecha.setText(
                    "No disponible"
            );

            labelHora.setText(
                    "No disponible"
            );

        } else {

            cargarFuncion(
                    funcion
            );
        }



        cargarAsientos();

        double total =
                reserva.calcularTotal();


        labelTotal.setText(
                String.format(
                        "$%,.0f",
                        total
                )
        );
    }


    private void cargarFuncion(
            Funcion funcion) {


        if (funcion.getPelicula() != null) {

            labelPelicula.setText(
                    funcion
                            .getPelicula()
                            .getTitulo()
            );

        } else {

            labelPelicula.setText(
                    "No disponible"
            );
        }



        if (funcion.getSala() != null) {

            labelSala.setText(
                    funcion
                            .getSala()
                            .getNombre()
            );

        } else {

            labelSala.setText(
                    "No disponible"
            );
        }


        if (funcion.getFechaFuncion() != null) {

            labelFecha.setText(
                    funcion
                            .getFechaFuncion()
                            .toString()
            );

        } else {

            labelFecha.setText(
                    "No disponible"
            );
        }


        if (funcion.getHoraInicio() != null) {

            labelHora.setText(
                    funcion
                            .getHoraInicio()
                            .toString()
            );

        } else {

            labelHora.setText(
                    "No disponible"
            );
        }
    }



    private void cargarAsientos() {

        List<DetalleReserva> detalles =
                reserva.getDetalles();


        if (
                detalles == null
                        ||
                        detalles.isEmpty()
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
                i < detalles.size();
                i++
        ) {

            DetalleReserva detalle =
                    detalles.get(i);


            Asiento asiento =
                    detalle.getAsiento();


            if (asiento != null) {

                texto.append(
                        asiento.getFila()
                );

                texto.append(
                        asiento.getNumero()
                );
            }


            if (
                    i < detalles.size() - 1
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

        labelCliente.setText(
                "No disponible"
        );

        labelPelicula.setText(
                "No disponible"
        );

        labelSala.setText(
                "No disponible"
        );

        labelFecha.setText(
                "No disponible"
        );

        labelHora.setText(
                "No disponible"
        );

        labelAsientos.setText(
                "Ninguno"
        );

        labelTotal.setText(
                "$0"
        );

        btnContinuarPago.setEnabled(
                false
        );
    }

    private void volverAAsientos() {

        principal.mostrarPantalla(
                GUIPrincipal.ASIENTOS
        );
    }

    private void continuarAlPago() {

        if (reserva == null) {

            JOptionPane.showMessageDialog(
                    principal,
                    "No existe una reserva.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        principal.setReservaActual(
                reserva
        );


        principal.mostrarPantalla(
                GUIPrincipal.PAGO
        );
    }


    public void actualizarResumen() {

        cargarInformacion();
    }
}