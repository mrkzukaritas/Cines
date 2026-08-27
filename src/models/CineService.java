package models;

import exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE - CRUD de Cines y sus Salas.
 * Responsabilidad: crear/editar/eliminar cines y salas.
 * NO maneja funciones (eso es trabajo de FuncionController/FuncionService).
 */
public class CineService {

    private final List<Cine> cines = new ArrayList<>();
    private int contadorCineId = 1;
    private int contadorSalaId = 1;

    // ---------- CREATE: Cine ----------
    public Cine crearCine(String nombre, String direccion, String ciudad) throws ValidationException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("El nombre del cine es obligatorio.");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ValidationException("La ciudad es obligatoria.");
        }
        Cine nuevo = new Cine(contadorCineId++, nombre, direccion, ciudad);
        cines.add(nuevo);
        return nuevo;
    }

    // ---------- CREATE: Sala (dentro de un cine existente) ----------
    public Sala crearSala(Cine cine, String nombre, int capacidad, String tipo) throws ValidationException {
        if (!cines.contains(cine)) {
            throw new ValidationException("El cine no existe en el sistema.");
        }
        if (capacidad <= 0) {
            throw new ValidationException("La capacidad debe ser mayor a 0.");
        }
        Sala nueva = new Sala(contadorSalaId++, nombre, capacidad, tipo);
        cine.getSalas().add(nueva);
        return nueva;
    }

    // ---------- READ ----------
    public List<Cine> listarTodos() {
        return cines;
    }

    public Optional<Cine> buscarPorId(int id) {
        return cines.stream().filter(c -> c.getId() == id).findFirst();
    }

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

    // ---------- UPDATE ----------
    public boolean actualizarCine(int id, String nombre, String direccion, String ciudad) {
        Optional<Cine> encontrado = buscarPorId(id);
        if (encontrado.isEmpty()) {
            return false;
        }
        // Cine no tiene setters en la versión mínima que te di;
        // si quieres poder editarlo, agrega setNombre/setDireccion/setCiudad
        // a la clase Cine. Aquí queda listo el método para cuando los tengas.
        return true;
    }

    // ---------- DELETE ----------
    public boolean eliminarCine(int id) {
        return cines.removeIf(c -> c.getId() == id);
    }

    public boolean eliminarSala(int idCine, int idSala) {
        Optional<Cine> cine = buscarPorId(idCine);
        if (cine.isEmpty()) {
            return false;
        }
        return cine.get().getSalas().removeIf(s -> s.getId() == idSala);
    }
}