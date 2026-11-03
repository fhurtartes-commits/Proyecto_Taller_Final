package com.mycompany.mvctaller.modelo;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int existencias;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String nombre, String descripcion, double precioUnitario, int existencias) {
        this.idRepuesto = idRepuesto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = (precioUnitario < 0) ? 0.0 : precioUnitario;
        this.existencias = (existencias < 0) ? 0 : existencias;             
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = (precioUnitario < 0) ? 0.0 : precioUnitario;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = (existencias < 0) ? 0 : existencias;
    }
}