package fitclub.service;

import fitclub.model.Turno;
import fitclub.model.enums.DiaSemana;
import fitclub.dao.ITurnoDAO;

import java.time.LocalTime;
import java.util.List;

/**
 * Capa de servicios para la gestión de turnos de entrenadores.
 *
 * @author Juan Camilo Rangel Osias
 */

public class TurnoService {

    private final ITurnoDAO turnoDAO;

    public TurnoService(ITurnoDAO turnoDAO) {
        this.turnoDAO = turnoDAO;
    }

    /**
     * Asigna un turno a un entrenador
     */
    public void asignarTurno(Turno turno) {
        // Validaciones
        if (turno == null) {
            throw new IllegalArgumentException("El turno no puede ser nulo");
        }
        if (turno.getCedulaEntrenador() == null || turno.getCedulaEntrenador().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador es requerida");
        }
        if (turno.getDia() == null) {
            throw new IllegalArgumentException("El día de la semana es requerido");
        }
        if (turno.getHoraInicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es requerida");
        }
        if (turno.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de fin es requerida");
        }
        if (!turno.isRangoValido()) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        turnoDAO.insertar(turno);
    }

    /**
     * Actualiza un turno existente
     */
    public void actualizarTurno(Turno turno) {
        if (turno == null) {
            throw new IllegalArgumentException("El turno no puede ser nulo");
        }
        if (turno.getIdTurno() <= 0) {
            throw new IllegalArgumentException("El ID del turno es requerido");
        }
        if (turnoDAO.buscarPorId(turno.getIdTurno()) == null) {
            throw new IllegalArgumentException("No existe un turno con el ID: " + turno.getIdTurno());
        }
        if (!turno.isRangoValido()) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        turnoDAO.actualizar(turno);
    }

    /**
     * Elimina un turno por su ID
     */
    public void eliminarTurno(int idTurno) {
        if (idTurno <= 0) {
            throw new IllegalArgumentException("El ID de turno debe ser mayor a cero");
        }
        if (turnoDAO.buscarPorId(idTurno) == null) {
            throw new IllegalArgumentException("No existe un turno con el ID: " + idTurno);
        }
        turnoDAO.eliminar(idTurno);
    }

    /**
     * Busca un turno por su ID
     */
    public Turno buscarPorId(int idTurno) {
        if (idTurno <= 0) {
            throw new IllegalArgumentException("El ID de turno debe ser mayor a cero");
        }
        return turnoDAO.buscarPorId(idTurno);
    }

    /**
     * Lista todos los turnos de un entrenador
     */
    public List<Turno> listarPorEntrenador(String cedulaEntrenador) {
        if (cedulaEntrenador == null || cedulaEntrenador.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador es requerida");
        }
        return turnoDAO.listarPorEntrenador(cedulaEntrenador);
    }

    /**
     * Lista todos los turnos de todos los entrenadores
     */
    public List<Turno> listarTodos() {
        return turnoDAO.listarTodos();
    }

    /**
     * Lista turnos de un entrenador para un día específico
     */
    public List<Turno> listarPorEntrenadorYDia(String cedulaEntrenador, DiaSemana dia) {
        if (cedulaEntrenador == null || cedulaEntrenador.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del entrenador es requerida");
        }
        if (dia == null) {
            throw new IllegalArgumentException("El día es requerido");
        }

        return listarPorEntrenador(cedulaEntrenador).stream()
                .filter(t -> t.getDia() == dia)
                .toList();
    }

    /**
     * Verifica si un entrenador tiene turno asignado en un día específico
     */
    public boolean tieneTurno(String cedulaEntrenador, DiaSemana dia) {
        return !listarPorEntrenadorYDia(cedulaEntrenador, dia).isEmpty();
    }

    /**
     * Obtiene el horario completo de un entrenador como String
     */
    public String getHorarioString(String cedulaEntrenador) {
        List<Turno> turnos = listarPorEntrenador(cedulaEntrenador);
        if (turnos.isEmpty()) {
            return "Sin horario asignado";
        }

        StringBuilder sb = new StringBuilder();
        for (Turno t : turnos) {
            sb.append(t.getDia())
                    .append(": ")
                    .append(t.getHoraInicio())
                    .append(" - ")
                    .append(t.getHoraFin())
                    .append("\n");
        }
        return sb.toString();
    }
    
}
