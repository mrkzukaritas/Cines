package views;

import javax.swing.*;
import java.awt.*;

public class Estilos {

    // ----- PALETA DE COLORES -----
    public static final Color ROJO_PRINCIPAL   = new Color(139, 30, 43);
    public static final Color ROJO_OSCURO      = new Color(90, 15, 25);
    public static final Color CREMA_FONDO      = new Color(245, 240, 230);
    public static final Color BLANCO           = Color.WHITE;
    public static final Color GRIS_TEXTO       = new Color(90, 90, 90);
    public static final Color GRIS_PLACEHOLDER = new Color(160, 160, 160);

    // ----- FUENTES -----
    public static final Font FUENTE_TITULO   = new Font("Segoe UI", Font.BOLD, 34);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.ITALIC, 18);
    public static final Font FUENTE_LABEL    = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FUENTE_CAMPO    = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FUENTE_BOTON    = new Font("Segoe UI", Font.BOLD, 15);

    // ----- ESPACIADOS -----
    public static final int PADDING_GRANDE = 40;
    public static final int PADDING_MEDIO  = 20;
    public static final int PADDING_CHICO  = 8;

    public static BotonRedondeado crearBotonPrincipal(String texto) {
        return new BotonRedondeado(texto, ROJO_PRINCIPAL, ROJO_OSCURO, BLANCO);
    }

    public static BotonRedondeado crearBotonSecundario(String texto) {
        BotonRedondeado boton = new BotonRedondeado(texto, CREMA_FONDO, new Color(230, 224, 210), ROJO_PRINCIPAL);
        boton.setForeground(ROJO_PRINCIPAL);
        return boton;
    }

    public static void aplicarFondoFormulario(JComponent panel) {
        panel.setBackground(CREMA_FONDO);
        panel.setOpaque(true);
    }
}
