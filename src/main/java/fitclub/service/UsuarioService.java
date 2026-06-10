package fitclub.service;

import fitclub.dao.IUsuarioDAO;
import fitclub.model.Usuario;
import fitclub.model.enums.RolUsuario;

public class UsuarioService {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioService(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Autentica un usuario usando fn_login de la BD.
     * Retorna null si las credenciales son incorrectas.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña en texto plano (mínimo 6 caracteres).
     * @return Usuario autenticado, o {@code null} si las credenciales son incorrectas.
     */
    public Usuario login(String username, String password) {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        if (password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");

        Usuario usuario = usuarioDAO.login(username, password);

        if (usuario == null) return null;

        if (!usuario.isActivo())
            throw new IllegalStateException("El usuario está desactivado. Contacte al administrador.");

        return usuario;
    }

    /**
     * Cierra la sesión actual.
     */
    public void logout() {
        SesionActual.getInstancia().cerrarSesion();
    }

    /**
     * Verifica si hay un usuario autenticado.
     */
    public boolean haySesionActiva() {
        return SesionActual.getInstancia().haySesionActiva();
    }

    /**
     * Obtiene el usuario actual.
     */
    public Usuario getUsuarioActual() {
        return SesionActual.getInstancia().getUsuarioActual();
    }

    /**
     * Obtiene el rol del usuario actual.
     */
    public RolUsuario getRolActual() {
        return SesionActual.getInstancia().getRolActual();
    }

    /**
     * Verifica si el usuario actual es ADMIN.
     */
    public boolean esAdmin() {
        return SesionActual.getInstancia().esAdmin();
    }

    /**
     * Verifica si el usuario actual es RECEPCIONISTA.
     */
    public boolean esRecepcionista() {
        return SesionActual.getInstancia().esRecepcionista();
    }

    /**
     * Verifica si el usuario actual es ENTRENADOR.
     */
    public boolean esEntrenador() {
        return SesionActual.getInstancia().esEntrenador();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que la contraseña tenga mínimo 6 caracteres.
     *
     * @param usuario  Usuario a registrar.
     * @param password Contraseña en texto plano.
     */
    public void registrar(Usuario usuario, String password) {
        if (usuario == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        if (usuario.getRol() == null)
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        if (usuario.getRol() == RolUsuario.ENTRENADOR &&
                (usuario.getCedulaEntrenador() == null || usuario.getCedulaEntrenador().trim().isEmpty()))
            throw new IllegalArgumentException("Un entrenador debe tener una cédula asociada.");

        usuarioDAO.insertar(usuario, password);
    }

    /**
     * Activa o desactiva un usuario (toggle).
     *
     * @param idUsuario ID del usuario a modificar.
     */
    public void toggleActivo(int idUsuario) {
        if (idUsuario <= 0)
            throw new IllegalArgumentException("El ID de usuario debe ser mayor a cero.");
        usuarioDAO.toggleActivo(idUsuario);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param idUsuario ID del usuario a buscar.
     * @return Usuario encontrado o {@code null} si no existe.
     */
    public Usuario buscarPorId(int idUsuario) {
        if (idUsuario <= 0)
            throw new IllegalArgumentException("El ID de usuario debe ser mayor a cero.");
        return usuarioDAO.buscarPorId(idUsuario);
    }

    /**
     * Lista todos los usuarios. Solo accesible para ADMIN.
     *
     * @return Lista de todos los usuarios del sistema.
     * @throws SecurityException si el usuario actual no es ADMIN.
     */
    public java.util.List<Usuario> listarUsuarios() {
        if (!SesionActual.getInstancia().esAdmin())
            throw new SecurityException("Solo los administradores pueden listar usuarios.");
        return usuarioDAO.listarTodos();
    }
}