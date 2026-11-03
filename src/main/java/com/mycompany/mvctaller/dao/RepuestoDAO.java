package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAO {

    public boolean insertar(Repuesto repuesto) {
        String sql = "INSERT INTO bodega (nombre, descripcion, precio_unitario, existencias) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, repuesto.getNombre());
            ps.setString(2, repuesto.getDescripcion());
            ps.setDouble(3, repuesto.getPrecioUnitario());
            ps.setInt(4, repuesto.getExistencias());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar repuesto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Repuesto repuesto) {
        String sql = "UPDATE bodega SET nombre = ?, descripcion = ?, precio_unitario = ?, existencias = ? WHERE id_Bodega = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, repuesto.getNombre());
            ps.setString(2, repuesto.getDescripcion());
            ps.setDouble(3, repuesto.getPrecioUnitario());
            ps.setInt(4, repuesto.getExistencias());
            ps.setInt(5, repuesto.getIdRepuesto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar repuesto: " + e.getMessage());
            return false;
        }
    }

    public boolean abastecer(int idRepuesto, int cantidad) {
        if (cantidad <= 0) return false; 
        String sql = "UPDATE bodega SET existencias = existencias + ? WHERE id_Bodega = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idRepuesto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al abastecer repuesto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM bodega WHERE id_Bodega = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar repuesto: " + e.getMessage());
            return false;
        }
    }

    public Repuesto buscarPorId(int id) {
        String sql = "SELECT id_Bodega, nombre, descripcion, precio_unitario, existencias FROM bodega WHERE id_Bodega = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Repuesto r = new Repuesto();
                    r.setIdRepuesto(rs.getInt("id_Bodega"));
                    r.setNombre(rs.getString("nombre"));
                    r.setDescripcion(rs.getString("descripcion"));
                    r.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    r.setExistencias(rs.getInt("existencias"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar repuesto: " + e.getMessage());
        }
        return null;
    }

    public List<Repuesto> listarTodos() {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT id_Bodega, nombre, descripcion, precio_unitario, existencias FROM bodega";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Repuesto r = new Repuesto();
                r.setIdRepuesto(rs.getInt("id_Bodega"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setPrecioUnitario(rs.getDouble("precio_unitario"));
                r.setExistencias(rs.getInt("existencias"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos: " + e.getMessage());
        }
        return lista;
    }

    public List<Repuesto> listarAgotados() {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT id_Bodega, nombre, descripcion, precio_unitario, existencias FROM bodega WHERE existencias = 0";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Repuesto r = new Repuesto();
                r.setIdRepuesto(rs.getInt("id_Bodega"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setPrecioUnitario(rs.getDouble("precio_unitario"));
                r.setExistencias(rs.getInt("existencias"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos agotados: " + e.getMessage());
        }
        return lista;
    }

    public List<Repuesto> listarBajoInventario(int limite) {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT id_Bodega, nombre, descripcion, precio_unitario, existencias FROM bodega WHERE existencias > 0 AND existencias <= ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Repuesto r = new Repuesto();
                    r.setIdRepuesto(rs.getInt("id_Bodega"));
                    r.setNombre(rs.getString("nombre"));
                    r.setDescripcion(rs.getString("descripcion"));
                    r.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    r.setExistencias(rs.getInt("existencias"));
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos con bajo inventario: " + e.getMessage());
        }
        return lista;
    }
}