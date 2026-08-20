package models;

public class MetodoPago {

    private int id;
    private String tipo;

    public MetodoPago() {
    }

    public MetodoPago(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public boolean validar() {
        return tipo != null && !tipo.isEmpty();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}