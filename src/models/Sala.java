package models;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private int id;
    private String nombre;
    private int capacidad;
    private String tipo;

    private List<Asiento> asientos = new ArrayList<>();
    private List<Funcion> funciones = new ArrayList<>(); // funciones programadas en esta sala

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
        return asientos.stream().anyMatch(a -> "DISPONIBLE".equals(a.getEstado()));
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCapacidad() { return capacidad; }
    public String getTipo() { return tipo; }
    public List<Asiento> getAsientos() { return asientos; }
    public void setAsientos(List<Asiento> asientos) { this.asientos = asientos; }
    public List<Funcion> getFunciones() { return funciones; }
}