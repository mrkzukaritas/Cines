package models;

/**
 * Enum que representa los posibles roles de un Usuario.
 * Se usa para saber "es admin o no" sin necesidad de usar instanceof
 * en el Controller (evita romper el principio de sustitución de Liskov).
 */
public enum Rol {
    CLIENTE,
    ADMINISTRADOR
}
