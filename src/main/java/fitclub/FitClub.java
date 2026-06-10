package fitclub;

import fitclub.dao.Conexion;
import fitclub.view.LoginForm;

import javax.swing.*;
import java.sql.Connection;

/**
 * Punto de entrada principal del Sistema de Gestión Fit Club.
 * Verifica la conexión a la base de datos antes de iniciar la interfaz gráfica,
 * muestra el formulario de autenticación y registra un hook de cierre
 * para liberar recursos al terminar la aplicación.
 *
 * @author Equipo Fit Club
 */
public class FitClub {

    public static void main(String[] args) {
        if (!verificarConexion()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos.\nVerifica que PostgreSQL esté activo y las credenciales sean correctas.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        registrarCierreBD();
        SwingUtilities.invokeLater(LoginForm::new);
    }

    /**
     * Verifica que la conexión a la base de datos esté disponible.
     *
     * @return {@code true} si la conexión fue exitosa, {@code false} en caso contrario.
     */
    private static boolean verificarConexion() {
        Connection con = Conexion.getInstancia();
        if (con != null) {
            System.out.println("Conexión exitosa a fitclub_db.");
            return true;
        }
        System.err.println("Error: no se pudo establecer conexión con fitclub_db.");
        return false;
    }

    /**
     * Registra un shutdown hook para cerrar la conexión a la BD
     * cuando la aplicación se cierre, sin importar cómo termine.
     */
    private static void registrarCierreBD() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Conexion.cerrarConexion();
            System.out.println("Recursos liberados. Aplicación cerrada.");
        }));
    }
}