package fitclub.service;


import fitclub.model.Ejercicio;
import fitclub.model.Maquina;
import fitclub.dao.IEjercicioDAO;

import java.util.List;

/**
 * Capa de servicios para la gestión de ejercicios dentro de rutinas.
 *
 * @author Juan Camilo Rangel Osias
 */

public class EjercicioService {

    private final IEjercicioDAO ejercicioDAO;
    private final MaquinaService maquinaService;

    public EjercicioService(IEjercicioDAO ejercicioDAO, MaquinaService maquinaService) {
        this.ejercicioDAO = ejercicioDAO;
        this.maquinaService = maquinaService;
    }

    /**
     * Agrega un ejercicio a una rutina
     */
    public void agregarEjercicio(Ejercicio ejercicio) {
        // Validaciones
        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser nulo.");
        }
        if (ejercicio.getIdRutina() <= 0) {
            throw new IllegalArgumentException("El ID de rutina es requerido.");
        }
        if (ejercicio.getNombre() == null || ejercicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio no puede estar vacío.");
        }
        if (ejercicio.getSeries() <= 0) {
            throw new IllegalArgumentException("Las series deben ser mayores a cero.");
        }
        if (ejercicio.getRepeticiones() <= 0) {
            throw new IllegalArgumentException("Las repeticiones deben ser mayores a cero.");
        }

        // Validar que la máquina existe (si se asignó una)
        if (ejercicio.tieneMaquina()) {
            Maquina maquina = maquinaService.buscarPorId(ejercicio.getIdMaquina());
            if (maquina == null) {
                throw new IllegalArgumentException("La máquina especificada no existe.");
            }
            if (!maquina.isActiva()) {
                throw new IllegalArgumentException("La máquina no está activa.");
            }
        }

        ejercicioDAO.insertar(ejercicio);
    }

    /**
     * Actualiza un ejercicio existente
     */
    public void actualizarEjercicio(Ejercicio ejercicio) {
        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser nulo.");
        }
        if (ejercicio.getIdEjercicio() <= 0) {
            throw new IllegalArgumentException("El ID de ejercicio es requerido.");
        }
        if (ejercicioDAO.buscarPorId(ejercicio.getIdEjercicio()) == null) {
            throw new IllegalArgumentException("No existe un ejercicio con el ID: " + ejercicio.getIdEjercicio());
        }

        ejercicioDAO.actualizar(ejercicio);
    }

    /**
     * Elimina un ejercicio por su ID
     */
    public void eliminarEjercicio(int idEjercicio) {
        if (idEjercicio <= 0) {
            throw new IllegalArgumentException("El ID de ejercicio debe ser mayor a cero.");
        }
        if (ejercicioDAO.buscarPorId(idEjercicio) == null) {
            throw new IllegalArgumentException("No existe un ejercicio con el ID: " + idEjercicio);
        }
        ejercicioDAO.eliminar(idEjercicio);
    }

    /**
     * Lista todos los ejercicios de una rutina
     */
    public List<Ejercicio> listarPorRutina(int idRutina) {
        if (idRutina <= 0) {
            throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero.");
        }
        return ejercicioDAO.listarPorRutina(idRutina);
    }

    /**
     * Busca un ejercicio por su ID
     */
    public Ejercicio buscarPorId(int idEjercicio) {
        if (idEjercicio <= 0) {
            throw new IllegalArgumentException("El ID de ejercicio debe ser mayor a cero.");
        }
        return ejercicioDAO.buscarPorId(idEjercicio);
    }


}
