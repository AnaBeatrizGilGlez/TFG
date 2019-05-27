package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class Favorito extends AppCompatActivity {

    Bundle datos,dialog;
    String datos_obt;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;
    LinearLayout contenedor;
    Button elegir;
    TextView noexiste;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorito);

        datos = getIntent().getExtras();
        dialog = getIntent().getExtras();
        datos_obt= datos.getString("Google");

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        contenedor = findViewById(R.id.contenedor);
        elegir = findViewById(R.id.elegir);
        noexiste = findViewById(R.id.noexiste);

        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    contenedor.setVisibility(View.VISIBLE);
                    elegir.setVisibility(View.VISIBLE);
                    noexiste.setVisibility(View.GONE);

                }else{
                    contenedor.setVisibility(View.GONE);
                    elegir.setVisibility(View.GONE);
                    noexiste.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Salir(View view){
        Intent cambiar = new Intent(Favorito.this,Inicio_2.class);
        cambiar.putExtra("Google",datos_obt);
        cambiar.putExtra("dialog","0");
        startActivity(cambiar);
    }


    public void Buscar_ruta(View view){
        Intent cambiar = new Intent(Favorito.this, Ruta_3.class);
        cambiar.putExtra("Google",datos_obt);
        cambiar.putExtra("Check","0");
        finish();
        startActivity(cambiar);
    }
}
