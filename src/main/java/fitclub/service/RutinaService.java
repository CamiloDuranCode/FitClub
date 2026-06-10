package fitclub.service;

import fitclub.dao.IRutinaDAO;
import fitclub.model.Rutina;

import java.time.LocalDate;
import java.util.List;

/**
 * Capa de servicios para la gestión de rutinas del gimnasio Fit Club.
 * Contiene la lógica de negocio para asignar, actualizar y consultar
 * rutinas personalizadas de los clientes.
 *
 * @author Juan Camilo Rangel Osias
 */

public class RutinaService {

    private final IRutinaDAO rutinaDAO;

    public RutinaService(IRutinaDAO rutinaDAO) {
        this.rutinaDAO = rutinaDAO;
    }

    /**
     * Asigna una nueva rutina personalizada a un cliente.
     */
    public void asignarRutina(Rutina rutina, String clienteCedula, String entrenadorCedula) {
        // Validaciones
        if (rutina == null) {
            throw new IllegalArgumentException("La rutina no puede ser nula.");
        }
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        if (entrenadorCedula == null || entrenadorCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador no puede estar vacía.");
        }
        if (rutina.getNombre() == null || rutina.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la rutina no puede estar vacío.");
        }
        if (rutina.getDescripcion() == null || rutina.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la rutina no puede estar vacía.");
        }

        // Si no tiene fecha, usar la actual
        if (rutina.getFechaAsignacion() == null) {
            rutina.setFechaAsignacion(LocalDate.now());
        }

        rutinaDAO.insertar(rutina, clienteCedula, entrenadorCedula);
    }

    /**
     * Actualiza los datos de una rutina existente.
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
     */
    public List<Rutina> consultarRutinasCliente(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return rutinaDAO.listarPorCliente(clienteCedula);
    }

    /**
     * Busca una rutina por su ID.
     */
    public Rutina buscarPorId(int idRutina) {
        if (idRutina <= 0) {
            throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero.");
        }
        return rutinaDAO.buscarPorId(idRutina);
    }

    /**
     * Elimina una rutina por su ID.
     */
    public void eliminarRutina(int idRutina) {
        if (rutinaDAO.buscarPorId(idRutina) == null) {
            throw new IllegalArgumentException("No existe una rutina con el ID: " + idRutina);
        }
        rutinaDAO.eliminar(idRutina);
    }



}
