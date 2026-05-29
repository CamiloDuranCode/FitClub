package fitclub.service;

import fitclub.dao.IRutinaDAO;
import fitclub.model.Rutina;

import java.util.List;

/**
 * Capa de servicios para la gestión de rutinas del gimnasio Fit Club.
 * Contiene la lógica de negocio para asignar, actualizar y consultar
 * rutinas personalizadas de los clientes.
 */

public class RutinaService {

    private final IRutinaDAO rutinaDAO;

    public RutinaService(IRutinaDAO rutinaDAO) {
        this.rutinaDAO = rutinaDAO;
    }

    /**
     * Asigna una nueva rutina personalizada a un cliente.
     * Valida que la rutina, la cédula del cliente y la del entrenador no sean nulas.
     *
     * @param rutina           Rutina a asignar.
     * @param clienteCedula    Cédula del cliente que recibe la rutina.
     * @param entrenadorCedula Cédula del entrenador que diseña la rutina.
     * @throws IllegalArgumentException si alguno de los parámetros es nulo o vacío.
     */

    public void asignarRutina(Rutina rutina, String clienteCedula, String entrenadorCedula) {
        if (rutina == null) {
            throw new IllegalArgumentException("La rutina no puede ser nula.");
        }
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        if (entrenadorCedula == null || entrenadorCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador no puede estar vacía.");
        }
        rutinaDAO.insertar(rutina, clienteCedula, entrenadorCedula);
    }

    /**
     * Actualiza los datos de una rutina existente.
     *
     * @param rutina Rutina con los datos actualizados.
     * @throws IllegalArgumentException si la rutina es nula o no existe.
     */

    public void actualizarRutina(Rutina rutina) {
        if (rutina == null) {
            throw new IllegalArgumentException("La rutina no puede ser nula.");
        }
        if (rutinaDAO.buscarPorId(rutina.getIdRutina()) == null) {
            throw new IllegalArgumentException("No existe una rutina con el ID: " + rutina.getIdRutina());
        }
        rutinaDAO.actualizar(rutina);
    }

    /**
     * Consulta todas las rutinas asignadas a un cliente.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Lista de rutinas del cliente. Puede estar vacía si no tiene ninguna.
     * @throws IllegalArgumentException si la cédula es nula o vacía.
     */

    public List<Rutina> consultarRutinasCliente(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return rutinaDAO.listarPorCliente(clienteCedula);
    }

    /**
     * Busca una rutina por su ID.
     *
     * @param idRutina ID de la rutina a buscar.
     * @return La rutina encontrada, o {@code null} si no existe.
     * @throws IllegalArgumentException si el ID es menor o igual a cero.
     */

    public Rutina buscarPorId(int idRutina) {
        if (idRutina <= 0) {
            throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero.");
        }
        return rutinaDAO.buscarPorId(idRutina);
    }

    /**
     * Elimina una rutina por su ID.
     * Valida que la rutina exista antes de eliminar.
     *
     * @param idRutina ID de la rutina a eliminar.
     * @throws IllegalArgumentException si la rutina no existe.
     */

    public void eliminarRutina(int idRutina) {
        if (rutinaDAO.buscarPorId(idRutina) == null) {
            throw new IllegalArgumentException("No existe una rutina con el ID: " + idRutina);
        }
        rutinaDAO.eliminar(idRutina);
    }



}
