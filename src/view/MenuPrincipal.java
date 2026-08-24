package view;

import controller.*;
import models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Interfaz grafica (Swing) del sistema de reservas de cine.
 * Navega entre pantallas con CardLayout: Registro -> Cines -> Funciones -> Asientos -> Resumen/Pago.
 */
public class MenuPrincipal extends JFrame {

    // ---- nombres de las "pantallas" del CardLayout ----
    private static final String PANEL_REGISTRO = "REGISTRO";
    private static final String PANEL_CINES = "CINES";
    private static final String PANEL_FUNCIONES = "FUNCIONES";
    private static final String PANEL_ASIENTOS = "ASIENTOS";
    private static final String PANEL_RESUMEN = "RESUMEN";

    // ---- controladores ----
    private final CineController cineController;
    private final FuncionController funcionController;
    private final ClienteController clienteController = new ClienteController();
    private final ReservaController reservaController = new ReservaController();
    private final PagoController pagoController = new PagoController();

    // ---- estado de la sesion ----
    private Cliente clienteActual;
    private Cine cineSeleccionado;
    private Funcion funcionSeleccionada;
    private Reserva reservaEnCurso;
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();

    // ---- layout principal ----
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedor = new JPanel(cardLayout);

    // ---- componentes que se refrescan dinamicamente ----
    private DefaultTableModel modeloTablaCines;
    private JTable tablaCines;
    private DefaultTableModel modeloTablaFunciones;
    private JTable tablaFunciones;
    private JPanel gridAsientos;
    private JLabel labelResumenAsientos;
    private JLabel labelTotal;
    private JComboBox<String> comboMetodoPago;

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public MenuPrincipal() {
        super("Sistema de Reservas - Cine");

        List<Cine> cines = crearCinesDePrueba();
        this.cineController = new CineController(cines);
        this.funcionController = new FuncionController(cines);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(650, 480));

        panelContenedor.add(construirPanelRegistro(), PANEL_REGISTRO);
        panelContenedor.add(construirPanelCines(), PANEL_CINES);
        panelContenedor.add(construirPanelFunciones(), PANEL_FUNCIONES);
        panelContenedor.add(construirPanelAsientos(), PANEL_ASIENTOS);
        panelContenedor.add(construirPanelResumen(), PANEL_RESUMEN);

        add(panelContenedor);
    }

    public void iniciar() {
        setVisible(true);
    }

    // =====================================================================
    // PANEL 1: Registro de cliente
    // =====================================================================
    private JPanel construirPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setPreferredSize(new Dimension(350, 140));

        JTextField campoNombre = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoTelefono = new JTextField();

        form.add(new JLabel("Nombre:"));
        form.add(campoNombre);
        form.add(new JLabel("Email:"));
        form.add(campoEmail);
        form.add(new JLabel("Telefono:"));
        form.add(campoTelefono);

        JButton btnContinuar = new JButton("Continuar");
        form.add(new JLabel());
        form.add(btnContinuar);

        JPanel contenedor = new JPanel(new BorderLayout(0, 15));
        JLabel titulo = new JLabel("Bienvenido al sistema de reservas de cine", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(form, BorderLayout.CENTER);

        btnContinuar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String email = campoEmail.getText().trim();
            String telefono = campoTelefono.getText().trim();
            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y email son obligatorios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            clienteActual = clienteController.registrarCliente(nombre, email, telefono);
            refrescarTablaCines();
            cardLayout.show(panelContenedor, PANEL_CINES);
        });

        panel.add(contenedor);
        return panel;
    }

    // =====================================================================
    // PANEL 2: Lista de cines
    // =====================================================================
    private JPanel construirPanelCines() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Cines disponibles");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(titulo, BorderLayout.NORTH);

        modeloTablaCines = new DefaultTableModel(new Object[]{"ID", "Nombre", "Ciudad", "Direccion"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCines = new JTable(modeloTablaCines);
        tablaCines.setRowHeight(24);
        panel.add(new JScrollPane(tablaCines), BorderLayout.CENTER);

        JButton btnVerFunciones = new JButton("Ver funciones de este cine");
        btnVerFunciones.addActionListener(e -> {
            int fila = tablaCines.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un cine primero.");
                return;
            }
            int idCine = (int) modeloTablaCines.getValueAt(fila, 0);
            cineController.buscarCinePorId(idCine).ifPresent(cine -> {
                cineSeleccionado = cine;
                refrescarTablaFunciones();
                cardLayout.show(panelContenedor, PANEL_FUNCIONES);
            });
        });
        panel.add(btnVerFunciones, BorderLayout.SOUTH);

        return panel;
    }

    private void refrescarTablaCines() {
        modeloTablaCines.setRowCount(0);
        for (Cine c : cineController.listarCines()) {
            modeloTablaCines.addRow(new Object[]{c.getId(), c.getNombre(), c.getCiudad(), c.getDireccion()});
        }
    }

    // =====================================================================
    // PANEL 3: Funciones de un cine
    // =====================================================================
    private JPanel construirPanelFunciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Funciones disponibles");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(titulo, BorderLayout.NORTH);

        modeloTablaFunciones = new DefaultTableModel(
                new Object[]{"ID", "Pelicula", "Sala", "Fecha", "Hora", "Formato", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaFunciones = new JTable(modeloTablaFunciones);
        tablaFunciones.setRowHeight(24);
        panel.add(new JScrollPane(tablaFunciones), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("<- Volver a cines");
        JButton btnElegir = new JButton("Elegir asientos");

        btnVolver.addActionListener(e -> cardLayout.show(panelContenedor, PANEL_CINES));

        btnElegir.addActionListener(e -> {
            int fila = tablaFunciones.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una funcion primero.");
                return;
            }
            int idFuncion = (int) modeloTablaFunciones.getValueAt(fila, 0);
            funcionController.buscarFuncionPorId(idFuncion).ifPresent(funcion -> {
                funcionSeleccionada = funcion;
                reservaEnCurso = reservaController.crearReserva(clienteActual, funcionSeleccionada);
                asientosSeleccionados.clear();
                refrescarGridAsientos();
                cardLayout.show(panelContenedor, PANEL_ASIENTOS);
            });
        });

        botones.add(btnVolver);
        botones.add(btnElegir);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void refrescarTablaFunciones() {
        modeloTablaFunciones.setRowCount(0);
        for (Funcion f : cineController.listarFunciones(cineSeleccionado)) {
            String pelicula = f.getPelicula() != null ? f.getPelicula().getTitulo() : "?";
            String sala = f.getSala() != null ? f.getSala().getNombre() : "?";
            modeloTablaFunciones.addRow(new Object[]{
                    f.getId(), pelicula, sala, f.getFechaFuncion(),
                    f.getHoraInicio().format(FORMATO_HORA), f.getFormato(), f.getPrecio()
            });
        }
    }

    // =====================================================================
    // PANEL 4: Seleccion de asientos (grid de botones)
    // =====================================================================
    private JPanel construirPanelAsientos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Elige tus asientos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(titulo, BorderLayout.NORTH);

        gridAsientos = new JPanel();
        panel.add(new JScrollPane(gridAsientos), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        labelResumenAsientos = new JLabel("Asientos elegidos: ninguno");
        pie.add(labelResumenAsientos, BorderLayout.NORTH);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("<- Volver a funciones");
        JButton btnContinuar = new JButton("Continuar al resumen");

        btnVolver.addActionListener(e -> {
            for (Asiento a : asientosSeleccionados) {
                a.marcarDisponible();
            }
            asientosSeleccionados.clear();
            cardLayout.show(panelContenedor, PANEL_FUNCIONES);
        });

        btnContinuar.addActionListener(e -> {
            if (asientosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Elige al menos un asiento.");
                return;
            }
            for (Asiento a : asientosSeleccionados) {
                reservaController.agregarAsiento(reservaEnCurso, a);
            }
            actualizarResumen();
            cardLayout.show(panelContenedor, PANEL_RESUMEN);
        });

        botones.add(btnVolver);
        botones.add(btnContinuar);
        pie.add(botones, BorderLayout.SOUTH);
        panel.add(pie, BorderLayout.SOUTH);

        return panel;
    }

    private void refrescarGridAsientos() {
        gridAsientos.removeAll();
        asientosSeleccionados.clear();

        Sala sala = funcionSeleccionada.getSala();
        List<Asiento> asientos = sala.consultarAsientos();

        java.util.LinkedHashMap<String, List<Asiento>> porFila = new java.util.LinkedHashMap<>();
        for (Asiento a : asientos) {
            porFila.computeIfAbsent(a.getFila(), k -> new ArrayList<>()).add(a);
        }

        gridAsientos.setLayout(new GridLayout(porFila.size(), 0, 6, 6));

        for (var entrada : porFila.entrySet()) {
            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
            for (Asiento asiento : entrada.getValue()) {
                JToggleButton boton = new JToggleButton(asiento.getFila() + String.valueOf(asiento.getNumero()));
                boton.setPreferredSize(new Dimension(55, 35));

                boolean ocupado = "OCUPADA".equals(asiento.getEstado());
                boton.setEnabled(!ocupado);
                boton.setBackground(ocupado ? new Color(220, 90, 90) : new Color(120, 200, 120));
                boton.setOpaque(true);

                boton.addActionListener(e -> {
                    if (boton.isSelected()) {
                        asientosSeleccionados.add(asiento);
                        boton.setBackground(new Color(90, 140, 220));
                    } else {
                        asientosSeleccionados.remove(asiento);
                        boton.setBackground(new Color(120, 200, 120));
                    }
                    actualizarResumenAsientosSeleccionados();
                });

                filaPanel.add(boton);
            }
            gridAsientos.add(filaPanel);
        }

        actualizarResumenAsientosSeleccionados();
        gridAsientos.revalidate();
        gridAsientos.repaint();
    }

    private void actualizarResumenAsientosSeleccionados() {
        if (asientosSeleccionados.isEmpty()) {
            labelResumenAsientos.setText("Asientos elegidos: ninguno");
            return;
        }
        StringBuilder sb = new StringBuilder("Asientos elegidos: ");
        for (Asiento a : asientosSeleccionados) {
            sb.append(a.getFila()).append(a.getNumero()).append("  ");
        }
        labelResumenAsientos.setText(sb.toString());
    }

    // =====================================================================
    // PANEL 5: Resumen y pago
    // =====================================================================
    private JPanel construirPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Resumen de tu reserva");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        labelTotal = new JLabel("Total: $0");
        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD, 15f));
        labelTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPago.add(new JLabel("Metodo de pago:"));
        comboMetodoPago = new JComboBox<>(new String[]{"Tarjeta", "Efectivo", "PSE"});
        panelPago.add(comboMetodoPago);
        panelPago.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(Box.createVerticalStrut(10));
        centro.add(labelTotal);
        centro.add(Box.createVerticalStrut(15));
        centro.add(panelPago);
        panel.add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton("Confirmar y pagar");
        btnConfirmar.addActionListener(e -> confirmarYPagar());
        botones.add(btnConfirmar);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarResumen() {
        double total = reservaEnCurso.calcularTotal();
        labelTotal.setText("Total a pagar: $" + total + "  (" + reservaEnCurso.getDetalles().size() + " asiento(s))");
    }

    private void confirmarYPagar() {
        reservaController.confirmarReserva(reservaEnCurso);

        String tipo = (String) comboMetodoPago.getSelectedItem();
        MetodoPago metodo = new MetodoPago(comboMetodoPago.getSelectedIndex() + 1, tipo);

        Pago pago = pagoController.procesarPago(reservaEnCurso, metodo);
        if (pago == null) {
            JOptionPane.showMessageDialog(this, "El metodo de pago no es valido.",
                    "Error de pago", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Pago exitoso!\nReserva #" + reservaEnCurso.getId()
                        + "\nTotal pagado: $" + pago.getMonto()
                        + "\nMetodo: " + metodo.getTipo(),
                "Reserva confirmada", JOptionPane.INFORMATION_MESSAGE);

        reservaEnCurso = null;
        funcionSeleccionada = null;
        cineSeleccionado = null;
        cardLayout.show(panelContenedor, PANEL_CINES);
    }

    // =====================================================================
    // Datos de prueba (solo modelo, sin clases externas)
    // =====================================================================
    private List<Cine> crearCinesDePrueba() {
        List<Cine> cines = new ArrayList<>();

        Pelicula p1 = new Pelicula(1, "Dune: Parte Dos", "La guerra por Arrakis continua.",
                166, "Ciencia ficcion", "PG-13", "Ingles", LocalDate.of(2024, 3, 1));
        Pelicula p2 = new Pelicula(2, "Intensamente 2", "Riley enfrenta nuevas emociones.",
                96, "Animacion", "G", "Ingles", LocalDate.of(2024, 6, 14));
        Pelicula p3 = new Pelicula(3, "Deadpool & Wolverine", "El dueto mas caotico del multiverso.",
                128, "Accion", "R", "Ingles", LocalDate.of(2024, 7, 26));

        Cine cineCentro = new Cine(1, "Cinemark Centro", "Cra 5 # 10-20", "Ibague");

        Sala sala1 = new Sala(1, "Sala 1", 20, "2D");
        sala1.setAsientos(generarAsientos(4, 5));
        Funcion f1 = new Funcion(1, LocalDate.now(), LocalTime.of(15, 0),
                LocalTime.of(16, 30), 12000, "2D", "PROGRAMADA");
        f1.setPelicula(p1);
        f1.setSala(sala1);
        f1.setTipoFuncion(TipoFuncionEnum.DOBLADA);
        sala1.getFunciones().add(f1);

        Sala sala2 = new Sala(2, "Sala 2 (VIP)", 12, "VIP");
        sala2.setAsientos(generarAsientos(3, 4));
        Funcion f2 = new Funcion(2, LocalDate.now(), LocalTime.of(18, 30),
                LocalTime.of(20, 20), 22000, "VIP", "PROGRAMADA");
        f2.setPelicula(p2);
        f2.setSala(sala2);
        f2.setTipoFuncion(TipoFuncionEnum.SUBTITULADA);
        sala2.getFunciones().add(f2);

        cineCentro.getSalas().add(sala1);
        cineCentro.getSalas().add(sala2);

        Cine cineNorte = new Cine(2, "Procinal Norte", "Av. Ambala # 45-10", "Ibague");

        Sala sala3 = new Sala(3, "Sala 1", 20, "2D");
        sala3.setAsientos(generarAsientos(4, 5));
        Funcion f3 = new Funcion(3, LocalDate.now().plusDays(1), LocalTime.of(20, 0),
                LocalTime.of(21, 40), 15000, "2D", "PROGRAMADA");
        f3.setPelicula(p3);
        f3.setSala(sala3);
        f3.setTipoFuncion(TipoFuncionEnum.ORIGINAL);
        sala3.getFunciones().add(f3);

        cineNorte.getSalas().add(sala3);

        cines.add(cineCentro);
        cines.add(cineNorte);
        return cines;
    }

    private List<Asiento> generarAsientos(int filas, int porFila) {
        List<Asiento> asientos = new ArrayList<>();
        int id = 1;
        char letra = 'A';
        for (int f = 0; f < filas; f++) {
            for (int n = 1; n <= porFila; n++) {
                asientos.add(new Asiento(id++, String.valueOf(letra), n, "DISPONIBLE"));
            }
            letra++;
        }
        return asientos;
    }
}
