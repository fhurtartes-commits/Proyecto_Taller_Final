package modelo;

import java.math.BigDecimal;

public class OrdenServicio {
    private int idDetalleServicio;
    private String servicioEstado;
    private int idServicios;
    private int idOrden;
    private BigDecimal servicioValor;

    public OrdenServicio() {}

    public int getIdDetalleServicio() { return idDetalleServicio; }
    public void setIdDetalleServicio(int idDetalleServicio) { this.idDetalleServicio = idDetalleServicio; }

    public String getServicioEstado() { return servicioEstado; }
    public void setServicioEstado(String servicioEstado) { this.servicioEstado = servicioEstado; }

    public int getIdServicios() { return idServicios; }
    public void setIdServicios(int idServicios) { this.idServicios = idServicios; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public BigDecimal getServicioValor() { return servicioValor; }
    public void setServicioValor(BigDecimal servicioValor) { this.servicioValor = servicioValor; }
}