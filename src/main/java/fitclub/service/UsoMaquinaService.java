package fitclub.service;

import fitclub.model.Maquina;
import fitclub.model.UsoMaquina;
import fitclub.model.enums.EstadoMaquina;

import java.util.List;

/**
 * Capa de servicios para la gestión de uso de máquinas.
 * Utiliza los stored procedures sp_iniciar_uso_maquina y sp_finalizar_uso_maquina.
 *
 * @author Juan Camilo Rangel Osias
 */

public class UsoMaquinaService {

    private final IUsoMaquinaDAO usoMaquinaDAO;
    private final MaquinaService maquinaService;

    public UsoMaquinaService(IUsoMaquinaDAO usoMaquinaDAO, MaquinaService maquinaService) {
        this.usoMaquinaDAO = usoMaquinaDAO;
        this.maquinaService = maquinaService;
    }

    /**
     * Inicia el uso de una máquina por un cliente
     * Valida que la máquina esté disponible antes de iniciar
     */
    public void iniciarUso(String cedulaCliente, int idMaquina) {
        // Validaciones
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
        }
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }

        // Verificar que la máquina existe y está disponible
        Maquina maquina = maquinaService.buscarPorId(idMaquina);
        if (maquina == null) {
            throw new IllegalArgumentException("La máquina no existe");
        }
        if (!maquina.isActiva()) {
            throw new IllegalStateException("La máquina no está activa");
        }
        if (maquina.getEstado() != EstadoMaquina.DISPONIBLE) {
            throw new IllegalStateException("La máquina no está disponible. Estado actual: " + maquina.getEstado());
        }

        // Registrar el uso (el trigger actualizará el estado de la máquina)
        usoMaquinaDAO.iniciarUso(cedulaCliente, idMaquina);
    }

    /**
     * Finaliza el uso de una máquina
     */
    public void finalizarUso(int idUso) {
        if (idUso <= 0) {
            throw new IllegalArgumentException("El ID de uso debe ser mayor a cero");
        }
        // El trigger actualizará automáticamente la máquina a DISPONIBLE
        usoMaquinaDAO.finalizarUso(idUso);
    }

    /**
     * Lista todos los usos activos (sin fecha_fin)
     */
    public List<UsoMaquina> listarUsosActivos() {
        return usoMaquinaDAO.listarActivos();
    }

    /**
     * Lista el historial de usos de una máquina específica
     */
    public List<UsoMaquina> listarPorMaquina(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        return usoMaquinaDAO.listarPorMaquina(idMaquina);
    }

    /**
     * Lista el historial de usos de un cliente específico
     */
    public List<UsoMaquina> listarPorCliente(String cedulaCliente) {
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
        }
        return usoMaquinaDAO.listarPorCliente(cedulaCliente);
    }

    /**
     * Verifica si una máquina está actualmente en uso
     */
    public boolean maquinaEnUso(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        Maquina maquina = maquinaService.buscarPorId(idMaquina);
        return maquina != null && maquina.getEstado() == EstadoMaquina.EN_USO;
    }

    /**
     * Obtiene el uso activo de una máquina (si lo tiene)
     */
    public UsoMaquina obtenerUsoActivoDeMaquina(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        return usoMaquinaDAO.listarActivos().stream()
                .filter(u -> u.getIdMaquina() == idMaquina)
                .findFirst()
                .orElse(null);
    }
}
