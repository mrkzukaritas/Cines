
package models;

import exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE - catálogo central de películas (CRUD).
 * En memoria por ahora; el día que conectes base de datos,
 * solo cambia el "adentro" de estos métodos, el Controller no se entera.
 */
public class PeliculaService {

    private final List<Pelicula> peliculas = new ArrayList<>();
    private int contadorId = 1;

    // ---------- CREATE ----------
    public Pelicula crear(Pelicula p) throws ValidationException {
        validar(p);
        p.setId(contadorId++);
        peliculas.add(p);
        return p;
    }

    // ---------- READ ----------
    public List<Pelicula> listarTodas() {
        return peliculas;
    }

    public Optional<Pelicula> buscarPorId(int id) {
        return peliculas.stream().filter(p -> p.getId() == id).findFirst();
    }

    // ---------- UPDATE ----------
    // Recibe el id a modificar y un objeto con los datos nuevos.
    public boolean actualizar(int id, Pelicula datosNuevos) throws ValidationException {
        Optional<Pelicula> existente = buscarPorId(id);
        if (existente.isEmpty()) {
            return false;
        }
        validar(datosNuevos);

        Pelicula p = existente.get();
        p.setTitulo(datosNuevos.getTitulo());
        p.setSinopsis(datosNuevos.getSinopsis());
        p.setDuracion(datosNuevos.getDuracion());
        p.setGenero(datosNuevos.getGenero());
        p.setClasificacion(datosNuevos.getClasificacion());
        p.setIdioma(datosNuevos.getIdioma());
        p.setFechaEstreno(datosNuevos.getFechaEstreno());
        p.setRutaImagen(datosNuevos.getRutaImagen());
        return true;
    }

    // ---------- DELETE ----------
    public boolean eliminar(int id) {
        return peliculas.removeIf(p -> p.getId() == id);
    }

    // ---------- Validación interna ----------
    private void validar(Pelicula p) throws ValidationException {
        if (p.getTitulo() == null || p.getTitulo().trim().isEmpty()) {
            throw new ValidationException("El título de la película es obligatorio.");
        }
        if (p.getDuracion() <= 0) {
            throw new ValidationException("La duración debe ser mayor a 0 minutos.");
        }
        if (p.getGenero() == null || p.getGenero().trim().isEmpty()) {
            throw new ValidationException("El género es obligatorio.");
        }
    }
}