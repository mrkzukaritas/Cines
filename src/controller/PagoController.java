package controller;

import models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoController {

    private List<Pago> pagos = new ArrayList<>();
    private int siguienteId = 1;

    public Pago procesarPago(Reserva reserva, MetodoPago metodoPago) {

        // La reserva debe existir
        if (reserva == null) {
            return null;
        }

        // Debe tener al menos un asiento
        if (reserva.getDetalles().isEmpty()) {
            return null;
        }

        // El método de pago debe ser válido
        if (metodoPago == null || !metodoPago.validar()) {
            return null;
        }

        double total = reserva.calcularTotal();

        Pago pago = new Pago(
                siguienteId++,
                (float) total,
                LocalDateTime.now(),
                "APROBADO"
        );

        pago.setMetodoPago(metodoPago);
        pago.setReserva(reserva);

        // El pago cambia el estado de la reserva
        reserva.setEstado(EstadoReserva.PAGADA);

        pagos.add(pago);

        return pago;
    }

    public List<Pago> listarPagos() {
        return pagos;
    }
}