package com.example.busguideapplication;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Inicio extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Spinner combolugares,salida_personas;
    Button buscar;
    TextView nombre_perfil;
    private GoogleApiClient googleApiClient;
    ImageView confi,foto_perfil;
    String aux,aux_salida;
    Integer aux_no=0;
    private String mDeviceList=null;
    String inicializar="null";
    Bundle datos,dialog;
    String datos_obt,dialog_obt;
    ArrayAdapter<CharSequence> adapter;
    TextView start;
    Dispositivo LL = new Dispositivo("FC:23:60:ED:0B:B7", "Calle La Laguna Nº1");
    Dispositivo Intercambiador_LL = new Dispositivo("CC:F7:38:83:39:83", "Intercambiador La Laguna");
    Dispositivo Peras= new Dispositivo("E2:C3:B1:E0:2D:8B", "Calle Las peras Nº7");
    Dispositivo Rayo = new Dispositivo("E1:FF:56:62:7F:F3", "Dulceria el Rayo");
    Dispositivo Norte = new Dispositivo("E3:10:F4:C0:4F:0E","Autopista Norte");
    Dispositivo Intercambiador_SC = new Dispositivo("C7:9B:B3:C7:B0:88", "Intercambiador Santa Cruz");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mDeviceList=result.getDevice().getAddress();
            Log.i(String.valueOf(getApplicationContext()), "Inicio " + mDeviceList);
            if (mDeviceList.equals(LL.getDispositivo())) {
                stopScanning();
                inicializar=LL.getNombre();
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals(Intercambiador_LL.getDispositivo())){
                stopScanning();
                inicializar=Intercambiador_LL.getNombre();
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals(Peras.getDispositivo())){
                stopScanning();
                inicializar=Peras.getNombre();
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals(Intercambiador_SC.getDispositivo())){
                stopScanning();
                inicializar=Intercambiador_SC.getNombre();
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals(Norte.getDispositivo())){
                stopScanning();
                inicializar=Norte.getNombre();
                aux_no=1;
                Alerta();
            }
            if(mDeviceList.equals(Rayo.getDispositivo())){
                stopScanning();
                inicializar=Rayo.getNombre();
                aux_no=1;
                Alerta();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(String.valueOf(getApplicationContext()), "Inicionesee " );
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i(String.valueOf(getApplicationContext()), "Inicione " + errorCode);
            super.onScanFailed(errorCode);
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        datos = getIntent().getExtras();
        dialog = getIntent().getExtras();
        foto_perfil=findViewById(R.id.foto_perfil);
        datos_obt= datos.getString("Google");
        dialog_obt=dialog.getString("dialog");

        if(dialog_obt.equals("1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Si usted activa la vibración y pone sonido a su teléfono el móvil le avisará de todo, sin necesidad de estar usted pendiente");
            builder.setNegativeButton(R.string.aceptar, null);
            Dialog dialog = builder.create();
            dialog.show();
        }

        if(datos_obt.equals("0")) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    setUserData(user);
                }else{
                    goLogInscreen();
                }
            }
        };

        combolugares= findViewById(R.id.spinnerlugares);
        salida_personas=findViewById(R.id.salida_persona);
        confi = findViewById(R.id.confi);
        buscar=findViewById(R.id.buscar);
        start=findViewById(R.id.Start);
        nombre_perfil=findViewById(R.id.nombre_perfil);
        startScanning();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            if (datos_obt.equals("1")) {
                if(!(user.getDisplayName() ==null)) {
                    nombre_perfil.setText("Bienvenido/a " + user.getDisplayName());
                }else{
                    nombre_perfil.setText("Bienvenido/a " + user.getEmail());
                }
                if(!(user.getPhotoUrl() ==null)){
                    foto_perfil.setImageURI(user.getPhotoUrl());
                    foto_perfil.setVisibility(View.VISIBLE);
                }else{
                    foto_perfil.setVisibility(View.GONE);
                }
            } else {
                confi.setVisibility(View.GONE);
                setUserData(user);
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

    private void setUserData(FirebaseUser user){
        nombre_perfil.setText("Bienvenido/a " + user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(foto_perfil);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();

        if(firebaseAuthListener!=null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    private void goLogInscreen(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    public void startScanning() {
        System.out.println("start scanning");
        Log.i(String.valueOf(getApplicationContext()), "Inicio");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(String.valueOf(getApplicationContext()), "Iniciote "); btScanner.startScan(leScanCallback);
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
            finish();
            startActivity(new Intent(Inicio.this, MainActivity.class));
        }else{
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        goLogInscreen();
                    }else{
                        Toast.makeText(getApplicationContext(), "No se puede cerrar sesión", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void Configurar(View view){
        finish();
        Intent otro=new Intent(Inicio.this,Configuracion.class);
        otro.putExtra("dialog","0");
        finish();
        startActivity(otro);
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
                    cambiar.putExtra("Objeto_sc",Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL",Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll",LL);
                    cambiar.putExtra("Objeto_rayo",Rayo);
                    cambiar.putExtra("Objeto_peras",Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Salida", aux_salida);
                    cambiar.putExtra("Datos", aux);
                    cambiar.putExtra("Google",datos_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Check","0");
                    finish();
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
                                cambiar.putExtra("Objeto_sc",Intercambiador_SC);
                                cambiar.putExtra("Objeto_IntLL",Intercambiador_LL);
                                cambiar.putExtra("Objeto_ll",LL);
                                cambiar.putExtra("Objeto_rayo",Rayo);
                                cambiar.putExtra("Objeto_peras",Peras);
                                cambiar.putExtra("Objeto_norte", Norte);
                                cambiar.putExtra("Salida", inicializar);
                                cambiar.putExtra("Datos", aux);
                                cambiar.putExtra("Google", datos_obt);
                                cambiar.putExtra("Check", "0");
                                finish();
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
                        cambiar.putExtra("Objeto_sc",Intercambiador_SC);
                        cambiar.putExtra("Objeto_IntLL",Intercambiador_LL);
                        cambiar.putExtra("Objeto_ll",LL);
                        cambiar.putExtra("Objeto_rayo",Rayo);
                        cambiar.putExtra("Objeto_peras",Peras);
                        cambiar.putExtra("Objeto_norte", Norte);
                        cambiar.putExtra("Salida", aux_salida);
                        cambiar.putExtra("Datos", aux);
                        Log.i(String.valueOf(Inicio.this),aux);
                        cambiar.putExtra("Google",datos_obt);
                        cambiar.putExtra("Check","0");
                        finish();
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
                        cambiar.putExtra("Objeto",Intercambiador_SC);
                        cambiar.putExtra("Salida", inicializar);
                        cambiar.putExtra("Objeto_sc",Intercambiador_SC);
                        cambiar.putExtra("Objeto_IntLL",Intercambiador_LL);
                        cambiar.putExtra("Objeto_ll",LL);
                        cambiar.putExtra("Objeto_rayo",Rayo);
                        cambiar.putExtra("Objeto_peras",Peras);
                        cambiar.putExtra("Objeto_norte", Norte);
                        cambiar.putExtra("Datos", aux);
                        cambiar.putExtra("Google", datos_obt);
                        cambiar.putExtra("Check", "0");
                        finish();
                        startActivity(cambiar);
                    }
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
