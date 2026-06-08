package fitclub.model;

import java.time.LocalDateTime;

/**
 * Representa el registro de uso de una máquina por un cliente.
 * Incluye fecha/hora de inicio y fin del uso.
 *
 * @author Juan Camilo Rangel Osias
 */

public class UsoMaquina {

    private int idUso;
    private int idMaquina;
    private String cedulaCliente;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    public UsoMaquina(int idUso, int idMaquina, LocalDateTime fechaHoraInicio,
                      String cedulaCliente, LocalDateTime fechaHoraFin) {
        this.idUso = idUso;
        this.idMaquina = idMaquina;
        this.fechaHoraInicio = fechaHoraInicio;
        this.cedulaCliente = cedulaCliente;
        this.fechaHoraFin = fechaHoraFin;
    }

    public int getIdUso() {
        return idUso;
    }

    public void setIdUso(int idUso) {
        if (idUso <= 0) {
            throw new IllegalArgumentException("El ID de uso debe ser mayor a cero");
        }
        this.idUso = idUso;
    }

    public int getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(int idMaquina) {
        if (idMaquina <= 0) {
            throw new IllegalArgumentException("El ID de máquina debe ser mayor a cero");
        }
        this.idMaquina = idMaquina;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
        }
        this.cedulaCliente = cedulaCliente;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        if (fechaHoraInicio == null) {
            throw new IllegalArgumentException("La fecha/hora de inicio no puede ser nula");
        }
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        if (fechaHoraFin != null && fechaHoraFin.isBefore(fechaHoraInicio)) {
            throw new IllegalArgumentException("La fecha/hora de fin no puede ser anterior al inicio");
        }
        this.fechaHoraFin = fechaHoraFin;
    }

    // Métodos de ayuda
    public boolean estaActivo() {
        return fechaHoraFin == null;
    }

    public long getDuracionMinutos() {
        if (fechaHoraFin == null) {
            return 0;
        }
        return java.time.Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
    }

    @Override
    public String toString() {
        return "Máquina " + idMaquina + " - Cliente " + cedulaCliente +
                " - Inicio: " + fechaHoraInicio;
    }
}
