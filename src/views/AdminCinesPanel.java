package views;

import controller.CineController;
import models.Administrador;
import models.Asiento;
import models.Cine;
import models.Sala;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminCinesPanel extends JPanel {

    private final MainFrame frame;
    private final Administrador administrador;
    private final CineController cineController;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public AdminCinesPanel(
            MainFrame frame,
            Administrador administrador,
            CineController cineController
    ) {
        this.frame = frame;
        this.administrador = administrador;
        this.cineController = cineController;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestionar cines y salas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Ciudad", "Dirección", "# Salas"}, 0
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

        JTextField campoNombreCine = new JTextField();
        JTextField campoDireccion = new JTextField();
        JTextField campoCiudad = new JTextField();
        JButton btnAgregarCine = new JButton("Agregar cine");

        JTextField campoNombreSala = new JTextField();
        JTextField campoCapacidad = new JTextField();
        JTextField campoTipoSala = new JTextField();
        JButton btnAgregarSala = new JButton("Agregar sala al cine seleccionado");

        JPanel formCine = new JPanel(new GridLayout(2, 3, 6, 4));
        formCine.add(new JLabel("Nombre cine"));
        formCine.add(new JLabel("Dirección"));
        formCine.add(new JLabel("Ciudad"));
        formCine.add(campoNombreCine);
        formCine.add(campoDireccion);
        formCine.add(campoCiudad);

        JPanel formSala = new JPanel(new GridLayout(2, 3, 6, 4));
        formSala.add(new JLabel("Nombre sala"));
        formSala.add(new JLabel("Capacidad"));
        formSala.add(new JLabel("Tipo (2D/3D/VIP)"));
        formSala.add(campoNombreSala);
        formSala.add(campoCapacidad);
        formSala.add(campoTipoSala);

        btnAgregarCine.addActionListener(e -> {
            String nombre = campoNombreCine.getText().trim();
            String direccion = campoDireccion.getText().trim();
            String ciudad = campoCiudad.getText().trim();

            Cine creado = cineController.registrarCine(nombre, direccion, ciudad);
            if (creado == null) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el cine. Revisa los datos.");
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
                JOptionPane.showMessageDialog(this, "Selecciona un cine primero.");
                return;
            }
            int idCine = (int) modeloTabla.getValueAt(fila, 0);

            int capacidad;
            try {
                capacidad = Integer.parseInt(campoCapacidad.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número.");
                return;
            }

            String nombreSala = campoNombreSala.getText().trim();
            String tipoSala = campoTipoSala.getText().trim();

            cineController.buscarCinePorId(idCine).ifPresentOrElse(cine -> {
                Sala sala = cineController.registrarSala(cine, nombreSala, capacidad, tipoSala);
                if (sala == null) {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar la sala. Revisa los datos.");
                    return;
                }
                // Asientos por defecto (4 filas x 5 columnas) para poder probar reservas.
                sala.setAsientos(generarAsientos(4, 5));

                campoNombreSala.setText("");
                campoCapacidad.setText("");
                campoTipoSala.setText("");
                cargarTabla();
            }, () -> JOptionPane.showMessageDialog(this, "El cine seleccionado ya no existe."));
        });

        JButton btnVolver = new JButton("Volver al panel admin");
        btnVolver.addActionListener(e -> frame.mostrarAdministrador(administrador));

        JPanel sur = new JPanel();
        sur.setLayout(new BoxLayout(sur, BoxLayout.Y_AXIS));
        sur.add(formCine);
        sur.add(Box.createVerticalStrut(4));
        sur.add(btnAgregarCine);
        sur.add(Box.createVerticalStrut(10));
        sur.add(formSala);
        sur.add(Box.createVerticalStrut(4));
        sur.add(btnAgregarSala);
        sur.add(Box.createVerticalStrut(10));

        JPanel botonesFinales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonesFinales.add(btnVolver);
        sur.add(botonesFinales);

        return sur;
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

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cine c : cineController.listarCines()) {
            modeloTabla.addRow(new Object[]{
                    c.getId(), c.getNombre(), c.getCiudad(), c.getDireccion(), c.getSalas().size()
            });
        }
    }
}