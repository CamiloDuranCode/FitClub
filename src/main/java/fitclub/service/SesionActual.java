package fitclub.service;


import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;

/**
 * Gestiona la sesión actual del usuario autenticado en el sistema.
 * Implementa patrón Singleton para mantener una única instancia en memoria.
 *
 * @author Juan Camilo Rangel Osias
 */

public class SesionActual {

    private static SesionActual instancia;
    private Usuario usuarioActual;

    private SesionActual() {
        // Constructor privado para Singleton
    }

    public static SesionActual getInstancia() {  // ← Tipo de retorno corregido
        if (instancia == null) {
            instancia = new SesionActual();  // ← Constructor corregido
        }
        return instancia;
    }

    /**
     * Inicia sesión con un usuario autenticado
     */
    public void iniciarSesion(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (!usuario.isActivo()) {
            throw new IllegalStateException("El usuario está desactivado");
        }
        this.usuarioActual = usuario;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    /**
     * Verifica si hay una sesión activa
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    public Usuario getUsuarioActual() {
        if (usuarioActual == null) {
            throw new IllegalStateException("No hay sesión activa");
        }
        return usuarioActual;
    }

    /**
     * Obtiene el rol del usuario actual
     */
    public RolUsuario getRolActual() {
        return getUsuarioActual().getRol();
    }

    /**
     * Verifica si el usuario actual tiene un rol específico
     */
    public boolean tieneRol(RolUsuario rol) {
        return haySesionActiva() && getRolActual() == rol;
    }

    /**
     * Verifica si el usuario actual es ADMIN
     */
    public boolean esAdmin() {
        return tieneRol(RolUsuario.ADMIN);
    }

    /**
     * Verifica si el usuario actual es RECEPCIONISTA
     */
    public boolean esRecepcionista() {
        return tieneRol(RolUsuario.RECEPCIONISTA);
    }

    /**
     * Verifica si el usuario actual es ENTRENADOR
     */
    public boolean esEntrenador() {
        return tieneRol(RolUsuario.ENTRENADOR);
    }


}
