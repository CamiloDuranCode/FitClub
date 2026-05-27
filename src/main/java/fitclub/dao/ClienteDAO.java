package fitclub.dao;

import fitclub.model.Cliente;

import java.util.List;


public interface ClienteDAO {
    void insertar(Cliente cliente);
    void actualizar(Cliente cliente);
    void eliminar(String cedula);
    Cliente buscarPorCedula(String cedula);
    List<Cliente> listarTodos();
}