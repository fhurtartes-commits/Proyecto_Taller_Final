package modelo;

public class OrdenRepuesto {
    private int idDetalleRepuesto;
    private String repuestoEstado;
    private int repuestoCantidad;
    private int idBodega;
    private int idOrden;

    public OrdenRepuesto() {}

    public int getIdDetalleRepuesto() { return idDetalleRepuesto; }
    public void setIdDetalleRepuesto(int idDetalleRepuesto) { this.idDetalleRepuesto = idDetalleRepuesto; }

    public String getRepuestoEstado() { return repuestoEstado; }
    public void setRepuestoEstado(String repuestoEstado) { this.repuestoEstado = repuestoEstado; }

    public int getRepuestoCantidad() { return repuestoCantidad; }
    public void setRepuestoCantidad(int repuestoCantidad) { this.repuestoCantidad = repuestoCantidad; }

    public int getIdBodega() { return idBodega; }
    public void setIdBodega(int idBodega) { this.idBodega = idBodega; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }
}