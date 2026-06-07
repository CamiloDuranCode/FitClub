package fitclub.model;

import fitclub.model.enums.TipoAsistencia;

import java.time.LocalDateTime;

/**
 * Representa un registro de asistencia de un cliente al gimnasio Fit Club.
 * Almacena la fecha y hora exacta de la visita junto con observaciones opcionales.
 */

public class Asistencia {

    private int idAsistencia;
    private LocalDateTime fechaHora;
    private TipoAsistencia tipo;
    private String observacion;

    /**
     * Constructor de Asistencia.
     *
     * @param idAsistencia Identificador único del registro de asistencia.
     * @param fechaHora    Fecha y hora en que se registró la asistencia.
     * @param tipo         Tipo de asistencia como enum {@link TipoAsistencia}.
     * @param observacion  Observación adicional sobre la visita (puede ser vacía).
     */

    public Asistencia(int idAsistencia, LocalDateTime fechaHora, TipoAsistencia tipo, String observacion) {
        this.idAsistencia = idAsistencia;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
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

    public TipoAsistencia getTipo() { return tipo; }

    /**
     * Establece el tipo de asistencia.
     *
     * @param tipo Valor del enum {@link TipoAsistencia}. No puede ser nulo.
     */
    public void setTipo(TipoAsistencia tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de asistencia no puede ser nulo.");
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = (observacion == null) ? "" : observacion;
    }
}
