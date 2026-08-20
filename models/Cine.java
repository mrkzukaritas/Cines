package models;

import java.util.ArrayList;
import java.util.List;

public class Cine {

    private int id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<Sala> salas = new ArrayList<>();

    public Cine() {
    }

    public Cine(int id, String nombre, String direccion, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public List<Sala> listarSalas() {
        return salas;
    }

    public List<Funcion> listarFunciones() {
        List<Funcion> funciones = new ArrayList<>();
        for (Sala s : salas) {
            funciones.addAll(s.getFunciones());
        }
        return funciones;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }
}