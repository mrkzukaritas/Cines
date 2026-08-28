package views;

import controller.CineController;
import controller.FuncionController;
import controller.LoginController;
import controller.PagoController;
import controller.PeliculaController;
import controller.ReservaController;
import models.Administrador;
import models.CineService;
import models.Cliente;
import models.Funcion;
import models.Pelicula;
import models.Reserva;
import models.Rol;
import models.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // =========================================================
    // CARD LAYOUT
    // =========================================================

    private final CardLayout cardLayout;
    private final JPanel panelContenedor;

    // =========================================================
    // SERVICES
    // =========================================================

    private final CineService cineService;

    // =========================================================
    // CONTROLLERS
    // =========================================================

    private final LoginController loginController;
    private final PeliculaController peliculaController;
    private final FuncionController funcionController;
    private final CineController cineController;
    private final ReservaController reservaController;
    private final PagoController pagoController;

    // =========================================================
    // USUARIO ACTUAL
    // =========================================================

    private Cliente clienteActual;
    private Administrador administradorActual;

    // =========================================================
    // NOMBRES DE LOS PANELES
    // =========================================================

    public static final String PANEL_LOGIN = "LOGIN";
    public static final String PANEL_CLIENTE = "CLIENTE";
    public static final String PANEL_PELICULAS = "PELICULAS";
    public static final String PANEL_FUNCIONES = "FUNCIONES";
    public static final String PANEL_ASIENTOS = "ASIENTOS";
    public static final String PANEL_RESUMEN = "RESUMEN";
    public static final String PANEL_PAGO = "PAGO";
    public static final String PANEL_ADMIN = "ADMIN";
    public static final String PANEL_ADMIN_PELICULAS = "ADMIN_PELICULAS";
    public static final String PANEL_ADMIN_CINES = "ADMIN_CINES";
    public static final String PANEL_ADMIN_FUNCIONES = "ADMIN_FUNCIONES";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /**
     * Recibe el LoginController ya creado desde Main.java
     * (que a su vez ya cargó el AuthService con el administrador inicial).
     * MainFrame NO crea su propio AuthService para no duplicar la
     * lista de usuarios registrados.
     */
    public MainFrame(LoginController loginController) {

        this.loginController = loginController;

        setTitle("Sistema de Cine");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =====================================================
        // SERVICES Y CONTROLLERS PROPIOS DEL CINE
        // (no dependen de autenticación, se crean aquí)
        // =====================================================

        cineService = new CineService();

        peliculaController = new PeliculaController(new models.PeliculaService());
        funcionController = new FuncionController(cineService);
        cineController = new CineController(cineService, funcionController);
        reservaController = new ReservaController();
        pagoController = new PagoController();

        // =====================================================
        // CARD LAYOUT
        // =====================================================

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        crearPaneles();

        add(panelContenedor);

        cardLayout.show(panelContenedor, PANEL_LOGIN);
    }

    // =========================================================
    // CREAR PANELES INICIALES
    // =========================================================

    private void crearPaneles() {

        LoginPanel loginPanel = new LoginPanel(this, loginController);
        panelContenedor.add(loginPanel, PANEL_LOGIN);
    }

    // =========================================================
    // MOSTRAR LOGIN
    // =========================================================

    public void mostrarLogin() {
        clienteActual = null;
        administradorActual = null;
        cardLayout.show(panelContenedor, PANEL_LOGIN);
    }

    public void mostrarRegistro() {
        RegistroPanel panel = new RegistroPanel(this, loginController);
        panelContenedor.add(panel, "REGISTRO");
        cardLayout.show(panelContenedor, "REGISTRO");
    }

    // =========================================================
    // LOGIN: DIFERENCIAR CLIENTE / ADMINISTRADOR
    // =========================================================

    public void iniciarSesion(Usuario usuario) {

        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            mostrarAdministrador((Administrador) usuario);
        } else {
            mostrarCliente((Cliente) usuario);
        }
    }

    // =========================================================
    // FLUJO CLIENTE
    // =========================================================

    public void mostrarCliente(Cliente cliente) {
        this.clienteActual = cliente;
        ClientePanel panel = new ClientePanel(this, cliente);
        panelContenedor.add(panel, PANEL_CLIENTE);
        cardLayout.show(panelContenedor, PANEL_CLIENTE);
    }

    public void mostrarPeliculas(Cliente cliente) {
        this.clienteActual = cliente;
        PeliculasPanel panel = new PeliculasPanel(this, cliente, peliculaController, funcionController);
        panelContenedor.add(panel, PANEL_PELICULAS);
        cardLayout.show(panelContenedor, PANEL_PELICULAS);
    }

    public void mostrarFunciones(Cliente cliente, Pelicula pelicula) {
        this.clienteActual = cliente;
        FuncionesPanel panel = new FuncionesPanel(this, cliente, pelicula, funcionController);
        panelContenedor.add(panel, PANEL_FUNCIONES);
        cardLayout.show(panelContenedor, PANEL_FUNCIONES);
    }

    public void mostrarAsientos(Cliente cliente, Funcion funcion) {
        this.clienteActual = cliente;
        AsientosPanel panel = new AsientosPanel(this, cliente, funcion, reservaController);
        panelContenedor.add(panel, PANEL_ASIENTOS);
        cardLayout.show(panelContenedor, PANEL_ASIENTOS);
    }

    public void mostrarResumen(Cliente cliente, Reserva reserva) {
        ResumenReservaPanel panel = new ResumenReservaPanel(this, cliente, reserva);
        panelContenedor.add(panel, PANEL_RESUMEN);
        cardLayout.show(panelContenedor, PANEL_RESUMEN);
    }

    public void mostrarPago(Cliente cliente, Reserva reserva) {
        PagoPanel panel = new PagoPanel(this, cliente, reserva, pagoController);
        panelContenedor.add(panel, PANEL_PAGO);
        cardLayout.show(panelContenedor, PANEL_PAGO);
    }

    public void mostrarReservas(Cliente cliente) {
        ReservasPanel panel = new ReservasPanel(this, cliente, reservaController);
        panelContenedor.add(panel, "RESERVAS");
        cardLayout.show(panelContenedor, "RESERVAS");
    }

    public void mostrarPerfil(Cliente cliente) {
        PerfilPanel panel = new PerfilPanel(this, cliente);
        panelContenedor.add(panel, "PERFIL");
        cardLayout.show(panelContenedor, "PERFIL");
    }

    // =========================================================
    // FLUJO ADMINISTRADOR
    // =========================================================

    public void mostrarAdministrador(Administrador administrador) {
        this.administradorActual = administrador;
        AdminPanel panel = new AdminPanel(this, administrador);
        panelContenedor.add(panel, PANEL_ADMIN);
        cardLayout.show(panelContenedor, PANEL_ADMIN);
    }

    public void mostrarAdminPeliculas(Administrador administrador) {
        AdminPeliculasPanel panel = new AdminPeliculasPanel(this, administrador, peliculaController);
        panelContenedor.add(panel, PANEL_ADMIN_PELICULAS);
        cardLayout.show(panelContenedor, PANEL_ADMIN_PELICULAS);
    }

    public void mostrarAdminCines(Administrador administrador) {
        AdminCinesPanel panel = new AdminCinesPanel(this, administrador, cineController);
        panelContenedor.add(panel, PANEL_ADMIN_CINES);
        cardLayout.show(panelContenedor, PANEL_ADMIN_CINES);
    }

    public void mostrarAdminFunciones(Administrador administrador) {
        AdminFuncionesPanel panel = new AdminFuncionesPanel(this, administrador, cineController, peliculaController, funcionController);
        panelContenedor.add(panel, PANEL_ADMIN_FUNCIONES);
        cardLayout.show(panelContenedor, PANEL_ADMIN_FUNCIONES);
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public Administrador getAdministradorActual() {
        return administradorActual;
    }

    public PeliculaController getPeliculaController() {
        return peliculaController;
    }

    public FuncionController getFuncionController() {
        return funcionController;
    }

    public CineController getCineController() {
        return cineController;
    }

    public ReservaController getReservaController() {
        return reservaController;
    }
}