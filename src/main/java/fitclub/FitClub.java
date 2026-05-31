package fitclub;

import fitclub.view.MainFrame;
import javax.swing.*;
import fitclub.dao.Conexion;
import java.sql.Connection;

public class FitClub {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
        Connection con = Conexion.getInstancia();
        if (con != null) {
            System.out.println("Conexión exitosa a fitclub_db");
            Conexion.cerrarConexion();
        } else {
            System.out.println("Falló la conexión");
        }
    }
}
