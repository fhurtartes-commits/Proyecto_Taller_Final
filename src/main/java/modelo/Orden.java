package modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Orden {
    private int idOrden;
    private String estadoOrden;
    private Date fechaEntrada;
    private Date fechaSalida;
    private BigDecimal costoTotal;
    private int idMecanico;
    private String noPlaca;

    public Orden() {}

    public Orden(int idMecanico, String noPlaca) {
        this.idMecanico = idMecanico;
        this.noPlaca = noPlaca;
        this.estadoOrden = "PENDIENTE";
    }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public String getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(String estadoOrden) { this.estadoOrden = estadoOrden; }

    public Date getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(Date fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public Date getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }

    public BigDecimal getCostoTotal() { return costoTotal; }
    public void setCostoTotal(BigDecimal costoTotal) { this.costoTotal = costoTotal; }

    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }

    public String getNoPlaca() { return noPlaca; }
    public void setNoPlaca(String noPlaca) { this.noPlaca = noPlaca; }
}