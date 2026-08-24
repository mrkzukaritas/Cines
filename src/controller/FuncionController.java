package controller;

import models.Asiento;
import models.Cine;
import models.Funcion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FuncionController {

    private List<Cine> cines;

    public FuncionController(List<Cine> cines) {
        this.cines = cines;
    }

    public List<Funcion> listarTodasFunciones() {
        List<Funcion> funciones = new ArrayList<>();
        for (Cine c : cines) {
            funciones.addAll(c.listarFunciones());
        }
        return funciones;
    }

    public Optional<Funcion> buscarFuncionPorId(int id) {
        return listarTodasFunciones().stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    public List<Funcion> listarPorPelicula(String tituloParcial) {
        String buscado = tituloParcial.toLowerCase();
        return listarTodasFunciones().stream()
                .filter(f -> f.getPelicula() != null
                        && f.getPelicula().getTitulo().toLowerCase().contains(buscado))
                .collect(Collectors.toList());
    }

    public List<Funcion> listarPorFecha(LocalDate fecha) {
        return listarTodasFunciones().stream()
                .filter(f -> fecha.equals(f.getFechaFuncion()))
                .collect(Collectors.toList());
    }

    public List<Asiento> listarAsientosDisponibles(Funcion funcion) {
        if (funcion.getSala() == null) {
            return new ArrayList<>();
        }
        return funcion.getSala().consultarAsientos().stream()
                .filter(a -> "DISPONIBLE".equals(a.getEstado()))
                .collect(Collectors.toList());
    }
}
