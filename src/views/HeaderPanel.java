package views;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel{

    private Image imagenFondo;
    private final String textoSuperpuesto;

    public HeaderPanel(String rutaImagen){
        this(rutaImagen,null);
    }

    public HeaderPanel(String rutaImagen,String textoSuperpuesto) {
        this.textoSuperpuesto = textoSuperpuesto;

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

        if (imagenFondo == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int imgW = imagenFondo.getWidth(this);
        int imgH = imagenFondo.getHeight(this);
        if (imgW <= 0 || imgH <= 0) { g2.dispose(); return; }

        int panelW = getWidth();
        int panelH = getHeight();

        // Escala tipo "cover": llena el panel sin deformar, recorta el sobrante
        double escala = Math.max((double) panelW / imgW, (double) panelH / imgH);
        int nuevoAncho = (int) Math.ceil(imgW * escala);
        int nuevoAlto = (int) Math.ceil(imgH * escala);
        int x = (panelW - nuevoAncho) / 2;
        int y = (panelH - nuevoAlto) / 2;

        g2.drawImage(imagenFondo, x, y, nuevoAncho, nuevoAlto, this);

        // Texto superpuesto, ubicado en el tercio inferior del banner
        // (zona donde normalmente hay un area oscura en el diseño)
        if (textoSuperpuesto != null && !textoSuperpuesto.isEmpty()) {
            g2.setFont(Estilos.FUENTE_TITULO.deriveFont(Font.BOLD, 26f));
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics();
            int textoAncho = fm.stringWidth(textoSuperpuesto);
            int textoX = (panelW - textoAncho) / 2;
            int textoY = (int) (panelH * 0.85); // 85% de la altura: zona oscura inferior

            g2.drawString(textoSuperpuesto, textoX, textoY);
        }

        g2.dispose();
    }
}
