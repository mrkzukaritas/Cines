package view;

import controller.ReservaController;
import models.Asiento;
import models.Funcion;
import models.Reserva;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI para seleccionar los asientos de una función.
 *
 * Esta vista trabaja directamente con:
 * - GUIPrincipal
 * - Funcion
 * - Reserva
 * - ReservaController
 *
 * No utiliza SesionReserva.
 */
public class GUISeleccionarAsiento extends JPanel implements IGUIEstilos {

    // ============================================================
    // ATRIBUTOS
    // ============================================================

    private final GUIPrincipal principal;
    private final Funcion funcionSeleccionada;

    private Reserva reserva;

    private final ReservaController reservaController;

    private final List<Asiento> asientosSeleccionados;

    private JPanel gridAsientos;

    private JLabel labelFuncion;
    private JLabel labelAsientosSeleccionados;

    private JButton btnVolver;
    private JButton btnContinuar;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public GUISeleccionarAsiento(
            GUIPrincipal principal,
            Funcion funcionSeleccionada) {

        this.principal = principal;
        this.funcionSeleccionada = funcionSeleccionada;

        this.reserva =
                principal.getReservaActual();

        this.reservaController =
                principal.getReservaController();

        this.asientosSeleccionados =
                new ArrayList<>();

        construirGUI();
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
                        15,
                        15,
                        15,
                        15
                )
        );

        setBackground(
                COLOR_FONDO
        );


        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        panelSuperior.setBackground(
                COLOR_FONDO
        );


        JLabel titulo =
                new JLabel(
                        "Seleccionar asientos"
                );

        titulo.setFont(
                FUENTE_TITULO
        );

        titulo.setForeground(
                COLOR_TEXTO
        );


        labelFuncion =
                new JLabel(
                        obtenerInformacionFuncion()
                );

        labelFuncion.setFont(
                FUENTE_NORMAL
        );

        labelFuncion.setForeground(
                COLOR_GRIS
        );


        panelSuperior.add(
                titulo,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                labelFuncion,
                BorderLayout.SOUTH
        );


        add(
                panelSuperior,
                BorderLayout.NORTH
        );


        gridAsientos =
                new JPanel();

        gridAsientos.setBackground(
                COLOR_BLANCO
        );

        gridAsientos.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        JScrollPane scroll =
                new JScrollPane(
                        gridAsientos
                );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        COLOR_BORDE
                )
        );


        add(
                scroll,
                BorderLayout.CENTER
        );


        JPanel panelInferior =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panelInferior.setBackground(
                COLOR_FONDO
        );


        labelAsientosSeleccionados =
                new JLabel(
                        "Asientos seleccionados: ninguno"
                );

        labelAsientosSeleccionados.setFont(
                FUENTE_NORMAL
        );

        labelAsientosSeleccionados.setForeground(
                COLOR_TEXTO
        );


        panelInferior.add(
                labelAsientosSeleccionados,
                BorderLayout.NORTH
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
                        "<- Volver a funciones"
                );

        btnVolver.setFont(
                FUENTE_NORMAL
        );


        btnContinuar =
                new JButton(
                        "Continuar"
                );

        btnContinuar.setFont(
                FUENTE_NEGRITA
        );

        btnContinuar.setBackground(
                COLOR_PRIMARIO
        );

        btnContinuar.setForeground(
                COLOR_BLANCO
        );

        btnContinuar.setFocusPainted(
                false
        );


        panelBotones.add(
                btnVolver
        );

        panelBotones.add(
                btnContinuar
        );


        panelInferior.add(
                panelBotones,
                BorderLayout.SOUTH
        );


        add(
                panelInferior,
                BorderLayout.SOUTH
        );



        btnVolver.addActionListener(
                e -> volverAFunciones()
        );


        btnContinuar.addActionListener(
                e -> continuar()
        );


        construirAsientos();
    }


    private String obtenerInformacionFuncion() {

        if (funcionSeleccionada == null) {

            return "Función no seleccionada";
        }


        String pelicula =
                funcionSeleccionada.getPelicula() != null
                        ?
                        funcionSeleccionada
                                .getPelicula()
                                .getTitulo()
                        :
                        "Sin película";


        String fecha =
                funcionSeleccionada.getFechaFuncion() != null
                        ?
                        funcionSeleccionada
                                .getFechaFuncion()
                                .toString()
                        :
                        "Sin fecha";


        String hora =
                funcionSeleccionada.getHoraInicio() != null
                        ?
                        funcionSeleccionada
                                .getHoraInicio()
                                .toString()
                        :
                        "Sin hora";


        return "Película: "
                + pelicula
                + " | Fecha: "
                + fecha
                + " | Hora: "
                + hora;
    }



    private void construirAsientos() {

        gridAsientos.removeAll();


        if (funcionSeleccionada == null) {

            return;
        }


        if (funcionSeleccionada.getSala() == null) {

            JLabel mensaje =
                    new JLabel(
                            "Esta función no tiene una sala asignada.",
                            SwingConstants.CENTER
                    );

            mensaje.setFont(
                    FUENTE_NORMAL
            );

            gridAsientos.add(
                    mensaje
            );

            return;
        }


        List<Asiento> asientos =
                funcionSeleccionada
                        .getSala()
                        .consultarAsientos();


        if (asientos == null || asientos.isEmpty()) {

            JLabel mensaje =
                    new JLabel(
                            "No hay asientos disponibles.",
                            SwingConstants.CENTER
                    );

            mensaje.setFont(
                    FUENTE_NORMAL
            );

            gridAsientos.add(
                    mensaje
            );

            return;
        }


        Map<String, List<Asiento>> porFila =
                new LinkedHashMap<>();


        for (Asiento asiento : asientos) {

            porFila
                    .computeIfAbsent(
                            asiento.getFila(),
                            k -> new ArrayList<>()
                    )
                    .add(asiento);
        }


        gridAsientos.setLayout(
                new GridLayout(
                        porFila.size(),
                        1,
                        5,
                        5
                )
        );


        for (
                Map.Entry<String, List<Asiento>> entrada :
                porFila.entrySet()
        ) {

            JPanel fila =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.CENTER,
                                    6,
                                    5
                            )
                    );

            fila.setBackground(
                    COLOR_BLANCO
            );


            JLabel etiquetaFila =
                    new JLabel(
                            entrada.getKey()
                    );

            etiquetaFila.setFont(
                    FUENTE_NEGRITA
            );

            etiquetaFila.setPreferredSize(
                    new Dimension(
                            25,
                            35
                    )
            );


            fila.add(
                    etiquetaFila
            );


            for (
                    Asiento asiento :
                    entrada.getValue()
            ) {

                crearBotonAsiento(
                        fila,
                        asiento
                );
            }


            gridAsientos.add(
                    fila
            );
        }


        gridAsientos.revalidate();
        gridAsientos.repaint();
    }

   private void crearBotonAsiento(
            JPanel fila,
            Asiento asiento) {

        JToggleButton boton =
                new JToggleButton(
                        asiento.getFila()
                                +
                                asiento.getNumero()
                );


        boton.setPreferredSize(
                new Dimension(
                        55,
                        35
                )
        );


        boton.setFont(
                FUENTE_PEQUENA
        );


        boolean ocupado =
                "OCUPADA".equals(
                        asiento.getEstado()
                );


        if (ocupado) {

            boton.setEnabled(
                    false
            );

            boton.setBackground(
                    COLOR_ERROR
            );

            boton.setToolTipText(
                    "Asiento ocupado"
            );
        }

        else {

            boton.setBackground(
                    COLOR_EXITO
            );

            boton.setOpaque(
                    true
            );

            boton.setFocusPainted(
                    false
            );

            boton.setToolTipText(
                    "Asiento disponible"
            );


            boton.addActionListener(
                    e -> {

                        if (boton.isSelected()) {

                            if (
                                    !asientosSeleccionados
                                            .contains(asiento)
                            ) {

                                asientosSeleccionados.add(
                                        asiento
                                );
                            }

                            boton.setBackground(
                                    COLOR_SECUNDARIO
                            );

                        } else {

                            asientosSeleccionados.remove(
                                    asiento
                            );

                            boton.setBackground(
                                    COLOR_EXITO
                            );
                        }


                        actualizarAsientos();
                    }
            );
        }


        fila.add(
                boton
        );
    }



    private void actualizarAsientos() {

        if (
                asientosSeleccionados.isEmpty()
        ) {

            labelAsientosSeleccionados.setText(
                    "Asientos seleccionados: ninguno"
            );

            return;
        }


        StringBuilder texto =
                new StringBuilder(
                        "Asientos seleccionados: "
                );


        for (
                int i = 0;
                i < asientosSeleccionados.size();
                i++
        ) {

            Asiento asiento =
                    asientosSeleccionados.get(i);


            texto.append(
                    asiento.getFila()
            );

            texto.append(
                    asiento.getNumero()
            );


            if (
                    i <
                            asientosSeleccionados.size() - 1
            ) {

                texto.append(
                        ", "
                );
            }
        }


        labelAsientosSeleccionados.setText(
                texto.toString()
        );
    }


    private void continuar() {


        if (
                asientosSeleccionados.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Debes seleccionar al menos un asiento.",
                    "Asientos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (reserva == null) {

            JOptionPane.showMessageDialog(
                    principal,
                    "No existe una reserva.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }



        for (
                Asiento asiento :
                asientosSeleccionados
        ) {

            boolean agregado =
                    reservaController.agregarAsiento(
                            reserva,
                            asiento
                    );


            if (!agregado) {

                JOptionPane.showMessageDialog(
                        principal,
                        "El asiento "
                                + asiento.getFila()
                                + asiento.getNumero()
                                + " ya no está disponible.",
                        "Asiento no disponible",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }



        principal.setReservaActual(
                reserva
        );



        principal.mostrarPantalla(
                GUIPrincipal.RESERVA
        );
    }


    private void volverAFunciones() {

        asientosSeleccionados.clear();


        principal.mostrarPantalla(
                GUIPrincipal.FUNCIONES
        );
    }
}