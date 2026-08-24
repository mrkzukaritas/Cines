package models;

public class Asiento {

    private int id;
    private String fila;
    private int numero;
    private String estado; // "DISPONIBLE" u "OCUPADA"

    public Asiento() {
    }

    public Asiento(int id, String fila, int numero, String estado) {
        this.id = id;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public void marcarOcupada() {
        this.estado = "OCUPADA";
    }

    public void marcarDisponible() {
        this.estado = "DISPONIBLE";
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}