package com.example.busguideapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class Beacon_2 extends AppCompatActivity {
    Bundle datos,valor,check;
    TextView dates;
    Button parar, cambiar;
    MediaPlayer mp;
    String lugar,numero_string;
    Integer numero;
    long tiempo;
    private Vibrator vibrator;
    String valor_obt,datos_obt,check_obt;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon);

        datos = getIntent().getExtras();
        datos_obt= datos.getString("Datos");
        valor = getIntent().getExtras();
        valor_obt = valor.getString("Google");
        check=getIntent().getExtras();
        check_obt=check.getString("Check");

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();

        dates = findViewById(R.id.datos);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp=MediaPlayer.create(this, R.raw.sonido);

        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                List<String> direcciones = new ArrayList<String>();
                List<String> nombre_direcciones = new ArrayList<String>();
                List<String> paradas = new ArrayList<String>();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                String salida = dataSnapshot.child("Salida").getValue().toString();
                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();

                for(DataSnapshot note : dataSnapshot.child("Beacons").getChildren()){
                    direcciones.add(note.child("direccion").getValue().toString());
                    nombre_direcciones.add(note.child("nombre").getValue().toString());
                }

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                    String paradas_n = note.getValue().toString();
                    paradas.add(paradas_n);
                }

                mDatabase.child("Dispositivos").child("dispositivos").setValue("nothing");

                for(int i=0;i<direcciones.size();i++){
                    if(datos_obt.equals(direcciones.get(i))){
                        Log.i(String.valueOf(getApplicationContext()),"Hix2");
                        lugar = nombre_direcciones.get(i);
                        dates.setText(nombre_direcciones.get(i));
                        String dispositivo = dataSnapshot.child("Disp-Nombre").child(nombre_direcciones.get(i)).getValue().toString();
                        mDatabase.child("Dispositivos").child("dispositivos").setValue(dispositivo);
                        mDatabase.child("Dispositivos").child("Beacons").child(dispositivo).child("encontrado").setValue("true");
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Beacon_2.this);
                builder.setMessage(paradas.get(Integer.parseInt(check_obt)));
                builder.setNegativeButton(R.string.aceptar, null);
                builder.create();
                builder.show();

                if(lugar.equals(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue())){
                    mp.start();
                }else{
                    if (vibrator.hasVibrator()) {
                        tiempo = 800;
                        vibrator.vibrate(tiempo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        parar = findViewById(R.id.parar);
        cambiar= findViewById(R.id.cambiar);
    }

    public void Parar_Informacion(View view){
        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    String dispositivo = dataSnapshot.child("dispositivos").getValue().toString();
                    mDatabase.child("Dispositivos").child("Beacons").child(dispositivo).child("encontrado").setValue(true);
                    if(lugar.equals(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue())) {
                        mp.stop();
                        Intent llegar=new Intent(Beacon_2.this, Fin.class);
                        llegar.putExtra("Cumplida",lugar);
                        llegar.putExtra("Google",valor_obt);
                        llegar.putExtra("dialog","0");
                        finish();
                        startActivity(llegar);
                    }else{
                        Intent vuelta = new Intent(Beacon_2.this, Ruta_2.class);
                        numero=Integer.parseInt(check_obt);
                        numero=numero+1;
                        numero_string=String.valueOf(numero);
                        vuelta.putExtra("Google",valor_obt);
                        vuelta.putExtra("Check",numero_string);
                        finish();
                        startActivity(vuelta);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Cambiar_Ruta(View view){
        Intent cambiar = new Intent(Beacon_2.this,Inicio_2.class);
        cambiar.putExtra("Google",valor_obt);
        cambiar.putExtra("dialog","0");
        finish();
        startActivity(cambiar);
    }
}
