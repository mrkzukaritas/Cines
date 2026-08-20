package models;

public interface TipoFuncion {
    int getId();
    String getNombre();
}
```

        ```java
package modelo;

public enum TipoFuncionEnum implements TipoFuncion {
    DOBLADA(1, "Doblada"),
    SUBTITULADA(2, "Subtitulada"),
    ORIGINAL(3, "Original");

    private final int id;
    private final String nombre;

    TipoFuncionEnum(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }
}