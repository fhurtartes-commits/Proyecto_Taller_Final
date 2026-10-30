package modelo;

public class Repuesto {
    private int idRepuesto;
    private String descripcion;
    private double precioUnitario;
    private int existencias;

    // Constructor vacío
    public Repuesto() {
    }

    // Constructor con parámetros
    public Repuesto(int idRepuesto, String descripcion, double precioUnitario, int existencias) {
        this.idRepuesto = idRepuesto;
        this.descripcion = descripcion;
        setPrecioUnitario(precioUnitario);
        setExistencias(existencias); 
    }

    // Getters y Setters
    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
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
        // Evita precios negativos
        if (precioUnitario < 0) {
            this.precioUnitario = 0.0;
        } else {
            this.precioUnitario = precioUnitario;
        }
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        // Validación corregida: si es menor a 0 se asigna 0, de lo contrario guarda el valor correcto
        if (existencias < 0) {
            this.existencias = 0; 
        } else {
            this.existencias = existencias;
        }
    }
}