package fitclub.service;

import fitclub.dao.IMembresiaDAO;
import fitclub.model.Membresia;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Capa de servicios para la gestión de membresías del gimnasio Fit Club.
 * Contiene la lógica de negocio para registrar, actualizar, calcular el estado
 * y generar alertas de vencimiento de membresías.
 */

public class MembresiaService {

    private static final int DIAS_ALERTA = 5;

    private final IMembresiaDAO membresiaDAO;

    public MembresiaService(IMembresiaDAO membresiaDAO) {
        this.membresiaDAO = membresiaDAO;
    }

    /**
     * Registra una nueva membresía para un cliente.
     *
     * @param membresia     Membresía a registrar.
     * @param clienteCedula Cédula del cliente al que se asigna la membresía.
     * @throws IllegalArgumentException si la membresía o la cédula son nulas.
     */

    public void registrarMembresia(Membresia membresia, String clienteCedula) {
        if (membresia == null) {
            throw new IllegalArgumentException("La membresía no puede ser nula.");
        }
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        membresiaDAO.insertar(membresia, clienteCedula);
    }

    /**
     * Actualiza los datos de una membresía existente.
     *
     * @param membresia Membresía con los datos actualizados.
     * @throws IllegalArgumentException si la membresía es nula o no existe.
     */

    public void actualizarMembresia(Membresia membresia) {
        if (membresia == null) {
            throw new IllegalArgumentException("La membresía no puede ser nula.");
        }
        if (membresiaDAO.buscarPorId(membresia.getIdMembresia()) == null) {
            throw new IllegalArgumentException("No existe una membresía con el ID: " + membresia.getIdMembresia());
        }
        membresiaDAO.actualizar(membresia);
    }

    /**
     * Cancela una membresía del sistema por su ID.
     * La membresía deja de estar activa pero su historial
     * de pagos asociados se conserva.
     *
     * @param idMembresia ID de la membresía a cancelar.
     * @throws IllegalArgumentException si la membresía no existe.
     */
    public void cancelarMembresia(int idMembresia) {
        if (membresiaDAO.buscarPorId(idMembresia) == null) {
            throw new IllegalArgumentException("No existe una membresía con el ID: " + idMembresia);
        }
        membresiaDAO.cancelar(idMembresia);
    }

    /**
     * Retorna los días restantes hasta el vencimiento de una membresía.
     * Retorna 0 si ya está vencida.
     *
     * @param idMembresia ID de la membresía a consultar.
     * @return Días restantes como long. Negativo si está vencida.
     */

    public long diasParaVencimiento(int idMembresia) {
        Membresia membresia = membresiaDAO.buscarPorId(idMembresia);
        if (membresia == null) {
            throw new IllegalArgumentException("No existe una membresía con el ID: " + idMembresia);
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), membresia.getFechaVencimiento());
    }

    /**
     * Retorna la lista de membresías próximas a vencer de un cliente.
     * Se consideran próximas a vencer las que vencen en los próximos 5 días.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Lista de membresías por vencer.
     */

    public List<Membresia> obtenerAlertasVencimiento(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        List<Membresia> membresias = membresiaDAO.listarPorCliente(clienteCedula);
        LocalDate hoy = LocalDate.now();

        return membresias.stream()
                .filter(m -> {
                    long dias = ChronoUnit.DAYS.between(hoy, m.getFechaVencimiento());
                    return dias >= 0 && dias <= DIAS_ALERTA;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las membresías de un cliente.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Lista de membresías del cliente.
     */

    public List<Membresia> listarMembresiasCliente(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return membresiaDAO.listarPorCliente(clienteCedula);
    }

    /**
     * Retorna las membresías activas de un cliente.
     *
     * @param clienteCedula Cédula del cliente.
     * @return Lista de membresías activas.
     */
    public List<Membresia> listarMembresiasActivas(String clienteCedula) {
        if (clienteCedula == null || clienteCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía.");
        }
        return membresiaDAO.listarActivasPorCliente(clienteCedula);
    }
}
