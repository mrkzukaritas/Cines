package controller;

import models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoController {

    private List<Pago> pagos = new ArrayList<>();
    private int siguienteId = 1;

    /**
     * Procesa el pago de una reserva. Si el metodo de pago es valido y la reserva
     * esta confirmada, crea el Pago, lo asocia a la reserva y marca la reserva como PAGADA.
     * Devuelve null si el metodo de pago no es valido.
     */
    public Pago procesarPago(Reserva reserva, MetodoPago metodoPago) {
        if (!metodoPago.validar()) {
            return null;
        }

        double total = reserva.calcularTotal();
        Pago pago = new Pago(siguienteId++, (float) total, LocalDateTime.now(), "APROBADO");
        pago.setMetodoPago(metodoPago);
        pago.setReserva(reserva);

        reserva.setEstado(EstadoReserva.PAGADA);
        pagos.add(pago);
        return pago;
    }

    public List<Pago> listarPagos() {
        return pagos;
    }
}
