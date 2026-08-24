package view;

import controlador.*;
import datos.DataInicializador;
import modelo.*;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);

    private final CineController cineController;
    private final FuncionController funcionController;
    private final ClienteController clienteController = new ClienteController();
    private final ReservaController reservaController = new ReservaController();
    private final PagoController pagoController = new PagoController();

    // estado de la sesion actual (mientras el usuario navega el menu)
    private Cliente clienteActual;
    private Reserva reservaEnCurso;

    public MenuPrincipal() {
        List<Cine> cines = DataInicializador.inicializarCines();
        this.cineController = new CineController(cines);
        this.funcionController = new FuncionController(cines);
    }

    public void iniciar() {
        registrarCliente();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1 -> verCinesYSalas();
                case 2 -> verFunciones();
                case 3 -> verAsientosDisponibles();
                case 4 -> iniciarReserva();
                case 5 -> agregarAsientosAReserva();
                case 6 -> confirmarYPagar();
                case 7 -> verMisReservas();
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
        System.out.println("Hasta luego, " + clienteActual.getNombre() + "!");
    }

    private void mostrarMenu() {
        System.out.println("\n===== CINE - MENU PRINCIPAL =====");
        System.out.println("1. Ver cines y salas");
        System.out.println("2. Ver funciones de un cine");
        System.out.println("3. Ver asientos disponibles de una funcion");
        System.out.println("4. Iniciar una reserva");
        System.out.println("5. Agregar asientos a la reserva en curso");
        System.out.println("6. Confirmar y pagar reserva en curso");
        System.out.println("7. Ver mis reservas");
        System.out.println("0. Salir");
    }

    private void registrarCliente() {
        System.out.println("=== Bienvenido al sistema de reservas de cine ===");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine();
        clienteActual = clienteController.registrarCliente(nombre, email, telefono);
    }

    private void verCinesYSalas() {
        for (Cine cine : cineController.listarCines()) {
            System.out.println("\n[" + cine.getId() + "] " + cine.getNombre()
                    + " - " + cine.getCiudad() + " (" + cine.getDireccion() + ")");
            for (Sala sala : cine.listarSalas()) {
                System.out.println("   -> " + sala);
            }
        }
    }

    private void verFunciones() {
        int idCine = leerEntero("ID del cine: ");
        cineController.buscarCinePorId(idCine).ifPresentOrElse(cine -> {
            List<Funcion> funciones = cineController.listarFunciones(cine);
            if (funciones.isEmpty()) {
                System.out.println("Ese cine no tiene funciones programadas.");
                return;
            }
            for (Funcion f : funciones) {
                System.out.println(f);
            }
        }, () -> System.out.println("Cine no encontrado."));
    }

    private void verAsientosDisponibles() {
        int idFuncion = leerEntero("ID de la funcion: ");
        funcionController.buscarFuncionPorId(idFuncion).ifPresentOrElse(funcion -> {
            List<Asiento> disponibles = funcionController.listarAsientosDisponibles(funcion);
            System.out.println("Asientos disponibles (" + disponibles.size() + "):");
            for (Asiento a : disponibles) {
                System.out.print(a.getFila() + a.getNumero() + "  ");
            }
            System.out.println();
        }, () -> System.out.println("Funcion no encontrada."));
    }

    private void iniciarReserva() {
        int idFuncion = leerEntero("ID de la funcion para reservar: ");
        var funcionOpt = funcionController.buscarFuncionPorId(idFuncion);
        if (funcionOpt.isEmpty()) {
            System.out.println("Funcion no encontrada.");
            return;
        }
        reservaEnCurso = reservaController.crearReserva(clienteActual, funcionOpt.get());
        System.out.println("Reserva #" + reservaEnCurso.getId() + " iniciada. "
                + "Ahora agrega asientos con la opcion 5.");
    }

    private void agregarAsientosAReserva() {
        if (!hayReservaEnCurso()) return;

        List<Asiento> disponibles = funcionController.listarAsientosDisponibles(reservaEnCurso.getFuncion());
        if (disponibles.isEmpty()) {
            System.out.println("No quedan asientos disponibles para esta funcion.");
            return;
        }

        System.out.println("Disponibles: ");
        for (Asiento a : disponibles) {
            System.out.print(a.getFila() + a.getNumero() + "  ");
        }
        System.out.println();

        System.out.print("Fila (ej A): ");
        String fila = sc.nextLine().trim().toUpperCase();
        int numero = leerEntero("Numero de asiento: ");

        Asiento elegido = disponibles.stream()
                .filter(a -> a.getFila().equalsIgnoreCase(fila) && a.getNumero() == numero)
                .findFirst()
                .orElse(null);

        if (elegido == null) {
            System.out.println("Ese asiento no existe o ya esta ocupado.");
            return;
        }

        boolean ok = reservaController.agregarAsiento(reservaEnCurso, elegido);
        if (ok) {
            System.out.println("Asiento " + fila + numero + " agregado. Total actual: "
                    + reservaEnCurso.getTotal());
        } else {
            System.out.println("No se pudo agregar el asiento (ya estaba ocupado).");
        }
    }

    private void confirmarYPagar() {
        if (!hayReservaEnCurso()) return;

        if (reservaEnCurso.getDetalles().isEmpty()) {
            System.out.println("La reserva no tiene asientos. Agrega al menos uno antes de pagar.");
            return;
        }

        reservaController.confirmarReserva(reservaEnCurso);
        System.out.println("Reserva confirmada. Total a pagar: " + reservaEnCurso.getTotal());

        System.out.println("Metodos de pago: 1) Tarjeta  2) Efectivo  3) PSE");
        int op = leerEntero("Elige metodo de pago: ");
        String tipo = switch (op) {
            case 1 -> "Tarjeta";
            case 2 -> "Efectivo";
            case 3 -> "PSE";
            default -> "Efectivo";
        };
        MetodoPago metodo = new MetodoPago(op, tipo);

        Pago pago = pagoController.procesarPago(reservaEnCurso, metodo);
        if (pago != null) {
            System.out.println("Pago exitoso -> " + pago);
        } else {
            System.out.println("El metodo de pago no es valido.");
        }

        reservaEnCurso = null;
    }

    private void verMisReservas() {
        List<Reserva> reservas = reservaController.listarReservasPorCliente(clienteActual);
        if (reservas.isEmpty()) {
            System.out.println("Aun no tienes reservas.");
            return;
        }
        for (Reserva r : reservas) {
            System.out.println(r);
        }
    }

    private boolean hayReservaEnCurso() {
        if (reservaEnCurso == null) {
            System.out.println("No tienes una reserva en curso. Usa la opcion 4 primero.");
            return false;
        }
        return true;
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            System.out.print("Ingresa un numero valido: ");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine(); // limpiar el salto de linea
        return valor;
    }
}
