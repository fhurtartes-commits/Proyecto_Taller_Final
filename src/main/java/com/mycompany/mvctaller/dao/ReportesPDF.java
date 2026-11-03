package com.mycompany.mvctaller.dao;

import com.mycompany.mvctaller.config.Conexion;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportesPDF {

    private final Conexion conexionBD = new Conexion();

    private static final List<String> ESTADOS_ORDEN =
            Arrays.asList("PENDIENTE", "EN PROCESO", "FINALIZADA", "CANCELADA");

    private String normalizarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) return "SIN ESTADO";
        return estado.trim().toUpperCase().replace('_', ' ');
    }

    private static final String SELECT_ORDEN =
            "SELECT id_orden, estado_orden, fecha_entrada, fecha_salida, " +
            "id_Mecanico, No_Placa, costo_total FROM orden";


    private boolean estadoVacio(String estado) {
        return estado == null || estado.trim().isEmpty();
    }

    private Map<String, Object> mapearOrden(ResultSet rs) throws SQLException {
        Map<String, Object> fila = new LinkedHashMap<>();
        fila.put("id_orden", rs.getInt("id_orden"));
        fila.put("estado_orden", rs.getString("estado_orden"));
        fila.put("fecha_entrada", rs.getDate("fecha_entrada"));
        fila.put("fecha_salida", rs.getDate("fecha_salida"));
        fila.put("id_Mecanico", rs.getInt("id_Mecanico"));
        fila.put("No_Placa", rs.getString("No_Placa"));
        fila.put("costo_total", rs.getBigDecimal("costo_total"));
        return fila;
    }


    public List<String> obtenerEstadosDisponibles() {
        Set<String> estados = new LinkedHashSet<>(ESTADOS_ORDEN);

        String sql = "SELECT DISTINCT estado_orden FROM orden WHERE estado_orden IS NOT NULL";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                estados.add(normalizarEstado(rs.getString("estado_orden")));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar estados disponibles: " + e.getMessage());
        }

        return new ArrayList<>(estados);
    }

    public List<Map<String, Object>> obtenerOrdenesPorEstado(String estado) {

        List<Map<String, Object>> lista = new ArrayList<>();
        if (estadoVacio(estado)) {
            return lista;
        }

        String sql = SELECT_ORDEN +
                " WHERE REPLACE(UPPER(TRIM(estado_orden)), '_', ' ') = ? ORDER BY id_orden";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, normalizarEstado(estado));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes por estado: " + e.getMessage());
        }

        return lista;
    }

    public Map<String, List<Map<String, Object>>> obtenerSeccionPorEstado(String estado) {
        Map<String, List<Map<String, Object>>> grupos = new LinkedHashMap<>();
        if (!estadoVacio(estado)) {
            grupos.put(normalizarEstado(estado), obtenerOrdenesPorEstado(estado));
        }
        return grupos;
    }

    public Map<String, List<Map<String, Object>>> obtenerOrdenesAgrupadasPorEstado() {

        Map<String, List<Map<String, Object>>> grupos = new LinkedHashMap<>();
        for (String e : ESTADOS_ORDEN) {
            grupos.put(e, new ArrayList<>());
        }

        String sql = SELECT_ORDEN + " ORDER BY id_orden";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = mapearOrden(rs);
                String clave = normalizarEstado((String) fila.get("estado_orden"));
                grupos.computeIfAbsent(clave, k -> new ArrayList<>()).add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes agrupadas por estado: " + e.getMessage());
        }

        return grupos;
    }

    public List<Map<String, Object>> obtenerHistorialPorPlaca(String noPlaca) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = SELECT_ORDEN +
                " WHERE UPPER(TRIM(No_Placa)) = UPPER(TRIM(?)) ORDER BY fecha_entrada DESC";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, noPlaca);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar historial por placa: " + e.getMessage());
        }

        return lista;
    }

    public List<Map<String, Object>> obtenerRepuestosStockBajo(int limite) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = "SELECT id_Bodega, existencias, precio_unitario " +
                     "FROM bodega WHERE existencias <= ?";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("id_Bodega", rs.getInt("id_Bodega"));
                    fila.put("existencias", rs.getInt("existencias"));
                    fila.put("precio_unitario", rs.getBigDecimal("precio_unitario"));
                    lista.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar repuestos con bajo stock: " + e.getMessage());
        }

        return lista;
    }

    public List<Map<String, Object>> obtenerOrdenesPorMecanicoYEstado(int idMecanico, String estado) {

        List<Map<String, Object>> lista = new ArrayList<>();
        if (estadoVacio(estado)) {
            return lista;
        }

        boolean todos = "TODOS".equalsIgnoreCase(estado.trim());

        String sql = SELECT_ORDEN + " WHERE id_Mecanico = ?" +
                (todos ? "" : " AND REPLACE(UPPER(TRIM(estado_orden)), '_', ' ') = ?") +
                " ORDER BY id_orden";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMecanico);
            if (!todos) {
                ps.setString(2, normalizarEstado(estado));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar órdenes por mecánico y estado: " + e.getMessage());
        }

        return lista;
    }

    public List<Map<String, Object>> obtenerIngresosPorFechas(Date fechaInicio, Date fechaFin) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = "SELECT Fecha, costoTotal, id_Orden " +
                     "FROM factura WHERE Fecha BETWEEN ? AND ? ORDER BY Fecha";

        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("Fecha", rs.getDate("Fecha"));
                    fila.put("costoTotal", rs.getBigDecimal("costoTotal"));
                    fila.put("id_Orden", rs.getInt("id_Orden"));
                    lista.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar ingresos por fechas: " + e.getMessage());
        }

        return lista;
    }
}