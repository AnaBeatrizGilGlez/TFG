package com.example.busguideapplication;

import java.io.Serializable;

final public class Dispositivo implements Serializable {

    private String dispositivo, nombre;
    private boolean encontrado;

    public Dispositivo(String dispositivo, String nombre){
        this.dispositivo=dispositivo;
        this.encontrado=false;
        this.nombre=nombre;
    }

    boolean disp_enc(){
        return this.encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    String getNombre(){
        return this.nombre;
    }

    boolean isEncontrado(){
        return this.encontrado;
    }

    String getDispositivo(){
        return this.dispositivo;
    }

}
