package fitclub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Asistencia.
 *
 * @author Wilberto Ariza Zapata
 */
public class AsistenciaDAO implements IAsistenciaDAO {

    private Connection con;

    public AsistenciaDAO() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Asistencia asistencia, String clienteCedula) {
        String sql = "INSERT INTO asistencia (cliente_cedula, fecha_hora, observacion) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ps.setTimestamp(2, Timestamp.valueOf(asistencia.getFechaHora()));
            ps.setString(3, asistencia.getObservacion());
            ps.executeUpdate();
            System.out.println("Asistencia registrada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar asistencia: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idAsistencia) {
        String sql = "DELETE FROM asistencia WHERE id_asistencia = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAsistencia);
            ps.executeUpdate();
            System.out.println("Asistencia eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar asistencia: " + e.getMessage());
        }
    }

    @Override
    public Asistencia buscarPorId(int idAsistencia) {
        String sql = "SELECT * FROM asistencia WHERE id_asistencia = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAsistencia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Asistencia(
                        rs.getInt("id_asistencia"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("observacion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar asistencia: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Asistencia> listarPorCliente(String clienteCedula) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE cliente_cedula = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Asistencia(
                        rs.getInt("id_asistencia"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("observacion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar asistencias: " + e.getMessage());
        }
        return lista;
    }
}