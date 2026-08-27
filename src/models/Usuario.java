package models;

/**
 * MODEL (Capa Model del MVC)
 *
 * Clase abstracta Usuario, tal como aparece en el diagrama.
 * No se puede instanciar directamente: solo se instancian sus hijas
 * Cliente y Administrador.
 *
 * Atributos protegidos (protected) para que las subclases puedan
 * acceder a ellos directamente si lo necesitan.
 */
public abstract class Usuario {

    // ---------- Atributos del diagrama ----------
    protected int id;
    protected String nombre;
    protected String email;
    protected String password;
    protected String telefono;

    // ---------- Constructor ----------
    public Usuario(int id, String nombre, String email, String password, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
    }

    // ---------- Métodos abstractos del diagrama ----------
    // Cada subclase (Cliente / Administrador) puede tener un comportamiento
    // distinto al registrarse o iniciar sesión, por eso son abstractos.
    public abstract void registrarse();
    public abstract void iniciarSesion();

    // ---------- Métodos comunes del diagrama ----------
    // Estos son iguales para Cliente y Administrador, por eso NO son abstractos,
    // están implementados aquí y las hijas los heredan tal cual.
    public void cerrarSesion() {
        System.out.println("Usuario " + nombre + " cerró sesión.");
    }

    public void actualizarPerfil() {
        System.out.println("Perfil de " + nombre + " actualizado.");
    }

    // ---------- Método clave para saber el rol sin instanceof ----------
    // Cada subclase lo implementa devolviendo su propio Rol.
    public abstract Rol getRol();

    // ---------- Getters y Setters ----------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}