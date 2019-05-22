package com.example.busguideapplication;

import android.support.annotation.NonNull;
import com.google.firebase.database.*;

public class Rutas_class {

    private DatabaseReference mDatabase;
    private String nombre_ruta;
    private String nombre_dest;

    public Rutas_class(String nombre){
        this.nombre_ruta=nombre;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(nombre).child("destino").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre_dest = dataSnapshot.child("destino").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String setNombre_dest(){
        return nombre_dest;
    }

}
