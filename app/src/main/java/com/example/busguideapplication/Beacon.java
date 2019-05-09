package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Beacon extends AppCompatActivity {
    private static final String TAG = "Beacon" ;
    Bundle datos,salida,destino,valor;
    TextView dates;
    Button parar, cambiar;
    String lugar;
    String salida_obt, destino_obt,valor_obt,datos_obt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon);

        salida = getIntent().getExtras();
        salida_obt= salida.getString("Salida");
        destino = getIntent().getExtras();
        destino_obt= destino.getString("Destino");
        datos = getIntent().getExtras();
        datos_obt= datos.getString("Datos");
        valor = getIntent().getExtras();
        valor_obt = valor.getString("Google");
        dates = findViewById(R.id.datos);
        if(datos_obt.equals("FC:23:60:ED:0B:B7")) {
            lugar="Calle La Laguna Nº1";
            dates.setText("Calle La Laguna Nº1");
        }
        if(datos_obt.equals("E2:C3:B1:E0:2D:8B")){
            lugar="Calle Las Peras Nº7";
            dates.setText("Calle Las Peras Nº7");
        }
        if(datos_obt.equals("E1:FF:56:62:7F:F3")){
            lugar="Dulceria el Rayo";
            dates.setText("Dulceria el Rayo");
        }
        if(datos_obt.equals("CC:F7:38:83:39:83")){
            lugar="Intercambiador La Laguna";
            dates.setText("Intercambiador La Laguna");
        }
        if(datos_obt.equals("E3:10:F4:C0:4F:0E")){
            lugar="Autopista Norte";
            dates.setText("Autopista Norte");
        }
        if(datos_obt.equals("C7:9B:B3:C7:B0:88")) {
            lugar = "Intercambiador Santa Cruz";
            dates.setText("Intercambiador Santa Cruz");
        }

        parar = findViewById(R.id.parar);
        cambiar= findViewById(R.id.cambiar);
    }

    public void Parar_Informacion(View view){
        if(lugar.equals(destino_obt)){
            Intent llegar=new Intent(Beacon.this, Fin.class);
            llegar.putExtra("Cumplida",lugar);
            llegar.putExtra("Google",valor_obt);
            startActivity(llegar);
        }else {
            Intent vuelta = new Intent(Beacon.this, Ruta.class);
            vuelta.putExtra("Salida", salida_obt);
            vuelta.putExtra("Datos", destino_obt);
            vuelta.putExtra("Google",valor_obt);
            startActivity(vuelta);
        }
    }

    public void Cambiar_Ruta(View view){
        Intent cambiar = new Intent(Beacon.this,Inicio.class);
        cambiar.putExtra("Google",valor_obt);
        startActivity(cambiar);
    }
}
