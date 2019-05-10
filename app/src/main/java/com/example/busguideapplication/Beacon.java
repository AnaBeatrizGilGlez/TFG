package com.example.busguideapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Beacon extends AppCompatActivity {
    private static final String TAG = "Beacon" ;
    Bundle datos,salida,destino,valor,check;
    TextView dates;
    Button parar, cambiar;
    MediaPlayer mp;
    String lugar,numero_string;
    Integer numero;
    long tiempo;
    private Vibrator vibrator;
    String salida_obt, destino_obt,valor_obt,datos_obt,check_obt;

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
        check=getIntent().getExtras();
        check_obt=check.getString("Check");

        dates = findViewById(R.id.datos);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp=MediaPlayer.create(this, R.raw.sonido);

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

        if(lugar.equals(destino_obt)){
            mp.start();
        }else{
            if (vibrator.hasVibrator()) {
                tiempo = 800;
                vibrator.vibrate(tiempo);
            }
        }
    }

    public void Parar_Informacion(View view){
        if(lugar.equals(destino_obt)){
            Intent llegar=new Intent(Beacon.this, Fin.class);
            llegar.putExtra("Cumplida",lugar);
            llegar.putExtra("Google",valor_obt);
            mp.stop();
            startActivity(llegar);
        }else {
            Intent vuelta = new Intent(Beacon.this, Ruta.class);
            numero=Integer.parseInt(check_obt);
            numero=numero+1;
            numero_string=String.valueOf(numero);
            vuelta.putExtra("Salida", salida_obt);
            vuelta.putExtra("Datos", destino_obt);
            vuelta.putExtra("Google",valor_obt);
            vuelta.putExtra("Check",numero_string);
            startActivity(vuelta);
        }
    }

    public void Cambiar_Ruta(View view){
        Intent cambiar = new Intent(Beacon.this,Inicio.class);
        cambiar.putExtra("Google",valor_obt);
        startActivity(cambiar);
    }
}
