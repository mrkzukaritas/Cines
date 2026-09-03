package views;

import javax.swing.*;
import java.awt.*;

public class DialogoEstilizado {

    public static void mostrarExito(Component padre, String titulo, String mensaje) {
        mostrar(padre, titulo, mensaje, "check", new Color(60, 140, 90));
    }

    public static void mostrarError(Component padre, String titulo, String mensaje) {
        mostrar(padre, titulo, mensaje, "x", Estilos.ROJO_PRINCIPAL);
    }

    public static void mostrarAdvertencia(Component padre, String titulo, String mensaje) {
        mostrar(padre, titulo, mensaje, "!", new Color(200, 150, 40));
    }

    /** Diálogo de confirmación con Cancelar */
    public static boolean confirmar(Component padre, String titulo, String mensaje) {

        JDialog dialogo = crearBase(padre);
        boolean[] resultado = {false};

        JPanel contenido = crearContenido(titulo, mensaje, "?", Estilos.ROJO_PRINCIPAL);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        Estilos.aplicarFondoFormulario(panelBotones);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        BotonRedondeado btnNo = Estilos.crearBotonSecundario("Cancelar");
        BotonRedondeado btnSi = Estilos.crearBotonPrincipal("Sí, continuar");

        btnNo.addActionListener(e -> { resultado[0] = false; dialogo.dispose(); });
        btnSi.addActionListener(e -> { resultado[0] = true; dialogo.dispose(); });

        panelBotones.add(btnNo);
        panelBotones.add(btnSi);
        contenido.add(panelBotones, BorderLayout.SOUTH);

        // Enter confirma (equivale a "Sí"), Escape cancela
        dialogo.getRootPane().registerKeyboardAction(
                e -> { resultado[0] = true; dialogo.dispose(); },
                KeyStroke.getKeyStroke("ENTER"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        dialogo.getRootPane().registerKeyboardAction(
                e -> dialogo.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialogo.setContentPane(contenido);
        dialogo.pack();
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);

        return resultado[0];
    }

    private static void mostrar(Component padre, String titulo, String mensaje, String icono, Color colorIcono) {

        JDialog dialogo = crearBase(padre);

        JPanel contenido = crearContenido(titulo, mensaje, icono, colorIcono);

        BotonRedondeado btnAceptar = Estilos.crearBotonPrincipal("Aceptar");
        btnAceptar.addActionListener(e -> dialogo.dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        Estilos.aplicarFondoFormulario(panelBoton);
        panelBoton.add(btnAceptar);
        contenido.add(panelBoton, BorderLayout.SOUTH);

        // Enter cierra el dialogo
        dialogo.getRootPane().registerKeyboardAction(
                e -> dialogo.dispose(),
                KeyStroke.getKeyStroke("ENTER"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialogo.setContentPane(contenido);
        dialogo.pack();
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
    }


    private static JDialog crearBase(Component padre) {
        Window ventana = SwingUtilities.getWindowAncestor(padre);
        JDialog dialogo = new JDialog(ventana, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setUndecorated(true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return dialogo;
    }


    private static JPanel crearContenido(String titulo, String mensaje, String icono, Color colorIcono) {

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createLineBorder(colorIcono, 1));

        // ---------- Encabezado de color ----------
        JPanel encabezado = new JPanel(new BorderLayout(12, 0));
        encabezado.setBackground(colorIcono);
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel circuloIcono = crearCirculoIcono(icono);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 17f));
        labelTitulo.setForeground(Color.WHITE);

        encabezado.add(circuloIcono, BorderLayout.WEST);
        encabezado.add(labelTitulo, BorderLayout.CENTER);

        // ---------- Cuerpo del mensaje ----------
        Estilos.aplicarFondoFormulario(contenedor);

        JLabel labelMensaje = new JLabel(
                "<html><div style='width:260px;'>"
                        + mensaje.replace("\n", "<br>")
                        + "</div></html>"
        );
        labelMensaje.setFont(Estilos.FUENTE_LABEL);
        labelMensaje.setForeground(Color.DARK_GRAY);
        labelMensaje.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        contenedor.add(encabezado, BorderLayout.NORTH);
        contenedor.add(labelMensaje, BorderLayout.CENTER);

        return contenedor;
    }


    private static JPanel crearCirculoIcono(String icono) {
        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());

                Color colorTrazo = getParent() != null ? getParent().getBackground() : Color.DARK_GRAY;
                g2.setColor(colorTrazo);
                g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int w = getWidth(), h = getHeight();

                if ("check".equals(icono)) {
                    g2.drawLine((int) (w * 0.26), (int) (h * 0.52), (int) (w * 0.42), (int) (h * 0.68));
                    g2.drawLine((int) (w * 0.42), (int) (h * 0.68), (int) (w * 0.76), (int) (h * 0.30));

                } else if ("x".equals(icono)) {
                    g2.drawLine((int) (w * 0.28), (int) (h * 0.28), (int) (w * 0.72), (int) (h * 0.72));
                    g2.drawLine((int) (w * 0.72), (int) (h * 0.28), (int) (w * 0.28), (int) (h * 0.72));

                } else {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    FontMetrics fm = g2.getFontMetrics();
                    int textoX = (w - fm.stringWidth(icono)) / 2;
                    int textoY = (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(icono, textoX, textoY);
                }

                g2.dispose();
            }
        };
        circulo.setOpaque(false);
        circulo.setPreferredSize(new Dimension(34, 34));
        return circulo;
    }
}