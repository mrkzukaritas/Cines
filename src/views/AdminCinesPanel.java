package views;

import controller.CineController;
import models.Administrador;
import models.Cine;
import models.Sala;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminCinesPanel extends JPanel {

    private final MainFrame frame;
    private final Administrador administrador;
    private final CineController cineController;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private DefaultTableModel modeloTablaSalas;
    private JTable tablaSalas;

    // Campos del formulario de sala
    private JTextField campoNombreSala;
    private JTextField campoCapacidad;
    private JTextField campoTipoSala;

    public AdminCinesPanel(
            MainFrame frame,
            Administrador administrador,
            CineController cineController
    ) {
        this.frame = frame;
        this.administrador = administrador;
        this.cineController = cineController;

        setLayout(new BorderLayout(10, 10));
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoGCines.png");
        header.setPreferredSize(new Dimension(0, 90));
        add(header, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Ciudad", "Dirección", "# Salas"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        modeloTablaSalas = new DefaultTableModel(
                new Object[]{"ID Sala", "Nombre", "Capacidad", "Tipo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSalas = new JTable(modeloTablaSalas);
        estilizarTabla(tablaSalas);

        // Al seleccionar un cine, se cargan sus salas en la tabla de abajo.
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarTablaSalas();
            }
        });

        // Al seleccionar una sala, se rellenan los campos del formulario
        tablaSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCamposSalaSeleccionada();
            }
        });

        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 12, 12));
        Estilos.aplicarFondoFormulario(panelTablas);
        panelTablas.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_MEDIO, Estilos.PADDING_MEDIO, 0, Estilos.PADDING_MEDIO));
        panelTablas.add(envolverConTitulo("Cines", tabla));
        panelTablas.add(envolverConTitulo("Salas del cine seleccionado", tablaSalas));
        add(panelTablas, BorderLayout.CENTER);

        add(construirFormulario(), BorderLayout.SOUTH);

        cargarTabla();
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

    private JPanel envolverConTitulo(String titulo, JTable tablaAEnvolver) {
        JPanel panel = new JPanel(new BorderLayout());
        Estilos.aplicarFondoFormulario(panel);

        JLabel label = new JLabel(titulo);
        label.setFont(Estilos.FUENTE_LABEL);
        label.setForeground(Estilos.ROJO_PRINCIPAL);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JScrollPane scroll = new JScrollPane(tablaAEnvolver);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 224, 210)));
        scroll.setPreferredSize(new Dimension(0, 110));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirFormulario() {

        JTextField campoNombreCine = crearCampoTexto();
        JTextField campoDireccion = crearCampoTexto();
        JTextField campoCiudad = crearCampoTexto();
        BotonRedondeado btnAgregarCine = Estilos.crearBotonPrincipal("Agregar cine");

        campoNombreSala = crearCampoTexto();
        campoCapacidad = crearCampoTexto();
        campoTipoSala = crearCampoTexto();
        BotonRedondeado btnAgregarSala = Estilos.crearBotonPrincipal("Agregar sala al cine seleccionado");
        BotonRedondeado btnActualizarSala = Estilos.crearBotonSecundario("Actualizar sala seleccionada");

        JPanel formCine = new JPanel(new GridLayout(2, 3, 10, 4));
        Estilos.aplicarFondoFormulario(formCine);
        formCine.add(crearEtiquetaFormulario("Nombre cine"));
        formCine.add(crearEtiquetaFormulario("Dirección"));
        formCine.add(crearEtiquetaFormulario("Ciudad"));
        formCine.add(campoNombreCine);
        formCine.add(campoDireccion);
        formCine.add(campoCiudad);

        JPanel formSala = new JPanel(new GridLayout(2, 3, 10, 4));
        Estilos.aplicarFondoFormulario(formSala);
        formSala.add(crearEtiquetaFormulario("Nombre sala"));
        formSala.add(crearEtiquetaFormulario("Capacidad"));
        formSala.add(crearEtiquetaFormulario("Tipo (2D/3D/VIP)"));
        formSala.add(campoNombreSala);
        formSala.add(campoCapacidad);
        formSala.add(campoTipoSala);

        btnAgregarCine.addActionListener(e -> {
            String nombre = campoNombreCine.getText().trim();
            String direccion = campoDireccion.getText().trim();
            String ciudad = campoCiudad.getText().trim();

            Cine creado = cineController.registrarCine(nombre, direccion, ciudad);
            if (creado == null) {
                DialogoEstilizado.mostrarError(this, "Error", "No se pudo registrar el cine. Revisa los datos.");
                return;
            }
            campoNombreCine.setText("");
            campoDireccion.setText("");
            campoCiudad.setText("");
            cargarTabla();
        });

        btnAgregarSala.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                DialogoEstilizado.mostrarAdvertencia(this, "Falta seleccionar", "Selecciona un cine primero.");
                return;
            }
            int idCine = (int) modeloTabla.getValueAt(fila, 0);

            int capacidad;
            try {
                capacidad = Integer.parseInt(campoCapacidad.getText().trim());
            } catch (NumberFormatException ex) {
                DialogoEstilizado.mostrarError(this, "Dato inválido", "La capacidad debe ser un número.");
                return;
            }

            String nombreSala = campoNombreSala.getText().trim();
            String tipoSala = campoTipoSala.getText().trim();

            cineController.buscarCinePorId(idCine).ifPresentOrElse(cine -> {
                Sala sala = cineController.registrarSala(cine, nombreSala, capacidad, tipoSala);
                if (sala == null) {
                    DialogoEstilizado.mostrarError(this, "Error", "No se pudo registrar la sala. Revisa los datos.");
                    return;
                }

                // Genera los asientos respetando la capacidad real ingresada arriba
                sala.setAsientos(Sala.generarAsientos(capacidad));

                campoNombreSala.setText("");
                campoCapacidad.setText("");
                campoTipoSala.setText("");
                cargarTabla();
                cargarTablaSalas();
            }, () -> DialogoEstilizado.mostrarError(this, "Error", "El cine seleccionado ya no existe."));
        });

        btnActualizarSala.addActionListener(e -> {
            int filaSala = tablaSalas.getSelectedRow();
            if (filaSala == -1) {
                DialogoEstilizado.mostrarAdvertencia(this, "Falta seleccionar", "Selecciona la sala que quieres actualizar.");
                return;
            }
            int idSala = (int) modeloTablaSalas.getValueAt(filaSala, 0);

            int capacidad;
            try {
                capacidad = Integer.parseInt(campoCapacidad.getText().trim());
            } catch (NumberFormatException ex) {
                DialogoEstilizado.mostrarError(this, "Dato inválido", "La capacidad debe ser un número.");
                return;
            }

            String nombreSala = campoNombreSala.getText().trim();
            String tipoSala = campoTipoSala.getText().trim();

            boolean actualizada = cineController.actualizarSala(idSala, nombreSala, capacidad, tipoSala);
            if (!actualizada) {
                DialogoEstilizado.mostrarError(this, "Error", "No se pudo actualizar la sala. Revisa los datos.");
                return;
            }

            DialogoEstilizado.mostrarExito(this, "Listo", "Sala actualizada correctamente.");
            campoNombreSala.setText("");
            campoCapacidad.setText("");
            campoTipoSala.setText("");
            cargarTablaSalas();
        });

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver al panel admin");
        btnVolver.addActionListener(e -> frame.mostrarAdministrador(administrador));

        JPanel sur = new JPanel();
        sur.setLayout(new BoxLayout(sur, BoxLayout.Y_AXIS));
        Estilos.aplicarFondoFormulario(sur);
        sur.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        sur.add(formCine);
        sur.add(Box.createVerticalStrut(4));

        JPanel botonCine = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Estilos.aplicarFondoFormulario(botonCine);
        botonCine.add(btnAgregarCine);
        sur.add(botonCine);

        sur.add(Box.createVerticalStrut(8));
        sur.add(formSala);
        sur.add(Box.createVerticalStrut(4));

        JPanel botonesSala = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        Estilos.aplicarFondoFormulario(botonesSala);
        botonesSala.add(btnAgregarSala);
        botonesSala.add(btnActualizarSala);
        sur.add(botonesSala);
        sur.add(Box.createVerticalStrut(6));

        JPanel botonesFinales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Estilos.aplicarFondoFormulario(botonesFinales);
        botonesFinales.add(btnVolver);
        sur.add(botonesFinales);

        return sur;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(Estilos.FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Estilos.ROJO_PRINCIPAL),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        campo.setOpaque(false);
        return campo;
    }

    private JLabel crearEtiquetaFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL);
        label.setForeground(Estilos.GRIS_TEXTO);
        return label;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cine c : cineController.listarCines()) {
            modeloTabla.addRow(new Object[]{
                    c.getId(), c.getNombre(), c.getCiudad(), c.getDireccion(), c.getSalas().size()
            });
        }
    }

    /**
     * Recarga la tabla de salas del cine actualmente seleccionado en la tabla de cines.
     * Se llama al seleccionar un cine, al agregar una sala y al actualizar una sala.
     */
    private void cargarTablaSalas() {
        modeloTablaSalas.setRowCount(0);

        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }

        int idCine = (int) modeloTabla.getValueAt(fila, 0);

        cineController.buscarCinePorId(idCine).ifPresent(cine -> {
            for (Sala s : cine.getSalas()) {
                modeloTablaSalas.addRow(new Object[]{
                        s.getId(), s.getNombre(), s.getCapacidad(), s.getTipo()
                });
            }
        });
    }

    /**
     * Rellena el formulario de sala con los datos de la sala seleccionada,
     * para que el usuario pueda modificarlos y luego pulsar "Actualizar sala seleccionada".
     */
    private void cargarCamposSalaSeleccionada() {
        int filaSala = tablaSalas.getSelectedRow();
        if (filaSala == -1) {
            return;
        }

        campoNombreSala.setText(String.valueOf(modeloTablaSalas.getValueAt(filaSala, 1)));
        campoCapacidad.setText(String.valueOf(modeloTablaSalas.getValueAt(filaSala, 2)));
        campoTipoSala.setText(String.valueOf(modeloTablaSalas.getValueAt(filaSala, 3)));
    }
}