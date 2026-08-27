package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Funcion {

    private int id;
    private LocalDate fechaFuncion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double precio;
    private String formato;
    private String estado;

    private Pelicula pelicula;
    private Sala sala;
    private TipoFuncion tipoFuncion;

    public Funcion() {
    }

    public Funcion(int id, LocalDate fechaFuncion, LocalTime horaInicio, LocalTime horaFin,
                   double precio, String formato, String estado) {
        this.id = id;
        this.fechaFuncion = fechaFuncion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
        this.formato = formato;
        this.estado = estado;
    }

    public boolean consultarDisponibilidad() {
        return sala != null && sala.consultarDisponibilidad();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getFechaFuncion() { return fechaFuncion; }
    public void setFechaFuncion(LocalDate fechaFuncion) { this.fechaFuncion = fechaFuncion; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }
    public TipoFuncion getTipoFuncion() { return tipoFuncion; }
    public void setTipoFuncion(TipoFuncion tipoFuncion) { this.tipoFuncion = tipoFuncion; }
}