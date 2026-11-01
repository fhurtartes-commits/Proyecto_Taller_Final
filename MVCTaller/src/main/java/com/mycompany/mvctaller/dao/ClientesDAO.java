package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientesDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Clientes> listar() {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Clientes cli = new Clientes(
                    rs.getInt("id_Cliente"),
                    rs.getString("nombre"),
                    rs.getString("nit"),
                    rs.getString("telefono")
                );
                lista.add(cli);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Clientes objeto) {
        if (buscarPorID(objeto.getIdCliente()) != null) {
            System.err.println("Error: Ya existe un cliente registrado con el ID " + objeto.getIdCliente());
            return false;
        }

        if (existeNit(objeto.getNit())) {
            System.err.println("Error: Ya existe un cliente registrado con el NIT " + objeto.getNit());
            return false;
        }

        String sql = "INSERT INTO clientes (id_Cliente, nombre, nit, telefono) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, objeto.getIdCliente());
            ps.setString(2, objeto.getNombre());
            ps.setString(3, objeto.getNit());
            ps.setString(4, objeto.getTelefono());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Clientes objeto) {
        String sql = "UPDATE clientes SET nombre = ?, nit = ?, telefono = ? WHERE id_Cliente = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getNit());
            ps.setString(3, objeto.getTelefono());
            ps.setInt(4, objeto.getIdCliente());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        if (tieneVehiculosAsociados(id)) {
            System.err.println("Error: No se puede eliminar el cliente con ID " + id + " porque tiene vehículos asociados.");
            return false;
        }

        String sql = "DELETE FROM clientes WHERE id_Cliente = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public Clientes buscarPorID(int id) {
        String sql = "SELECT * FROM clientes WHERE id_Cliente = ?";
        Clientes cli = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cli = new Clientes(
                    rs.getInt("id_Cliente"),
                    rs.getString("nombre"),
                    rs.getString("nit"),
                    rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        }
        return cli;
    }

    private boolean existeNit(String nit) {
        String sql = "SELECT id_Cliente FROM clientes WHERE nit = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nit);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar NIT: " + e.getMessage());
            return false;
        }
    }

    private boolean tieneVehiculosAsociados(int idCliente) {
        String sql = "SELECT COUNT(*) FROM vehiculos WHERE id_Cliente = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar vehículos asociados: " + e.getMessage());
        }
        return false;
    }
}