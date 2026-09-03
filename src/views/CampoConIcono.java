package views;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CampoConIcono extends JPanel{

    private final JTextField campo;
    private IconoOjo iconoOjo;
    private boolean passwordVisible = false;
    private final boolean esPassword;

    public CampoConIcono(String etiqueta, String emoji) {
        this(etiqueta, emoji, false);
    }

    /**
     * @param etiqueta     texto de la etiqueta
     * @param emoji
     * @param esPassword   si es true, usa JPasswordField
     */

    public CampoConIcono(String etiqueta, String emoji, boolean esPassword) {
        this.esPassword = esPassword;

        setLayout(new BorderLayout(12, 4));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, Estilos.PADDING_MEDIO, 0));

        JLabel icono = new JLabel(emoji);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        icono.setForeground(Estilos.ROJO_PRINCIPAL);
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        icono.setVerticalAlignment(SwingConstants.TOP);
        icono.setPreferredSize(new Dimension(36, 30));
        icono.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel label = new JLabel(etiqueta);
        label.setFont(Estilos.FUENTE_LABEL);
        label.setForeground(Color.DARK_GRAY);

        campo = esPassword ? new JPasswordField() : new JTextField();
        campo.setFont(Estilos.FUENTE_CAMPO);
        campo.setOpaque(false);
        campo.setBorder(new MatteBorder(0, 0, 2, 0, Estilos.ROJO_PRINCIPAL));

        JPanel textoYCampo = new JPanel();
        textoYCampo.setOpaque(false);
        textoYCampo.setLayout(new BoxLayout(textoYCampo, BoxLayout.Y_AXIS));
        label.setAlignmentX(LEFT_ALIGNMENT);

        if (esPassword) {
            JPanel campoConOjo = new JPanel(new BorderLayout(6, 0));
            campoConOjo.setOpaque(false);
            campoConOjo.setAlignmentX(LEFT_ALIGNMENT);

            iconoOjo = new IconoOjo();
            iconoOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconoOjo.setToolTipText("Mostrar contraseña");
            iconoOjo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    togglePasswordVisibility();
                }
            });

            campoConOjo.add(campo, BorderLayout.CENTER);
            campoConOjo.add(iconoOjo, BorderLayout.EAST);

            textoYCampo.add(label);
            textoYCampo.add(Box.createVerticalStrut(6));
            textoYCampo.add(campoConOjo);
        } else {
            campo.setAlignmentX(LEFT_ALIGNMENT);
            textoYCampo.add(label);
            textoYCampo.add(Box.createVerticalStrut(6));
            textoYCampo.add(campo);
        }

        add(icono, BorderLayout.WEST);
        add(textoYCampo, BorderLayout.CENTER);

        setMaximumSize(new Dimension(ANCHO_MAXIMO_DEFECTO + 50, getPreferredSize().height));
    }

    private void togglePasswordVisibility() {
        if (!esPassword) return;

        JPasswordField pf = (JPasswordField) campo;
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            pf.setEchoChar((char) 0);
            iconoOjo.setTachado(false);
            iconoOjo.setToolTipText("Ocultar contraseña");
        } else {
            pf.setEchoChar('•');
            iconoOjo.setTachado(true);
            iconoOjo.setToolTipText("Mostrar contraseña");
        }
    }

    private static class IconoOjo extends JComponent {

        private boolean tachado = true; // true = contraseña oculta (ojo con diagonal)

        IconoOjo() {
            setPreferredSize(new Dimension(24, 24));
            setOpaque(false);
        }

        void setTachado(boolean tachado) {
            this.tachado = tachado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Estilos.ROJO_PRINCIPAL);
            g2.setStroke(new BasicStroke(1.8f));

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2;
            int ojoW = 18;
            int ojoH = 10;

            g2.drawArc(cx - ojoW / 2, cy - ojoH / 2, ojoW, ojoH, 0, 180);
            g2.drawArc(cx - ojoW / 2, cy - ojoH / 2, ojoW, ojoH, 180, 180);

            int pupilaR = 3;
            g2.fillOval(cx - pupilaR, cy - pupilaR, pupilaR * 2, pupilaR * 2);

            if (tachado) {
                g2.drawLine(cx - ojoW / 2 - 1, cy + ojoH / 2 + 1, cx + ojoW / 2 + 1, cy - ojoH / 2 - 1);
            }

            g2.dispose();
        }
    }

    private static final int ANCHO_MAXIMO_DEFECTO = 350;

    public void setAnchoMaximo(int anchoPx) {
        campo.setMaximumSize(new Dimension(anchoPx, campo.getPreferredSize().height));
        setMaximumSize(new Dimension(anchoPx + 50, getPreferredSize().height));
        revalidate();
    }

    public String getTexto() {
        return campo.getText().trim();
    }

    public void setTexto(String valor) {
        campo.setText(valor);
    }

    public JTextField getCampoInterno() {
        return campo;
    }
}