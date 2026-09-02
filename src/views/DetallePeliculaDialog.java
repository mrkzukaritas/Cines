package views;

import models.Pelicula;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Popup con toda la información de una película: foto, título, sinopsis
 * y ficha técnica. Se abre ENCIMA de la pantalla actual sin cerrarla ni
 * navegar a otra parte (el CardLayout de MainFrame no se toca).
 *
 * Uso:
 *   DetallePeliculaDialog.mostrar(this, pelicula);
 */
public class DetallePeliculaDialog {

    private static final int ANCHO_IMAGEN = 220;
    private static final int ALTO_IMAGEN = 320;

    public static void mostrar(Component parent, Pelicula pelicula) {

        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                "Detalles de la película",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setLayout(new BorderLayout(0, 0));

        JPanel contenido = new JPanel(new BorderLayout(20, 0));
        Estilos.aplicarFondoFormulario(contenido);
        contenido.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_MEDIO, Estilos.PADDING_MEDIO,
                Estilos.PADDING_MEDIO, Estilos.PADDING_MEDIO
        ));

        // ---------- IMAGEN (izquierda) ----------
        JLabel labelImagen = new JLabel();
        labelImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagen.setVerticalAlignment(SwingConstants.CENTER);
        labelImagen.setOpaque(true);
        labelImagen.setBackground(new Color(210, 205, 195));
        labelImagen.setBorder(BorderFactory.createLineBorder(new Color(225, 218, 205)));
        cargarImagenAsync(labelImagen, pelicula.getRutaImagen());

        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setOpaque(false);
        panelImagen.add(labelImagen, BorderLayout.NORTH);
        contenido.add(panelImagen, BorderLayout.WEST);

        // ---------- INFO (derecha) ----------
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        JLabel labelTitulo = new JLabel(pelicula.getTitulo());
        labelTitulo.setFont(Estilos.FUENTE_TITULO.deriveFont(24f));
        labelTitulo.setForeground(Estilos.ROJO_PRINCIPAL);
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelFicha = new JLabel(construirFicha(pelicula));
        labelFicha.setFont(Estilos.FUENTE_LABEL);
        labelFicha.setForeground(Estilos.GRIS_TEXTO);
        labelFicha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelSinopsisTitulo = new JLabel("Sinopsis");
        labelSinopsisTitulo.setFont(Estilos.FUENTE_LABEL);
        labelSinopsisTitulo.setForeground(Estilos.ROJO_PRINCIPAL);
        labelSinopsisTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        String sinopsis = pelicula.getSinopsis() != null && !pelicula.getSinopsis().isBlank()
                ? pelicula.getSinopsis()
                : "Sin sinopsis disponible.";

        JTextArea areaSinopsis = new JTextArea(sinopsis);
        areaSinopsis.setFont(Estilos.FUENTE_CAMPO.deriveFont(14f));
        areaSinopsis.setForeground(new Color(60, 60, 60));
        areaSinopsis.setLineWrap(true);
        areaSinopsis.setWrapStyleWord(true);
        areaSinopsis.setEditable(false);
        areaSinopsis.setOpaque(false);
        areaSinopsis.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaSinopsis.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JScrollPane scrollSinopsis = new JScrollPane(areaSinopsis);
        scrollSinopsis.setBorder(BorderFactory.createEmptyBorder());
        scrollSinopsis.setOpaque(false);
        scrollSinopsis.getViewport().setOpaque(false);
        scrollSinopsis.setPreferredSize(new Dimension(380, 140));
        scrollSinopsis.setMaximumSize(new Dimension(380, 140));
        scrollSinopsis.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(labelTitulo);
        panelInfo.add(Box.createVerticalStrut(8));
        panelInfo.add(labelFicha);
        panelInfo.add(Box.createVerticalStrut(18));
        panelInfo.add(labelSinopsisTitulo);
        panelInfo.add(scrollSinopsis);

        contenido.add(panelInfo, BorderLayout.CENTER);

        // ---------- BOTÓN CERRAR ----------
        BotonRedondeado btnCerrar = Estilos.crearBotonSecundario("Cerrar");
        btnCerrar.addActionListener(e -> dialogo.dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Estilos.aplicarFondoFormulario(panelBoton);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, Estilos.PADDING_CHICO, Estilos.PADDING_MEDIO));
        panelBoton.add(btnCerrar);

        dialogo.add(contenido, BorderLayout.CENTER);
        dialogo.add(panelBoton, BorderLayout.SOUTH);

        dialogo.setSize(680, 420);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true); // el panel de fondo (PeliculasPanel) sigue intacto detrás
    }

    private static String construirFicha(Pelicula p) {
        StringBuilder sb = new StringBuilder("<html>");
        sb.append(p.getGenero() != null ? p.getGenero() : "").append(" &nbsp;·&nbsp; ");
        sb.append(p.getDuracion()).append(" min &nbsp;·&nbsp; ");
        sb.append(p.getClasificacion() != null ? p.getClasificacion() : "").append("<br>");
        sb.append("Idioma: ").append(p.getIdioma() != null ? p.getIdioma() : "-").append("<br>");
        if (p.getFechaEstreno() != null) {
            sb.append("Estreno: ").append(p.getFechaEstreno().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        sb.append("</html>");
        return sb.toString();
    }

    private static void cargarImagenAsync(JLabel label, String rutaOUrl) {
        if (rutaOUrl == null || rutaOUrl.isBlank()) return;

        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() {
                try {
                    if (rutaOUrl.startsWith("http")) {
                        return javax.imageio.ImageIO.read(new java.net.URL(rutaOUrl));
                    } else {
                        java.net.URL local = label.getClass().getClassLoader().getResource(rutaOUrl);
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
                        Image escalada = imagen.getScaledInstance(ANCHO_IMAGEN, ALTO_IMAGEN, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(escalada));
                        label.setText(null);
                    }
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }
}