package views;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class TarjetaPelicula extends JPanel {

    private static final int ANCHO = 190;
    private static final int ALTO_IMAGEN = 240;
    private static final int ALTO_TITULO = 55;
    private static final int RADIO = 18;

    private final PanelImagenCover panelImagen;
    private Runnable alHacerClick;
    private boolean hover = false;

    public TarjetaPelicula(String titulo, String rutaOUrlImagen) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(ANCHO, ALTO_IMAGEN + ALTO_TITULO));
        setMaximumSize(new Dimension(ANCHO, ALTO_IMAGEN + ALTO_TITULO));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ----- IMAGEN -----
        panelImagen = new PanelImagenCover();
        panelImagen.setPreferredSize(new Dimension(ANCHO, ALTO_IMAGEN));
        cargarImagenAsync(rutaOUrlImagen);
        add(panelImagen, BorderLayout.NORTH);

        // ----- TITULO -----
        JLabel labelTitulo = new JLabel(titulo.toUpperCase(), SwingConstants.CENTER);
        labelTitulo.setFont(Estilos.FUENTE_BOTON);
        labelTitulo.setForeground(Color.WHITE);

        JPanel franjaTitulo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? Estilos.ROJO_OSCURO : Estilos.ROJO_PRINCIPAL);
                // Solo redondea las esquinas inferiores (la superior ya la tapa la imagen)
                g2.fill(new RoundRectangle2D.Float(0, -RADIO, getWidth(), getHeight() + RADIO, RADIO, RADIO));
                g2.dispose();
            }
        };
        franjaTitulo.setOpaque(false);
        franjaTitulo.setPreferredSize(new Dimension(ANCHO, ALTO_TITULO));
        franjaTitulo.add(labelTitulo, BorderLayout.CENTER);

        add(franjaTitulo, BorderLayout.SOUTH);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (alHacerClick != null) alHacerClick.run();
            }
            @Override
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        };
        addMouseListener(mouseAdapter);
        panelImagen.addMouseListener(mouseAdapter);
        franjaTitulo.addMouseListener(mouseAdapter);
    }

    public void addActionListener(Runnable accion) {
        this.alHacerClick = accion;
    }

    private void cargarImagenAsync(String rutaOUrl) {
        if (rutaOUrl == null || rutaOUrl.isBlank()) return;

        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() {
                try {
                    if (rutaOUrl.startsWith("http")) {
                        return javax.imageio.ImageIO.read(new java.net.URL(rutaOUrl));
                    } else {
                        java.net.URL local = getClass().getClassLoader().getResource(rutaOUrl);
                        return local != null
                                ? new ImageIcon(local).getImage()
                                : new ImageIcon(rutaOUrl).getImage();
                    }
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    Image imagen = get();
                    if (imagen != null) {
                        panelImagen.setImagen(imagen);
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private static class PanelImagenCover extends JPanel {

        private Image imagen;

        void setImagen(Image imagen) {
            this.imagen = imagen;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            if (imagen == null) {
                g2.setColor(new Color(210, 205, 195));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int imgW = imagen.getWidth(this);
            int imgH = imagen.getHeight(this);
            if (imgW <= 0 || imgH <= 0) { g2.dispose(); return; }

            int panelW = getWidth();
            int panelH = getHeight();

            double escala = Math.max((double) panelW / imgW, (double) panelH / imgH);
            int nuevoAncho = (int) Math.ceil(imgW * escala);
            int nuevoAlto = (int) Math.ceil(imgH * escala);
            int x = (panelW - nuevoAncho) / 2;
            int y = (panelH - nuevoAlto) / 2;

            g2.drawImage(imagen, x, y, nuevoAncho, nuevoAlto, this);
            g2.dispose();
        }
    }

}
