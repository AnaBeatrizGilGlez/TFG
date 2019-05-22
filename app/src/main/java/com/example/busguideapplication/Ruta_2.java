package com.example.busguideapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class Ruta_2 extends AppCompatActivity {
    TextView paradas,numeroparadas, lugar,detectar,tiempotitulo,tiempo;
    Button buscar,cancelar;
    CheckedTextView uno,dos,tres,cuatro;
    private String mDeviceList=null;
    BluetoothManager btManager;
    TextView peripheralTextView;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Bundle valor, check;
    String valor_obt, check_obt;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mDeviceList=result.getDevice().getAddress();
            Log.i(String.valueOf(getApplicationContext()), mDeviceList);
            mDatabase.child("Dispositivos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    List<String> direcciones = new ArrayList<String>();
                    List<String> nombre_direcciones = new ArrayList<String>();
                    List<String> encontrado = new ArrayList<String>();
                    List<String> ruta = new ArrayList<String>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot note : dataSnapshot.child("Beacons").getChildren()) {
                            direcciones.add(note.child("direccion").getValue().toString());
                            nombre_direcciones.add(note.child("nombre").getValue().toString());
                            encontrado.add(note.child("encontrado").getValue().toString());
                            ruta.add(note.child("ruta").getValue().toString());
                        }

                        for(int i=0;i<direcciones.size();i++) {
                            if (mDeviceList.equals(direcciones.get(i))) {
                                if((encontrado.get(i).equals("false")) && (ruta.get(i).equals("true"))){
                                    Intent cambiar = new Intent(Ruta_2.this, Beacon_2.class);
                                    stopScanning();
                                    cambiar.putExtra("Datos", mDeviceList);
                                    cambiar.putExtra("Google", valor_obt);
                                    cambiar.putExtra("dialog","0");
                                    cambiar.putExtra("Check", check_obt);
                                    finish();
                                    startActivity(cambiar);
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
        setContentView(R.layout.ruta);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();

        valor = getIntent().getExtras();
        check = getIntent().getExtras();
        valor_obt = valor.getString("Google");
        check_obt = check.getString("Check");

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
        uno = findViewById(R.id.Uno);
        uno.setVisibility(View.GONE);
        tres = findViewById(R.id.Tres);
        tres.setVisibility(View.GONE);
        dos = findViewById(R.id.Dos);
        dos.setVisibility(View.GONE);
        cuatro = findViewById(R.id.Cuatro);
        cuatro.setVisibility(View.GONE);

        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                List<String> paradas = new ArrayList<String>();
                List<String> en_ruta = new ArrayList<String>();
                lugar.setText(dataSnapshot.child("Salida").getValue() + "-" + dataSnapshot.child("Destino").getValue());
                String salida = dataSnapshot.child("Salida").getValue().toString();
                String destino = dataSnapshot.child("Destino").getValue().toString();
                String s_paradas = dataSnapshot.child("Rutas").child(salida).child(destino).child("numero_paradas").getValue().toString();
                Integer n_paradas = Integer.parseInt(s_paradas);
                String s_tiempo = dataSnapshot.child("Rutas").child(salida).child(destino).child("tiempo").getValue().toString();
                //Integer n_tiempo = Integer.parseInt(s_tiempo);
                numeroparadas.setText(dataSnapshot.child("Rutas").child(salida).child(destino).child("numero_paradas").getValue().toString());
                tiempo.setText(dataSnapshot.child("Rutas").child(salida).child(destino).child("tiempo").getValue().toString());
                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                    String paradas_n = note.getValue().toString();
                    paradas.add(paradas_n);
                }

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Dispositivos_ruta").getChildren()) {
                    en_ruta.add(note.getValue().toString());
                }

                for (int i = 0; i < en_ruta.size(); i++) {
                    mDatabase.child("Dispositivos").child("Beacons").child(en_ruta.get(i)).child("ruta").setValue(true);
                }

                for (int i = 0; i < paradas.size(); i++) {
                    if (i == 0) {
                        uno.setVisibility(View.VISIBLE);
                        uno.setText(paradas.get(i));
                    } else if (i == 1) {
                        uno.setVisibility(View.VISIBLE);
                        uno.setText(paradas.get(i - 1));
                        dos.setVisibility(View.VISIBLE);
                        dos.setText(paradas.get(i));
                    } else {
                        if (i == 2) {
                            uno.setVisibility(View.VISIBLE);
                            uno.setText(paradas.get(i - 2));
                            dos.setVisibility(View.VISIBLE);
                            dos.setText(paradas.get(i - 1));
                            tres.setVisibility(View.VISIBLE);
                            tres.setText(paradas.get(i));
                        } else {
                            if (i == 3) {
                                uno.setVisibility(View.VISIBLE);
                                uno.setText(paradas.get(i - 3));
                                dos.setVisibility(View.VISIBLE);
                                dos.setText(paradas.get(i - 2));
                                tres.setVisibility(View.VISIBLE);
                                tres.setText(paradas.get(i - 1));
                                cuatro.setVisibility(View.VISIBLE);
                                cuatro.setText(paradas.get(i));
                            }
                        }
                    }
                    /*peripheralTextView.append(paradas.get(i) + "\n");
                    final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
                    if (scrollAmount > 0)
                        peripheralTextView.scrollTo(0, scrollAmount);*/

                }
                if (check_obt.equals("1")) {
                    uno.setChecked(true);
                    numeroparadas.setText(String.valueOf(n_paradas - 1));
                    String ruta = destino + " - " + salida;
                    //Integer tiempo_n = n_tiempo - Integer.parseInt(dataSnapshot.child("Tiempos").child(ruta).child("tiempo").getValue().toString());
                    //tiempo.setText(String.valueOf(n_tiempo - tiempo_n));
                } else {
                    if (check_obt.equals("2")) {
                        uno.setChecked(true);
                        dos.setChecked(true);
                        numeroparadas.setText(String.valueOf(n_paradas - 2));
                        tiempo.setText("10 minutos");  //Parametro a pasar en cambiar
                    } else {
                        if (check_obt.equals("3")) {
                            uno.setChecked(true);
                            dos.setChecked(true);
                            tres.setChecked(true);
                            numeroparadas.setText(String.valueOf(n_paradas - 3));
                            tiempo.setText("10 minutos");
                        }
                    }
                }
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
        System.out.println("stopping scanning");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }

    public void Cancelar_Ruta (View view){
        mDatabase.child("Dispositivos").child("Destino").setValue("Seleccione lugar");
        mDatabase.child("Dispositivos").child("Salida").setValue("Seleccione lugar");
        mDatabase.child("Dispositivos").child("inicializar").setValue("nothing");

        Intent cambiar= new Intent(Ruta_2.this,Inicio_2.class);
        cambiar.putExtra("Google", valor_obt);
        cambiar.putExtra("dialog","0");
        finish();
        startActivity(cambiar);
    }
}
