package models;

import java.util.ArrayList;
import java.util.List;

public class AsientoFactory {

    private static final int ASIENTOS_POR_FILA_DEFECTO = 5;

    private AsientoFactory() {
        // Clase de utilidad: no se instancia.
    }

    public static List<Asiento> crearAsientos(int capacidad) {
        return crearAsientos(capacidad, ASIENTOS_POR_FILA_DEFECTO);
    }

    public static List<Asiento> crearAsientos(int capacidad, int asientosPorFila) {
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
