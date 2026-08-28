package controller;

import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservaController {

    private List<Reserva> reservas = new ArrayList<>();
    private int siguienteId = 1;

    public Reserva crearReserva(Cliente cliente, Funcion funcion) {
        Reserva reserva = new Reserva(siguienteId++, LocalDate.now(), EstadoReserva.PENDIENTE, 0);
        reserva.setCliente(cliente);
        reserva.setFuncion(funcion);
        reservas.add(reserva);
        return reserva;
    }

    /**
     * Agrega un asiento a la reserva, valida que este disponible y lo marca como ocupado.
     * Devuelve false si el asiento ya estaba ocupado.
     */
    public boolean agregarAsiento(Reserva reserva, Asiento asiento) {
        if (!"DISPONIBLE".equals(asiento.getEstado())) {
            return false;
        }
        double precio = reserva.getFuncion() != null ? reserva.getFuncion().getPrecio() : 0;
        asiento.marcarOcupada();
        reserva.agregarAsiento(asiento, precio);
        reserva.calcularTotal();
        return true;
    }

    public void confirmarReserva(Reserva reserva) {
        reserva.confirmar();
    }

    public void cancelarReserva(Reserva reserva) {
        for (DetalleReserva d : reserva.getDetalles()) {
            if (d.getAsiento() != null) {
                d.getAsiento().marcarDisponible();
            }
        }
        reserva.cancelar();
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }

    public List<Reserva> listarReservasPorCliente(Cliente cliente) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getCliente() != null && r.getCliente().getId() == cliente.getId()) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public Optional<Reserva> buscarPorId(int id) {
        return reservas.stream().filter(r -> r.getId() == id).findFirst();
    }
}
