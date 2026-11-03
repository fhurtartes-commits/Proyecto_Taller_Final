package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import com.mycompany.mvctaller.modelo.Servicios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiciosDAO {

    public List<Servicios> listarTodos() {
        List<Servicios> lista = new ArrayList<>();
        String sql = "SELECT id_Servicios, tipo_Servicio, valor_Servicio FROM servicios";
        
        try (Connection conn = Conexion.iniciarConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Servicios s = new Servicios(
                    rs.getInt("id_Servicios"),
                    rs.getString("tipo_Servicio"),
                    rs.getBigDecimal("valor_Servicio")
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los servicios: " + e.getMessage());
        }
        return lista;
    }
}