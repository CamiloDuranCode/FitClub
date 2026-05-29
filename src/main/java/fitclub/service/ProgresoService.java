package fitclub.service;

import fitclub.dao.IProgresoDAO;
import fitclub.model.Progreso;

import java.util.List;

/**
 * Capa de servicios para el seguimiento de progreso físico en el gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar y consultar el progreso
 * físico de los clientes vinculado a sus rutinas asignadas.
 */

public class ProgresoService {

    private final IProgresoDAO progresoDAO;

    public ProgresoService(IProgresoDAO progresoDAO) {
        this.progresoDAO = progresoDAO;
    }

    /**
     * Registra un nuevo progreso físico de un cliente vinculado a una rutina.
     *
     * @param progreso      Progreso a registrar.
     * @param clienteCedula Cédula del cliente.
     * @param rutinaId      ID de la rutina asociada al progreso.
     * @throws IllegalArgumentException si el progreso o la cédula son nulos,
     *                                  o si el ID de rutina es inválido.
     */

    public void registrarProgreso(Progreso progreso, String clienteCedula, int rutinaId) {
        if (progreso == null) {
            throw new IllegalArgumentException("El progreso no puede ser nulo.");
        }
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        if (rutinaId <= 0) {
            throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero.");
        }
        progresoDAO.insertar(progreso, clienteCedula, rutinaId);
    }

    /**
     * Actualiza un registro de progreso existente.
     *
     * @param progreso Progreso con los datos actualizados.
     * @throws IllegalArgumentException si el progreso es nulo o no existe.
     */

    public void actualizarProgreso(Progreso progreso) {
        if (progreso == null) {
            throw new IllegalArgumentException("El progreso no puede ser nulo.");
        }
        if (progresoDAO.buscarPorId(progreso.getIdProgreso()) == null) {
            throw new IllegalArgumentException("No existe un registro de progreso con el ID: " + progreso.getIdProgreso());
        }
        progresoDAO.actualizar(progreso);
    }

    /**
     * Consulta el historial de progreso físico de un cliente.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Lista de registros de progreso. Puede estar vacía si no hay registros.
     * @throws IllegalArgumentException si la cédula es nula o vacía.
     */

    public List<Progreso> consultarProgreso(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return progresoDAO.listarPorCliente(clienteCedula);
    }

    /**
     * Busca un registro de progreso por su ID.
     *
     * @param idProgreso ID del registro a buscar.
     * @return El progreso encontrado, o {@code null} si no existe.
     * @throws IllegalArgumentException si el ID es menor o igual a cero.
     */

    public Progreso buscarPorId(int idProgreso) {
        if (idProgreso <= 0) {
            throw new IllegalArgumentException("El ID de progreso debe ser mayor a cero.");
        }
        return progresoDAO.buscarPorId(idProgreso);
    }

    /**
     * Elimina un registro de progreso por su ID.
     *
     * @param idProgreso ID del registro a eliminar.
     * @throws IllegalArgumentException si el registro no existe.
     */

    public void eliminarProgreso(int idProgreso) {
        if (progresoDAO.buscarPorId(idProgreso) == null) {
            throw new IllegalArgumentException("No existe un registro de progreso con el ID: " + idProgreso);
        }
        progresoDAO.eliminar(idProgreso);
    }

}
