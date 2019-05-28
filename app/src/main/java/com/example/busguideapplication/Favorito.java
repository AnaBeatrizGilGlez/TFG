package com.example.busguideapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class Favorito extends AppCompatActivity {

    Bundle datos,dialog;
    String datos_obt;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;
    LinearLayout contenedor;
    Button elegir;
    TextView noexiste,ayudausuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorito);

        datos = getIntent().getExtras();
        dialog = getIntent().getExtras();
        datos_obt= datos.getString("Google");

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        contenedor = findViewById(R.id.contenedor);
        elegir = findViewById(R.id.elegir);
        ayudausuario = findViewById(R.id.ayudausuario);
        noexiste = findViewById(R.id.noexiste);

        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    contenedor.setVisibility(View.VISIBLE);
                    elegir.setVisibility(View.VISIBLE);
                    noexiste.setVisibility(View.GONE);
                    ArrayList<Check> lista = new ArrayList<Check>();
                    ArrayList<String> paradas_favoritas = new ArrayList<>();
                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_fav").setValue("0");
                    for (DataSnapshot note : dataSnapshot.getChildren()) {
                        String paradas_n = note.child("Direccion").getValue().toString();
                        paradas_favoritas.add(paradas_n);
                        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Seleccionada").setValue("false");
                    }
                    for(int i=0;i<paradas_favoritas.size();i++){
                        lista.add(new Check(i,paradas_favoritas.get(i)));
                    }

                    final Integer[] aux = {0};
                    for(final Check c:lista){
                        final CheckBox cb = new CheckBox(getApplicationContext());
                        final String[] numero = {"0"};
                        cb.setText(c.nombre);
                        cb.setId(c.cod);
                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(cb.isChecked()) {
                                    for(DataSnapshot note : dataSnapshot.getChildren()){
                                        Log.i(String.valueOf(getApplicationContext()),"Probando" + note.child("Direccion").getValue() + "Otra cosa" + c.nombre);
                                        if(note.child("Direccion").getValue().equals(c.nombre)){
                                            Log.i(String.valueOf(getApplicationContext()),"ENTRO");
                                            numero[0] = note.getKey();
                                        }
                                    }
                                    aux[0]++;
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(numero[0]).child("Seleccionada").setValue("true");
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_fav").setValue(aux[0]);
                                }else{
                                    for(DataSnapshot note : dataSnapshot.getChildren()){
                                        if(c.nombre.equals(note.child("Direccion"))){
                                            numero[0] =note.getKey();
                                        }
                                    }
                                    aux[0]--;
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(numero[0]).child("Seleccionada").setValue("false");
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_fav").setValue(aux[0]);
                                }
                            }
                        });
                        cb.setTextColor(Color.BLACK);
                        contenedor.addView(cb);
                    }
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
        Intent cambiar = new Intent(Favorito.this,Inicio.class);
        cambiar.putExtra("Google",datos_obt);
        cambiar.putExtra("dialog","0");
        startActivity(cambiar);
    }

    public void Eliminar_ruta(View view){
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String aux = dataSnapshot.child("aux_fav").getValue().toString();
                Integer aux_fav = Integer.parseInt(aux);
                ArrayList<String> rutas_eliminar = new ArrayList<>();
                ArrayList<String> favoritos_eliminar = new ArrayList<>();
                ArrayList<String> favoritos_seleccionados_eliminar = new ArrayList<>();
                Integer auxiliar=0;
                Integer auxiliar_fav=0;
                if (aux_fav == 0) {
                    ayudausuario.setVisibility(View.VISIBLE);
                    ayudausuario.setText("Por favor selecciona la ruta o las rutas que quiere eliminar de favoritos");
                } else {
                    for (DataSnapshot note : dataSnapshot.child("Favoritos").getChildren()) {
                        if (note.child("Seleccionada").getValue().equals("true")) {
                            rutas_eliminar.add(note.getKey());
                            auxiliar++;
                            auxiliar_fav++;
                        }
                        if(auxiliar_fav!=0) {
                            if (auxiliar_fav%2!=0) {
                                auxiliar_fav++;
                            } else {
                                if (auxiliar_fav%2==0) {
                                    favoritos_eliminar.add(note.child("Direccion").getValue().toString());
                                    favoritos_seleccionados_eliminar.add(note.child("Seleccionada").getValue().toString());
                                }
                            }
                        }
                    }

                    for(int i=0;i<auxiliar;i++){
                        favoritos_eliminar.add(null);
                        favoritos_seleccionados_eliminar.add(null);
                    }

                    Integer i=0;
                    Integer j=0;
                    Integer otro_aux=0;
                    for(DataSnapshot note : dataSnapshot.child("Favoritos").getChildren()){
                        if(note.getKey().equals(rutas_eliminar.get(i))){
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Direccion").setValue(favoritos_eliminar.get(j));
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Seleccionada").setValue(favoritos_seleccionados_eliminar.get(j));
                            if(i!=rutas_eliminar.size()-1) {
                                i++;
                            }
                            j++;
                            otro_aux=1;
                        }else{
                            if(otro_aux==1){
                                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Direccion").setValue(favoritos_eliminar.get(j));
                                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Seleccionada").setValue(favoritos_seleccionados_eliminar.get(j));
                                j++;
                            }else{
                                if(note.getKey().equals(rutas_eliminar.get(i+1)) && (otro_aux==1)){
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Direccion").setValue(favoritos_eliminar.get(j));
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Favoritos").child(note.getKey()).child("Seleccionada").setValue(favoritos_seleccionados_eliminar.get(j));
                                    j++;
                                }
                            }
                        }
                    }
                    Intent cambiar = new Intent(Favorito.this,Favorito.class);
                    cambiar.putExtra("Google",datos_obt);
                    finish();
                    startActivity(cambiar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Buscar_ruta(View view){
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("aux_fav").exists()){
                    String aux=dataSnapshot.child("aux_fav").getValue().toString();
                    Integer aux_fav=Integer.parseInt(aux);
                    String parada_seleccionada = null;
                    if(aux_fav==0){
                        ayudausuario.setVisibility(View.VISIBLE);
                        ayudausuario.setText("Por favor seleccione una de sus rutas favoritas");
                    }else{
                        if(aux_fav>1){
                            ayudausuario.setVisibility(View.VISIBLE);
                            ayudausuario.setText("Por favor seleccione unicamente una de sus rutas favoritas");
                        }else{
                            for(DataSnapshot note : dataSnapshot.child("Favoritos").getChildren()){
                                if(note.child("Seleccionada").getValue().equals("true")){
                                    parada_seleccionada=note.child("Direccion").getValue().toString();
                                }
                            }
                            String[] parts = parada_seleccionada.split("-");
                            String part1=parts[0];
                            String part2=parts[1];
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Salida").setValue(part1);
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Destino").setValue(part2);
                            Intent cambiar = new Intent(Favorito.this, Ruta.class);
                            cambiar.putExtra("Google",datos_obt);
                            cambiar.putExtra("Check","0");
                            finish();
                            startActivity(cambiar);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
