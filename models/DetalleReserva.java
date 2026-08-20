package models;

public class DetalleReserva {

    private int id;
    private double precio;

    private Reserva reservaaaaaa;
    private Asiento asiento;


    public DetalleReserva(Asiento asiento, double precio, Reserva reserva) {
        this.asiento = asiento;
        this.precio = precio;
        this.reserva = reserva;
    }

    public double calcularSubtotal() {
        return precio;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
}