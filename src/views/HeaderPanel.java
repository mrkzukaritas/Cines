package views;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel{

    private Image imagenFondo;

    public HeaderPanel(String rutaImagen) {
        setPreferredSize(new Dimension(0, 200));
        setLayout(new BorderLayout());

        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
            imagenFondo = icon.getImage();
        } catch (Exception e) {
            imagenFondo = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
