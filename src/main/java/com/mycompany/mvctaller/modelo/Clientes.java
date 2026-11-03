package com.mycompany.mvctaller.modelo;

public class Clientes {
    private int idCliente;
    private String nombre;
    private String nit;
    private String telefono;

    public Clientes(int idCliente, String nombre, String nit, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
    }

    public int getIdCliente() {return idCliente;}
    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getNit() {return nit;}
    public void setNit(String nit) {this.nit = nit;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}

    public int getId() {
        return idCliente;
    }
}