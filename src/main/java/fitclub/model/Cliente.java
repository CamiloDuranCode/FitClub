package fitclub.model;

import java.time.LocalDate;

/**
 * Representa a un cliente registrado en el gimnasio Fit Club.
 * Extiende {@link Persona} agregando datos específicos del cliente
 * como fecha de nacimiento y dirección.
 */
public class Cliente extends Persona {

    private LocalDate fechaNacimiento;
    private String direccion;

    /**
     * Constructor de Cliente.
     *
     * @param cedula          Número de cédula del cliente.
     * @param nombre          Nombre completo del cliente.
     * @param telefono        Teléfono de contacto del cliente.
     * @param fechaNacimiento Fecha de nacimiento del cliente.
     * @param direccion       Dirección de residencia del cliente.
     */
    public Cliente(String cedula, String nombre, String telefono, LocalDate fechaNacimiento, String direccion) {
        super(cedula, nombre, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
    }

    /**
     * Retorna la fecha de nacimiento del cliente.
     *
     * @return fechaNacimiento como {@link LocalDate}.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del cliente.
     * No puede ser nula ni futura.
     *
     * @param fechaNacimiento Nueva fecha de nacimiento.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
        if (fechaNacimiento.isAfter(LocalDate.now())) throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Retorna la dirección de residencia del cliente.
     *
     * @return direccion como {@link String}.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de residencia del cliente.
     * No puede ser nula ni vacía.
     *
     * @param direccion Nueva dirección.
     */
    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) throw new IllegalArgumentException("La dirección no puede estar vacía.");
        this.direccion = direccion;
    }
}