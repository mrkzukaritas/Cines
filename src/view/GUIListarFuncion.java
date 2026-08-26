package view;

import controller.FuncionController;
import controller.ReservaController;
import models.Cine;
import models.Funcion;
import models.Reserva;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;


public class GUIListarFuncion extends JPanel implements IGUIEstilos {

    private final GUIPrincipal principal;
    private final FuncionController funcionController;
    private final ReservaController reservaController;

    private JTable tablaFunciones;
    private DefaultTableModel modeloTablaFunciones;

    private JButton btnVolver;
    private JButton btnElegirAsientos;

    private JLabel labelCine;

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");


    public GUIListarFuncion(
            GUIPrincipal principal,
            FuncionController funcionController,
            ReservaController reservaController) {

        this.principal = principal;
        this.funcionController = funcionController;
        this.reservaController = reservaController;

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
                                0,
                                5
                        )
                );

        panelSuperior.setBackground(
                COLOR_FONDO
        );


        JLabel titulo =
                new JLabel(
                        "Funciones disponibles"
                );

        titulo.setFont(
                FUENTE_SUBTITULO
        );

        titulo.setForeground(
                COLOR_TEXTO
        );


        panelSuperior.add(
                titulo,
                BorderLayout.NORTH
        );


        labelCine =
                new JLabel(
                        "Cine: "
                );

        labelCine.setFont(
                FUENTE_NORMAL
        );

        labelCine.setForeground(
                COLOR_GRIS
        );


        panelSuperior.add(
                labelCine,
                BorderLayout.SOUTH
        );


        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        modeloTablaFunciones =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Película",
                                "Sala",
                                "Fecha",
                                "Hora",
                                "Formato",
                                "Precio"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };


        tablaFunciones =
                new JTable(
                        modeloTablaFunciones
                );


        tablaFunciones.setRowHeight(
                ALTO_TABLA
        );

        tablaFunciones.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaFunciones.setAutoCreateRowSorter(
                true
        );


        JScrollPane scroll =
                new JScrollPane(
                        tablaFunciones
                );


        add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBotones.setBackground(
                COLOR_FONDO
        );


        btnVolver =
                new JButton(
                        "<- Volver a cines"
                );

        btnVolver.setFont(
                FUENTE_NORMAL
        );


        btnElegirAsientos =
                new JButton(
                        "Elegir asientos"
                );

        btnElegirAsientos.setFont(
                FUENTE_NORMAL
        );

        btnElegirAsientos.setBackground(
                COLOR_PRIMARIO
        );

        btnElegirAsientos.setForeground(
                COLOR_BLANCO
        );


        panelBotones.add(
                btnVolver
        );

        panelBotones.add(
                btnElegirAsientos
        );


        add(
                panelBotones,
                BorderLayout.SOUTH
        );


        btnVolver.addActionListener(
                e -> volverACines()
        );

        btnElegirAsientos.addActionListener(
                e -> seleccionarFuncion()
        );


        tablaFunciones.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        if (
                                e.getClickCount() == 2
                                        &&
                                        SwingUtilities.isLeftMouseButton(e)
                        ) {

                            seleccionarFuncion();
                        }
                    }
                }
        );
    }

    public void refrescarTablaFunciones() {

        modeloTablaFunciones.setRowCount(0);


        Cine cine =
                principal.getCineSeleccionado();


        if (cine == null) {

            labelCine.setText(
                    "Cine: No seleccionado"
            );

            return;
        }


        labelCine.setText(
                "Cine: " + cine.getNombre()
        );


        for (
                Funcion funcion :
                cine.listarFunciones()
        ) {

            String pelicula =
                    funcion.getPelicula() != null
                            ?
                            funcion.getPelicula().getTitulo()
                            :
                            "?";


            String sala =
                    funcion.getSala() != null
                            ?
                            funcion.getSala().getNombre()
                            :
                            "?";


            String fecha =
                    funcion.getFechaFuncion() != null
                            ?
                            funcion.getFechaFuncion().toString()
                            :
                            "?";


            String hora =
                    funcion.getHoraInicio() != null
                            ?
                            funcion.getHoraInicio()
                                    .format(FORMATO_HORA)
                            :
                            "?";


            String formato =
                    funcion.getFormato() != null
                            ?
                            funcion.getFormato()
                            :
                            "?";


            double precio =
                    funcion.getPrecio();


            modeloTablaFunciones.addRow(
                    new Object[]{
                            funcion.getId(),
                            pelicula,
                            sala,
                            fecha,
                            hora,
                            formato,
                            precio
                    }
            );
        }
    }


    private void seleccionarFuncion() {

        int fila =
                tablaFunciones.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Selecciona una función primero.",
                    "Función no seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int filaModelo =
                tablaFunciones.convertRowIndexToModel(
                        fila
                );


        int idFuncion =
                Integer.parseInt(
                        modeloTablaFunciones
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );


        funcionController
                .buscarFuncionPorId(idFuncion)
                .ifPresentOrElse(

                        funcion -> {

                            // Guardar función
                            principal.setFuncionSeleccionada(
                                    funcion
                            );


                            // Verificar cliente
                            if (
                                    principal.getClienteActual()
                                            == null
                            ) {

                                JOptionPane.showMessageDialog(
                                        principal,
                                        "No hay un cliente registrado.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );

                                return;
                            }


                            // Crear reserva
                            try {

                                Reserva reserva =
                                        reservaController.crearReserva(
                                                principal.getClienteActual(),
                                                funcion
                                        );


                                // Guardar reserva
                                principal.setReservaActual(
                                        reserva
                                );


                                // Ir a selección de asientos
                                principal.mostrarPantalla(
                                        GUIPrincipal.ASIENTOS
                                );

                            } catch (Exception ex) {

                                JOptionPane.showMessageDialog(
                                        principal,
                                        "No fue posible crear la reserva.\n"
                                                + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        },

                        () -> {

                            JOptionPane.showMessageDialog(
                                    principal,
                                    "No se encontró la función seleccionada.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                );
    }


    private void volverACines() {

        principal.setFuncionSeleccionada(
                null
        );

        principal.setReservaActual(
                null
        );

        principal.mostrarPantalla(
                GUIPrincipal.CINES
        );
    }

    public void limpiarSeleccion() {

        tablaFunciones.clearSelection();
    }
}