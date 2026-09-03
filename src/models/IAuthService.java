package models;

import exceptions.ValidationException;
import exceptions.AutenticacionException;

public interface IAuthService {

    void registrar(Usuario nuevoUsuario) throws ValidationException;

    Usuario iniciarSesion(String email, String password) throws AutenticacionException;

    void agregarUsuarioInicial(Usuario usuario);
}