package view;

import controller.CineController;
import controller.ClienteController;
import controller.FuncionController;
import controller.PagoController;
import controller.ReservaController;

import models.Asiento;
import models.Cliente;
import models.Cine;
import models.Funcion;
import models.Pelicula;
import models.Reserva;
import models.Sala;
import models.TipoFuncionEnum;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class GUIPrincipal extends JFrame {


    public static final String REGISTRO = "REGISTRO";
    public static final String CINES = "CINES";
    public static final String FUNCIONES = "FUNCIONES";
    public static final String ASIENTOS = "ASIENTOS";
    public static final String RESERVA = "RESERVA";
    public static final String PAGO = "PAGO";


    private final CardLayout cardLayout;
    private final JPanel panelContenedor;



    private final CineController cineController;
    private final FuncionController funcionController;
    private final ClienteController clienteController;
    private final ReservaController reservaController;
    private final PagoController pagoController;


    private Cliente clienteActual;
    private Cine cineSeleccionado;
    private Funcion funcionSeleccionada;
    private Reserva reservaActual;


    private GUIRegistro guiRegistro;
    private GUIListarCine guiListarCine;
    private GUIListarFuncion guiListarFuncion;
    private GUISeleccionarAsiento guiSeleccionarAsiento;
    private GUIReserva guiReserva;
    private GUIPago guiPago;


    public GUIPrincipal() {

        super("Sistema de Reservas - Cine");


        List<Cine> cines =
                crearCinesDePrueba();


        cineController =
                new CineController(
                        cines
                );

        funcionController =
                new FuncionController(
                        cines
                );

        clienteController =
                new ClienteController();

        reservaController =
                new ReservaController();

        pagoController =
                new PagoController();

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(
                750,
                550
        );

        setMinimumSize(
                new Dimension(
                        650,
                        480
                )
        );

        setLocationRelativeTo(
                null
        );

        cardLayout =
                new CardLayout();

        panelContenedor =
                new JPanel(
                        cardLayout
                );

        guiRegistro =
                new GUIRegistro(
                        this,
                        clienteController
                );


        guiListarCine =
                new GUIListarCine(
                        this,
                        cineController
                );


        guiListarFuncion =
                new GUIListarFuncion(
                        this,
                        funcionController,
                        reservaController
                );


        guiSeleccionarAsiento = null;

        guiReserva = null;

        guiPago = null;

        panelContenedor.add(
                guiRegistro,
                REGISTRO
        );

        panelContenedor.add(
                guiListarCine,
                CINES
        );

        panelContenedor.add(
                guiListarFuncion,
                FUNCIONES
        );


        add(
                panelContenedor
        );

        mostrarPantalla(
                REGISTRO
        );
    }

    public void iniciar() {

        setLocationRelativeTo(
                null
        );

        setVisible(
                true
        );
    }

    public void mostrarPantalla(
            String pantalla) {

        switch (pantalla) {

            case CINES:

                guiListarCine
                        .refrescarTablaCines();

                break;

            case FUNCIONES:

                guiListarFuncion
                        .refrescarTablaFunciones();

                break;

            case ASIENTOS:

                if (
                        funcionSeleccionada == null
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Primero debes seleccionar una función.",
                            "Función no seleccionada",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                mostrarSeleccionarAsiento();

                return;

            case RESERVA:

                if (
                        reservaActual == null
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No existe una reserva.",
                            "Reserva no encontrada",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                mostrarReserva();

                return;

            case PAGO:

                if (
                        reservaActual == null
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No existe una reserva.",
                            "Reserva no encontrada",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                mostrarPago();

                return;
        }


        cardLayout.show(
                panelContenedor,
                pantalla
        );
    }


    public void setClienteActual(
            Cliente cliente) {

        this.clienteActual =
                cliente;
    }

    public Cliente getClienteActual() {

        return clienteActual;
    }

    public void setCineSeleccionado(
            Cine cine) {

        this.cineSeleccionado =
                cine;
    }

    public Cine getCineSeleccionado() {

        return cineSeleccionado;
    }

    public void setFuncionSeleccionada(
            Funcion funcion) {

        this.funcionSeleccionada =
                funcion;
    }

    public Funcion getFuncionSeleccionada() {

        return funcionSeleccionada;
    }

    public void setReservaActual(
            Reserva reserva) {

        this.reservaActual =
                reserva;
    }

    public Reserva getReservaActual() {

        return reservaActual;
    }

    public void mostrarSeleccionarAsiento() {

        if (
                funcionSeleccionada == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes seleccionar una función.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                clienteActual == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes registrar un cliente.",
                    "Cliente no registrado",
                    JOptionPane.WARNING_MESSAGE
            );

            mostrarPantalla(
                    REGISTRO
            );

            return;
        }

        if (
                reservaActual == null
        ) {

            reservaActual =
                    reservaController.crearReserva(
                            clienteActual,
                            funcionSeleccionada
                    );
        }


        guiSeleccionarAsiento =
                new GUISeleccionarAsiento(
                        this,
                        funcionSeleccionada
                );


        panelContenedor.add(
                guiSeleccionarAsiento,
                ASIENTOS
        );

        cardLayout.show(
                panelContenedor,
                ASIENTOS
        );
    }

    public void mostrarReserva() {

        if (
                reservaActual == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existe una reserva.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        guiReserva =
                new GUIReserva(
                        this,
                        reservaActual
                );

        panelContenedor.add(
                guiReserva,
                RESERVA
        );


        cardLayout.show(
                panelContenedor,
                RESERVA
        );
    }

    public void mostrarPago() {

        if (
                reservaActual == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existe una reserva.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        guiPago =
                new GUIPago(
                        this,
                        reservaController,
                        pagoController
                );

        panelContenedor.add(
                guiPago,
                PAGO
        );

        cardLayout.show(
                panelContenedor,
                PAGO
        );
    }

    public CineController getCineController() {

        return cineController;
    }

    public FuncionController getFuncionController() {

        return funcionController;
    }

    public ClienteController getClienteController() {

        return clienteController;
    }

    public ReservaController getReservaController() {

        return reservaController;
    }

    public PagoController getPagoController() {

        return pagoController;
    }

    public void limpiarDatos() {

        clienteActual = null;

        cineSeleccionado = null;

        funcionSeleccionada = null;

        reservaActual = null;

        guiSeleccionarAsiento = null;

        guiReserva = null;

        guiPago = null;
    }

    private List<Cine> crearCinesDePrueba() {

        List<Cine> cines =
                new ArrayList<>();

        Pelicula p1 =
                new Pelicula(
                        1,
                        "Dune: Parte Dos",
                        "La guerra por Arrakis continúa.",
                        166,
                        "Ciencia ficción",
                        "PG-13",
                        "Inglés",
                        LocalDate.of(
                                2024,
                                3,
                                1
                        )
                );


        Pelicula p2 =
                new Pelicula(
                        2,
                        "Intensamente 2",
                        "Riley enfrenta nuevas emociones.",
                        96,
                        "Animación",
                        "G",
                        "Inglés",
                        LocalDate.of(
                                2024,
                                6,
                                14
                        )
                );


        Pelicula p3 =
                new Pelicula(
                        3,
                        "Deadpool & Wolverine",
                        "El dúo más caótico del multiverso.",
                        128,
                        "Acción",
                        "R",
                        "Inglés",
                        LocalDate.of(
                                2024,
                                7,
                                26
                        )
                );

        Cine cineCentro =
                new Cine(
                        1,
                        "Cinemark Centro",
                        "Cra 5 # 10-20",
                        "Ibagué"
                );

        Sala sala1 =
                new Sala(
                        1,
                        "Sala 1",
                        20,
                        "2D"
                );


        sala1.setAsientos(
                generarAsientos(
                        4,
                        5
                )
        );

        Funcion f1 =
                new Funcion(
                        1,
                        LocalDate.now(),
                        LocalTime.of(
                                15,
                                0
                        ),
                        LocalTime.of(
                                16,
                                30
                        ),
                        12000,
                        "2D",
                        "PROGRAMADA"
                );


        f1.setPelicula(
                p1
        );

        f1.setSala(
                sala1
        );

        f1.setTipoFuncion(
                TipoFuncionEnum.DOBLADA
        );


        sala1
                .getFunciones()
                .add(f1);


        Sala sala2 =
                new Sala(
                        2,
                        "Sala 2 (VIP)",
                        12,
                        "VIP"
                );


        sala2.setAsientos(
                generarAsientos(
                        3,
                        4
                )
        );

    Funcion f2 =
                new Funcion(
                        2,
                        LocalDate.now(),
                        LocalTime.of(
                                18,
                                30
                        ),
                        LocalTime.of(
                                20,
                                20
                        ),
                        22000,
                        "VIP",
                        "PROGRAMADA"
                );


        f2.setPelicula(
                p2
        );

        f2.setSala(
                sala2
        );

        f2.setTipoFuncion(
                TipoFuncionEnum.SUBTITULADA
        );


        sala2
                .getFunciones()
                .add(f2);


        cineCentro
                .getSalas()
                .add(sala1);

        cineCentro
                .getSalas()
                .add(sala2);


        Cine cineNorte =
                new Cine(
                        2,
                        "Procinal Norte",
                        "Av. Ambalá # 45-10",
                        "Ibagué"
                );



        Sala sala3 =
                new Sala(
                        3,
                        "Sala 1",
                        20,
                        "2D"
                );


        sala3.setAsientos(
                generarAsientos(
                        4,
                        5
                )
        );


        Funcion f3 =
                new Funcion(
                        3,
                        LocalDate.now().plusDays(1),
                        LocalTime.of(
                                20,
                                0
                        ),
                        LocalTime.of(
                                21,
                                40
                        ),
                        15000,
                        "2D",
                        "PROGRAMADA"
                );


        f3.setPelicula(
                p3
        );

        f3.setSala(
                sala3
        );

        f3.setTipoFuncion(
                TipoFuncionEnum.ORIGINAL
        );


        sala3
                .getFunciones()
                .add(f3);


        cineNorte
                .getSalas()
                .add(sala3);


        cines.add(
                cineCentro
        );

        cines.add(
                cineNorte
        );


        return cines;
    }


    private List<Asiento> generarAsientos(
            int filas,
            int porFila) {

        List<Asiento> asientos =
                new ArrayList<>();


        int id = 1;

        char letra = 'A';


        for (
                int f = 0;
                f < filas;
                f++
        ) {

            for (
                    int n = 1;
                    n <= porFila;
                    n++
            ) {

                asientos.add(
                        new Asiento(
                                id++,
                                String.valueOf(letra),
                                n,
                                "DISPONIBLE"
                        )
                );
            }


            letra++;
        }


        return asientos;
    }
}