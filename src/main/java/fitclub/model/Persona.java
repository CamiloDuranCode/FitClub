package fitclub.model;

/**
 * Clase base abstracta que representa a una persona dentro del sistema Fit Club.
 * Define los atributos y comportamientos comunes a {@link Cliente} y {@link Entrenador}.
 */
public abstract class Persona {

    private String cedula;
    private String nombre;
    private String telefono;

    /**
     * Constructor de Persona.
     *
     * @param cedula   Número de cédula único que identifica a la persona.
     * @param nombre   Nombre completo de la persona.
     * @param telefono Número de teléfono de contacto.
     */
    public Persona(String cedula, String nombre, String telefono) {
        if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía.");
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (telefono == null || telefono.trim().isEmpty()) throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getCedula() { return cedula; }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía.");
        this.cedula = cedula;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.nombre = nombre;
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        this.telefono = telefono;
    }
}