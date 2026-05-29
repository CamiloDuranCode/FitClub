package fitclub.dao;

import fitclub.model.Rutina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Rutina.
 *
 * @author Wilberto Ariza Zapata
 */
public class RutinaDAO implements IRutinaDAO {

    private Connection con;

    public RutinaDAO() {
        this.con = Conexion.getInstancia();
    }

    @Override
    public void insertar(Rutina rutina, String clienteCedula, String entrenadorCedula) {
        String sql = "INSERT INTO rutina (cliente_cedula, entrenador_cedula, descripcion, fecha_asignacion) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ps.setString(2, entrenadorCedula);
            ps.setString(3, rutina.getDescripcion());
            ps.setDate(4, Date.valueOf(rutina.getFechaAsignacion()));
            ps.executeUpdate();
            System.out.println("Rutina insertada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar rutina: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Rutina rutina) {
        String sql = "UPDATE rutina SET descripcion = ?, fecha_asignacion = ? WHERE id_rutina = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, rutina.getDescripcion());
            ps.setDate(2, Date.valueOf(rutina.getFechaAsignacion()));
            ps.setInt(3, rutina.getIdRutina());
            ps.executeUpdate();
            System.out.println("Rutina actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar rutina: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idRutina) {
        String sql = "DELETE FROM rutina WHERE id_rutina = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRutina);
            ps.executeUpdate();
            System.out.println("Rutina eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar rutina: " + e.getMessage());
        }
    }

    @Override
    public Rutina buscarPorId(int idRutina) {
        String sql = "SELECT * FROM rutina WHERE id_rutina = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRutina);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Rutina(
                        rs.getInt("id_rutina"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_asignacion").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar rutina: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Rutina> listarPorCliente(String clienteCedula) {
        List<Rutina> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutina WHERE cliente_cedula = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, clienteCedula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Rutina(
                        rs.getInt("id_rutina"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_asignacion").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar rutinas: " + e.getMessage());
        }
        return lista;
    }
}