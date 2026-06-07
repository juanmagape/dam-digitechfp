package Modelo;

public class Contacto {
    private String name;
    private String apellido;
    private int numero;

    public Contacto(String name, String apellido, int numero) {
        this.name = name;
        this.apellido = apellido;
        this.numero = numero;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getName() {
        return name;
    }

    public String getApellido() {
        return apellido;
    }

    public int getNumero() {
        return numero;
    }

    public String toString() {
        return name + " " + apellido + " | " + numero ;
    }
}
