package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AsientoBoton extends JToggleButton{

    private final boolean ocupado;
    private final int radio = 10;

    public AsientoBoton(String texto, boolean ocupado) {
        super(texto);
        this.ocupado = ocupado;

        setFont(Estilos.FUENTE_LABEL);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setPreferredSize(new Dimension(60, 40));

        if (ocupado) {
            setEnabled(false);
        } else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        getModel().addChangeListener(e -> repaint());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D forma = new RoundRectangle2D.Float(
                1, 1, getWidth() - 2, getHeight() - 2, radio, radio);

        if (ocupado) {
            g2.setColor(new Color(200, 200, 200));
            g2.fill(forma);
            setForeground(new Color(140, 140, 140));

        } else if (isSelected()) {
            g2.setColor(Estilos.ROJO_PRINCIPAL);
            g2.fill(forma);
            setForeground(Color.WHITE);

        } else {
            g2.setColor(Estilos.CREMA_FONDO);
            g2.fill(forma);
            g2.setColor(Estilos.ROJO_PRINCIPAL);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(
                    1.5f, 1.5f, getWidth() - 3f, getHeight() - 3f, radio, radio));
            setForeground(Estilos.ROJO_PRINCIPAL);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
