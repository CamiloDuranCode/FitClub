package fitclub.service;

import fitclub.dao.IClienteDAO;
import fitclub.model.Cliente;

import java.util.List;

/**
 * Capa de servicios para la gestión de clientes del gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar, buscar y actualizar clientes,
 * delegando la persistencia a la capa DAO mediante {@link IClienteDAO}.
 */

public class ClienteService {

    private final IClienteDAO clienteDAO;

    public ClienteService(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * Valida que el cliente no sea nulo y que su cédula no esté ya registrada.
     *
     * @param cliente Cliente a registrar.
     * @throws IllegalArgumentException si el cliente es nulo o la cédula ya existe.
     */

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede estar vacío.");
        }
        if (clienteDAO.buscarPorCedula(cliente.getCedula()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con la cédula: " + cliente.getCedula());
        }
        clienteDAO.insertar(cliente);
    }

    /**
     * Busca un cliente por su número de cédula.
     *
     * @param cedula Cédula del cliente a buscar.
     * @return El cliente encontrado, o {@code null} si no existe.
     * @throws IllegalArgumentException si la cédula es nula o vacía.
     */

    public Cliente buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        return clienteDAO.buscarPorCedula(cedula);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    /**
     * Actualiza los datos de un cliente existente.
     * Valida que el cliente exista antes de actualizar.
     *
     * @param cliente Cliente con los datos actualizados.
     * @throws IllegalArgumentException si el cliente es nulo o no existe en el sistema.
     */

    public void actualizarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (clienteDAO.buscarPorCedula(cliente.getCedula()) == null) {
            throw new IllegalArgumentException("No existe un cliente con la cédula: " + cliente.getCedula());
        }
        clienteDAO.actualizar(cliente);
    }

    /**
     * Elimina un cliente del sistema por su cédula.
     * Valida que el cliente exista antes de eliminar.
     *
     * @param cedula Cédula del cliente a eliminar.
     * @throws IllegalArgumentException si la cédula es nula, vacía o el cliente no existe.
     */
    public void eliminarCliente(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        if (clienteDAO.buscarPorCedula(cedula) == null) {
            throw new IllegalArgumentException("No existe un cliente con la cédula: " + cedula);
        }
        clienteDAO.eliminar(cedula);
    }
}
