package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HeaderPanel extends JPanel {

    private Image imagenFondo;
    private final String textoSuperpuesto;
    private double proporcionImagen = -1;

    private static final Color COLOR_FONDO_HEADER = new Color(139, 20, 30); // ajusta al rojo exacto del banner
    private static final int ALTO_MAXIMO = 230;

    public HeaderPanel(String rutaImagen) {
        this(rutaImagen, null);
    }

    public HeaderPanel(String rutaImagen, String textoSuperpuesto) {
        this.textoSuperpuesto = textoSuperpuesto;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 200)); // valor inicial, antes de conocer el ancho real

        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
            imagenFondo = icon.getImage();

            int imgW = imagenFondo.getWidth(null);
            int imgH = imagenFondo.getHeight(null);
            if (imgW > 0 && imgH > 0) {
                proporcionImagen = (double) imgH / imgW;
            }
        } catch (Exception e) {
            imagenFondo = null;
        }

        // Cada vez que cambia el ancho del panel, recalculamos el alto ideal
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarAltoSegunAncho();
            }
        });
    }

    private void actualizarAltoSegunAncho() {
        if (proporcionImagen <= 0) return;

        int anchoActual = getWidth();
        if (anchoActual <= 0) return;

        int altoDeseado = (int) Math.round(anchoActual * proporcionImagen);
        altoDeseado = Math.min(altoDeseado, ALTO_MAXIMO);

        Dimension actual = getPreferredSize();
        if (actual.height != altoDeseado) {
            setPreferredSize(new Dimension(anchoActual, altoDeseado));
            revalidate();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();

        // Fondo de respaldo por si aún no se estabilizó el tamaño (evita parpadeos blancos)
        g2.setColor(COLOR_FONDO_HEADER);
        g2.fillRect(0, 0, panelW, panelH);

        if (imagenFondo != null) {
            int imgW = imagenFondo.getWidth(this);
            int imgH = imagenFondo.getHeight(this);

            if (imgW > 0 && imgH > 0) {
                // Al mantener la misma proporción, "contain" y "cover" casi coinciden;
                // usamos "contain" igual como red de seguridad.
                double escala = Math.min((double) panelW / imgW, (double) panelH / imgH);
                int nuevoAncho = (int) Math.round(imgW * escala);
                int nuevoAlto = (int) Math.round(imgH * escala);
                int x = (panelW - nuevoAncho) / 2;
                int y = (panelH - nuevoAlto) / 2;

                g2.drawImage(imagenFondo, x, y, nuevoAncho, nuevoAlto, this);
            }
        }

        if (textoSuperpuesto != null && !textoSuperpuesto.isEmpty()) {
            g2.setFont(Estilos.FUENTE_TITULO.deriveFont(Font.BOLD, 26f));
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics();
            int textoAncho = fm.stringWidth(textoSuperpuesto);
            int textoX = (panelW - textoAncho) / 2;
            int textoY = (int) (panelH * 0.85);

            g2.drawString(textoSuperpuesto, textoX, textoY);
        }

        g2.dispose();
    }

}