package fitclub.dao;

import fitclub.model.Membresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Membresia.
 *
 * @author Wilberto Ariza Zapata
 */
public class MembresiaDAO implements IMembresiaDAO {

    @Override
    public void insertar(Membresia membresia, String clienteCedula) {
        String sql = "INSERT INTO membresia (cliente_cedula, tipo, fecha_inicio, fecha_vencimiento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            ps.setObject(2, membresia.getTipo().toLowerCase(), Types.OTHER);
            ps.setDate(3, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(4, Date.valueOf(membresia.getFechaVencimiento()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Membresia membresia) {
        String sql = "UPDATE membresia SET tipo = ?, fecha_inicio = ?, fecha_vencimiento = ? WHERE id_membresia = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setObject(1, membresia.getTipo().toLowerCase(), Types.OTHER);
            ps.setDate(2, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(3, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(4, membresia.getIdMembresia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelar(int idMembresia) {
        String sql = "UPDATE membresia SET activo = false WHERE id_membresia = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMembresia);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cancelar membresía: " + e.getMessage(), e);
        }
    }

    @Override
    public Membresia buscarPorId(int idMembresia) {
        String sql = "SELECT * FROM membresia WHERE id_membresia = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idMembresia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Membresia(
                            rs.getInt("id_membresia"),
                            rs.getString("tipo"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar membresía: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Membresia> listarPorCliente(String clienteCedula) {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresia WHERE cliente_cedula = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Membresia(
                            rs.getInt("id_membresia"),
                            rs.getString("tipo"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar membresías: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Membresia> listarActivasPorCliente(String clienteCedula) {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresia WHERE cliente_cedula = ? AND activo = true";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, clienteCedula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Membresia(
                            rs.getInt("id_membresia"),
                            rs.getString("tipo"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_vencimiento").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar membresías activas: " + e.getMessage(), e);
        }
        return lista;
    }
}