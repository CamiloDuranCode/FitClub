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
     * @param cedula           Número de cédula del cliente.
     * @param nombre           Nombre completo del cliente.
     * @param telefono         Teléfono de contacto del cliente.
     * @param fechaNacimiento  Fecha de nacimiento del cliente.
     * @param direccion        Dirección de residencia del cliente.
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
     *
     * @param fechaNacimiento Nueva fecha de nacimiento.
     */

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
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
     *
     * @param direccion Nueva dirección.
     */

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
