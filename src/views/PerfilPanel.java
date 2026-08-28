package views;

import models.Cliente;

import javax.swing.*;
import java.awt.*;

public class PerfilPanel extends JPanel {

    private final MainFrame frame;
    private final Cliente cliente;

    public PerfilPanel(
            MainFrame frame,
            Cliente cliente
    ) {
        this.frame = frame;
        this.cliente = cliente;

        setLayout(new BorderLayout(20, 20));

        JLabel titulo = new JLabel("Mi perfil", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        add(titulo, BorderLayout.NORTH);

        JPanel datos = new JPanel(new GridLayout(3, 2, 10, 15));
        datos.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        datos.add(new JLabel("Nombre:"));
        datos.add(new JLabel(cliente.getNombre()));

        datos.add(new JLabel("Email:"));
        datos.add(new JLabel(cliente.getEmail()));

        datos.add(new JLabel("Teléfono:"));
        datos.add(new JLabel(cliente.getTelefono()));

        add(datos, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));
        add(btnVolver, BorderLayout.SOUTH);
    }
}