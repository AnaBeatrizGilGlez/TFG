package com.example.busguideapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Configuracion extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    EditText contraseña,Nombre;
    Button cambiar,salir;
    ImageView camarafotos;

    Uri uriprofile;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);

        contraseña = findViewById(R.id.contraseña);
        Nombre = findViewById(R.id.Nombre);
        cambiar= findViewById(R.id.cambiar);
        salir = findViewById(R.id.salir);
        camarafotos=findViewById(R.id.camarafotos);

        mAuth = FirebaseAuth.getInstance();

    }

    public void Cambiar_Foto(View view){
        //showImageChooser();
    }

    public void Salir(View view){
        startActivity(new Intent(Configuracion.this,Ruta.class));
    }

    public void Cambiar_Datos(View view){

    }
}
