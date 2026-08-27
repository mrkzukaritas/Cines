package models;

/**
 * MODEL (Capa Model del MVC)
 *
 * Cliente hereda de Usuario (extends Usuario).
 * Obtiene automáticamente: id, nombre, email, password, telefono
 * y los métodos cerrarSesion() y actualizarPerfil().
 *
 * Solo agrega los métodos propios que aparecen bajo "Cliente" en el diagrama.
 */
public class Cliente extends Usuario {

    public Cliente(int id, String nombre, String email, String password, String telefono) {
        super(id, nombre, email, password, telefono); // llama al constructor de Usuario
    }

    // ---------- Implementación de métodos abstractos heredados ----------
    @Override
    public void registrarse() {
        System.out.println("Cliente " + nombre + " registrado exitosamente.");
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Cliente " + nombre + " inició sesión.");
    }

    @Override
    public Rol getRol() {
        return Rol.CLIENTE;
    }

    // ---------- Métodos propios del diagrama (solo Cliente) ----------
    public void buscarFunciones(String pelicula) {
        System.out.println("Buscando funciones de: " + pelicula);
    }

    public void compararPrecios(String pelicula) {
        System.out.println("Comparando precios para: " + pelicula);
    }

    public void seleccionarSilla(String funcion) {
        System.out.println("Silla seleccionada para la función: " + funcion);
    }

    public void realizarReserva() {
        System.out.println("Reserva realizada por " + nombre);
    }

    public void pagarReserva() {
        System.out.println("Reserva pagada por " + nombre);
    }

    public void verHistorialReservar() {
        System.out.println("Mostrando historial de reservas de " + nombre);
    }
}