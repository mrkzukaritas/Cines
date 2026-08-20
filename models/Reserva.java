package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private int id;
    private LocalDate fechaReserva;
    private EstadoReserva estado;
    private double total;

    private Cliente cliente;
    private Funcion funcion;
    private List<DetalleReserva> detalles = new ArrayList<>();

    public Reserva() {
    }

    public Reserva(int id, LocalDate fechaReserva, EstadoReserva estado, double total) {
        this.id = id;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
        this.total = total;
    }

    public void agregarSilla(Asiento asiento, double precio) {
        DetalleReserva detalle = new DetalleReserva(asiento, precio, this);
        detalles.add(detalle);
    }

    public void confirmar() {
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    public double calcularTotal() {
        double suma = 0;
        for (DetalleReserva d : detalles) {
            suma += d.calcularSubtotal();
        }
        this.total = suma;
        return total;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public List<DetalleReserva> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleReserva> detalles) {
        this.detalles = detalles;
    }
}