package com.fernandolerma.verano.Clases;

public class Sensores {
    private String Nombre;
    private String Usuarios;
    private double Temperatura;
    private int Humedad;
    private double Manejo_temperatura;
    private int Manejo_humedad;

    public Sensores() {
    }

    public Sensores(String nombre, String usuarios, double temperatura, int humedad, double manejo_temperatura, int manejo_humedad) {
        Nombre = nombre;
        Usuarios = usuarios;
        Temperatura = temperatura;
        Humedad = humedad;
        Manejo_temperatura = manejo_temperatura;
        Manejo_humedad = manejo_humedad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUsuarios() {
        return Usuarios;
    }

    public void setUsuarios(String usuarios) {
        Usuarios = usuarios;
    }

    public double getTemperatura() {
        return Temperatura;
    }

    public void setTemperatura(double temperatura) {
        Temperatura = temperatura;
    }

    public int getHumedad() {
        return Humedad;
    }

    public void setHumedad(int humedad) {
        Humedad = humedad;
    }

    public double getManejo_temperatura() {
        return Manejo_temperatura;
    }

    public void setManejo_temperatura(double manejo_temperatura) {
        Manejo_temperatura = manejo_temperatura;
    }

    public int getManejo_humedad() {
        return Manejo_humedad;
    }

    public void setManejo_humedad(int manejo_humedad) {
        Manejo_humedad = manejo_humedad;
    }
}
