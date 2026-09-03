package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class BotonRedondeado extends JButton {

    private final Color colorNormal;
    private final Color colorHover;
    private boolean hover = false;
    private final int radio = 30;
    private boolean tamanioMinimoActivo = true;

    public BotonRedondeado(String texto, Color colorNormal, Color colorHover, Color colorTexto) {
        super(texto.toUpperCase());
        this.colorNormal = colorNormal;
        this.colorHover = colorHover;

        setForeground(colorTexto);
        setFont(Estilos.FUENTE_BOTON);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }

    public void usarComoBotonIcono() {
        this.tamanioMinimoActivo = false;
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hover ? colorHover : colorNormal);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radio, radio));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        if (!tamanioMinimoActivo) {
            return super.getPreferredSize();
        }
        Dimension d = super.getPreferredSize();
        return new Dimension(Math.max(d.width, 160), Math.max(d.height, 48));
    }

}