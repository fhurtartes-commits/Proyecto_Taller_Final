package modelo;

public class Mecanico {
    private int idMecanico;
    private String nombre;
    private String especialidad;
    private String telefono;

    public Mecanico() {
    }

    public Mecanico(int idMecanico, String nombre, String especialidad, String telefono) {
        this.idMecanico = idMecanico;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }

    public int getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(int idMecanico) {
        this.idMecanico = idMecanico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecilidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}