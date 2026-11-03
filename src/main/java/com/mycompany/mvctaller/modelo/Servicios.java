package com.mycompany.mvctaller.modelo;

import java.math.BigDecimal;

public class Servicios {
    private int idServicios;
    private String tipoServicio;
    private BigDecimal valorServicio;

    public Servicios() {
    }

    public Servicios(int idServicios, String tipoServicio, BigDecimal valorServicio) {
        this.idServicios = idServicios;
        this.tipoServicio = tipoServicio;
        this.valorServicio = valorServicio;
    }

    public int getIdServicios() {
        return idServicios;
    }

    public void setIdServicios(int idServicios) {
        this.idServicios = idServicios;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public BigDecimal getValorServicio() {
        return valorServicio;
    }

    public void setValorServicio(BigDecimal valorServicio) {
        this.valorServicio = valorServicio;
    }
}
