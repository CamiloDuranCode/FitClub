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
     * @param cedula    Número de cédula único que identifica a la persona.
     * @param nombre    Nombre completo de la persona.
     * @param telefono  Número de teléfono de contacto.
     */

    public Persona(String cedula, String nombre, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    /**
     * Retorna el número de cédula de la persona.
     *
     * @return cedula como {@link String}.
     */

    public String getCedula() {
        return cedula;
    }

    /**
     * Establece el número de cédula de la persona.
     *
     * @param cedula Nuevo número de cédula.
     */

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * Retorna el nombre completo de la persona.
     *
     * @return nombre como {@link String}.
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo de la persona.
     *
     * @param nombre Nuevo nombre.
     */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el teléfono de contacto de la persona.
     *
     * @return telefono como {@link String}.
     */

    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto de la persona.
     *
     * @param telefono Nuevo número de teléfono.
     */

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
