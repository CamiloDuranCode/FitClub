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
    private String password;
    private RolUsuario rol;
    private String nombre;
    private boolean activo;
    private String cedulaEntrenador;

    public Usuario(int idUsuario, String username, String password,
                   RolUsuario rol, String nombre, boolean activo,
                   String cedulaEntrenador) {

        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.activo = activo;
        this.cedulaEntrenador = cedulaEntrenador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCedulaEntrenador() {
        return cedulaEntrenador;
    }

    public void setCedulaEntrenador(String cedulaEntrenador) {
        this.cedulaEntrenador = cedulaEntrenador;
    }

    // Métodos de ayuda
    public boolean esAdmin() { return rol == RolUsuario.ADMIN; }
    public boolean esRecepcionista() { return rol == RolUsuario.RECEPCIONISTA; }
    public boolean esEntrenador() { return rol == RolUsuario.ENTRENADOR; }

    @Override
    public String toString() {
        return nombre + " (" + username + " - " + rol + ")";
    }

}
