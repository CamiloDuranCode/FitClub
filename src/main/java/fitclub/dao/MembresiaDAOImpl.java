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
public class MembresiaDAOImpl implements MembresiaDAO {

    private Connection con;

    public MembresiaDAOImpl() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Membresia membresia, String clienteCedula) {
        String sql = "INSERT INTO membresia (cliente_cedula, tipo, fecha_inicio, fecha_vencimiento) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ps.setString(2, membresia.getTipo());
            ps.setDate(3, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(4, Date.valueOf(membresia.getFechaVencimiento()));
            ps.executeUpdate();
            System.out.println("Membresía insertada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar membresía: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Membresia membresia) {
        String sql = "UPDATE membresia SET tipo = ?, fecha_inicio = ?, fecha_vencimiento = ? WHERE id_membresia = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, membresia.getTipo());
            ps.setDate(2, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(3, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(4, membresia.getIdMembresia());
            ps.executeUpdate();
            System.out.println("Membresía actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar membresía: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idMembresia) {
        String sql = "DELETE FROM membresia WHERE id_membresia = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idMembresia);
            ps.executeUpdate();
            System.out.println("Membresía eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar membresía: " + e.getMessage());
        }
    }

    @Override
    public Membresia buscarPorId(int idMembresia) {
        String sql = "SELECT * FROM membresia WHERE id_membresia = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idMembresia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Membresia(
                        rs.getString("tipo"),
                        rs.getInt("id_membresia"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_vencimiento").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar membresía: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Membresia> listarPorCliente(String clienteCedula) {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresia WHERE cliente_cedula = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Membresia(
                        rs.getString("tipo"),
                        rs.getInt("id_membresia"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_vencimiento").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar membresías: " + e.getMessage());
        }
        return lista;
    }
}