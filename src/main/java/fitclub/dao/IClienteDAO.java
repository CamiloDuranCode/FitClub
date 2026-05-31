package fitclub.dao;

import fitclub.model.Cliente;
import java.util.List;

public interface IClienteDAO {
    void insertar(Cliente cliente);
    void actualizar(Cliente cliente);
    void desactivar(String cedula);
    Cliente buscarPorCedula(String cedula);
    List<Cliente> listarTodos();
    List<Cliente> listarActivos();
}