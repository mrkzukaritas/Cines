package controller;

import models.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CONTROLLER - maneja el CRUD de Funciones y las consultas que necesita
 * el flujo del cliente: "esta película, ¿en qué cines la están dando?".
 *
 * Recibe la lista de Cines en el constructor (igual que ya haces en
 * MenuPrincipal con CineController/FuncionController), porque las
 * funciones "viven" dentro de sala.getFunciones(), y las salas dentro
 * de cine.getSalas(). No hay una lista separada de funciones: se
 * recorre la estructura Cine -> Sala -> Funcion.
 */
public class FuncionController {

    private final CineService cineService;
    private int contadorId = 1;

    /**
     * Recibe el CineService (no una lista fija) para que, si el
     * Administrador crea un cine nuevo en tiempo real, FuncionController
     * lo vea automáticamente sin necesidad de reconstruir nada.
     */
    public FuncionController(CineService cineService) {
        this.cineService = cineService;
    }

    private List<Cine> cines() {
        return cineService.listarTodos();
    }

    // =====================================================================
    // CREATE (uso: Administrador -> programar función)
    // =====================================================================
    public Funcion crearFuncion(Cine cine, Sala sala, Pelicula pelicula, TipoFuncion tipoFuncion,
                                LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                                double precio, String formato) {

        if (!cine.getSalas().contains(sala)) {
            System.out.println(">> Esa sala no pertenece al cine seleccionado.");
            return null;
        }
        if (precio <= 0) {
            System.out.println(">> El precio debe ser mayor a 0.");
            return null;
        }
        if (!horaFin.isAfter(horaInicio)) {
            System.out.println(">> La hora de fin debe ser posterior a la hora de inicio.");
            return null;
        }

        Funcion nueva = new Funcion(contadorId++, fecha, horaInicio, horaFin, precio, formato, "PROGRAMADA");
        nueva.setPelicula(pelicula);
        nueva.setSala(sala);
        nueva.setTipoFuncion(tipoFuncion);

        sala.getFunciones().add(nueva);
        System.out.println(">> Función creada con éxito (ID " + nueva.getId() + ").");
        return nueva;
    }

    // =====================================================================
    // READ
    // =====================================================================

    /** Todas las funciones de un cine (recorre todas sus salas). */
    public List<Funcion> listarPorCine(Cine cine) {
        List<Funcion> resultado = new ArrayList<>();
        for (Sala sala : cine.getSalas()) {
            resultado.addAll(sala.getFunciones());
        }
        return resultado;
    }

    /** Todas las funciones de una película, sin importar en qué cine. */
    public List<Funcion> listarPorPelicula(Pelicula pelicula) {
        List<Funcion> resultado = new ArrayList<>();
        for (Cine cine : cines()) {
            for (Sala sala : cine.getSalas()) {
                for (Funcion f : sala.getFunciones()) {
                    if (f.getPelicula() != null && f.getPelicula().getId() == pelicula.getId()) {
                        resultado.add(f);
                    }
                }
            }
        }
        return resultado;
    }

    /**
     * CLAVE PARA TU FLUJO DE CLIENTE:
     * dada una película, devuelve la lista de cines (sin repetir)
     * donde tiene al menos una función programada.
     */
    public List<Cine> listarCinesQueProyectan(Pelicula pelicula) {
        List<Cine> resultado = new ArrayList<>();
        for (Cine cine : cines()) {
            boolean laProyecta = cine.getSalas().stream()
                    .flatMap(sala -> sala.getFunciones().stream())
                    .anyMatch(f -> f.getPelicula() != null && f.getPelicula().getId() == pelicula.getId());
            if (laProyecta) {
                resultado.add(cine);
            }
        }
        return resultado;
    }

    public Optional<Funcion> buscarFuncionPorId(int id) {
        for (Cine cine : cines()) {
            for (Sala sala : cine.getSalas()) {
                for (Funcion f : sala.getFunciones()) {
                    if (f.getId() == id) {
                        return Optional.of(f);
                    }
                }
            }
        }
        return Optional.empty();
    }

    // =====================================================================
    // UPDATE (uso: Administrador -> editar función / definirPrecio)
    // =====================================================================
    public boolean actualizarFuncion(int id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                                     double precio, String formato, TipoFuncion tipoFuncion) {
        Optional<Funcion> encontrada = buscarFuncionPorId(id);
        if (encontrada.isEmpty()) {
            System.out.println(">> No se encontró la función con id " + id);
            return false;
        }
        Funcion f = encontrada.get();
        f.setFechaFuncion(fecha);
        f.setHoraInicio(horaInicio);
        f.setHoraFin(horaFin);
        f.setPrecio(precio);
        f.setFormato(formato);
        f.setTipoFuncion(tipoFuncion);
        return true;
    }

    /** Atajo para "definirPrecio()" del diagrama de Administrador. */
    public boolean definirPrecio(int idFuncion, double nuevoPrecio) {
        Optional<Funcion> encontrada = buscarFuncionPorId(idFuncion);
        if (encontrada.isEmpty() || nuevoPrecio <= 0) {
            return false;
        }
        encontrada.get().setPrecio(nuevoPrecio);
        return true;
    }

    // =====================================================================
    // DELETE (uso: Administrador -> cancelarFuncion)
    // =====================================================================
    public boolean cancelarFuncion(int id) {
        for (Cine cine : cines()) {
            for (Sala sala : cine.getSalas()) {
                boolean eliminada = sala.getFunciones().removeIf(f -> f.getId() == id);
                if (eliminada) {
                    return true;
                }
            }
        }
        System.out.println(">> No se encontró la función con id " + id);
        return false;
    }
    public Cine buscarCineDeSala(Sala sala) {

        for (Cine cine : cines()) {

            if (cine.getSalas().contains(sala)) {
                return cine;
            }
        }

        return null;
    }
}