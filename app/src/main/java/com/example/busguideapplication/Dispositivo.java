package com.example.busguideapplication;

import java.io.Serializable;

final public class Dispositivo implements Serializable {

    private String dispositivo, nombre;
    private boolean encontrado,en_ruta;

    public Dispositivo(String dispositivo, String nombre){
        this.dispositivo=dispositivo;
        this.encontrado=false;
        this.nombre=nombre;
        this.en_ruta=false;
    }

    boolean disp_enc(){
        return this.encontrado;
    }

    boolean isEn_ruta(){
        return this.en_ruta;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public void setEn_ruta(boolean en_ruta){
        this.en_ruta=en_ruta;
    }

    String getNombre(){
        return this.nombre;
    }

    String getDispositivo(){
        return this.dispositivo;
    }

}
