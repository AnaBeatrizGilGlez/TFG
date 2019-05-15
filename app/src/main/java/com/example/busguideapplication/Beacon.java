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
    Dispositivo Intercambiador_SC, Intercambiador_LL,Rayo, Peras, LL, Norte;

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
        Intercambiador_SC = (Dispositivo)getIntent().getSerializableExtra("Objeto_sc");
        Intercambiador_LL = (Dispositivo)getIntent().getSerializableExtra("Objeto_IntLL");
        LL=(Dispositivo)getIntent().getSerializableExtra("Objeto_ll");
        Peras=(Dispositivo)getIntent().getSerializableExtra("Objeto_peras");
        Norte=(Dispositivo)getIntent().getSerializableExtra("Objeto_norte");
        Rayo=(Dispositivo)getIntent().getSerializableExtra("Objeto_rayo");

        dates = findViewById(R.id.datos);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp=MediaPlayer.create(this, R.raw.sonido);

        if(datos_obt.equals(LL.getDispositivo())) {
            lugar=LL.getNombre();
            dates.setText(LL.getNombre());
            LL.setEncontrado(true);
        }
        if(datos_obt.equals(Peras.getDispositivo())) {
            lugar=Peras.getNombre();
            dates.setText(Peras.getNombre());
            Peras.setEncontrado(true);
        }
        if(datos_obt.equals(Rayo.getDispositivo())) {
            lugar=Rayo.getNombre();
            dates.setText(Rayo.getNombre());
            Rayo.setEncontrado(true);
        }
        if(datos_obt.equals(Intercambiador_LL.getDispositivo())) {
            lugar=Intercambiador_LL.getNombre();
            dates.setText(Intercambiador_LL.getNombre());
            Intercambiador_LL.setEncontrado(true);
        }
        if(datos_obt.equals(Norte.getDispositivo())) {
            lugar=Norte.getNombre();
            dates.setText(Norte.getNombre());
            Norte.setEncontrado(true);
        }
        if(datos_obt.equals(Intercambiador_SC.getDispositivo())) {
            lugar = Intercambiador_SC.getNombre();
            dates.setText(Intercambiador_SC.getNombre());
            Intercambiador_SC.setEncontrado(true);
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
            finish();
            startActivity(llegar);
        }else {
            Intent vuelta = new Intent(Beacon.this, Ruta.class);
            numero=Integer.parseInt(check_obt);
            numero=numero+1;
            numero_string=String.valueOf(numero);
            vuelta.putExtra("Objeto_sc",Intercambiador_SC);
            vuelta.putExtra("Objeto_IntLL",Intercambiador_LL);
            vuelta.putExtra("Objeto_ll",LL);
            vuelta.putExtra("Objeto_rayo",Rayo);
            vuelta.putExtra("Objeto_peras",Peras);
            vuelta.putExtra("Objeto_norte", Norte);
            vuelta.putExtra("Salida", salida_obt);
            vuelta.putExtra("Datos", destino_obt);
            vuelta.putExtra("Google",valor_obt);
            vuelta.putExtra("Check",numero_string);
            finish();
            startActivity(vuelta);
        }
    }

    public void Cambiar_Ruta(View view){
        Intent cambiar = new Intent(Beacon.this,Inicio.class);
        cambiar.putExtra("Google",valor_obt);
        finish();
        startActivity(cambiar);
    }
}
