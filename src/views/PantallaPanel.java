package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class PantallaPanel extends JPanel{

    public PantallaPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(0, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int margenLateral = Math.max(ancho / 6, 40);
        int curvaAncho = ancho - (margenLateral * 2);
        int curvaAlto = 22;
        int y = 6;

        GradientPaint degradado = new GradientPaint(
                margenLateral, y, Estilos.ROJO_PRINCIPAL,
                margenLateral, y + curvaAlto, new Color(220, 210, 195)
        );
        g2.setPaint(degradado);
        g2.fill(new Arc2D.Float(
                margenLateral, y, curvaAncho, curvaAlto * 2,
                0, -180, Arc2D.CHORD
        ));

        g2.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 13f));
        g2.setColor(Estilos.GRIS_TEXTO);
        FontMetrics fm = g2.getFontMetrics();
        String texto = "P A N T A L L A";
        int textoX = (ancho - fm.stringWidth(texto)) / 2;
        g2.drawString(texto, textoX, y + curvaAlto + fm.getAscent() + 6);

        g2.dispose();
    }

}
