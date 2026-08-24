package controller;

import modelo.Cine;
import modelo.Funcion;
import modelo.Sala;

import java.util.List;
import java.util.Optional;

public class CineController {

    private List<Cine> cines;

    public CineController(List<Cine> cines) {
        this.cines = cines;
    }

    public List<Cine> listarCines() {
        return cines;
    }

    public Optional<Cine> buscarCinePorId(int id) {
        return cines.stream().filter(c -> c.getId() == id).findFirst();
    }

    public List<Sala> listarSalas(Cine cine) {
        return cine.listarSalas();
    }

    public List<Funcion> listarFunciones(Cine cine) {
        return cine.listarFunciones();
    }
}
