package views;

import controller.CineController;
import controller.FuncionController;
import controller.PeliculaController;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminFuncionesPanel extends JPanel {

    private final MainFrame frame;
    private final Administrador administrador;
    private final CineController cineController;
    private final PeliculaController peliculaController;
    private final FuncionController funcionController;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JComboBox<Cine> comboCine;
    private JComboBox<Sala> comboSala;
    private JComboBox<Pelicula> comboPelicula;
    private JComboBox<TipoFuncionEnum> comboTipoFuncion;

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public AdminFuncionesPanel(
            MainFrame frame,
            Administrador administrador,
            CineController cineController,
            PeliculaController peliculaController,
            FuncionController funcionController
    ) {
        this.frame = frame;
        this.administrador = administrador;
        this.cineController = cineController;
        this.peliculaController = peliculaController;
        this.funcionController = funcionController;

        setLayout(new BorderLayout(10, 10));
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoGFunciones.png");
        header.setPreferredSize(new Dimension(0, 90));
        add(header, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Película", "Sala", "Fecha", "Hora", "Precio"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 224, 210)));
        scroll.setPreferredSize(new Dimension(0, 200));

        JPanel panelTabla = new JPanel(new BorderLayout());
        Estilos.aplicarFondoFormulario(panelTabla);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_MEDIO, Estilos.PADDING_MEDIO, 0, Estilos.PADDING_MEDIO));

        JLabel labelTabla = new JLabel("Funciones del cine seleccionado");
        labelTabla.setFont(Estilos.FUENTE_LABEL);
        labelTabla.setForeground(Estilos.ROJO_PRINCIPAL);
        labelTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        panelTabla.add(labelTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        add(construirFormulario(), BorderLayout.SOUTH);

        cargarCombos();
    }

    private void estilizarTabla(JTable t) {
        t.setRowHeight(28);
        t.setFont(Estilos.FUENTE_CAMPO);
        t.setGridColor(new Color(230, 224, 210));
        t.setSelectionBackground(new Color(240, 210, 210));
        t.setSelectionForeground(Color.DARK_GRAY);
        t.setShowGrid(true);

        JTableHeader encabezadoTabla = t.getTableHeader();
        encabezadoTabla.setFont(Estilos.FUENTE_LABEL);
        encabezadoTabla.setBackground(Estilos.ROJO_PRINCIPAL);
        encabezadoTabla.setForeground(Color.WHITE);
        encabezadoTabla.setPreferredSize(new Dimension(0, 32));
    }

    private JPanel construirFormulario() {

        comboCine = new JComboBox<>();
        comboSala = new JComboBox<>();
        comboPelicula = new JComboBox<>();
        comboTipoFuncion = new JComboBox<>(TipoFuncionEnum.values());
        estilizarCombo(comboCine);
        estilizarCombo(comboSala);
        estilizarCombo(comboPelicula);
        estilizarCombo(comboTipoFuncion);

        // ---------- Campo de fecha con selector de calendario ----------
        JTextField campoFecha = crearCampoTexto(LocalDate.now().toString());
        campoFecha.setEditable(false);

        BotonRedondeado btnCalendario = Estilos.crearBotonSecundario("");
        btnCalendario.usarComoBotonIcono();
        btnCalendario.setIcon(new IconoCalendario(16));
        btnCalendario.setToolTipText("Elegir fecha");
        btnCalendario.setPreferredSize(new Dimension(28, 28));
        btnCalendario.setMinimumSize(new Dimension(28, 28));
        btnCalendario.setMaximumSize(new Dimension(28, 28));
        btnCalendario.addActionListener(e -> {
            LocalDate actual;
            try {
                actual = LocalDate.parse(campoFecha.getText().trim());
            } catch (Exception ex) {
                actual = LocalDate.now();
            }
            LocalDate elegida = SelectorFecha.elegirFecha(this, actual);
            if (elegida != null) {
                campoFecha.setText(elegida.toString());
            }
        });

        JPanel panelFecha = new JPanel(new BorderLayout(6, 0));
        panelFecha.setOpaque(false);
        panelFecha.add(campoFecha, BorderLayout.CENTER);
        panelFecha.add(btnCalendario, BorderLayout.EAST);

        JTextField campoHoraInicio = crearCampoTexto("20:00");
        JTextField campoHoraFin = crearCampoTexto("22:00");
        JTextField campoPrecio = crearCampoTexto("");
        JTextField campoFormato = crearCampoTexto("2D");

        comboCine.addActionListener(e -> actualizarSalasYTabla());

        JPanel form = new JPanel(new GridLayout(2, 5, 10, 4));
        form.setOpaque(false);
        form.add(crearEtiquetaFormulario("Cine"));
        form.add(crearEtiquetaFormulario("Sala"));
        form.add(crearEtiquetaFormulario("Película"));
        form.add(crearEtiquetaFormulario("Tipo función"));
        form.add(crearEtiquetaFormulario("Formato (2D/3D)"));
        form.add(comboCine);
        form.add(comboSala);
        form.add(comboPelicula);
        form.add(comboTipoFuncion);
        form.add(campoFormato);

        JPanel form2 = new JPanel(new GridLayout(2, 4, 10, 4));
        form2.setOpaque(false);
        form2.add(crearEtiquetaFormulario("Fecha"));
        form2.add(crearEtiquetaFormulario("Hora inicio (HH:mm)"));
        form2.add(crearEtiquetaFormulario("Hora fin (HH:mm)"));
        form2.add(crearEtiquetaFormulario("Precio"));
        form2.add(panelFecha);
        form2.add(campoHoraInicio);
        form2.add(campoHoraFin);
        form2.add(campoPrecio);

        BotonRedondeado btnCrear = Estilos.crearBotonPrincipal("Crear función");
        BotonRedondeado btnCancelarFuncion = Estilos.crearBotonSecundario("Cancelar seleccionada");
        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver al panel admin");

        btnCrear.addActionListener(e -> {
            Cine cine = (Cine) comboCine.getSelectedItem();
            Sala sala = (Sala) comboSala.getSelectedItem();
            Pelicula pelicula = (Pelicula) comboPelicula.getSelectedItem();
            TipoFuncionEnum tipo = (TipoFuncionEnum) comboTipoFuncion.getSelectedItem();

            if (cine == null || sala == null || pelicula == null) {
                DialogoEstilizado.mostrarAdvertencia(this, "Falta seleccionar", "Selecciona cine, sala y película.");
                return;
            }

            try {
                LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
                LocalTime inicio = LocalTime.parse(campoHoraInicio.getText().trim());
                LocalTime fin = LocalTime.parse(campoHoraFin.getText().trim());
                double precio = Double.parseDouble(campoPrecio.getText().trim());

                Funcion creada = funcionController.crearFuncion(
                        cine, sala, pelicula, tipo, fecha, inicio, fin, precio,
                        campoFormato.getText().trim()
                );

                if (creada == null) {
                    DialogoEstilizado.mostrarError(this, "Error",
                            "No se pudo crear la función. Revisa precio y horas.");
                    return;
                }

                campoPrecio.setText("");
                cargarTabla(cine);

            } catch (Exception ex) {
                DialogoEstilizado.mostrarError(this, "Datos inválidos",
                        "Revisa el formato de hora (HH:mm) y precio.\n" + ex.getMessage());
            }
        });

        btnCancelarFuncion.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                DialogoEstilizado.mostrarAdvertencia(this, "Falta seleccionar", "Selecciona una función primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            funcionController.cancelarFuncion(id);

            Cine cine = (Cine) comboCine.getSelectedItem();
            if (cine != null) {
                cargarTabla(cine);
            }
        });

        btnVolver.addActionListener(e -> frame.mostrarAdministrador(administrador));

        JPanel sur = new JPanel();
        sur.setLayout(new BoxLayout(sur, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(sur);
        sur.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        sur.add(envolverEnTarjeta(form));
        sur.add(Box.createVerticalStrut(10));
        sur.add(envolverEnTarjeta(form2));
        sur.add(Box.createVerticalStrut(12));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        Estilos.aplicarFondoFormulario(botones);
        botones.add(btnVolver);
        botones.add(btnCancelarFuncion);
        botones.add(btnCrear);
        sur.add(botones);

        return sur;
    }

    /**
     * Envuelve un panel de campos en una "tarjeta" blanca con borde redondeado,
     * para separar visualmente los grupos de datos del formulario.
     */
    private JPanel envolverEnTarjeta(JPanel contenido) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 224, 210), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private JTextField crearCampoTexto(String valorInicial) {
        JTextField campo = new JTextField(valorInicial);
        campo.setFont(Estilos.FUENTE_CAMPO.deriveFont(15f));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Estilos.ROJO_PRINCIPAL),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        campo.setOpaque(false);
        return campo;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(Estilos.FUENTE_CAMPO.deriveFont(15f));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Estilos.ROJO_PRINCIPAL),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
    }

    private JLabel crearEtiquetaFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 14f));
        label.setForeground(Estilos.ROJO_PRINCIPAL);
        return label;
    }

    private void actualizarSalasYTabla() {
        comboSala.removeAllItems();
        Cine cine = (Cine) comboCine.getSelectedItem();
        if (cine != null) {
            for (Sala s : cine.getSalas()) {
                comboSala.addItem(s);
            }
            cargarTabla(cine);
        }
    }

    private void cargarCombos() {
        comboCine.removeAllItems();
        for (Cine c : cineController.listarCines()) {
            comboCine.addItem(c);
        }
        comboPelicula.removeAllItems();
        for (Pelicula p : peliculaController.listarPeliculas()) {
            comboPelicula.addItem(p);
        }
        modeloTabla.setRowCount(0);

        // IMPORTANTE: agregar items a un combo vacío selecciona el primero
        // automáticamente, pero NO dispara el ActionListener. Por eso hay
        // que llamar esto a mano, o la tabla de funciones queda vacía
        // hasta que el usuario cambie de cine manualmente.
        actualizarSalasYTabla();
    }

    private void cargarTabla(Cine cine) {
        modeloTabla.setRowCount(0);
        for (Funcion f : funcionController.listarPorCine(cine)) {
            String pelicula = f.getPelicula() != null ? f.getPelicula().getTitulo() : "?";
            modeloTabla.addRow(new Object[]{
                    f.getId(), pelicula, f.getSala().getNombre(), f.getFechaFuncion(),
                    f.getHoraInicio().format(FORMATO_HORA), f.getPrecio()
            });
        }
    }

    /**
     * Ícono de calendario dibujado a mano con Graphics2D.
     * Evita depender de que la fuente del sistema tenga glyphs de emoji
     * (📅 se veía como un ícono genérico en algunos entornos).
     */
    private static class IconoCalendario implements Icon {
        private final int tam;

        IconoCalendario(int tam) {
            this.tam = tam;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = tam;
            int alto = tam;

            // Cuerpo del calendario
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y + 3, ancho, alto - 3, 3, 3);

            // Encabezado rojo
            g2.setColor(Estilos.ROJO_PRINCIPAL);
            g2.fillRoundRect(x, y + 3, ancho, 5, 3, 3);

            // Borde
            g2.setColor(Estilos.ROJO_PRINCIPAL);
            g2.drawRoundRect(x, y + 3, ancho - 1, alto - 4, 3, 3);

            // Anillas
            g2.fillRect(x + 3, y, 2, 5);
            g2.fillRect(x + ancho - 5, y, 2, 5);

            // Línea del día
            g2.drawLine(x + 3, y + alto - 6, x + ancho - 3, y + alto - 6);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tam;
        }

        @Override
        public int getIconHeight() {
            return tam;
        }
    }
}