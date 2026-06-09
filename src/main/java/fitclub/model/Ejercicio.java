package fitclub.model;

/**
 * Representa un ejercicio dentro de una rutina de entrenamiento.
 * Puede estar asociado opcionalmente a una máquina.
 *
 * @author Juan Camilo Rangel Osias
 */

public class Ejercicio {

    private int idEjercicio;
    private int idRutina;
    private String nombre;
    private int series;
    private int repeticiones;
    private String descripcion;
    private Integer idMaquina;  // Opcional, puede ser null

    /**
     * Constructor completo
     */
    public Ejercicio(int idEjercicio, int idRutina, String nombre,
                     int series, int repeticiones, String descripcion, Integer idMaquina) {
        this.idEjercicio = idEjercicio;
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.series = series;
        this.repeticiones = repeticiones;
        this.descripcion = descripcion;
        this.idMaquina = idMaquina;
    }

    /**
     * Constructor para crear nuevos ejercicios (sin ID)
     */
    public Ejercicio(int idRutina, String nombre, int series, int repeticiones,
                     String descripcion, Integer idMaquina) {
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.series = series;
        this.repeticiones = repeticiones;
        this.descripcion = descripcion;
        this.idMaquina = idMaquina;
    }

    /**
     * Constructor sin máquina
     */
    public Ejercicio(int idRutina, String nombre, int series, int repeticiones, String descripcion) {
        this(idRutina, nombre, series, repeticiones, descripcion, null);
    }

    // Getters y Setters
    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        if (idEjercicio <= 0) {
            throw new IllegalArgumentException("El ID de ejercicio debe ser mayor a cero");
        }
        this.idEjercicio = idEjercicio;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        if (idRutina <= 0) {
            throw new IllegalArgumentException("El ID de rutina debe ser mayor a cero");
        }
        this.idRutina = idRutina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        if (series <= 0) {
            throw new IllegalArgumentException("Las series deben ser mayores a cero");
        }
        this.series = series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        if (repeticiones <= 0) {
            throw new IllegalArgumentException("Las repeticiones deben ser mayores a cero");
        }
        this.repeticiones = repeticiones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    // Métodos de ayuda
    public boolean tieneMaquina() {
        return idMaquina != null && idMaquina > 0;
    }

    @Override
    public String toString() {
        return nombre + " - " + series + "x" + repeticiones;
    }
}
