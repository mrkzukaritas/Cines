package views;

import controller.PeliculaController;
import models.Administrador;
import models.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTextField campoFoto; // ruta o URL del póster

    public AdminPeliculasPanel(
            MainFrame frame,
            Administrador administrador,
            PeliculaController peliculaController
    ) {
        this.frame = frame;
        this.administrador = administrador;
        this.peliculaController = peliculaController;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestionar películas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Título", "Género", "Duración", "Foto"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(construirFormulario(), BorderLayout.SOUTH);

        cargarTabla();
    }

    private JPanel construirFormulario() {

        campoTitulo = new JTextField();
        campoSinopsis = new JTextField();
        campoDuracion = new JTextField();
        campoGenero = new JTextField();
        campoClasificacion = new JTextField();
        campoIdioma = new JTextField();
        campoFoto = new JTextField();

        JPanel form = new JPanel(new GridLayout(2, 7, 6, 4));
        form.add(new JLabel("Título"));
        form.add(new JLabel("Sinopsis"));
        form.add(new JLabel("Duración (min)"));
        form.add(new JLabel("Género"));
        form.add(new JLabel("Clasificación"));
        form.add(new JLabel("Idioma"));
        form.add(new JLabel("Ruta/URL foto"));

        form.add(campoTitulo);
        form.add(campoSinopsis);
        form.add(campoDuracion);
        form.add(campoGenero);
        form.add(campoClasificacion);
        form.add(campoIdioma);
        form.add(campoFoto);

        JButton btnAgregar = new JButton("Agregar película");
        JButton btnEliminar = new JButton("Eliminar seleccionada");
        JButton btnVolver = new JButton("Volver al panel admin");

        btnAgregar.addActionListener(e -> agregarPelicula());
        btnEliminar.addActionListener(e -> eliminarPelicula());
        btnVolver.addActionListener(e -> frame.mostrarAdministrador(administrador));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnVolver);
        botones.add(btnEliminar);
        botones.add(btnAgregar);

        JPanel contenedor = new JPanel(new BorderLayout(6, 6));
        contenedor.add(form, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        return contenedor;
    }

    private void agregarPelicula() {

        String titulo = campoTitulo.getText().trim();
        String sinopsis = campoSinopsis.getText().trim();
        String genero = campoGenero.getText().trim();
        String clasificacion = campoClasificacion.getText().trim();
        String idioma = campoIdioma.getText().trim();
        String rutaFoto = campoFoto.getText().trim();

        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio.");
            return;
        }

        int duracion;
        try {
            duracion = Integer.parseInt(campoDuracion.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número (minutos).");
            return;
        }

        Pelicula creada = peliculaController.registrarPelicula(
                titulo, sinopsis, duracion, genero, clasificacion, idioma,
                LocalDate.now(), rutaFoto
        );

        if (creada == null) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la película. Revisa los datos.");
            return;
        }

        limpiarCampos();
        cargarTabla();
    }

    private void eliminarPelicula() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una película primero.");
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