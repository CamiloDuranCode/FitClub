package fitclub;

import fitclub.dao.Conexion;
import java.sql.Connection;

public class FitClub {
    public static void main(String[] args) {
        Connection con = Conexion.getInstancia();
        if (con != null) {
            System.out.println("Conexión exitosa a fitclub_db");
            Conexion.cerrarConexion();
        } else {
            System.out.println("Falló la conexión");
        }
    }
}