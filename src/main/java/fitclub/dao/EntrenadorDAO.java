package fitclub.dao;

import fitclub.model.Entrenador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para operaciones CRUD de Entrenador.
 *
 * @author Wilberto Ariza Zapata
 */
public class EntrenadorDAO implements IEntrenadorDAO {

    @Override
    public void insertar(Entrenador entrenador) {
        String sqlPersona = "INSERT INTO persona (cedula, nombre, telefono) VALUES (?, ?, ?)";
        String sqlEntrenador = "INSERT INTO entrenador (cedula, especialidad, horario) VALUES (?, ?, ?)";
        Connection con = Conexion.getInstancia();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona);
                 PreparedStatement psEntrenador = con.prepareStatement(sqlEntrenador)) {

                psPersona.setString(1, entrenador.getCedula());
                psPersona.setString(2, entrenador.getNombre());
                psPersona.setString(3, entrenador.getTelefono());
                psPersona.executeUpdate();

                psEntrenador.setString(1, entrenador.getCedula());
                psEntrenador.setString(2, entrenador.getEspecialidad());
                psEntrenador.setString(3, entrenador.getHorario());
                psEntrenador.executeUpdate();

                con.commit();
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Error al insertar entrenador: " + e.getMessage(), e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void actualizar(Entrenador entrenador) {
        String sqlPersona = "UPDATE persona SET nombre = ?, telefono = ? WHERE cedula = ?";
        String sqlEntrenador = "UPDATE entrenador SET especialidad = ?, horario = ? WHERE cedula = ?";
        Connection con = Conexion.getInstancia();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona);
                 PreparedStatement psEntrenador = con.prepareStatement(sqlEntrenador)) {

                psPersona.setString(1, entrenador.getNombre());
                psPersona.setString(2, entrenador.getTelefono());
                psPersona.setString(3, entrenador.getCedula());
                psPersona.executeUpdate();

                psEntrenador.setString(1, entrenador.getEspecialidad());
                psEntrenador.setString(2, entrenador.getHorario());
                psEntrenador.setString(3, entrenador.getCedula());
                psEntrenador.executeUpdate();

                con.commit();
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Error al actualizar entrenador: " + e.getMessage(), e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void eliminar(String cedula) {
        String sqlEntrenador = "DELETE FROM entrenador WHERE cedula = ?";
        String sqlPersona = "DELETE FROM persona WHERE cedula = ?";
        Connection con = Conexion.getInstancia();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psEntrenador = con.prepareStatement(sqlEntrenador);
                 PreparedStatement psPersona = con.prepareStatement(sqlPersona)) {

                psEntrenador.setString(1, cedula);
                psEntrenador.executeUpdate();

                psPersona.setString(1, cedula);
                psPersona.executeUpdate();

                con.commit();
            }
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Error al eliminar entrenador: " + e.getMessage(), e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public Entrenador buscarPorCedula(String cedula) {
        String sql = "SELECT p.cedula, p.nombre, p.telefono, e.especialidad, e.horario " +
                "FROM persona p JOIN entrenador e ON p.cedula = e.cedula " +
                "WHERE p.cedula = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Entrenador(
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("especialidad"),
                            rs.getString("horario")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar entrenador: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Entrenador> listarTodos() {
        List<Entrenador> lista = new ArrayList<>();
        String sql = "SELECT p.cedula, p.nombre, p.telefono, e.especialidad, e.horario " +
                "FROM persona p JOIN entrenador e ON p.cedula = e.cedula";
        try (Statement st = Conexion.getInstancia().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Entrenador(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("especialidad"),
                        rs.getString("horario")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar entrenadores: " + e.getMessage(), e);
        }
        return lista;
    }
}