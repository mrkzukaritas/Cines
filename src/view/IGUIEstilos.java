package view;

import java.awt.Color;
import java.awt.Font;

public interface IGUIEstilos {


    Color COLOR_FONDO =
            new Color(
                    246,
                    240,
                    232
            );

    Color COLOR_BLANCO =
            Color.WHITE;

    Color COLOR_TEXTO =
            new Color(
                    45,
                    37,
                    35
            );

    Color COLOR_GRIS =
            new Color(
                    115,
                    105,
                    100
            );

    Color COLOR_GRIS_CLARO =
            new Color(
                    175,
                    165,
                    158
            );

    Color COLOR_BORDE =
            new Color(
                    215,
                    205,
                    198
            );



    Color COLOR_PRIMARIO =
            new Color(
                    170,
                    20,
                    55
            );

    Color COLOR_PRIMARIO_OSCURO =
            new Color(
                    65,
                    15,
                    28
            );

    Color COLOR_PRIMARIO_CLARO =
            new Color(
                    190,
                    28,
                    62
            );

    Color COLOR_DORADO =
            new Color(
                    205,
                    150,
                    55
            );

    Color COLOR_DORADO_CLARO =
            new Color(
                    235,
                    190,
                    90
            );

    Color COLOR_SECUNDARIO =
            COLOR_DORADO;


    Color COLOR_EXITO =
            new Color(
                    76,
                    175,
                    80
            );

    Color COLOR_ERROR =
            new Color(
                    220,
                    70,
                    70
            );

    Color COLOR_ADVERTENCIA =
            new Color(
                    255,
                    193,
                    7
            );

    Color COLOR_INFO =
            new Color(
                    33,
                    150,
                    243
            );



    Color COLOR_ASIENTO_DISPONIBLE =
            COLOR_EXITO;

    Color COLOR_ASIENTO_SELECCIONADO =
            COLOR_SECUNDARIO;

    Color COLOR_ASIENTO_OCUPADO =
            COLOR_ERROR;


    Font FUENTE_TITULO =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    28
            );

    Font FUENTE_TITULO_GRANDE =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    34
            );

    Font FUENTE_SUBTITULO =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    18
            );

    Font FUENTE_NORMAL =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    14
            );

    Font FUENTE_NEGRITA =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    14
            );

    Font FUENTE_PEQUENA =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    12
            );

    Font FUENTE_MUY_PEQUENA =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    11
            );

    Font FUENTE_TOTAL =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    20
            );


    int ANCHO_BOTON = 150;

    int ALTO_BOTON = 40;

    int ANCHO_BOTON_PEQUENO = 110;

    int ALTO_BOTON_PEQUENO = 32;

    int ANCHO_BOTON_GRANDE = 210;

    int ALTO_BOTON_GRANDE = 48;


    int ANCHO_CAMPO = 300;

    int ALTO_CAMPO = 38;

    int ANCHO_CAMPO_PEQUENO = 150;

    int ALTO_CAMPO_PEQUENO = 32;



    int ALTO_TABLA = 35;

    int ALTO_ENCABEZADO_TABLA = 40;



    int ANCHO_ASIENTO = 55;

    int ALTO_ASIENTO = 35;



    int ESPACIADO = 10;

    int ESPACIADO_PEQUENO = 5;

    int ESPACIADO_GRANDE = 20;


    int MARGEN = 20;

    int MARGEN_PEQUENO = 10;

    int MARGEN_GRANDE = 30;
}