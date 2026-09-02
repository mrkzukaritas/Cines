package views;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Calendario emergente simple, en Swing puro (sin librerías externas).
 * Uso:
 *   LocalDate fecha = SelectorFecha.elegirFecha(this, LocalDate.now());
 *   if (fecha != null) { ... el usuario eligió una fecha ... }
 *   // si devuelve null, el usuario cerró la ventana sin elegir nada
 */
public class SelectorFecha {

    public static LocalDate elegirFecha(Component parent, LocalDate fechaInicial) {

        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                "Selecciona una fecha",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setLayout(new BorderLayout(5, 5));

        final LocalDate[] fechaSeleccionada = { null };
        final YearMonth[] mesActual = {
                YearMonth.from(fechaInicial != null ? fechaInicial : LocalDate.now())
        };

        JLabel labelMes = new JLabel("", SwingConstants.CENTER);
        labelMes.setFont(labelMes.getFont().deriveFont(Font.BOLD, 16f));

        JPanel panelDias = new JPanel(new GridLayout(0, 7, 4, 4));
        panelDias.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Runnable[] refrescar = new Runnable[1];
        refrescar[0] = () -> {
            panelDias.removeAll();

            String nombreMes = mesActual[0].getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            labelMes.setText(Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1) + " " + mesActual[0].getYear());

            String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
            for (String d : diasSemana) {
                JLabel l = new JLabel(d, SwingConstants.CENTER);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                panelDias.add(l);
            }

            LocalDate primerDia = mesActual[0].atDay(1);
            int espaciosVacios = primerDia.getDayOfWeek().getValue() - 1; // Lunes = 1
            for (int i = 0; i < espaciosVacios; i++) {
                panelDias.add(new JLabel(""));
            }

            int diasEnMes = mesActual[0].lengthOfMonth();
            for (int dia = 1; dia <= diasEnMes; dia++) {
                LocalDate fecha = mesActual[0].atDay(dia);
                JButton boton = new JButton(String.valueOf(dia));
                boton.setMargin(new Insets(2, 2, 2, 2));

                if (fecha.equals(LocalDate.now())) {
                    boton.setBackground(new Color(200, 230, 255));
                    boton.setOpaque(true);
                }
                if (fecha.equals(fechaInicial)) {
                    boton.setBackground(new Color(90, 140, 220));
                    boton.setForeground(Color.WHITE);
                    boton.setOpaque(true);
                }

                boton.addActionListener(e -> {
                    fechaSeleccionada[0] = fecha;
                    dialogo.dispose();
                });
                panelDias.add(boton);
            }

            panelDias.revalidate();
            panelDias.repaint();
        };

        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        btnAnterior.addActionListener(e -> {
            mesActual[0] = mesActual[0].minusMonths(1);
            refrescar[0].run();
        });
        btnSiguiente.addActionListener(e -> {
            mesActual[0] = mesActual[0].plusMonths(1);
            refrescar[0].run();
        });

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(btnAnterior, BorderLayout.WEST);
        panelSuperior.add(labelMes, BorderLayout.CENTER);
        panelSuperior.add(btnSiguiente, BorderLayout.EAST);

        JButton btnHoy = new JButton("Hoy");
        btnHoy.addActionListener(e -> {
            fechaSeleccionada[0] = LocalDate.now();
            dialogo.dispose();
        });
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialogo.dispose());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.add(btnHoy);
        panelInferior.add(btnCancelar);

        dialogo.add(panelSuperior, BorderLayout.NORTH);
        dialogo.add(panelDias, BorderLayout.CENTER);
        dialogo.add(panelInferior, BorderLayout.SOUTH);

        refrescar[0].run();

        dialogo.setSize(300, 320);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true); // bloquea aquí hasta que se elija o se cancele

        return fechaSeleccionada[0];
    }
}