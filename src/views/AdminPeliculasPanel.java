package views;

import controller.PeliculaController;
import models.Administrador;
import models.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AdminPeliculasPanel extends JPanel {

    private final MainFrame frame;
    private final Administrador administrador;
    private final PeliculaController peliculaController;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JTextField campoTitulo;
    private JTextField campoSinopsis;
    private JTextField campoDuracion;
    private JTextField campoGenero;
    private JTextField campoClasificacion;
    private JTextField campoIdioma;
    private JTextField campoFoto;

    public AdminPeliculasPanel(
            MainFrame frame,
            Administrador administrador,
            PeliculaController peliculaController
    ) {
        this.frame = frame;
        this.administrador = administrador;
        this.peliculaController = peliculaController;

        setLayout(new BorderLayout(10, 10));
        Estilos.aplicarFondoFormulario(this);

        // ==========================================
        // HEADER
        // ==========================================

        HeaderPanel header = new HeaderPanel("src/images/encabezadoGPeliculas.png");
        header.setPreferredSize(new Dimension(0, 90));
        add(header, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Título", "Género", "Duración", "Foto"}, 0
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

        JLabel labelTabla = new JLabel("Películas registradas");
        labelTabla.setFont(Estilos.FUENTE_LABEL);
        labelTabla.setForeground(Estilos.ROJO_PRINCIPAL);
        labelTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        panelTabla.add(labelTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

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

    private JPanel construirFormulario() {

        campoTitulo = crearCampoTexto("");
        campoSinopsis = crearCampoTexto("");
        campoDuracion = crearCampoTexto("");
        campoGenero = crearCampoTexto("");
        campoClasificacion = crearCampoTexto("");
        campoIdioma = crearCampoTexto("");
        campoFoto = crearCampoTexto("");

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 4));
        form.setOpaque(false);
        form.add(crearEtiquetaFormulario("Título"));
        form.add(crearEtiquetaFormulario("Sinopsis"));
        form.add(crearEtiquetaFormulario("Duración (min)"));
        form.add(crearEtiquetaFormulario("Género"));
        form.add(campoTitulo);
        form.add(campoSinopsis);
        form.add(campoDuracion);
        form.add(campoGenero);

        JPanel form2 = new JPanel(new GridLayout(2, 3, 10, 4));
        form2.setOpaque(false);
        form2.add(crearEtiquetaFormulario("Clasificación"));
        form2.add(crearEtiquetaFormulario("Idioma"));
        form2.add(crearEtiquetaFormulario("Ruta/URL foto"));
        form2.add(campoClasificacion);
        form2.add(campoIdioma);
        form2.add(campoFoto);

        BotonRedondeado btnAgregar = Estilos.crearBotonPrincipal("Agregar película");
        BotonRedondeado btnEliminar = Estilos.crearBotonSecundario("Eliminar seleccionada");
        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver al panel admin");

        btnAgregar.addActionListener(e -> agregarPelicula());
        btnEliminar.addActionListener(e -> eliminarPelicula());
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
        botones.add(btnEliminar);
        botones.add(btnAgregar);
        sur.add(botones);

        return sur;
    }

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

    private JLabel crearEtiquetaFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD, 14f));
        label.setForeground(Estilos.ROJO_PRINCIPAL);
        return label;
    }

    private void agregarPelicula() {

        String titulo = campoTitulo.getText().trim();
        String sinopsis = campoSinopsis.getText().trim();
        String genero = campoGenero.getText().trim();
        String clasificacion = campoClasificacion.getText().trim();
        String idioma = campoIdioma.getText().trim();
        String rutaFoto = campoFoto.getText().trim();

        if (titulo.isEmpty()) {
            DialogoEstilizado.mostrarAdvertencia(this, "Falta información", "El título es obligatorio.");
            return;
        }

        int duracion;
        try {
            duracion = Integer.parseInt(campoDuracion.getText().trim());
        } catch (NumberFormatException ex) {
            DialogoEstilizado.mostrarAdvertencia(this, "Dato inválido",
                    "La duración debe ser un número (minutos).");
            return;
        }

        Pelicula creada = peliculaController.registrarPelicula(
                titulo, sinopsis, duracion, genero, clasificacion, idioma,
                LocalDate.now(), rutaFoto
        );

        if (creada == null) {
            DialogoEstilizado.mostrarError(this, "Error",
                    "No se pudo registrar la película. Revisa los datos.");
            return;
        }

        limpiarCampos();
        cargarTabla();
    }

    private void eliminarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            DialogoEstilizado.mostrarAdvertencia(this, "Falta seleccionar", "Selecciona una película primero.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        peliculaController.eliminarPelicula(id);
        cargarTabla();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Pelicula> peliculas = peliculaController.listarPeliculas();
        for (Pelicula p : peliculas) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getTitulo(), p.getGenero(),
                    p.getDuracion() + " min", p.getRutaImagen()
            });
        }
    }

    private void limpiarCampos() {
        campoTitulo.setText("");
        campoSinopsis.setText("");
        campoDuracion.setText("");
        campoGenero.setText("");
        campoClasificacion.setText("");
        campoIdioma.setText("");
        campoFoto.setText("");
    }
}