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

    /**
     * Genera automáticamente los asientos de una sala según su capacidad real.
     * Usa 5 asientos por fila (letras A, B, C...) y crea EXACTAMENTE
     * "capacidad" asientos, aunque la última fila quede incompleta.
     *
     * Ejemplos:
     *  - capacidad 20 -> filas A a D completas (5 asientos c/u)
     *  - capacidad 12 -> filas A, B completas (5+5) y C con solo 2 asientos
     *  - capacidad 7  -> fila A completa (5) y B con 2 asientos
     *
     * Es estático porque no depende de una Sala en particular:
     * cualquier clase puede llamar Sala.generarAsientos(capacidad)
     * sin necesitar una instancia.
     */
    public static List<Asiento> generarAsientos(int capacidad) {
        return generarAsientos(capacidad, 5);
    }

    /** Igual que arriba, pero permite elegir cuántos asientos por fila. */
    public static List<Asiento> generarAsientos(int capacidad, int asientosPorFila) {
        List<Asiento> asientos = new ArrayList<>();

        if (capacidad <= 0 || asientosPorFila <= 0) {
            return asientos;
        }

        int id = 1;
        char letra = 'A';
        int creados = 0;

        while (creados < capacidad) {
            int enEstaFila = Math.min(asientosPorFila, capacidad - creados);
            for (int n = 1; n <= enEstaFila; n++) {
                asientos.add(new Asiento(id++, String.valueOf(letra), n, "DISPONIBLE"));
                creados++;
            }
            letra++;
        }

        return asientos;
    }
}