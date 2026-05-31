package fitclub.service;

import fitclub.dao.IEntrenadorDAO;
import fitclub.model.Entrenador;

import java.util.List;

/**
 * Capa de servicios para la gestión de entrenadores del gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar, buscar y actualizar entrenadores,
 * delegando la persistencia a la capa DAO mediante {@link IEntrenadorDAO}.
 */

public class EntrenadorService {

    private final IEntrenadorDAO entrenadorDAO;

    public EntrenadorService(IEntrenadorDAO entrenadorDAO) {
        this.entrenadorDAO = entrenadorDAO;
    }

    /**
     * Registra un nuevo entrenador en el sistema.
     * Valida que el entrenador no sea nulo y que su cédula no esté ya registrada.
     *
     * @param entrenador Entrenador a registrar.
     * @throws IllegalArgumentException si el entrenador es nulo o la cédula ya existe.
     */

    public void registrarEntrenador(Entrenador entrenador) {
        if (entrenador == null) {
            throw new IllegalArgumentException("El entrenador no puede ser nulo.");
        }
        if (entrenadorDAO.buscarPorCedula(entrenador.getCedula()) != null) {
            throw new IllegalArgumentException("Ya existe un entrenador registrado con la cédula: " + entrenador.getCedula());
        }
        entrenadorDAO.insertar(entrenador);
    }

    /**
     * Busca un entrenador por su número de cédula.
     *
     * @param cedula Cédula del entrenador a buscar.
     * @return El entrenador encontrado, o {@code null} si no existe.
     * @throws IllegalArgumentException si la cédula es nula o vacía.
     */

    public Entrenador buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        return entrenadorDAO.buscarPorCedula(cedula);
    }

    /**
     * Retorna la lista de todos los entrenadores registrados en el sistema.
     *
     * @return Lista de entrenadores. Puede estar vacía si no hay registros.
     */
    public List<Entrenador> listarEntrenadores() {
        return entrenadorDAO.listarTodos();
    }

    /**
     * Actualiza los datos de un entrenador existente.
     * Valida que el entrenador exista antes de actualizar.
     *
     * @param entrenador Entrenador con los datos actualizados.
     * @throws IllegalArgumentException si el entrenador es nulo o no existe en el sistema.
     */

    public void actualizarEntrenador(Entrenador entrenador) {
        if (entrenador == null) {
            throw new IllegalArgumentException("El entrenador no puede ser nulo.");
        }
        if (entrenadorDAO.buscarPorCedula(entrenador.getCedula()) == null) {
            throw new IllegalArgumentException("No existe un entrenador con la cédula: " + entrenador.getCedula());
        }
        entrenadorDAO.actualizar(entrenador);
    }

    /**
     * Elimina un entrenador por su cédula.
     * Valida que el entrenador exista antes de eliminar.
     *
     * @param cedula Cédula del entrenador a eliminar.
     * @throws IllegalArgumentException si el entrenador no existe.
     */

    public void eliminarEntrenador(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        if (entrenadorDAO.buscarPorCedula(cedula) == null) {
            throw new IllegalArgumentException("No existe un entrenador con la cédula: " + cedula);
        }
        entrenadorDAO.eliminar(cedula);
    }
    
}
