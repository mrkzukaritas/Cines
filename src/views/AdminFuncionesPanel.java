package views;

import controller.CineController;
import controller.FuncionController;
import controller.PeliculaController;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestionar funciones", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Película", "Sala", "Fecha", "Hora", "Precio"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(construirFormulario(), BorderLayout.SOUTH);

        cargarCombos();
    }

    private JPanel construirFormulario() {

        comboCine = new JComboBox<>();
        comboSala = new JComboBox<>();
        comboPelicula = new JComboBox<>();
        comboTipoFuncion = new JComboBox<>(TipoFuncionEnum.values());

        JTextField campoFecha = new JTextField(LocalDate.now().toString());
        JTextField campoHoraInicio = new JTextField("20:00");
        JTextField campoHoraFin = new JTextField("22:00");
        JTextField campoPrecio = new JTextField();
        JTextField campoFormato = new JTextField("2D");

        comboCine.addActionListener(e -> {
            comboSala.removeAllItems();
            Cine cine = (Cine) comboCine.getSelectedItem();
            if (cine != null) {
                for (Sala s : cine.getSalas()) {
                    comboSala.addItem(s);
                }
                cargarTabla(cine);
            }
        });

        JPanel form = new JPanel(new GridLayout(2, 5, 6, 4));
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

        JPanel form2 = new JPanel(new GridLayout(2, 4, 6, 4));
        form2.add(new JLabel("Fecha (AAAA-MM-DD)"));
        form2.add(new JLabel("Hora inicio (HH:mm)"));
        form2.add(new JLabel("Hora fin (HH:mm)"));
        form2.add(new JLabel("Precio"));
        form2.add(campoFecha);
        form2.add(campoHoraInicio);
        form2.add(campoHoraFin);
        form2.add(campoPrecio);

        JButton btnCrear = new JButton("Crear función");
        JButton btnCancelarFuncion = new JButton("Cancelar seleccionada");
        JButton btnVolver = new JButton("Volver al panel admin");

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
                        "Revisa el formato de fecha (AAAA-MM-DD), hora (HH:mm) y precio.\n" + ex.getMessage());
            }
        });

        btnCancelarFuncion.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una función primero.");
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
        sur.add(form);
        sur.add(form2);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnVolver);
        botones.add(btnCancelarFuncion);
        botones.add(btnCrear);
        sur.add(botones);

        return sur;
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