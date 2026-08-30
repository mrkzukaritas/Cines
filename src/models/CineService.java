package models;

import exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE - CRUD de Cines y sus Salas.
 *
 * Responsabilidad:
 * - Crear cines
 * - Crear salas
 * - Consultar cines y salas
 * - Actualizar cines
 * - Eliminar cines y salas
 *
 * NO maneja funciones.
 * Las funciones son responsabilidad de FuncionController.
 */
public class CineService {

    private final List<Cine> cines = new ArrayList<>();

    private int contadorCineId = 1;
    private int contadorSalaId = 1;

    // =========================================================
    // CREATE - CINE
    // =========================================================

    public Cine crearCine(
            String nombre,
            String direccion,
            String ciudad
    ) throws ValidationException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException(
                    "El nombre del cine es obligatorio."
            );
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ValidationException(
                    "La ciudad es obligatoria."
            );
        }

        Cine nuevo = new Cine(
                contadorCineId++,
                nombre,
                direccion,
                ciudad
        );

        cines.add(nuevo);

        return nuevo;
    }

    // =========================================================
    // CREATE - SALA
    // =========================================================

    public Sala crearSala(
            Cine cine,
            String nombre,
            int capacidad,
            String tipo
    ) throws ValidationException {

        if (cine == null || !cines.contains(cine)) {
            throw new ValidationException(
                    "El cine no existe en el sistema."
            );
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException(
                    "El nombre de la sala es obligatorio."
            );
        }

        if (capacidad <= 0) {
            throw new ValidationException(
                    "La capacidad debe ser mayor a 0."
            );
        }

        Sala nueva = new Sala(
                contadorSalaId++,
                nombre,
                capacidad,
                tipo
        );

        // IMPORTANTE:
        // La sala ahora conoce a qué cine pertenece.
        nueva.setCine(cine);

        // La sala también queda registrada dentro del cine.
        cine.getSalas().add(nueva);

        return nueva;
    }

    // =========================================================
    // READ - CINES
    // =========================================================

    public List<Cine> listarTodos() {
        return cines;
    }

    public Optional<Cine> buscarPorId(int id) {

        return cines.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    // =========================================================
    // READ - SALAS
    // =========================================================

    public Optional<Sala> buscarSalaPorId(int idSala) {

        for (Cine cine : cines) {

            for (Sala sala : cine.getSalas()) {

                if (sala.getId() == idSala) {
                    return Optional.of(sala);
                }
            }
        }

        return Optional.empty();
    }

    // =========================================================
    // UPDATE - CINE
    // =========================================================

    public boolean actualizarCine(
            int id,
            String nombre,
            String direccion,
            String ciudad
    ) {

        Optional<Cine> encontrado = buscarPorId(id);

        if (encontrado.isEmpty()) {
            return false;
        }

        Cine cine = encontrado.get();

        cine.setNombre(nombre);
        cine.setDireccion(direccion);
        cine.setCiudad(ciudad);

        return true;
    }

    // =========================================================
    // UPDATE - SALA
    // =========================================================

    public boolean actualizarSala(
            int idSala,
            String nombre,
            int capacidad,
            String tipo
    ) throws ValidationException {

        Optional<Sala> encontrada = buscarSalaPorId(idSala);

        if (encontrada.isEmpty()) {
            return false;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException(
                    "El nombre de la sala es obligatorio."
            );
        }

        if (capacidad <= 0) {
            throw new ValidationException(
                    "La capacidad debe ser mayor a 0."
            );
        }

        Sala sala = encontrada.get();

        sala.setNombre(nombre);
        sala.setCapacidad(capacidad);
        sala.setTipo(tipo);

        return true;
    }

    // =========================================================
    // DELETE - CINE
    // =========================================================

    public boolean eliminarCine(int id) {

        return cines.removeIf(
                cine -> cine.getId() == id
        );
    }

    // =========================================================
    // DELETE - SALA
    // =========================================================

    public boolean eliminarSala(
            int idCine,
            int idSala
    ) {

        Optional<Cine> cine = buscarPorId(idCine);

        if (cine.isEmpty()) {
            return false;
        }

        return cine.get()
                .getSalas()
                .removeIf(sala -> sala.getId() == idSala);
    }
}