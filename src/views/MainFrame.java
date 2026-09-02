package views;

import controller.CineController;
import controller.FuncionController;
import controller.LoginController;
import controller.PagoController;
import controller.PeliculaController;
import controller.ReservaController;
import models.*;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    // DATOS DE PRUEBA: 2 cines Cinemark + películas + funciones
    // Se llama desde Main.java, después de crear la ventana.
    // =========================================================

    public void precargarDatosDemo() {

        // ---- Cine 1: Cinemark Atlantis (Bogotá) ----
        Cine atlantis = cineController.registrarCine(
                "Cinemark Atlantis", "Calle 81 No. 13-05", "Bogotá"
        );
        Sala salaAtlantis1 = cineController.registrarSala(atlantis, "Sala 1", 20, "2D");
        Sala salaAtlantis2 = cineController.registrarSala(atlantis, "Sala 2 (VIP)", 12, "VIP");
        salaAtlantis1.setAsientos(Sala.generarAsientos(salaAtlantis1.getCapacidad()));
        salaAtlantis2.setAsientos(Sala.generarAsientos(salaAtlantis2.getCapacidad()));

        // ---- Cine 2: Cinemark Floresta (Bogotá, otra dirección) ----
        Cine floresta = cineController.registrarCine(
                "Cinemark Floresta", "Avenida Carrera 68 # 90-88", "Bogotá"
        );
        Sala salaFloresta1 = cineController.registrarSala(floresta, "Sala 1", 20, "2D");
        Sala salaFloresta2 = cineController.registrarSala(floresta, "Sala 3D", 18, "3D");
        salaFloresta1.setAsientos(Sala.generarAsientos(salaFloresta1.getCapacidad()));
        salaFloresta2.setAsientos(Sala.generarAsientos(salaFloresta2.getCapacidad()));

        // ---- Películas (imágenes de marcador de posición, reales y funcionales) ----
        Pelicula dune2 = peliculaController.registrarPelicula(
                "Dune: Parte Dos", "Paul Atreides se une a los Fremen para vengar a su familia.",
                166, "Ciencia ficción", "PG-13", "Inglés", LocalDate.of(2024, 3, 1),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSCCQ15KlAYtueaZGYhHzpBgUQZEhD9ISA-kPihWufslFI9uA-LINyoFU&s=10"
        );
        Pelicula intensamente2 = peliculaController.registrarPelicula(
                "Intensamente 2", "Riley enfrenta nuevas emociones en la adolescencia.",
                96, "Animación", "G", "Inglés", LocalDate.of(2024, 6, 14),
                "https://lumiere-a.akamaihd.net/v1/images/1_intensamente_2_payoff_banner_pre_1_aa3d9114.png"
        );
        Pelicula deadpool = peliculaController.registrarPelicula(
                "Deadpool & Wolverine", "El dúo más caótico del multiverso Marvel.",
                128, "Acción", "R", "Inglés", LocalDate.of(2024, 7, 26),
                "https://lumiere-a.akamaihd.net/v1/images/tidalwave_payoff_poster_las_0a47c6a2.jpeg"
        );
        Pelicula kungfupanda = peliculaController.registrarPelicula(
                "Kung Fu Panda 4", "Po debe entrenar a un sucesor como Guerrero Dragón.",
                94, "Animación", "PG", "Inglés", LocalDate.of(2024, 3, 8),
                "https://static.wikia.nocookie.net/doblaje/images/1/11/Kung_Fu_Panda_4_Poster_Oficial.jpg/revision/latest?cb=20240222161910&path-prefix=es"
        );
        Pelicula ininterrumpida = peliculaController.registrarPelicula(
                "Un Lugar en Silencio: Día Uno", "El origen de la invasión alienígena en Nueva York.",
                100, "Terror", "PG-13", "Inglés", LocalDate.of(2024, 6, 28),
                "https://m.media-amazon.com/images/M/MV5BZDExZjJkNWUtMWFkNC00MDZiLThkNTEtMWVmYmQ3OGU3ZmM5XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg"
        );

        // ---- Funciones: repartidas entre los dos cines ----
        funcionController.crearFuncion(atlantis, salaAtlantis1, dune2, TipoFuncionEnum.DOBLADA,
                LocalDate.now(), LocalTime.of(19, 0), LocalTime.of(21, 30), 16000, "2D");
        funcionController.crearFuncion(atlantis, salaAtlantis2, deadpool, TipoFuncionEnum.SUBTITULADA,
                LocalDate.now(), LocalTime.of(20, 30), LocalTime.of(22, 40), 22000, "VIP");

        funcionController.crearFuncion(floresta, salaFloresta1, intensamente2, TipoFuncionEnum.DOBLADA,
                LocalDate.now(), LocalTime.of(16, 0), LocalTime.of(17, 40), 14000, "2D");
        funcionController.crearFuncion(floresta, salaFloresta2, kungfupanda, TipoFuncionEnum.DOBLADA,
                LocalDate.now(), LocalTime.of(15, 0), LocalTime.of(16, 40), 18000, "3D");
        funcionController.crearFuncion(floresta, salaFloresta1, ininterrumpida, TipoFuncionEnum.SUBTITULADA,
                LocalDate.now().plusDays(1), LocalTime.of(21, 0), LocalTime.of(22, 45), 16000, "2D");
    }
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