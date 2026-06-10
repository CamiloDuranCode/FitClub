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

    public void registrarEntrenador(Entrenador entrenador) {
        if (entrenador == null) throw new IllegalArgumentException("El entrenador no puede ser nulo.");
        if (entrenadorDAO.buscarPorCedula(entrenador.getCedula()) != null)
            throw new IllegalArgumentException("Ya existe un entrenador registrado con la cédula: " + entrenador.getCedula());
        entrenadorDAO.insertar(entrenador);
    }

    public Entrenador buscarPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía.");
        return entrenadorDAO.buscarPorCedula(cedula);
    }

    public List<Entrenador> listarEntrenadores() {
        return entrenadorDAO.listarTodos();
    }

    public List<Entrenador> listarEntrenadoresActivos() {
        return entrenadorDAO.listarActivos();
    }

    public void actualizarEntrenador(Entrenador entrenador) {
        if (entrenador == null) throw new IllegalArgumentException("El entrenador no puede ser nulo.");
        if (entrenadorDAO.buscarPorCedula(entrenador.getCedula()) == null)
            throw new IllegalArgumentException("No existe un entrenador con la cédula: " + entrenador.getCedula());
        entrenadorDAO.actualizar(entrenador);
    }

    /**
     * Desactiva un entrenador del sistema por su cédula.
     * El entrenador deja de aparecer en las listas activas pero
     * sus rutinas y registros asignados se conservan.
     *
     * @param cedula Cédula del entrenador a desactivar.
     * @throws IllegalArgumentException si la cédula es nula, vacía o el entrenador no existe.
     */
    public void desactivarEntrenador(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía.");
        if (entrenadorDAO.buscarPorCedula(cedula) == null)
            throw new IllegalArgumentException("No existe un entrenador con la cédula: " + cedula);
        entrenadorDAO.desactivar(cedula);
    }

    public void eliminarEntrenador(String cedula) {
        if (cedula == null || cedula.trim().isEmpty())
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        if (entrenadorDAO.buscarPorCedula(cedula) == null)
            throw new IllegalArgumentException("No existe un entrenador con la cédula: " + cedula);
        entrenadorDAO.eliminar(cedula);
    }
}