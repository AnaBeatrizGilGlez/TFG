package com.example.busguideapplication;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Inicio extends AppCompatActivity {
    private static final String TAG = "Inicio" ;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    Spinner combolugares,salida_personas;
    Button buscar;
    TextView nombre_perfil;
    ImageView imagen,foto_perfil;
    String aux,aux_salida;
    Integer aux_no=0;
    private String mDeviceList=null;
    String inicializar="null";
    Bundle datos;
    String datos_obt;
    ArrayAdapter<CharSequence> adapter;
    TextView start;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mDeviceList=result.getDevice().getAddress();
            Log.i(String.valueOf(getApplicationContext()),"Pos nah" + mDeviceList);
            if (mDeviceList.equals("FC:23:60:ED:0B:B7")) {
                stopScanning();
                inicializar="Calle La Laguna Nº1";
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals("CC:F7:38:83:39:83")){
                stopScanning();
                inicializar="Intercambiador La Laguna";
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals("E2:C3:B1:E0:2D:8B")){
                stopScanning();
                inicializar="Calle Las peras Nº7";
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals("C7:9B:B3:C7:B0:88")){
                stopScanning();
                inicializar="Intercambiador Santa Cruz";
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals("E3:10:F4:C0:4F:0E")){
                stopScanning();
                inicializar="Autopista Norte";
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals("E1:FF:56:62:7F:F3")){
                stopScanning();
                inicializar="Dulceria el Rayo";
                aux_no=1;
                Alerta();
            }
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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        combolugares= findViewById(R.id.spinnerlugares);
        salida_personas=findViewById(R.id.salida_persona);
        imagen = findViewById(R.id.imagen);
        buscar=findViewById(R.id.buscar);
        start=findViewById(R.id.Start);
        nombre_perfil=findViewById(R.id.nombre_perfil);
        datos = getIntent().getExtras();
        foto_perfil=findViewById(R.id.foto_perfil);
        datos_obt= datos.getString("Google");
        startScanning();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        Log.i(TAG, "Usuario " + user.getDisplayName());
        if(user!=null) {
            if (datos_obt.equals("1")) {
                if(!(user.getDisplayName() ==null)) {
                    String name = user.getDisplayName();
                    nombre_perfil.setText("Bienvenido/a " + name);
                }else{
                    nombre_perfil.setText("Bienvenido/a " + user.getEmail());
                }
                imagen.setVisibility(View.VISIBLE);
                if(!(user.getPhotoUrl() ==null)){
                    foto_perfil.setImageURI(user.getPhotoUrl());
                    foto_perfil.setVisibility(View.VISIBLE);
                }else{
                    foto_perfil.setVisibility(View.GONE);
                }
            } else {
                imagen.setVisibility(View.GONE);
                nombre_perfil.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }

        adapter = ArrayAdapter.createFromResource(Inicio.this, R.array.combo_lugare,
                android.R.layout.simple_spinner_item);
        salida_personas.setAdapter(adapter);
        salida_personas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aux_salida = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        combolugares.setAdapter(adapter);
        combolugares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aux = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

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

    public void Salir(View view){
        if(datos_obt.equals("1")){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Sesion cerrada", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Inicio.this, MainActivity.class));
        }else{
            Toast.makeText(getApplicationContext(), "Sesion cerrada", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(Inicio.this, MainActivity.class));
        }
    }

    public void Configurar(View view){
        startActivity(new Intent(Inicio.this,Configuracion.class));
    }

    public void funcion_normal(){
        if(aux.equals("Seleccione lugar")){
            Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                    Toast.LENGTH_SHORT).show();
        }else{
            if(aux_salida.equals("Seleccione lugar")){
                Toast.makeText(getApplicationContext(), "Por favor seleccione un lugar de salida.",
                        Toast.LENGTH_SHORT).show();
            }else{
                if(aux.equals(aux_salida)){
                    Toast.makeText(getApplicationContext(), "La parada destino es igual a la parada salida",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent cambiar = new Intent(Inicio.this, Ruta.class);
                    stopScanning();
                    cambiar.putExtra("Salida", aux_salida);
                    cambiar.putExtra("Datos", aux);
                    cambiar.putExtra("Google",datos_obt);
                    cambiar.putExtra("Check","0");
                    startActivity(cambiar);
                }
            }
        }
    }

    public void Alerta(){
        stopScanning();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Quiere usted salir de la parada " + inicializar + "?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        salida_personas.setVisibility(View.GONE);
                        start.setVisibility(View.GONE);
                        if(aux.equals("Seleccione lugar")){
                            Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                                    Toast.LENGTH_SHORT).show();
                            aux_no=0;
                        }else {
                            if (aux.equals(inicializar)) {
                                Toast.makeText(getApplicationContext(), "Usted se encuentra ya en esta parada destino. Seleccione otra parada destino por favor",
                                        Toast.LENGTH_SHORT).show();
                                aux_no=0;
                            } else {
                                Intent cambiar = new Intent(Inicio.this, Ruta.class);
                                cambiar.putExtra("Salida", inicializar);
                                cambiar.putExtra("Datos", aux);
                                cambiar.putExtra("Google", datos_obt);
                                cambiar.putExtra("Check", "0");
                                startActivity(cambiar);
                            }
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        funcion_normal();
                    }
                });
        Dialog dialog=builder.create();
        dialog.show();
    }

    public void Buscar_ruta(View view){
        if(aux.equals("Seleccione lugar")){
            Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                    Toast.LENGTH_SHORT).show();
        }else{
            if (inicializar.equals( "null") || aux_no!=0) {
                if(aux_salida.equals("Seleccione lugar")){
                    Toast.makeText(getApplicationContext(), "Por favor seleccione un lugar de salida. Ya que no se ha detectado ninguna parada",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(aux.equals(aux_salida)){
                        Toast.makeText(getApplicationContext(), "La parada destino es igual a la parada salida",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Intent cambiar = new Intent(Inicio.this, Ruta.class);
                        cambiar.putExtra("Salida", aux_salida);
                        cambiar.putExtra("Datos", aux);
                        cambiar.putExtra("Google",datos_obt);
                        cambiar.putExtra("Check","0");
                        startActivity(cambiar);
                    }
                }
            }else{
                if(aux.equals("Seleccione lugar")){
                    Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                            Toast.LENGTH_SHORT).show();
                    aux_no=0;
                }else {
                    if (aux.equals(inicializar)) {
                        Toast.makeText(getApplicationContext(), "Usted se encuentra ya en esta parada destino. Seleccione otra parada destino por favor",
                                Toast.LENGTH_SHORT).show();
                        aux_no=0;
                    } else {
                        Intent cambiar = new Intent(Inicio.this, Ruta.class);
                        cambiar.putExtra("Salida", inicializar);
                        cambiar.putExtra("Datos", aux);
                        cambiar.putExtra("Google", datos_obt);
                        cambiar.putExtra("Check", "0");
                        startActivity(cambiar);
                    }
                }
            }
        }
    }
}
