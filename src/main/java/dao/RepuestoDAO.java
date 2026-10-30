package dao;

import config.Conexion;
import modelo.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Repuesto> listar() {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM bodega";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Repuesto rep = new Repuesto();
                rep.setIdRepuesto(rs.getInt("id_Bodega"));
                rep.setDescripcion(rs.getString("descripcion"));
                rep.setPrecioUnitario(rs.getDouble("precio_unitario"));
                rep.setExistencias(rs.getInt("existencias"));
                lista.add(rep);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos: " + e.getMessage());
        }
        return lista;
    }
}