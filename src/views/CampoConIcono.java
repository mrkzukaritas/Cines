package views;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class CampoConIcono extends JPanel{

    private final JTextField campo;

    public CampoConIcono(String etiqueta, String emoji) {
        this(etiqueta, emoji, false);
    }

    /**
     * @param etiqueta     texto de la etiqueta
     * @param emoji
     * @param esPassword   si es true, usa JPasswordField
     */

    public CampoConIcono(String etiqueta, String emoji, boolean esPassword) {
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
        campo.setAlignmentX(LEFT_ALIGNMENT);
        textoYCampo.add(label);
        textoYCampo.add(Box.createVerticalStrut(6));
        textoYCampo.add(campo);

        add(icono, BorderLayout.WEST);
        add(textoYCampo, BorderLayout.CENTER);

        setMaximumSize(new Dimension(ANCHO_MAXIMO_DEFECTO + 50, getPreferredSize().height));
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
