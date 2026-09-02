package models;

import java.time.LocalDate;

public class Pelicula {

    private int id;
    private String titulo;
    private String sinopsis;
    private int duracion;
    private String genero;
    private String clasificacion;
    private String idioma;
    private LocalDate fechaEstreno;
    private String rutaImagen; // ruta o URL de la foto/poster

    public Pelicula() {
    }

    public Pelicula(int id, String titulo, String sinopsis, int duracion, String genero,
                    String clasificacion, String idioma, LocalDate fechaEstreno) {
        this.id = id;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.idioma = idioma;
        this.fechaEstreno = fechaEstreno;
    }

    public Pelicula(int id, String titulo, String sinopsis, int duracion, String genero,
                    String clasificacion, String idioma, LocalDate fechaEstreno, String rutaImagen) {
        this(id, titulo, sinopsis, duracion, genero, clasificacion, idioma, fechaEstreno);
        this.rutaImagen = rutaImagen;
    }

    public boolean consultarFunciones() {
        return true;
    }

    public void consultarDetalles() {
        System.out.println("--- " + titulo + " --- (" + rutaImagen + ")");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public LocalDate getFechaEstreno() { return fechaEstreno; }
    public void setFechaEstreno(LocalDate fechaEstreno) { this.fechaEstreno = fechaEstreno; }
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    @Override
    public String toString() {
        return titulo;
    }
}