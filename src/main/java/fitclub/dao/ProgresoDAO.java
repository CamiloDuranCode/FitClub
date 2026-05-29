package fitclub.dao;

import fitclub.model.Progreso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Progreso.
 *
 * @author Wilberto Ariza Zapata
 */
public class ProgresoDAO implements IProgresoDAO {

    private Connection con;

    public ProgresoDAO() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Progreso progreso, String clienteCedula, int rutinaId) {
        String sql = "INSERT INTO progreso (cliente_cedula, rutina_id, fecha_registro, peso, talla, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ps.setInt(2, rutinaId);
            ps.setDate(3, Date.valueOf(progreso.getFechaRegistro()));
            ps.setDouble(4, progreso.getPeso());
            ps.setDouble(5, progreso.getTalla());
            ps.setString(6, progreso.getObservaciones());
            ps.executeUpdate();
            System.out.println("Progreso insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar progreso: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Progreso progreso) {
        String sql = "UPDATE progreso SET fecha_registro = ?, peso = ?, talla = ?, observaciones = ? WHERE id_progreso = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(progreso.getFechaRegistro()));
            ps.setDouble(2, progreso.getPeso());
            ps.setDouble(3, progreso.getTalla());
            ps.setString(4, progreso.getObservaciones());
            ps.setInt(5, progreso.getIdProgreso());
            ps.executeUpdate();
            System.out.println("Progreso actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar progreso: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idProgreso) {
        String sql = "DELETE FROM progreso WHERE id_progreso = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idProgreso);
            ps.executeUpdate();
            System.out.println("Progreso eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar progreso: " + e.getMessage());
        }
    }

    @Override
    public Progreso buscarPorId(int idProgreso) {
        String sql = "SELECT * FROM progreso WHERE id_progreso = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idProgreso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Progreso(
                        rs.getInt("id_progreso"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getDouble("peso"),
                        rs.getDouble("talla"),
                        rs.getString("observaciones")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar progreso: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Progreso> listarPorCliente(String clienteCedula) {
        List<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM progreso WHERE cliente_cedula = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Progreso(
                        rs.getInt("id_progreso"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getDouble("peso"),
                        rs.getDouble("talla"),
                        rs.getString("observaciones")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar progresos: " + e.getMessage());
        }
        return lista;
    }
}