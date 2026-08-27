package controller;

import models.*;
import exceptions.ValidationException;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLLER - CRUD de Cines y Salas.
 *
 * IMPORTANTE: para "listar funciones de un cine" NO duplicamos lógica aquí.
 * Delegamos en FuncionController, que ya sabe recorrer cine -> sala -> funciones.
 * Así solo hay UN lugar que sabe cómo se listan las funciones.
 */
public class CineController {

    private final CineService cineService;
    private final FuncionController funcionController; // para delegar consultas de funciones

    public CineController(CineService cineService, FuncionController funcionController) {
        this.cineService = cineService;
        this.funcionController = funcionController;
    }

    // ---------- CREATE ----------
    public Cine registrarCine(String nombre, String direccion, String ciudad) {
        try {
            return cineService.crearCine(nombre, direccion, ciudad);
        } catch (ValidationException e) {
            System.out.println(">> Error al registrar cine: " + e.getMessage());
            return null;
        }
    }

    public Sala registrarSala(Cine cine, String nombre, int capacidad, String tipo) {
        try {
            return cineService.crearSala(cine, nombre, capacidad, tipo);
        } catch (ValidationException e) {
            System.out.println(">> Error al registrar sala: " + e.getMessage());
            return null;
        }
    }

    // ---------- READ ----------
    public List<Cine> listarCines() {
        return cineService.listarTodos();
    }

    public Optional<Cine> buscarCinePorId(int id) {
        return cineService.buscarPorId(id);
    }

    public Optional<Sala> buscarSalaPorId(int idSala) {
        return cineService.buscarSalaPorId(idSala);
    }

    /** Delegado: la lógica real vive en FuncionController. */
    public List<Funcion> listarFunciones(Cine cine) {
        return funcionController.listarPorCine(cine);
    }

    // ---------- DELETE ----------
    public boolean eliminarCine(int id) {
        boolean eliminado = cineService.eliminarCine(id);
        if (!eliminado) {
            System.out.println(">> No se encontró el cine con id " + id);
        }
        return eliminado;
    }

    public boolean eliminarSala(int idCine, int idSala) {
        boolean eliminado = cineService.eliminarSala(idCine, idSala);
        if (!eliminado) {
            System.out.println(">> No se encontró la sala.");
        }
        return eliminado;
    }
}