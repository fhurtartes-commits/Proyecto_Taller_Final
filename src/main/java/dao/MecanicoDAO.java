package dao;

import config.Conexion;
import modelo.Mecanico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MecanicoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Mecanico> listar() {
        List<Mecanico> lista = new ArrayList<>();
        String sql = "SELECT * FROM mecanicos";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Mecanico mec = new Mecanico();
                mec.setIdMecanico(rs.getInt("id_Mecanico"));
                mec.setNombre(rs.getString("nombre_mecanico"));
                mec.setEspecialidad(rs.getString("especialidad"));
                mec.setTelefono(rs.getString("telefono_mecanico"));
                lista.add(mec);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mecánicos: " + e.getMessage());
        }
        return lista;
    }
}