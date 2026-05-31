package fitclub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria que gestiona la conexión única a la base de datos
 * del sistema Fit Club mediante el patrón Singleton thread-safe.
 */
public class Conexion {

    private static final String URL      = "jdbc:postgresql://localhost:5432/fitclub_db";
    private static final String USUARIO  = "postgres";
    private static final String PASSWORD = "1234";

    private static volatile Connection instancia = null;

    private Conexion() {}

    /**
     * Retorna la instancia única de la conexión a la base de datos.
     * Si no existe o está cerrada, crea una nueva.
     *
     * @return instancia de {@link Connection}.
     */
    public static synchronized Connection getInstancia() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión establecida correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return instancia;
    }

    /**
     * Cierra la conexión activa a la base de datos si existe.
     */
    public static synchronized void cerrarConexion() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }
}