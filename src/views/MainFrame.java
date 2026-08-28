package views;

import controller.LoginController;
import models.Cliente;
import models.Rol;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;

    private LoginController loginController;

    // Usuario que inició sesión
    private Usuario usuarioActual;

    public static final String LOGIN = "LOGIN";
    public static final String REGISTRO = "REGISTRO";
    public static final String CLIENTE = "CLIENTE";
    public static final String ADMIN = "ADMIN";

    public MainFrame(LoginController loginController) {

        this.loginController = loginController;

        setTitle("Sistema de Cine");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // LOGIN
        panelContenedor.add(
                new LoginPanel(this, loginController),
                LOGIN
        );

        // REGISTRO
        panelContenedor.add(
                new RegistroPanel(this, loginController),
                REGISTRO
        );

        add(panelContenedor);

        mostrarLogin();
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public void mostrarLogin() {
        cardLayout.show(panelContenedor, LOGIN);
    }

    // =====================================================
    // REGISTRO
    // =====================================================

    public void mostrarRegistro() {
        cardLayout.show(panelContenedor, REGISTRO);
    }

    // =====================================================
    // LOGIN EXITOSO
    // =====================================================

    public void iniciarSesion(Usuario usuario) {

        this.usuarioActual = usuario;

        if (usuario.getRol() == Rol.ADMINISTRADOR) {

            mostrarAdmin();

        } else {

            mostrarCliente((Cliente) usuario);
        }
    }

    // =====================================================
    // PANEL CLIENTE
    // =====================================================

    public void mostrarCliente(Cliente cliente) {

        panelContenedor.add(
                new ClientePanel(this, cliente),
                CLIENTE
        );

        cardLayout.show(
                panelContenedor,
                CLIENTE
        );
    }

    // =====================================================
    // PANEL ADMIN
    // =====================================================

    public void mostrarAdmin() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        JLabel titulo = new JLabel(
                "PANEL DEL ADMINISTRADOR",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        panel.add(
                titulo,
                BorderLayout.CENTER
        );

        panelContenedor.add(
                panel,
                ADMIN
        );

        cardLayout.show(
                panelContenedor,
                ADMIN
        );
    }

    // =====================================================
    // CERRAR SESIÓN
    // =====================================================

    public void cerrarSesion() {

        usuarioActual = null;

        mostrarLogin();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}