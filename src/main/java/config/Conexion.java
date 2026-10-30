package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private static final String HOST = "mysql-82d3d9c-lopezmoratayacesiaabigail-42fc.k.aivencloud.com";
    private static final String PORT = "15424";
    private static final String DATABASE = "bd_taller"; 
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_QjaUKS874W9LJzRlqWf";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=true&serverTimezone=UTC";
    
    private Connection con = null;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos bd_taller en la nube!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver JDBC. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return con;
    }
}