package com.example.busguideapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import org.spongycastle.jcajce.provider.digest.SHA3;

import java.util.ArrayList;
import java.util.List;

public class Beacon extends AppCompatActivity {
    Bundle datos,valor,check;
    TextView dates,dates_paradas;
    Button parar, cambiar;
    MediaPlayer mp;
    String lugar,numero_string;
    Integer numero;
    long tiempo;
    private Vibrator vibrator;
    String valor_obt,datos_obt,check_obt;
    private String mDeviceList=null;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    mDeviceList = result.getDevice().getAddress();
                    Log.i(String.valueOf(getApplicationContext()), mDeviceList);
                    List<String> direcciones = new ArrayList<String>();
                    List<String> nombre_direcciones = new ArrayList<String>();
                    List<String> encontrado = new ArrayList<String>();
                    List<String> ruta = new ArrayList<String>();
                    List<String> en_ruta = new ArrayList<String>();
                    List<String> firma = new ArrayList<String>();
                    List<String> firma_comprobacion = new ArrayList<String>();
                    List<String> cerca_ruta = new ArrayList<>();
                    Integer check_int = Integer.parseInt(check_obt);

                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                    String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot note : dataSnapshot.child("Beacons").getChildren()) {
                            direcciones.add(note.child("direccion").getValue().toString());
                            nombre_direcciones.add(note.child("nombre").getValue().toString());
                            firma.add(note.child("firma").getValue().toString());
                        }

                        for(DataSnapshot note : dataSnapshot.child("Usuarios").child(user.getUid()).child("Beacons").getChildren()){
                            encontrado.add(note.child("encontrado").getValue().toString());
                            ruta.add(note.child("ruta").getValue().toString());
                        }

                        for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                            en_ruta.add(note.child("Dispositivo_ruta").getValue().toString());
                        }

                        for(DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas_pasadas").getChildren()){
                            cerca_ruta.add(note.getValue().toString());
                        }



                        for(int i=0;i<firma.size();i++){
                            String direccion = direcciones.get(i);
                            String nombre_direccion = nombre_direcciones.get(i);
                            String concat = direccion.concat(nombre_direccion);
                            byte[] input, output5;
                            StringBuffer hexString = new StringBuffer();
                            final SHA3.DigestSHA3 md = new SHA3.DigestSHA3(512);

                            input = concat.getBytes();
                            md.reset();
                            md.update(input);
                            output5=md.digest();
                            for(int j=0;j<output5.length;j++){
                                hexString.append(Integer.toHexString(0xFF & output5[j]));
                            }
                            firma_comprobacion.add(hexString.toString());
                        }

                        Log.i(String.valueOf(getApplicationContext()), "Tamaño " + direcciones.size());
                        if(cerca_ruta.size()>0) {
                            for (int i = 0; i < cerca_ruta.size(); i++) {
                                for (int j = 0; j < direcciones.size(); j++) {
                                    String direccion =dataSnapshot.child("Beacons").child(cerca_ruta.get(i)).child("direccion").getValue().toString();
                                    if(direccion.equals(direcciones.get(j))){
                                        direcciones.remove(j);
                                    }
                                }
                            }
                        }
                        Log.i(String.valueOf(getApplicationContext()), "Tamaño " + direcciones.size());

                        if((en_ruta.size()>0) && (en_ruta.size()>check_int+1)){
                            String direccion = dataSnapshot.child("Beacons").child(en_ruta.get(check_int+1)).child("direccion").getValue().toString();

                            for (int i = 0; i < direcciones.size(); i++) {
                                if (mDeviceList.equals(direcciones.get(i))) {
                                    if (mDeviceList.equals(direccion)) {
                                        if(firma.get(i).equals(firma_comprobacion.get(i))) {
                                            Log.i(String.valueOf(getApplicationContext()),"Firma de la direccion " + mDeviceList + "es " + firma_comprobacion.get(i));
                                            if ((encontrado.get(i).equals("false")) && (ruta.get(i).equals("true"))) {
                                                Intent cambiar = new Intent(Beacon.this, Beacon.class);
                                                numero=Integer.parseInt(check_obt);
                                                numero=numero+1;
                                                numero_string=String.valueOf(numero);
                                                stopScanning();
                                                cambiar.putExtra("Datos", mDeviceList);
                                                cambiar.putExtra("Google", valor_obt);
                                                cambiar.putExtra("Check", numero_string);
                                                finish();
                                                startActivity(cambiar);
                                            }
                                        }
                                    }else {
                                        if (ruta.get(i).equals("false")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Beacon.this);
                                            builder.setMessage("Usted se ha salido de ruta por favor bájese de la guagua para mostrarle la nueva ruta");
                                            builder.setNegativeButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent cambiar = new Intent(Beacon.this, Inicio.class);
                                                    cambiar.putExtra("Google", valor_obt);
                                                    cambiar.putExtra("dialog", "0");
                                                    startActivity(cambiar);
                                                }
                                            });
                                            builder.create();
                                            builder.show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

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
        final Integer check_int = Integer.parseInt(check_obt);

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        dates = findViewById(R.id.datos);
        dates_paradas=findViewById(R.id.datos_parada);

        stopScanning();
        startScanning();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp=MediaPlayer.create(this, R.raw.sonido);

        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                List<String> direcciones = new ArrayList<String>();
                List<String> nombre_direcciones = new ArrayList<String>();
                List<String> paradas = new ArrayList<String>();
                List<String> paradas_check = new ArrayList<String>();
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();

                for(DataSnapshot note : dataSnapshot.child("Beacons").getChildren()){
                    direcciones.add(note.child("direccion").getValue().toString());
                    nombre_direcciones.add(note.child("nombre").getValue().toString());
                }

                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                    String paradas_n = note.child("descripcion").getValue().toString();
                    paradas.add(paradas_n);
                    String paradas_checki = note.getKey();
                    paradas_check.add(paradas_checki);
                    Log.i(String.valueOf(getApplicationContext()), paradas_checki);
                }

                for(int i=0;i<direcciones.size();i++){
                    if(datos_obt.equals(direcciones.get(i))){
                        lugar = nombre_direcciones.get(i);
                        dates.setText(nombre_direcciones.get(i));
                        String dispositivo = dataSnapshot.child("Disp-Nombre").child(nombre_direcciones.get(i)).getValue().toString();
                        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("dispositivos").setValue(dispositivo);
                        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Beacons").child(dispositivo).child("encontrado").setValue("true");
                    }
                }

                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").child(paradas_check.get(check_int)).child("check").setValue("true");
                dates_paradas.setText(paradas.get(check_int));

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
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    String dispositivo = dataSnapshot.child("Usuarios").child(user.getUid()).child("dispositivos").getValue().toString();

                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Beacons").child(dispositivo).child("encontrado").setValue("true");
                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("dispositivos").setValue("nothing");

                    if(lugar.equals(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue())) {
                        mp.stop();
                        Intent llegar=new Intent(Beacon.this, Fin.class);
                        llegar.putExtra("Cumplida",lugar);
                        llegar.putExtra("Google",valor_obt);
                        llegar.putExtra("dialog","0");
                        finish();
                        startActivity(llegar);
                    }else{
                        Intent vuelta = new Intent(Beacon.this, Ruta.class);
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
        Intent cambiar = new Intent(Beacon.this,Inicio.class);
        cambiar.putExtra("Google",valor_obt);
        cambiar.putExtra("dialog","0");
        finish();
        startActivity(cambiar);
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

}

