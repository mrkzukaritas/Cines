package view;

import controller.ClienteController;
import models.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUIRegistro extends JPanel implements IGUIEstilos {


    private final GUIPrincipal principal;
    private final ClienteController clienteController;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;

    private JButton btnContinuar;



    public GUIRegistro(
            GUIPrincipal principal,
            ClienteController clienteController) {

        this.principal = principal;
        this.clienteController = clienteController;

        construirGUI();
    }


    private void construirGUI() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                COLOR_FONDO
        );

        setBorder(
                new EmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );



        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );


        add(
                crearContenido(),
                BorderLayout.CENTER
        );



        add(
                crearPie(),
                BorderLayout.SOUTH
        );



        btnContinuar.addActionListener(
                e -> registrarCliente()
        );
    }


    private JPanel crearEncabezado() {

        JPanel encabezado =
                new JPanel(
                        new BorderLayout()
                );

        encabezado.setBackground(
                COLOR_PRIMARIO_OSCURO
        );

        encabezado.setPreferredSize(
                new Dimension(
                        0,
                        165
                )
        );


        java.net.URL urlImagen =
                getClass().getResource(
                        "/imagenes/encabezado.png"
                );



        if (urlImagen == null) {

            JLabel error =
                    new JLabel(
                            "No se encontró encabezado.png",
                            SwingConstants.CENTER
                    );

            error.setForeground(
                    COLOR_BLANCO
            );

            error.setFont(
                    FUENTE_NORMAL
            );

            encabezado.add(
                    error,
                    BorderLayout.CENTER
            );

            return encabezado;
        }


        ImageIcon icono =
                new ImageIcon(
                        urlImagen
                );


        JLabel imagen =
                new JLabel() {

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR
                        );

                        g2.setRenderingHint(
                                RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY
                        );

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );


                        Image img =
                                icono.getImage();


                        int anchoOriginal =
                                img.getWidth(null);

                        int altoOriginal =
                                img.getHeight(null);


                        if (
                                anchoOriginal <= 0
                                        || altoOriginal <= 0
                        ) {

                            g2.dispose();

                            return;
                        }


                        int anchoDisponible =
                                getWidth();

                        int altoDisponible =
                                getHeight();


                        if (
                                anchoDisponible <= 0
                                        || altoDisponible <= 0
                        ) {

                            g2.dispose();

                            return;
                        }



                        double escalaAncho =
                                (double) anchoDisponible
                                        / anchoOriginal;

                        double escalaAlto =
                                (double) altoDisponible
                                        / altoOriginal;


                        double escala =
                                Math.min(
                                        escalaAncho,
                                        escalaAlto
                                );


                        int nuevoAncho =
                                (int)
                                        (
                                                anchoOriginal
                                                        * escala
                                        );


                        int nuevoAlto =
                                (int)
                                        (
                                                altoOriginal
                                                        * escala
                                        );


                        int x =
                                (
                                        anchoDisponible
                                                - nuevoAncho
                                ) / 2;


                        int y =
                                (
                                        altoDisponible
                                                - nuevoAlto
                                ) / 2;


                        g2.drawImage(
                                img,
                                x,
                                y,
                                nuevoAncho,
                                nuevoAlto,
                                null
                        );


                        g2.dispose();
                    }
                };


        imagen.setOpaque(
                true
        );

        imagen.setBackground(
                COLOR_PRIMARIO_OSCURO
        );


        encabezado.add(
                imagen,
                BorderLayout.CENTER
        );


        return encabezado;
    }


    private JPanel crearContenido() {

        JPanel contenedor =
                new JPanel(
                        new GridBagLayout()
                );

        contenedor.setBackground(
                COLOR_FONDO
        );

        contenedor.setBorder(
                new EmptyBorder(
                        25,
                        55,
                        15,
                        55
                )
        );


        JPanel formulario =
                new JPanel();

        formulario.setOpaque(
                false
        );

        formulario.setLayout(
                new BoxLayout(
                        formulario,
                        BoxLayout.Y_AXIS
                )
        );


        formulario.add(
                crearCampo(
                        "Nombre",
                        IconoTipo.USUARIO
                )
        );


        formulario.add(
                Box.createVerticalStrut(
                        18
                )
        );


        formulario.add(
                crearCampo(
                        "Correo electrónico",
                        IconoTipo.EMAIL
                )
        );


        formulario.add(
                Box.createVerticalStrut(
                        18
                )
        );


        formulario.add(
                crearCampo(
                        "Teléfono",
                        IconoTipo.TELEFONO
                )
        );



        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.NORTH;


        contenedor.add(
                formulario,
                gbc
        );


        return contenedor;
    }


    private JPanel crearCampo(
            String titulo,
            IconoTipo tipoIcono) {

        JPanel contenedor =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        contenedor.setOpaque(
                false
        );


        IconoPanel icono =
                new IconoPanel(
                        tipoIcono
                );

        icono.setPreferredSize(
                new Dimension(
                        28,
                        55
                )
        );


        contenedor.add(
                icono,
                BorderLayout.WEST
        );



        JPanel zona =
                new JPanel(
                        new BorderLayout()
                );

        zona.setOpaque(
                false
        );



        JLabel etiqueta =
                new JLabel(
                        titulo
                );

        etiqueta.setForeground(
                COLOR_TEXTO
        );

        etiqueta.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );


        JTextField campo =
                new JTextField();


        campo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        campo.setForeground(
                COLOR_TEXTO
        );

        campo.setBackground(
                COLOR_FONDO
        );

        campo.setOpaque(
                false
        );

        campo.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        COLOR_BORDE
                )
        );

        campo.setPreferredSize(
                new Dimension(
                        0,
                        38
                )
        );


        if (
                titulo.equals(
                        "Nombre"
                )
        ) {

            txtNombre = campo;

        } else if (
                titulo.equals(
                        "Correo electrónico"
                )
        ) {

            txtEmail = campo;

        } else {

            txtTelefono = campo;
        }


        campo.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e) {

                        campo.setBorder(
                                BorderFactory.createMatteBorder(
                                        0,
                                        0,
                                        2,
                                        0,
                                        COLOR_PRIMARIO
                                )
                        );
                    }


                    @Override
                    public void focusLost(
                            FocusEvent e) {

                        campo.setBorder(
                                BorderFactory.createMatteBorder(
                                        0,
                                        0,
                                        1,
                                        0,
                                        COLOR_BORDE
                                )
                        );
                    }
                }
        );


        zona.add(
                etiqueta,
                BorderLayout.NORTH
        );

        zona.add(
                campo,
                BorderLayout.CENTER
        );


        contenedor.add(
                zona,
                BorderLayout.CENTER
        );


        return contenedor;
    }


    private JPanel crearPie() {

        JPanel pie =
                new JPanel(
                        new BorderLayout()
                );

        pie.setBackground(
                COLOR_FONDO
        );

        pie.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                new Color(
                                        225,
                                        215,
                                        205
                                )
                        ),

                        new EmptyBorder(
                                18,
                                55,
                                25,
                                55
                        )
                )
        );


        btnContinuar =
                new JButton(
                        "CONTINUAR   →"
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );


                        Color fondo =
                                getModel().isRollover()
                                        ? COLOR_PRIMARIO_CLARO
                                        : COLOR_PRIMARIO;


                        g2.setColor(
                                fondo
                        );


                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                10,
                                10
                        );


                        g2.dispose();


                        super.paintComponent(
                                g
                        );
                    }
                };


        btnContinuar.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        btnContinuar.setForeground(
                COLOR_BLANCO
        );

        btnContinuar.setPreferredSize(
                new Dimension(
                        200,
                        50
                )
        );

        btnContinuar.setMinimumSize(
                new Dimension(
                        200,
                        50
                )
        );

        btnContinuar.setMaximumSize(
                new Dimension(
                        200,
                        50
                )
        );

        btnContinuar.setFocusPainted(
                false
        );

        btnContinuar.setBorderPainted(
                false
        );

        btnContinuar.setContentAreaFilled(
                false
        );

        btnContinuar.setOpaque(
                false
        );

        btnContinuar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        pie.add(
                btnContinuar,
                BorderLayout.EAST
        );


        return pie;
    }

    private enum IconoTipo {

        USUARIO,
        EMAIL,
        TELEFONO
    }


    private static class IconoPanel
            extends JPanel {

        private final IconoTipo tipo;


        public IconoPanel(
                IconoTipo tipo) {

            this.tipo = tipo;

            setOpaque(
                    false
            );
        }


        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    new Color(
                            181,
                            25,
                            48
                    )
            );


            g2.setStroke(
                    new BasicStroke(
                            1.8f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            int centroX =
                    getWidth() / 2;

            int centroY =
                    getHeight() / 2;


            if (
                    tipo == IconoTipo.USUARIO
            ) {

                g2.drawOval(
                        centroX - 4,
                        centroY - 11,
                        8,
                        8
                );

                g2.drawArc(
                        centroX - 8,
                        centroY - 2,
                        16,
                        13,
                        0,
                        180
                );
            }


            else if (
                    tipo == IconoTipo.EMAIL
            ) {

                int ancho = 17;
                int alto = 12;

                int x =
                        centroX - ancho / 2;

                int y =
                        centroY - alto / 2;


                g2.drawRoundRect(
                        x,
                        y,
                        ancho,
                        alto,
                        2,
                        2
                );

                g2.drawLine(
                        x,
                        y,
                        centroX,
                        y + 7
                );

                g2.drawLine(
                        centroX,
                        y + 7,
                        x + ancho,
                        y
                );
            }

            else {

                int ancho = 12;
                int alto = 20;

                int x =
                        centroX - ancho / 2;

                int y =
                        centroY - alto / 2;

                // Cuerpo del teléfono
                g2.drawRoundRect(
                        x,
                        y,
                        ancho,
                        alto,
                        3,
                        3
                );

                // Altavoz
                g2.drawLine(
                        centroX - 2,
                        y + 3,
                        centroX + 2,
                        y + 3
                );

                // Botón inferior
                g2.drawOval(
                        centroX - 1,
                        y + alto - 4,
                        2,
                        2
                );
            }
        }
    }

    private void registrarCliente() {

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        String email =
                txtEmail
                        .getText()
                        .trim();

        String telefono =
                txtTelefono
                        .getText()
                        .trim();


        if (
                nombre.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Ingresa tu nombre.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return;
        }

        if (
                email.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Ingresa tu correo electrónico.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            txtEmail.requestFocus();

            return;
        }


        if (
                !email.contains("@")
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Ingresa un correo electrónico válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtEmail.requestFocus();

            return;
        }


        if (
                telefono.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    principal,
                    "Ingresa tu teléfono.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            txtTelefono.requestFocus();

            return;
        }

        Cliente cliente =
                clienteController.registrarCliente(
                        nombre,
                        email,
                        telefono
                );


        principal.setClienteActual(
                cliente
        );

        JOptionPane.showMessageDialog(
                principal,
                "Cliente registrado correctamente.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiarCampos();


        principal.mostrarPantalla(
                GUIPrincipal.CINES
        );
    }


    public void limpiarCampos() {

        txtNombre.setText("");

        txtEmail.setText("");

        txtTelefono.setText("");

        txtNombre.requestFocus();
    }
}