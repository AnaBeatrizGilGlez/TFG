package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fin extends AppCompatActivity {
    Button Okey;
    Bundle lugar;
    TextView Mision;
    Bundle valor;
    String valor_obt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin);

        Okey=findViewById(R.id.Okey);
        Mision=findViewById(R.id.Mision);
        valor=getIntent().getExtras();
        valor_obt=valor.getString("Google");

        lugar = getIntent().getExtras();
        String mision_obt= lugar.getString("Cumplida");

        Mision.setText(Mision.getText() + mision_obt + " Esperemos que tenga un buen día.");
    }

    public void Aceptar(View view){
        Intent cambiar=new Intent(Fin.this,Inicio.class);
        cambiar.putExtra("Google",valor_obt);
        cambiar.putExtra("dialog","0");
        startActivity(cambiar);
    }
}
