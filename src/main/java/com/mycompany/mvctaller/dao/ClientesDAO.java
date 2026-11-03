package com.mycompany.mvctaller.dao;
 
import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
 
@Repository
public class ClientesDAO implements CrudDAO<Clientes> {
 
    private static final int MYSQL_ENTRADA_DUPLICADA = 1062;
    private static final int MYSQL_FK_VIOLADA_AL_BORRAR = 1451;
 
    @Override
    public boolean insertar(Clientes objeto) {
        validar(objeto);
        String nit = objeto.getNit().trim();
        objeto.setNit(nit);
 
        if (existeNit(nit, -1)) {
            throw new IllegalStateException("Ya existe un cliente con el NIT " + nit + ".");
        }
 
        String sql = "INSERT INTO clientes (nombre, nit, telefono) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, objeto.getNombre());
            ps.setString(2, nit);
            ps.setString(3, objeto.getTelefono());
 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_ENTRADA_DUPLICADA) {
                throw new IllegalStateException("Ya existe un cliente con el NIT " + nit + ".");
            }
            System.err.println("Error al momento de insertar un cliente: " + e.getMessage());
            return false;
        }
    }
 
    @Override
    public boolean actualizar(Clientes objeto) {
        validar(objeto);
        String nit = objeto.getNit().trim();
        objeto.setNit(nit);
 
        if (existeNit(nit, objeto.getIdCliente())) {
            throw new IllegalStateException("Ya existe otro cliente con el NIT " + nit + ".");
        }
 
        String sql = "UPDATE clientes SET nombre = ?, nit = ?, telefono = ? WHERE id_cliente = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, objeto.getNombre());
            ps.setString(2, nit);
            ps.setString(3, objeto.getTelefono());
            ps.setInt(4, objeto.getIdCliente());
 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_ENTRADA_DUPLICADA) {
                throw new IllegalStateException("Ya existe otro cliente con el NIT " + nit + ".");
            }
            System.err.println("Error al momento de actualizar el cliente: " + e.getMessage());
            return false;
        }
    }
 
    @Override
    public boolean eliminar(int id) {
        if (tieneVehiculos(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el cliente porque tiene vehículos asociados.");
        }
 
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_FK_VIOLADA_AL_BORRAR) {
                throw new IllegalStateException(
                        "No se puede eliminar el cliente porque tiene vehículos asociados.");
            }
            System.err.println("Error al momento de eliminar el cliente: " + e.getMessage());
            return false;
        }
    }
 
    @Override
    public Clientes buscarporID(int id) {
        String sql = "SELECT id_cliente, nombre, nit, telefono FROM clientes WHERE id_cliente = ?";
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
 
    public Clientes buscarPorNit(String nit) {
        if (nit == null) {
            return null;
        }
        String sql = "SELECT id_cliente, nombre, nit, telefono FROM clientes WHERE nit = ?";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, nit.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por NIT: " + e.getMessage());
        }
        return null;
    }
 
    @Override
    public List<Clientes> listarTodos() {
        List<Clientes> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, nit, telefono FROM clientes";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los clientes: " + e.getMessage());
        }
        return clientes;
    }
 
    public boolean existeNit(String nit, int idExcluir) {
        String sql = "SELECT 1 FROM clientes WHERE nit = ? AND id_cliente <> ? LIMIT 1";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, nit);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo verificar si el NIT ya existe.", e);
        }
    }
 
    public boolean tieneVehiculos(int idCliente) {
        String sql = "SELECT 1 FROM vehiculo WHERE id_cliente = ? LIMIT 1";
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo verificar los vehículos del cliente.", e);
        }
    }
 
    private void validar(Clientes c) {
        if (c == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (c.getNit() == null || c.getNit().trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT del cliente es obligatorio.");
        }
    }
 
    private Clientes mapearCliente(ResultSet rs) throws SQLException {
        return new Clientes(
                rs.getInt("id_cliente"),
                rs.getString("nombre"),
                rs.getString("nit"),
                rs.getString("telefono")
        );
    }
}