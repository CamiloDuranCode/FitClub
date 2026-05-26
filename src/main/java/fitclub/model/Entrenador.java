package fitclub.model;

/**
 * Representa a un entrenador que labora en el gimnasio Fit Club.
 * Extiende {@link Persona} agregando la especialidad deportiva
 * y el horario de trabajo del entrenador.
 */

public class Entrenador extends Persona{

    private String especialidad;
    private String horario;

    /**
     * Constructor de Entrenador.
     *
     * @param cedula       Número de cédula del entrenador.
     * @param nombre       Nombre completo del entrenador.
     * @param telefono     Teléfono de contacto del entrenador.
     * @param especialidad Área de especialidad del entrenador (ej. musculación, cardio).
     * @param horario      Horario de trabajo del entrenador.
     */

    public Entrenador(String cedula, String nombre, String telefono, String especialidad, String horario) {
        super(cedula, nombre, telefono);
        this.especialidad = especialidad;
        this.horario = horario;
    }

    /**
     * Retorna la especialidad del entrenador.
     *
     * @return especialidad como {@link String}.
     */

    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad del entrenador.
     *
     * @param especialidad Nueva especialidad.
     */

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Retorna el horario de trabajo del entrenador.
     *
     * @return horario como {@link String}.
     */

    public String getHorario() {
        return horario;
    }

    /**
     * Establece el horario de trabajo del entrenador.
     *
     * @param horario Nuevo horario.
     */

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
