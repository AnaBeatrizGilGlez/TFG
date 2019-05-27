package com.example.busguideapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class Ruta_3 extends AppCompatActivity {
    TextView paradas,numeroparadas, lugar,detectar,tiempotitulo,tiempo;
    Button buscar,cancelar;
    private String mDeviceList=null;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Bundle valor, check;
    String valor_obt, check_obt;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;
    LinearLayout contenedor;


    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    mDeviceList=result.getDevice().getAddress();
                    Log.i(String.valueOf(getApplicationContext()), mDeviceList);
                    List<String> direcciones = new ArrayList<String>();
                    List<String> nombre_direcciones = new ArrayList<String>();
                    List<String> encontrado = new ArrayList<String>();
                    List<String> ruta = new ArrayList<String>();
                    List<String> en_ruta = new ArrayList<String>();
                    Integer check_int = Integer.parseInt(check_obt);

                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                    String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot note : dataSnapshot.child("Beacons").getChildren()) {
                            direcciones.add(note.child("direccion").getValue().toString());
                            nombre_direcciones.add(note.child("nombre").getValue().toString());
                        }

                        for(DataSnapshot note : dataSnapshot.child("Usuarios").child(user.getUid()).child("Beacons").getChildren()){
                            encontrado.add(note.child("encontrado").getValue().toString());
                            ruta.add(note.child("ruta").getValue().toString());
                        }

                        for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                            en_ruta.add(note.child("Dispositivo_ruta").getValue().toString());
                        }

                        String direccion = dataSnapshot.child("Beacons").child(en_ruta.get(check_int)).child("direccion").getValue().toString();

                        for(int i=0;i<direcciones.size();i++) {
                            if (mDeviceList.equals(direcciones.get(i))) {
                                if(mDeviceList.equals(direccion)){
                                    if((encontrado.get(i).equals("false")) && (ruta.get(i).equals("true"))) {
                                        finish();
                                        Intent cambiar = new Intent(Ruta_3.this, Beacon_3.class);
                                        stopScanning();
                                        cambiar.putExtra("Datos", mDeviceList);
                                        cambiar.putExtra("Google", valor_obt);
                                        cambiar.putExtra("dialog", "0");
                                        cambiar.putExtra("Check", check_obt);
                                        startActivity(cambiar);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruta_2);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        contenedor = findViewById(R.id.contenedor);

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();

        valor = getIntent().getExtras();
        check = getIntent().getExtras();
        valor_obt = valor.getString("Google");
        check_obt = check.getString("Check");
        final Integer check_int = Integer.parseInt(check_obt);

        buscar = findViewById(R.id.buscar);
        stopScanning();
        startScanning();

        paradas = findViewById(R.id.paradas);
        numeroparadas = findViewById(R.id.numeroparadas);
        lugar = findViewById(R.id.lugar);
        cancelar = findViewById(R.id.cancelar);
        detectar = findViewById(R.id.detectar);
        tiempotitulo = findViewById(R.id.tiempotitulo);
        tiempo = findViewById(R.id.tiempo);

        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                List<String> paradas = new ArrayList<String>();
                List<String> paradas_check = new ArrayList<String>();
                List<String> en_ruta = new ArrayList<String>();
                List<String> tiempo_cont = new ArrayList<String>();
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                lugar.setText(dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue() + "-" + dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue());
                String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();
                String s_paradas = dataSnapshot.child("Rutas").child(salida).child(destino).child("numero_paradas").getValue().toString();
                Integer n_paradas = Integer.parseInt(s_paradas);

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                    String paradas_n = note.child("descripcion").getValue().toString();
                    paradas.add(paradas_n);
                }

                for(DataSnapshot note : dataSnapshot.child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").getChildren()){
                    String paradas_ncheck = note.child("check").getValue().toString();
                    paradas_check.add(paradas_ncheck);
                }

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("tiempo").getChildren()) {
                    String tiempo_n = note.getValue().toString();
                    tiempo_cont.add(tiempo_n);
                }

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                    en_ruta.add(note.child("Dispositivo_ruta").getValue().toString());
                }

                for (int i = 0; i < en_ruta.size(); i++) {
                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Beacons").child(en_ruta.get(i)).child("ruta").setValue(true);
                }

                ArrayList<Check> lista = new ArrayList<Check>();
                for(int i=0;i<paradas.size();i++){
                    lista.add(new Check(i,paradas.get(i)));
                }

                Integer i=0;
                for(Check c:lista){
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(c.nombre);
                    cb.setEnabled(false);
                    if(paradas_check.get(i).equals("true")){
                        cb.setChecked(true);
                    }else{
                        cb.setChecked(false);
                    }
                    cb.setId(c.cod);
                    cb.setTextColor(Color.BLACK);
                    contenedor.addView(cb);
                    i++;
                }
                n_paradas=n_paradas-check_int;
                String n_paraditas = String.valueOf(n_paradas);
                numeroparadas.setText(n_paraditas);
                tiempo.setText(tiempo_cont.get(check_int));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void startScanning() {
        System.out.println("start scanning");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }

    public void Cancelar_Ruta (View view){
        Intent cambiar= new Intent(Ruta_3.this,Inicio_2.class);
        cambiar.putExtra("Google", valor_obt);
        cambiar.putExtra("dialog","0");
        finish();
        startActivity(cambiar);
    }
}
