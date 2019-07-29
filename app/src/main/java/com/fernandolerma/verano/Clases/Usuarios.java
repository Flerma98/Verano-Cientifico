package com.fernandolerma.verano.Clases;

public class Usuarios {
    private String Nombre;
    private String Celular;
    private String Correo;
    private String Contraseña;

    public Usuarios() {
    }

    public Usuarios(String nombre, String celular, String correo, String contraseña) {
        Nombre = nombre;
        Celular = celular;
        Correo = correo;
        Contraseña = contraseña;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }
}
