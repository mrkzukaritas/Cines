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
        Estilos.aplicarFondoFormulario(this);

        HeaderPanel header = new HeaderPanel("src/images/encabezadoPerfil.png");
        add(header, BorderLayout.NORTH);

        JPanel datos = new JPanel(new GridLayout(3, 2, 3, 8));
        Estilos.aplicarFondoFormulario(datos);
        datos.setBorder(BorderFactory.createEmptyBorder(
                Estilos.PADDING_GRANDE, 150,
                Estilos.PADDING_GRANDE, 150));

        datos.add(crearEtiquetaClave("Nombre:"));
        datos.add(crearValor(cliente.getNombre()));

        datos.add(crearEtiquetaClave("Correo electrónico:"));
        datos.add(crearValor(cliente.getEmail()));

        datos.add(crearEtiquetaClave("Teléfono:"));
        datos.add(crearValor(cliente.getTelefono()));

        add(datos, BorderLayout.CENTER);

        BotonRedondeado btnVolver = Estilos.crearBotonSecundario("Volver");
        btnVolver.addActionListener(e -> frame.mostrarCliente(cliente));

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Estilos.aplicarFondoFormulario(panelVolver);
        panelVolver.setBorder(BorderFactory.createEmptyBorder(
                0, 0, Estilos.PADDING_GRANDE, 0));
        panelVolver.add(btnVolver);

        add(panelVolver, BorderLayout.SOUTH);
    }

    private JLabel crearEtiquetaClave(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD,18f));
        label.setForeground(Estilos.ROJO_PRINCIPAL);
        return label;
    }

    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Estilos.FUENTE_LABEL.deriveFont(Font.BOLD,18f));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }
}