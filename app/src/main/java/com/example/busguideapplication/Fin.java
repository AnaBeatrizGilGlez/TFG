package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Fin extends AppCompatActivity {
    Button Okey;
    Bundle lugar;
    EditText Mision;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin);

        Okey=findViewById(R.id.Okey);
        Mision=findViewById(R.id.Mision);

        lugar = getIntent().getExtras();
        String mision_obt= lugar.getString("Cumplida");

        Mision.setText("Usted ya ha llegado a su destino " + mision_obt + " Esperemos que tenga un buen día.");
    }

    public void Aceptar(View view){
        startActivity(new Intent(Fin.this,Inicio.class));
    }
}
