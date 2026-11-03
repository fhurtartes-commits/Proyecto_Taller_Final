package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Mecanico;
import com.mycompany.mvctaller.modelo.Orden;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MecanicoDAO {

    public boolean insertar(Mecanico objeto) {
        String sql = "INSERT INTO mecanicos (nombre_mecanico, especialidad, telefono_mecanico) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getEspecialidad());
            ps.setString(3, objeto.getTelefono());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar el mecánico: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Mecanico objeto) {
        String sql = "UPDATE mecanicos SET nombre_mecanico = ?, especialidad = ?, telefono_mecanico = ? WHERE id_Mecanico = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getEspecialidad());
            ps.setString(3, objeto.getTelefono());
            ps.setInt(4, objeto.getIdMecanico());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el mecánico: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idMecanico) {
        String sqlCheck = "SELECT COUNT(*) FROM orden WHERE id_Mecanico = ?";
        String sqlDelete = "DELETE FROM mecanicos WHERE id_Mecanico = ?";
        
        try (Connection conn = Conexion.iniciarConexion()) {
            
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idMecanico);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.err.println("No se puede eliminar el mecánico porque tiene órdenes asociadas.");
                        return false;
                    }
                }
            }
            
            try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setInt(1, idMecanico);
                psDel.executeUpdate();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar el mecánico: " + e.getMessage());
        }
        return false;
    }

    public Mecanico buscarporID(int idMecanico) {
        String sql = "SELECT id_Mecanico, nombre_mecanico, especialidad, telefono_mecanico FROM mecanicos WHERE id_Mecanico = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMecanico(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar mecánico: " + e.getMessage());
        }
        return null;
    }

    public List<Mecanico> listarTodos() {
        List<Mecanico> lista = new ArrayList<>();
        String sql = "SELECT id_Mecanico, nombre_mecanico, especialidad, telefono_mecanico FROM mecanicos";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearMecanico(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mecánicos: " + e.getMessage());
        }
        return lista;
    }

    public List<Orden> obtenerOrdenesAsignadas(int idMecanico) {
        List<Orden> listaOrdenes = new ArrayList<>();
        String sql = "SELECT * FROM orden WHERE id_Mecanico = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden o = new Orden();
                    o.setIdOrden(rs.getInt("id_orden"));
                    o.setEstadoOrden(rs.getString("estado_orden"));
                    o.setFechaEntrada(rs.getDate("fecha_entrada"));
                    o.setFechaSalida(rs.getDate("fecha_salida"));
                    o.setCostoTotal(rs.getBigDecimal("costo_total"));
                    o.setIdMecanico(rs.getInt("id_Mecanico"));
                    o.setNoPlaca(rs.getString("No_Placa"));
                    listaOrdenes.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar las órdenes del mecánico: " + e.getMessage());
        }
        return listaOrdenes;
    }

    private Mecanico mapearMecanico(ResultSet rs) throws SQLException {
        return new Mecanico(
                rs.getInt("id_Mecanico"),
                rs.getString("nombre_mecanico"),
                rs.getString("especialidad"),
                rs.getString("telefono_mecanico")
        );
    }
}