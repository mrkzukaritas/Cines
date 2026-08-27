package models;

/**
 * MODEL (Capa Model del MVC)
 *
 * Administrador hereda de Usuario, igual que Cliente.
 * Solo agrega los métodos propios que aparecen bajo "Administrador" en el diagrama.
 */
public class Administrador extends Usuario {

    public Administrador(int id, String nombre, String email, String password, String telefono) {
        super(id, nombre, email, password, telefono);
    }

    // ---------- Implementación de métodos abstractos heredados ----------
    @Override
    public void registrarse() {
        System.out.println("Administrador " + nombre + " registrado exitosamente.");
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Administrador " + nombre + " inició sesión.");
    }

    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }

    // ---------- Métodos propios del diagrama (solo Administrador) ----------
    public void registrarCine() {
        System.out.println(nombre + " registró un nuevo cine.");
    }

    public void registrarSala() {
        System.out.println(nombre + " registró una nueva sala.");
    }

    public void registrarPelicula() {
        System.out.println(nombre + " registró una nueva película.");
    }

    public void programarFuncion() {
        System.out.println(nombre + " programó una nueva función.");
    }

    public void definirPrecio() {
        System.out.println(nombre + " definió un nuevo precio.");
    }

    public void cancelarFuncion() {
        System.out.println(nombre + " canceló una función.");
    }
}