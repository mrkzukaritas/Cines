package views;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class SelectorFecha {

    public static LocalDate elegirFecha(Component parent, LocalDate fechaInicial) {

        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setUndecorated(true);
        dialogo.setLayout(new BorderLayout());

        final LocalDate[] fechaSeleccionada = { null };
        final YearMonth[] mesActual = {
                YearMonth.from(fechaInicial != null ? fechaInicial : LocalDate.now())
        };

        // ==========================================
        // ENCABEZADO CON EL MES/AÑO Y NAVEGACION
        // ==========================================

        JLabel labelTituloDialogo = new JLabel("Selecciona una fecha");
        labelTituloDialogo.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 15f));
        labelTituloDialogo.setForeground(Color.WHITE);
        labelTituloDialogo.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));

        JLabel labelMes = new JLabel("", SwingConstants.CENTER);
        labelMes.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 17f));
        labelMes.setForeground(Color.WHITE);

        JButton btnAnterior = crearBotonNavegacion("‹");
        JButton btnSiguiente = crearBotonNavegacion("›");

        JPanel filaNavegacion = new JPanel(new BorderLayout());
        filaNavegacion.setOpaque(false);
        filaNavegacion.add(btnAnterior, BorderLayout.WEST);
        filaNavegacion.add(labelMes, BorderLayout.CENTER);
        filaNavegacion.add(btnSiguiente, BorderLayout.EAST);

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(Estilos.ROJO_PRINCIPAL);
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        labelTituloDialogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaNavegacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        encabezado.add(labelTituloDialogo);
        encabezado.add(filaNavegacion);

        // ==========================================
        // GRILLA DE DIAS
        // ==========================================

        JPanel panelDias = new JPanel(new GridLayout(0, 7, 4, 4));
        Estilos.aplicarFondoFormulario(panelDias);
        panelDias.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        Runnable[] refrescar = new Runnable[1];
        refrescar[0] = () -> {
            panelDias.removeAll();

            String nombreMes = mesActual[0].getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            labelMes.setText(Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1) + " " + mesActual[0].getYear());

            String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
            for (String d : diasSemana) {
                JLabel l = new JLabel(d, SwingConstants.CENTER);
                l.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD));
                l.setForeground(Estilos.ROJO_PRINCIPAL);
                panelDias.add(l);
            }

            LocalDate primerDia = mesActual[0].atDay(1);
            int espaciosVacios = primerDia.getDayOfWeek().getValue() - 1; // Lunes = 1
            for (int i = 0; i < espaciosVacios; i++) {
                JLabel vacio = new JLabel("");
                vacio.setOpaque(false);
                panelDias.add(vacio);
            }

            int diasEnMes = mesActual[0].lengthOfMonth();
            for (int dia = 1; dia <= diasEnMes; dia++) {
                LocalDate fecha = mesActual[0].atDay(dia);

                boolean esHoy = fecha.equals(LocalDate.now());
                boolean esSeleccionado = fecha.equals(fechaInicial);

                JButton boton = crearBotonDia(String.valueOf(dia), esHoy, esSeleccionado);

                boton.addActionListener(e -> {
                    fechaSeleccionada[0] = fecha;
                    dialogo.dispose();
                });
                panelDias.add(boton);
            }

            panelDias.revalidate();
            panelDias.repaint();
        };

        btnAnterior.addActionListener(e -> {
            mesActual[0] = mesActual[0].minusMonths(1);
            refrescar[0].run();
        });
        btnSiguiente.addActionListener(e -> {
            mesActual[0] = mesActual[0].plusMonths(1);
            refrescar[0].run();
        });

        // ==========================================
        // BOTONES INFERIORES
        // ==========================================

        BotonRedondeado btnHoy = Estilos.crearBotonPrincipal("Hoy");
        btnHoy.addActionListener(e -> {
            fechaSeleccionada[0] = LocalDate.now();
            dialogo.dispose();
        });

        BotonRedondeado btnCancelar = Estilos.crearBotonSecundario("Cancelar");
        btnCancelar.addActionListener(e -> dialogo.dispose());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        Estilos.aplicarFondoFormulario(panelInferior);
        panelInferior.add(btnCancelar);
        panelInferior.add(btnHoy);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createLineBorder(Estilos.ROJO_PRINCIPAL, 1));
        contenedor.add(encabezado, BorderLayout.NORTH);
        contenedor.add(panelDias, BorderLayout.CENTER);
        contenedor.add(panelInferior, BorderLayout.SOUTH);

        dialogo.setContentPane(contenedor);

        // Escape cancela
        dialogo.getRootPane().registerKeyboardAction(
                e -> dialogo.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        refrescar[0].run();

        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);

        return fechaSeleccionada[0];
    }

    private static JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 18f));
        boton.setForeground(Color.WHITE);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private static JButton crearBotonDia(String texto, boolean esHoy, boolean esSeleccionado) {
        JButton boton = new JButton(texto);
        boton.setFont(Estilos.FUENTE_CAMPO);
        boton.setMargin(new Insets(4, 4, 4, 4));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);

        if (esSeleccionado) {
            boton.setBackground(Estilos.ROJO_PRINCIPAL);
            boton.setForeground(Color.WHITE);
            boton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        } else if (esHoy) {
            boton.setBackground(Estilos.CREMA_FONDO);
            boton.setForeground(Estilos.ROJO_PRINCIPAL);
            boton.setBorder(BorderFactory.createLineBorder(Estilos.ROJO_PRINCIPAL, 1));
        } else {
            boton.setBackground(Color.WHITE);
            boton.setForeground(Color.DARK_GRAY);
            boton.setBorder(BorderFactory.createLineBorder(new Color(230, 224, 210), 1));
        }

        return boton;
    }
}