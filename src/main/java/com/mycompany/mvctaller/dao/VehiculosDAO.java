package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Vehiculos;
import com.mycompany.mvctaller.modelo.Orden;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculosDAO {

    public boolean insertar(Vehiculos objeto) {
        String sql = "INSERT INTO vehiculo (No_Placa, modelo, marca, color, id_Cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNoPlaca());
            ps.setString(2, objeto.getModelo());
            ps.setString(3, objeto.getMarca());
            ps.setString(4, objeto.getColor());
            ps.setInt(5, objeto.getIdCliente());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar el vehículo (posible placa duplicada o cliente inexistente): " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Vehiculos objeto) {
        String sql = "UPDATE vehiculo SET modelo = ?, marca = ?, color = ?, id_Cliente = ? WHERE No_Placa = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getModelo());
            ps.setString(2, objeto.getMarca());
            ps.setString(3, objeto.getColor());
            ps.setInt(4, objeto.getIdCliente());
            ps.setString(5, objeto.getNoPlaca());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String noPlaca) {
        String sqlCheck = "SELECT COUNT(*) FROM orden WHERE No_Placa = ?";
        String sqlDelete = "DELETE FROM vehiculo WHERE No_Placa = ?";
        
        try (Connection conn = Conexion.iniciarConexion()) {
            
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setString(1, noPlaca);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.err.println("No se puede eliminar el vehículo porque tiene órdenes registradas.");
                        return false;
                    }
                }
            }
            
            try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setString(1, noPlaca);
                psDel.executeUpdate();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar el vehículo: " + e.getMessage());
        }
        return false;
    }

    public Vehiculos buscarporID(String noPlaca) {
        String sql = "SELECT No_Placa, modelo, marca, color, id_Cliente FROM vehiculo WHERE No_Placa = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, noPlaca);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVehiculo(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vehículo: " + e.getMessage());
        }
        return null;
    }

    public List<Vehiculos> listarTodos() {
        List<Vehiculos> lista = new ArrayList<>();
        String sql = "SELECT No_Placa, modelo, marca, color, id_Cliente FROM vehiculo";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar vehículos: " + e.getMessage());
        }
        return lista;
    }

    public List<Orden> obtenerHistorialOrdenes(String noPlaca) {
        List<Orden> listaOrdenes = new ArrayList<>();
        String sql = "SELECT * FROM orden WHERE No_Placa = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, noPlaca);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden o = new Orden();
                    o.setIdOrden(rs.getInt("id_orden"));
                    o.setEstadoOrden(rs.getString("estado_orden"));
                    o.setFechaEntrada(rs.getDate("fecha_entrada"));
                    o.setCostoTotal(rs.getBigDecimal("costo_total"));
                    o.setIdMecanico(rs.getInt("id_Mecanico"));
                    o.setNoPlaca(rs.getString("No_Placa"));
                    listaOrdenes.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar historial: " + e.getMessage());
        }
        return listaOrdenes;
    }

    private Vehiculos mapearVehiculo(ResultSet rs) throws SQLException {
        return new Vehiculos(
                rs.getString("No_Placa"),
                rs.getString("modelo"),
                rs.getString("marca"),
                rs.getString("color"),
                rs.getInt("id_Cliente")
        );
    }
}