package fitclub.model;

/**
 * Representa a un entrenador que labora en el gimnasio Fit Club.
 * Extiende {@link Persona} agregando la especialidad deportiva.
 * Los turnos semanales se gestionan mediante {@link Turno}.
 */
public class Entrenador extends Persona {

    private String especialidad;

    public Entrenador(String cedula, String nombre, String telefono, String especialidad) {
        super(cedula, nombre, telefono);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }

    public void setEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty())
            throw new IllegalArgumentException("La especialidad no puede estar vacía.");
        this.especialidad = especialidad;
    }
}