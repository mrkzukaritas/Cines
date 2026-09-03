package models;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private int id;
    private String nombre;
    private int capacidad;
    private String tipo;

    private Cine cine; // NUEVO

    private List<Asiento> asientos = new ArrayList<>();
    private List<Funcion> funciones = new ArrayList<>();

    public Sala(int id, String nombre, int capacidad, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipo = tipo;
    }

    public List<Asiento> consultarAsientos() {
        return asientos;
    }

    public boolean consultarDisponibilidad() {
        return asientos.stream()
                .anyMatch(a -> "DISPONIBLE".equals(a.getEstado()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // NUEVO
    public Cine getCine() {
        return cine;
    }

    // NUEVO
    public void setCine(Cine cine) {
        this.cine = cine;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", " + capacidad + " puestos)";
    }

    public static List<Asiento> generarAsientos(int capacidad) {
        return AsientoFactory.crearAsientos(capacidad);
    }

    /** Igual que arriba, pero permite elegir cuántos asientos por fila. */
    public static List<Asiento> generarAsientos(int capacidad, int asientosPorFila) {
        return AsientoFactory.crearAsientos(capacidad, asientosPorFila);
    }
}