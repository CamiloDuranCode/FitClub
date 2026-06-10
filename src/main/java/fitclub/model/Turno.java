package fitclub.model;

import fitclub.model.enums.DiaSemana;

import java.time.LocalTime;

/**
 * Representa un turno de trabajo de un entrenador en el gimnasio Fit Club.
 * Un entrenador solo puede tener un turno por día de la semana.
 *
 * @author Juan Camilo Rangel Osias
 */

public class Turno {

    private int idTurno;
    private String cedulaEntrenador;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    /**
     * Constructor completo
     */
    public Turno(int idTurno, String cedulaEntrenador, DiaSemana dia,
                 LocalTime horaInicio, LocalTime horaFin) {
        this.idTurno = idTurno;
        this.cedulaEntrenador = cedulaEntrenador;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Constructor para crear nuevos turnos (sin ID)
     */
    public Turno(String cedulaEntrenador, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
        this.cedulaEntrenador = cedulaEntrenador;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y Setters
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        if (idTurno <= 0) {
            throw new IllegalArgumentException("El ID de turno debe ser mayor a cero");
        }
        this.idTurno = idTurno;
    }

    public String getCedulaEntrenador() {
        return cedulaEntrenador;
    }

    public void setCedulaEntrenador(String cedulaEntrenador) {
        if (cedulaEntrenador == null || cedulaEntrenador.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador no puede estar vacía");
        }
        this.cedulaEntrenador = cedulaEntrenador;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        if (dia == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("La hora de inicio no puede ser nula");
        }
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) {
            throw new IllegalArgumentException("La hora de fin no puede ser nula");
        }
        if (horaInicio != null && horaFin.isBefore(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin no puede ser anterior a la hora de inicio");
        }
        this.horaFin = horaFin;
    }

    // Métodos de ayuda
    public int getDuracionHoras() {
        if (horaInicio == null || horaFin == null) {
            return 0;
        }
        return (int) java.time.Duration.between(horaInicio, horaFin).toHours();
    }

    public int getDuracionMinutos() {
        if (horaInicio == null || horaFin == null) {
            return 0;
        }
        return (int) java.time.Duration.between(horaInicio, horaFin).toMinutes();
    }

    public boolean isRangoValido() {
        return horaInicio != null && horaFin != null && horaFin.isAfter(horaInicio);
    }

    /**
     * Verifica si un horario está dentro del turno
     */
    public boolean contieneHorario(LocalTime hora) {
        if (hora == null || !isRangoValido()) {
            return false;
        }
        return !hora.isBefore(horaInicio) && !hora.isAfter(horaFin);
    }

    @Override
    public String toString() {
        return String.format("%s - %s a %s", dia, horaInicio, horaFin);
    }


}
