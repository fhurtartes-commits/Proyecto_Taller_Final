package dao;

import config.Conexion;
import modelo.Orden;

import java.math.BigDecimal;
import java.sql.*;

public class OrdenDAO {

    private final Conexion conexionBD;

    public OrdenDAO() {
        this.conexionBD = new Conexion();
    }

    // Comprueba inmutabilidad: las órdenes FINALIZADAS o CANCELADAS no se pueden modificar[cite: 3].
    public boolean esInmutable(int idOrden, Connection con) throws SQLException {
        String sql = "SELECT estado_orden FROM orden WHERE id_orden = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("estado_orden");
                if ("FINALIZADA".equalsIgnoreCase(estado) || "CANCELADA".equalsIgnoreCase(estado)) {
                    System.err.println("Operación rechazada: La orden #" + idOrden + " está " + estado + " y es inmutable.");
                    return true;
                }
            } else {
                System.err.println("La orden #" + idOrden + " no existe.");
                return true;
            }
        }
        return false;
    }

    // 1. Crear Orden (Estado inicial: PENDIENTE)[cite: 3]
    public boolean crearOrden(String noPlaca, int idMecanico) {
        String sql = "INSERT INTO orden (estado_orden, fecha_entrada, id_Mecanico, No_Placa) VALUES ('PENDIENTE', CURRENT_DATE(), ?, ?)";
        try (Connection con = conexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idMecanico);
            ps.setString(2, noPlaca);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        System.out.println("Orden #" + idGenerado + " creada exitosamente en estado PENDIENTE.");
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la orden: " + e.getMessage());
        }
        return false;
    }

    // 2. Iniciar Reparación (Pasa a EN PROCESO)[cite: 3]
    public boolean iniciarReparacion(int idOrden) {
        String sql = "UPDATE orden SET estado_orden = 'EN PROCESO' WHERE id_orden = ? AND estado_orden = 'PENDIENTE'";
        try (Connection con = conexionBD.getConnection()) {
            if (esInmutable(idOrden, con)) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idOrden);
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    System.out.println("La orden #" + idOrden + " cambió a estado EN PROCESO.");
                    return true;
                } else {
                    System.err.println("La orden no está en estado PENDIENTE.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar reparación: " + e.getMessage());
        }
        return false;
    }

    // 3. Registrar Servicio a Orden[cite: 3]
    public boolean registrarServicio(int idOrden, int idServicio) {
        String sqlValor = "SELECT valor_Servicio FROM servicios WHERE id_Servicios = ?";
        String sqlInsert = "INSERT INTO orden_servicios (servicio_estado, id_Servicios, id_Orden, servicio_valor) VALUES ('ASIGNADO', ?, ?, ?)";

        try (Connection con = conexionBD.getConnection()) {
            if (esInmutable(idOrden, con)) return false;

            BigDecimal valorServicio = BigDecimal.ZERO;
            try (PreparedStatement psV = con.prepareStatement(sqlValor)) {
                psV.setInt(1, idServicio);
                ResultSet rs = psV.executeQuery();
                if (rs.next()) {
                    valorServicio = rs.getBigDecimal("valor_Servicio");
                } else {
                    System.err.println("El servicio especificado no existe.");
                    return false;
                }
            }

            try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                psIns.setInt(1, idServicio);
                psIns.setInt(2, idOrden);
                psIns.setBigDecimal(3, valorServicio);
                psIns.executeUpdate();
                System.out.println("Servicio asignado correctamente a la orden #" + idOrden + ".");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar servicio: " + e.getMessage());
        }
        return false;
    }

    //Asignar repuesto con validación de stock
    public boolean asignarRepuesto(int idOrden, int idBodega, int cantidad) {
        String sqlStock = "SELECT existencias FROM bodega WHERE id_Bodega = ?";
        String sqlInsertRepuesto = "INSERT INTO orden_repuestos (repuesto_estado, repuesto_cantidad, id_Bodega, id_Orden) VALUES ('ASIGNADO', ?, ?, ?)";
        String sqlDescontarStock = "UPDATE bodega SET existencias = existencias - ? WHERE id_Bodega = ?";

        Connection con = null;
        try {
            con = conexionBD.getConnection();
            if (esInmutable(idOrden, con)) return false;

            con.setAutoCommit(false);

            //Validar stock disponible
            int existenciasActuales = 0;
            try (PreparedStatement psSt = con.prepareStatement(sqlStock)) {
                psSt.setInt(1, idBodega);
                ResultSet rs = psSt.executeQuery();
                if (rs.next()) {
                    existenciasActuales = rs.getInt("existencias");
                } else {
                    throw new SQLException("El repuesto/bodega #" + idBodega + " no existe.");
                }
            }

            if (existenciasActuales < cantidad) {
                throw new SQLException("Stock insuficiente en bodega. Disponible: " + existenciasActuales + ", Requerido: " + cantidad);
            }

            // Insertar detalle repuesto
            try (PreparedStatement psIns = con.prepareStatement(sqlInsertRepuesto)) {
                psIns.setInt(1, cantidad);
                psIns.setInt(2, idBodega);
                psIns.setInt(3, idOrden);
                psIns.executeUpdate();
            }

            // Descontar del inventario
            try (PreparedStatement psUpd = con.prepareStatement(sqlDescontarStock)) {
                psUpd.setInt(1, cantidad);
                psUpd.setInt(2, idBodega);
                psUpd.executeUpdate();
            }

            con.commit(); // CONFIRMAR TRANSACCIÓN[cite: 3]
            System.out.println("Repuesto asignado y stock descontado exitosamente.");
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // ROLLBACK EN CASO DE FALLO[cite: 3]
            }
            System.err.println("Transacción fallida (Rollback ejecutado): " + e.getMessage());
        } finally {
            restaurarConexion(con);
        }
        return false;
    }

    //Retirar repuesto de orden en Proceso
    public boolean retirarRepuesto(int idDetalleRepuesto) {
        String sqlSelect = "SELECT id_Bodega, repuesto_cantidad, id_Orden FROM orden_repuestos WHERE id_DetalleRepuesto = ?";
        String sqlDelete = "DELETE FROM orden_repuestos WHERE id_DetalleRepuesto = ?";
        String sqlRestaurarStock = "UPDATE bodega SET existencias = existencias + ? WHERE id_Bodega = ?";

        Connection con = null;
        try {
            con = conexionBD.getConnection();

            int idBodega = 0, cantidad = 0, idOrden = 0;
            try (PreparedStatement psSel = con.prepareStatement(sqlSelect)) {
                psSel.setInt(1, idDetalleRepuesto);
                ResultSet rs = psSel.executeQuery();
                if (rs.next()) {
                    idBodega = rs.getInt("id_Bodega");
                    cantidad = rs.getInt("repuesto_cantidad");
                    idOrden = rs.getInt("id_Orden");
                } else {
                    System.err.println("El detalle de repuesto #" + idDetalleRepuesto + " no fue encontrado.");
                    return false;
                }
            }

            if (esInmutable(idOrden, con)) return false;

            con.setAutoCommit(false);

            try (PreparedStatement psDel = con.prepareStatement(sqlDelete)) {
                psDel.setInt(1, idDetalleRepuesto);
                psDel.executeUpdate();
            }

            try (PreparedStatement psRes = con.prepareStatement(sqlRestaurarStock)) {
                psRes.setInt(1, cantidad);
                psRes.setInt(2, idBodega);
                psRes.executeUpdate();
            }

            con.commit();
            System.out.println("Repuesto retirado y " + cantidad + " unidades devueltas al inventario.");
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error al retirar repuesto: " + e.getMessage());
        } finally {
            restaurarConexion(con);
        }
        return false;
    }

    //Finalizar orden y generar factura
    public boolean finalizarOrden(int idOrden) {
        String sqlCheckServicios = "SELECT COUNT(*) FROM orden_servicios WHERE id_Orden = ?";
        String sqlSumServicios = "SELECT COALESCE(SUM(servicio_valor), 0) FROM orden_servicios WHERE id_Orden = ?";
        String sqlSumRepuestos = "SELECT COALESCE(SUM(r.repuesto_cantidad * b.precio_unitario), 0) " +
                                 "FROM orden_repuestos r JOIN bodega b ON r.id_Bodega = b.id_Bodega WHERE r.id_Orden = ?";
        String sqlUpdateOrden = "UPDATE orden SET estado_orden = 'FINALIZADA', fecha_salida = CURRENT_DATE(), costo_total = ? WHERE id_orden = ?";
        String sqlInsertFactura = "INSERT INTO factura (Fecha, costoTotal, id_Orden) VALUES (CURRENT_DATE(), ?, ?)";

        Connection con = null;
        try {
            con = conexionBD.getConnection();
            if (esInmutable(idOrden, con)) return false;

            //Validar que la orden tenga servicios
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckServicios)) {
                psCheck.setInt(1, idOrden);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("No se puede finalizar la orden: Debe registrar al menos un servicio.");
                    return false;
                }
            }

            con.setAutoCommit(false);

            BigDecimal totalServicios = BigDecimal.ZERO;
            BigDecimal totalRepuestos = BigDecimal.ZERO;

            try (PreparedStatement psS = con.prepareStatement(sqlSumServicios)) {
                psS.setInt(1, idOrden);
                ResultSet rs = psS.executeQuery();
                if (rs.next()) totalServicios = rs.getBigDecimal(1);
            }

            try (PreparedStatement psR = con.prepareStatement(sqlSumRepuestos)) {
                psR.setInt(1, idOrden);
                ResultSet rs = psR.executeQuery();
                if (rs.next()) totalRepuestos = rs.getBigDecimal(1);
            }

            BigDecimal costoTotal = totalServicios.add(totalRepuestos);

            // Actualizar Orden a finalizada
            try (PreparedStatement psUpd = con.prepareStatement(sqlUpdateOrden)) {
                psUpd.setBigDecimal(1, costoTotal);
                psUpd.setInt(2, idOrden);
                psUpd.executeUpdate();
            }

            // Generar Factura
            try (PreparedStatement psFact = con.prepareStatement(sqlInsertFactura)) {
                psFact.setBigDecimal(1, costoTotal);
                psFact.setInt(2, idOrden);
                psFact.executeUpdate();
            }

            con.commit();
            System.out.println("Orden #" + idOrden + " finalizada. Comprobante de pago generado por un costo total de: Q" + costoTotal);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error al finalizar la orden: " + e.getMessage());
        } finally {
            restaurarConexion(con);
        }
        return false;
    }

    // 7. Cancelar Orden (Reintegra repuestos en una única transacción)[cite: 3]
    public boolean cancelarOrden(int idOrden) {
        String sqlGetRepuestos = "SELECT id_Bodega, repuesto_cantidad FROM orden_repuestos WHERE id_Orden = ?";
        String sqlRestaurarStock = "UPDATE bodega SET existencias = existencias + ? WHERE id_Bodega = ?";
        String sqlUpdateOrden = "UPDATE orden SET estado_orden = 'CANCELADA' WHERE id_orden = ?";

        Connection con = null;
        try {
            con = conexionBD.getConnection();
            if (esInmutable(idOrden, con)) return false;

            con.setAutoCommit(false);

            //Devolver todos los repuestos asignados al inventario
            try (PreparedStatement psGet = con.prepareStatement(sqlGetRepuestos);
                 PreparedStatement psRest = con.prepareStatement(sqlRestaurarStock)) {

                psGet.setInt(1, idOrden);
                ResultSet rs = psGet.executeQuery();

                while (rs.next()) {
                    psRest.setInt(1, rs.getInt("repuesto_cantidad"));
                    psRest.setInt(2, rs.getInt("id_Bodega"));
                    psRest.addBatch();
                }
                psRest.executeBatch();
            }

            //Actualizar estado a cancelada
            try (PreparedStatement psUpd = con.prepareStatement(sqlUpdateOrden)) {
                psUpd.setInt(1, idOrden);
                psUpd.executeUpdate();
            }

            con.commit();
            System.out.println("Orden #" + idOrden + " CANCELADA. Todos los repuestos fueron devueltos al inventario.");
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error al cancelar la orden: " + e.getMessage());
        } finally {
            restaurarConexion(con);
        }
        return false;
    }

    private void restaurarConexion(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}