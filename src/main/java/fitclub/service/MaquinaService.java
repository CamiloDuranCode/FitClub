package fitclub.service;


import fitclub.dao.IMaquinaDAO;
import fitclub.model.Maquina;
import fitclub.model.enums.EstadoMaquina;

import java.util.List;

/**
 * Capa de servicios para la gestión de máquinas del gimnasio.
 *
 * @author Juan Camilo Rangel Osias
 */

public class MaquinaService {

    private final IMaquinaDAO maquinaDAO;

    public MaquinaService(IMaquinaDAO maquinaDAO) {
        this.maquinaDAO = maquinaDAO;
    }

    /**
     * Registra una nueva máquina en el sistema
     */
    public void registrarMaquina(Maquina maquina) {
        if (maquina == null) {
            throw new IllegalArgumentException("La máquina no puede ser nula");
        }
        if (maquina.getNombre() == null || maquina.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la máquina es requerido");
        }
        if (maquina.getEstado() == null) {
            maquina.setEstado(EstadoMaquina.DISPONIBLE);
        }
        maquinaDAO.insertar(maquina);
    }

    /**
     * Actualiza los datos de una máquina existente
     */
    public void actualizarMaquina(Maquina maquina) {
        if (maquina == null) {
            throw new IllegalArgumentException("La máquina no puede ser nula");
        }
        if (maquina.getIdMaquina() <= 0) {
            throw new IllegalArgumentException("El ID de la máquina es requerido");
        }
        maquinaDAO.actualizar(maquina);
    }

    /**
     * Desactiva una máquina (soft delete)
     */
    public void desactivarMaquina(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        maquinaDAO.desactivar(idMaquina);
    }

    /**
     * Busca una máquina por su ID
     */
    public Maquina buscarPorId(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        return maquinaDAO.buscarPorId(idMaquina);
    }

    /**
     * Lista todas las máquinas
     */
    public List<Maquina> listarTodas() {
        return maquinaDAO.listarTodas();
    }

    /**
     * Lista solo las máquinas activas
     */
    public List<Maquina> listarActivas() {
        return maquinaDAO.listarActivas();
    }

    /**
     * Lista máquinas por estado (DISPONIBLE, EN_USO, MANTENIMIENTO)
     */
    public List<Maquina> listarPorEstado(EstadoMaquina estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return maquinaDAO.listarTodas().stream()
                .filter(m -> m.getEstado() == estado)
                .filter(Maquina::isActiva)
                .toList();
    }

    /**
     * Lista máquinas disponibles para usar
     */
    public List<Maquina> listarDisponibles() {
        return listarPorEstado(EstadoMaquina.DISPONIBLE);
    }


}
