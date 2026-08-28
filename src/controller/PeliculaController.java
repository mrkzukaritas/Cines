package controller;

import models.Pelicula;
import models.PeliculaService;
import exceptions.ValidationException;

import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER - punto de entrada para la View
 * en todo lo relacionado con películas.
 */
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(
            PeliculaService peliculaService
    ) {
        this.peliculaService = peliculaService;
    }

    // ---------- CREATE ----------

    public Pelicula registrarPelicula(
            String titulo,
            String sinopsis,
            int duracion,
            String genero,
            String clasificacion,
            String idioma,
            LocalDate fechaEstreno,
            String rutaImagen
    ) {

        try {

            Pelicula nueva = new Pelicula(
                    0,
                    titulo,
                    sinopsis,
                    duracion,
                    genero,
                    clasificacion,
                    idioma,
                    fechaEstreno,
                    rutaImagen
            );

            return peliculaService.crear(nueva);

        } catch (ValidationException e) {

            System.out.println(
                    ">> Error al registrar película: "
                            + e.getMessage()
            );

            return null;
        }
    }

    // ---------- READ ----------

    public List<Pelicula> listarPeliculas() {

        return peliculaService.listarTodas();
    }

    public Pelicula buscarPorId(int id) {

        return peliculaService
                .buscarPorId(id)
                .orElse(null);
    }

    // ---------- UPDATE ----------

    public boolean actualizarPelicula(
            int id,
            String titulo,
            String sinopsis,
            int duracion,
            String genero,
            String clasificacion,
            String idioma,
            LocalDate fechaEstreno,
            String rutaImagen
    ) {

        try {

            Pelicula datosNuevos =
                    new Pelicula(
                            0,
                            titulo,
                            sinopsis,
                            duracion,
                            genero,
                            clasificacion,
                            idioma,
                            fechaEstreno,
                            rutaImagen
                    );

            boolean actualizado =
                    peliculaService.actualizar(
                            id,
                            datosNuevos
                    );

            if (!actualizado) {

                System.out.println(
                        ">> No se encontró la película con id "
                                + id
                );
            }

            return actualizado;

        } catch (ValidationException e) {

            System.out.println(
                    ">> Error al actualizar película: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ---------- DELETE ----------

    public boolean eliminarPelicula(int id) {

        boolean eliminado =
                peliculaService.eliminar(id);

        if (!eliminado) {

            System.out.println(
                    ">> No se encontró la película con id "
                            + id
            );
        }

        return eliminado;
    }
}