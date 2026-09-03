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
        JTextField campoFecha = new JTextField(LocalDate.now().toString());
        campoFecha.setEditable(false);

        JButton btnCalendario = new JButton("📅");
        btnCalendario.setToolTipText("Elegir fecha");
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

        JPanel panelFecha = new JPanel(new BorderLayout(4, 0));
        panelFecha.setOpaque(false);
        panelFecha.add(campoFecha, BorderLayout.CENTER);
        panelFecha.add(btnCalendario, BorderLayout.EAST);

        JTextField campoHoraInicio = new JTextField("20:00");
        JTextField campoHoraFin = new JTextField("22:00");
        JTextField campoPrecio = new JTextField();
        JTextField campoFormato = new JTextField("2D");

        comboCine.addActionListener(e -> actualizarSalasYTabla());

        JPanel form = new JPanel(new GridLayout(2, 5, 10, 4));
        Estilos.aplicarFondoFormulario(form);
        form.add(new JLabel("Cine"));
        form.add(new JLabel("Sala"));
        form.add(new JLabel("Película"));
        form.add(new JLabel("Tipo función"));
        form.add(new JLabel("Formato (2D/3D)"));
        form.add(comboCine);
        form.add(comboSala);
        form.add(comboPelicula);
        form.add(comboTipoFuncion);
        form.add(campoFormato);

        JPanel form2 = new JPanel(new GridLayout(2, 4, 10, 4));
        Estilos.aplicarFondoFormulario(form2);
        form2.add(new JLabel("Fecha"));
        form2.add(new JLabel("Hora inicio (HH:mm)"));
        form2.add(new JLabel("Hora fin (HH:mm)"));
        form2.add(new JLabel("Precio"));
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
                JOptionPane.showMessageDialog(this, "Selecciona cine, sala y película.");
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
                    JOptionPane.showMessageDialog(this,
                            "No se pudo crear la función. Revisa precio y horas.");
                    return;
                }

                campoPrecio.setText("");
                cargarTabla(cine);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
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

        sur.add(form);
        sur.add(Box.createVerticalStrut(6));
        sur.add(form2);
        sur.add(Box.createVerticalStrut(10));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        Estilos.aplicarFondoFormulario(botones);
        botones.add(btnVolver);
        botones.add(btnCancelarFuncion);
        botones.add(btnCrear);
        sur.add(botones);

        return sur;
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
}