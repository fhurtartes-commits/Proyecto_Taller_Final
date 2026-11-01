package com.mycompany.mvctaller.modelo;

public class Vehiculos {
   
private String noPlaca;
    private String modelo;
    private String marca;
    private String color;
    private int idCliente;

    public Vehiculos(String noPlaca, String modelo, String marca, String color, int idCliente) {
        this.noPlaca = noPlaca;
        this.modelo = modelo;
        this.marca = marca;
        this.color = color;
        this.idCliente = idCliente;
    }

    public String getNoPlaca() {return noPlaca;}
    public void setNoPlaca(String noPlaca) {this.noPlaca = noPlaca;}

    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public int getIdCliente() {return idCliente;}
    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}    
    
}
