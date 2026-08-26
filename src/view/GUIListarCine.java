package view;

import controller.CineController;
import models.Cine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class GUIListarCine extends JPanel implements IGUIEstilos {

    private final GUIPrincipal principal;
    private final CineController cineController;

    private JTable tablaCines;
    private DefaultTableModel modeloTablaCines;

    private JButton btnVerFunciones;

    public GUIListarCine(
            GUIPrincipal principal,
            CineController cineController) {

        this.principal = principal;
        this.cineController = cineController;

        construirGUI();

        refrescarTablaCines();
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

        JLabel titulo =
                new JLabel(
                        "Cines disponibles"
                );

        titulo.setFont(
                FUENTE_SUBTITULO
        );

        titulo.setForeground(
                COLOR_TEXTO
        );


        add(
                titulo,
                BorderLayout.NORTH
        );

        modeloTablaCines =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nombre",
                                "Ciudad",
                                "Dirección"
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


        tablaCines =
                new JTable(
                        modeloTablaCines
                );


        tablaCines.setRowHeight(
                ALTO_TABLA
        );

        tablaCines.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaCines.setAutoCreateRowSorter(
                true
        );


        JScrollPane scrollTabla =
                new JScrollPane(
                        tablaCines
                );


        add(
                scrollTabla,
                BorderLayout.CENTER
        );

         btnVerFunciones =
                new JButton(
                        "Ver funciones de este cine"
                );

        btnVerFunciones.setFont(
                FUENTE_NORMAL
        );

        btnVerFunciones.setPreferredSize(
                new Dimension(
                        220,
                        ALTO_BOTON
                )
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


        panelBotones.add(
                btnVerFunciones
        );


        add(
                panelBotones,
                BorderLayout.SOUTH
        );


        btnVerFunciones.addActionListener(
                e -> seleccionarCine()
        );


        tablaCines.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        if (
                                e.getClickCount() == 2
                                        &&
                                        SwingUtilities.isLeftMouseButton(e)
                        ) {

                            seleccionarCine();
                        }
                    }
                }
        );
    }


    public void refrescarTablaCines() {

        modeloTablaCines.setRowCount(0);

        for (
                Cine cine :
                cineController.listarCines()
        ) {

            modeloTablaCines.addRow(
                    new Object[]{
                            cine.getId(),
                            cine.getNombre(),
                            cine.getCiudad(),
                            cine.getDireccion()
                    }
            );
        }
    }



    private void seleccionarCine() {

        int fila =
                tablaCines.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Selecciona un cine primero.",
                    "Cine no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int filaModelo =
                tablaCines.convertRowIndexToModel(
                        fila
                );


        int idCine =
                Integer.parseInt(
                        modeloTablaCines
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );


        cineController
                .buscarCinePorId(idCine)
                .ifPresentOrElse(

                        cine -> {

                            // Guardar cine seleccionado
                            principal.setCineSeleccionado(
                                    cine
                            );


                            // Limpiar información anterior
                            principal.setFuncionSeleccionada(
                                    null
                            );

                            principal.setReservaActual(
                                    null
                            );


                            // Ir a funciones
                            principal.mostrarPantalla(
                                    GUIPrincipal.FUNCIONES
                            );
                        },

                        () -> {

                            JOptionPane.showMessageDialog(
                                    principal,
                                    "No se encontró el cine seleccionado.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                );
    }



    public void seleccionarPrimerCine() {

        if (
                tablaCines.getRowCount() > 0
        ) {

            tablaCines.setRowSelectionInterval(
                    0,
                    0
            );
        }
    }

    public void limpiarSeleccion() {

        tablaCines.clearSelection();
    }
}