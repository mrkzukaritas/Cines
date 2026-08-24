package controller;

import models.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>();
    private int siguienteId = 1;

    public Cliente registrarCliente(String nombre, String email, String telefono) {
        Cliente cliente = new Cliente(siguienteId++, nombre, email, telefono);
        clientes.add(cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    public Optional<Cliente> buscarPorId(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clientes.stream().filter(c -> c.getEmail().equalsIgnoreCase(email)).findFirst();
    }
}
