package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientesDAO implements CrudDAO<Clientes> {

    @Override
    public boolean insertar(Clientes objeto) {
        String sql = "INSERT INTO clientes (nombre, nit, telefono) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getNit());
            ps.setString(3, objeto.getTelefono());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al momento de insertar un cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Clientes objeto) {
        String sql = "UPDATE clientes SET nombre = ?, nit = ?, telefono = ? WHERE id_cliente = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getNit());
            ps.setString(3, objeto.getTelefono());
            ps.setInt(4, objeto.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al momento de actualizar el cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al momento de eliminar el cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Clientes buscarporID(int id) {
        String sql = "SELECT id_cliente, nit, nombre, telefono FROM clientes WHERE id_cliente = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Clientes> listarTodos() {
        List<Clientes> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nit, nombre, telefono FROM clientes";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // <-- Incluye el ResultSet aquí adentro
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los clientes: " + e.getMessage());
        }
        return clientes;
    }
    
    private Clientes mapearCliente(ResultSet rs) throws SQLException {
        return new Clientes(
                rs.getInt("id_cliente"),
                rs.getString("nit"),
                rs.getString("nombre"),
                rs.getString("telefono")
        );
    }
}