package fitclub.model;

import java.time.LocalDateTime;

/**
 * Representa un registro de asistencia de un cliente al gimnasio Fit Club.
 * Almacena la fecha y hora exacta de la visita junto con observaciones opcionales.
 */

public class Asistencia {

    private int idAsistencia;
    private LocalDateTime fechaHora;
    private String observacion;

    /**
     * Constructor de Asistencia.
     *
     * @param idAsistencia Identificador único del registro de asistencia.
     * @param fechaHora    Fecha y hora en que se registró la asistencia.
     * @param observacion  Observación adicional sobre la visita (puede ser vacía).
     */

    public Asistencia(int idAsistencia, LocalDateTime fechaHora, String observacion) {
        this.idAsistencia = idAsistencia;
        this.fechaHora = fechaHora;
        this.observacion = observacion;
    }

    public int getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(int idAsistencia) {

        if(idAsistencia <= 0) throw new IllegalArgumentException("El ID de asistencia debe ser mayor a cero");
        this.idAsistencia = idAsistencia;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora del registro.
     * No se permiten fechas futuras.
     *
     * @param fechaHora Nueva fecha y hora de asistencia.
     */

    public void setFechaHora(LocalDateTime fechaHora) {

        if (fechaHora == null) throw new IllegalArgumentException("La fecha y la hora deben rellenarse");
        if (fechaHora.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("No se puede registrar una asistencia con fecha futura");
        this.fechaHora = fechaHora;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = (observacion == null) ? "" : observacion;
    }
}
