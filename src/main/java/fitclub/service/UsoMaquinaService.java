package fitclub.service;

import fitclub.model.Maquina;
import fitclub.model.UsoMaquina;
import fitclub.model.enums.EstadoMaquina;
import fitclub.dao.IUsoMaquinaDAO;
import java.util.List;

public class UsoMaquinaService {

    private final IUsoMaquinaDAO usoMaquinaDAO;
    private final MaquinaService maquinaService;

    public UsoMaquinaService(IUsoMaquinaDAO usoMaquinaDAO, MaquinaService maquinaService) {
        this.usoMaquinaDAO = usoMaquinaDAO;
        this.maquinaService = maquinaService;
    }

    public void iniciarUso(String cedulaCliente, int idMaquina) {
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty())
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
        if (idMaquina <= 0)
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");

        Maquina maquina = maquinaService.buscarPorId(idMaquina);
        if (maquina == null)
            throw new IllegalArgumentException("La máquina no existe");
        if (!maquina.isActiva())
            throw new IllegalStateException("La máquina no está activa");
        if (maquina.getEstado() != EstadoMaquina.DISPONIBLE)
            throw new IllegalStateException("La máquina no está disponible. Estado actual: " + maquina.getEstado());

        usoMaquinaDAO.iniciarUso(cedulaCliente, idMaquina);
    }

    public void finalizarUso(int idUso) {
        if (idUso <= 0)
            throw new IllegalArgumentException("El ID de uso debe ser mayor a cero");
        usoMaquinaDAO.finalizarUso(idUso);
    }

    public List<UsoMaquina> listarUsosActivos() {
        return usoMaquinaDAO.listarUsosActivos();   // ← nombre correcto
    }

    public List<UsoMaquina> listarPorMaquina(int idMaquina) {
        if (idMaquina <= 0)
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        return usoMaquinaDAO.listarPorMaquina(idMaquina);
    }

    public List<UsoMaquina> listarPorCliente(String cedulaCliente) {
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty())
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
        return usoMaquinaDAO.listarPorCliente(cedulaCliente);
    }

    public boolean maquinaEnUso(int idMaquina) {
        if (idMaquina <= 0)
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        Maquina maquina = maquinaService.buscarPorId(idMaquina);
        return maquina != null && maquina.getEstado() == EstadoMaquina.EN_USO;
    }

    public UsoMaquina obtenerUsoActivoDeMaquina(int idMaquina) {
        if (idMaquina <= 0)
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        return usoMaquinaDAO.listarUsosActivos().stream()   // ← nombre correcto
                .filter(u -> u.getIdMaquina() == idMaquina)
                .findFirst()
                .orElse(null);
    }
}