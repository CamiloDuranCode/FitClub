package fitclub.model;


import fitclub.model.enums.EstadoMaquina;

/**
 * Representa una máquina del gimnasio Fit Club.
 * Contiene información del equipo, ubicación y estado actual.
 *
 * @author Juan Camilo Rangel Osias
 */

public class Maquina {

    private int idMaquina;
    private String nombre;
    private String tipo;
    private String ubicacion;
    private EstadoMaquina estado;
    private boolean activa;

    public Maquina(int idMaquina, String nombre, String tipo,
                   String ubicacion, EstadoMaquina estado, boolean activa) {
        this.idMaquina = idMaquina;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.activa = activa;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la máquina no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoMaquina getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaquina estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado de la máquina no puede ser nulo");
        }
        this.estado = estado;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    // Métodos de ayuda
    public boolean estaDisponible() {
        return estado == EstadoMaquina.DISPONIBLE && activa;
    }

    public boolean estaEnUso() {
        return estado == EstadoMaquina.EN_USO;
    }

    public boolean estaEnMantenimiento() {
        return estado == EstadoMaquina.MANTENIMIENTO;
    }

    @Override
    public String toString() {
        return nombre + " (" + estado + ")";
    }
}
