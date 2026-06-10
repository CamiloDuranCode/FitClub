package fitclub.model;

import fitclub.model.enums.RolUsuario;

/**
 * Representa un usuario del sistema Fit Club con acceso autenticado.
 *
 * @author Juan Camilo Rangel Osias
 */
public class Usuario {

    private int idUsuario;
    private String username;
    private String passwordHash;
    private RolUsuario rol;
    private String nombre;
    private boolean activo;
    private String cedulaEntrenador;

    /**
     * Constructor de Usuario.
     *
     * @param idUsuario        Identificador único del usuario.
     * @param username         Nombre de usuario para autenticación. No puede estar vacío.
     * @param passwordHash     Hash SHA-256 de la contraseña. No puede estar vacío.
     * @param rol              Rol del usuario en el sistema.
     * @param nombre           Nombre completo del usuario.
     * @param activo           Estado del usuario en el sistema.
     * @param cedulaEntrenador Cédula del entrenador vinculado (solo para rol ENTRENADOR).
     */
    public Usuario(int idUsuario, String username, String passwordHash,
                   RolUsuario rol, String nombre, boolean activo,
                   String cedulaEntrenador) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.nombre = nombre;
        this.activo = activo;
        this.cedulaEntrenador = cedulaEntrenador;
    }

    public int getIdUsuario() { return idUsuario; }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario <= 0) throw new IllegalArgumentException("El ID de usuario debe ser mayor a cero.");
        this.idUsuario = idUsuario;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        if (username.contains(" "))
            throw new IllegalArgumentException("El nombre de usuario no puede contener espacios.");
        this.username = username;
    }

    public String getPasswordHash() { return passwordHash; }

    private String password;

    public void setPassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener mínimo 6 caracteres.");
        this.password = password;
    }

    public RolUsuario getRol() { return rol; }

    public void setRol(RolUsuario rol) {
        if (rol == null) throw new IllegalArgumentException("El rol no puede ser nulo.");
        this.rol = rol;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.nombre = nombre;
    }

    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) { this.activo = activo; }

    public String getCedulaEntrenador() { return cedulaEntrenador; }

    public void setCedulaEntrenador(String cedulaEntrenador) {
        if (rol == RolUsuario.ENTRENADOR && (cedulaEntrenador == null || cedulaEntrenador.trim().isEmpty()))
            throw new IllegalArgumentException("Un usuario con rol ENTRENADOR debe tener cédula de entrenador.");
        this.cedulaEntrenador = cedulaEntrenador;
    }

    public boolean esAdmin()         { return rol == RolUsuario.ADMIN; }
    public boolean esRecepcionista() { return rol == RolUsuario.RECEPCIONISTA; }
    public boolean esEntrenador()    { return rol == RolUsuario.ENTRENADOR; }

    @Override
    public String toString() {
        return nombre + " (" + username + " - " + rol + ")";
    }
}