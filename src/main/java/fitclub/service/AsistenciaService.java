package fitclub.service;

import fitclub.dao.IAsistenciaDAO;
import fitclub.model.Asistencia;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Capa de servicios para la gestión de asistencia del gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar y consultar el historial
 * de asistencia de los clientes por cédula.
 */

public class AsistenciaService {

    private final IAsistenciaDAO asistenciaDAO;

    public AsistenciaService(IAsistenciaDAO asistenciaDAO) {
        this.asistenciaDAO = asistenciaDAO;
    }

    /**
     * Registra una nueva asistencia para un cliente.
     * Valida que la cédula no esté vacía y que la fecha no sea futura.
     *
     * @param asistencia    Asistencia a registrar.
     * @param clienteCedula Cédula del cliente que asiste.
     * @throws IllegalArgumentException si la asistencia o la cédula son nulas,
     *                                  o si la fecha es futura.
     */

    public void registrarAsistencia(Asistencia asistencia, String clienteCedula) {
        if (asistencia == null) {
            throw new IllegalArgumentException("La asistencia no puede ser nula.");
        }
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        if (asistencia.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede registrar una asistencia con fecha futura.");
        }
        asistenciaDAO.insertar(asistencia, clienteCedula);
    }

    /**
     * Consulta el historial completo de asistencia de un cliente por cédula.
     *
     * @param clienteCedula Cédula del cliente a consultar.
     * @return Lista de asistencias del cliente. Puede estar vacía si no hay registros.
     * @throws IllegalArgumentException si la cédula es nula o vacía.
     */

    public List<Asistencia> consultarHistorial(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return asistenciaDAO.listarPorCliente(clienteCedula);
    }

    /**
     * Retorna el total de asistencias registradas de un cliente.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Número total de asistencias.
     */

    public int contarAsistencias(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return asistenciaDAO.listarPorCliente(clienteCedula).size();
    }

    /**
     * Busca un registro de asistencia por su ID.
     *
     * @param idAsistencia ID del registro a buscar.
     * @return La asistencia encontrada, o {@code null} si no existe.
     * @throws IllegalArgumentException si el ID es menor o igual a cero.
     */

    public Asistencia buscarPorId(int idAsistencia) {
        if (idAsistencia <= 0) {
            throw new IllegalArgumentException("El ID de asistencia debe ser mayor a cero.");
        }
        return asistenciaDAO.buscarPorId(idAsistencia);
    }

    /**
     * Elimina un registro de asistencia por su ID.
     * Valida que el registro exista antes de eliminar.
     *
     * @param idAsistencia ID del registro a eliminar.
     * @throws IllegalArgumentException si el registro no existe.
     */

    public void eliminarAsistencia(int idAsistencia) {
        if (asistenciaDAO.buscarPorId(idAsistencia) == null) {
            throw new IllegalArgumentException("No existe un registro de asistencia con el ID: " + idAsistencia);
        }
        asistenciaDAO.eliminar(idAsistencia);
    }

}
