package com.example.busguideapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Spinner combolugares,salida_personas;
    Button buscar, favoritos;
    TextView nombre_perfil;
    private GoogleApiClient googleApiClient;
    ImageView confi,foto_perfil;
    String aux,aux_salida;
    String mDeviceList = "null";
    String inicializar="null";
    Bundle datos,dialog;
    String datos_obt,dialog_obt;
    ArrayAdapter<CharSequence> adapter;
    TextView start;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebasedata;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    mDeviceList = result.getDevice().getAddress();
                    Log.i(String.valueOf(getApplicationContext()), mDeviceList);
                    List<String> direcciones = new ArrayList<>();
                    List<String> nombre_direcciones = new ArrayList<>();
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    for (DataSnapshot note : dataSnapshot.child("Beacons").getChildren()) {
                        direcciones.add(note.child("direccion").getValue().toString());
                        nombre_direcciones.add(note.child("nombre").getValue().toString());
                    }

                    if (dataSnapshot.exists()) {
                        for(int i=0;i<direcciones.size();i++) {
                            if (mDeviceList.equals(direcciones.get(i))) {
                                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("inicializar").setValue(nombre_direcciones.get(i));
                                Log.i(String.valueOf(getApplicationContext()), "Hi");
                                stopScanning();
                                Funcion();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
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

    private void Funcion() {
        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (!dataSnapshot.child("Usuarios").child(user.getUid()).child("inicializar").getValue().equals("nothing")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Inicio.this);
                    builder.setMessage("¿Quiere usted salir de la parada " + dataSnapshot.child("Usuarios").child(user.getUid()).child("inicializar").getValue() + "?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    salida_personas.setVisibility(View.GONE);
                                    start.setVisibility(View.GONE);
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Salida").setValue(dataSnapshot.child("Usuarios").child(user.getUid()).child("inicializar").getValue());
                                    if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals("Seleccione lugar")) {
                                        Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                                                Toast.LENGTH_SHORT).show();
                                        Log.i(String.valueOf(getApplicationContext()), "Yes");
                                        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("0");
                                    } else {
                                        if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals(inicializar)) {
                                            Toast.makeText(getApplicationContext(), "Usted se encuentra ya en esta parada destino. Seleccione otra parada destino por favor",
                                                    Toast.LENGTH_SHORT).show();
                                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("0");
                                        } else {
                                            List<String> hijo = new ArrayList<>();
                                            String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                                            String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();
                                            for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                                                String paradas_checki = note.getKey();
                                                hijo.add(paradas_checki);
                                            }

                                            for (int i = 0; i < hijo.size(); i++) {
                                                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").child(hijo.get(i)).child("check").setValue("false");
                                            }

                                            Intent cambiar = new Intent(Inicio.this, Ruta.class);
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
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("0");
                                    if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals("Seleccione lugar")) {
                                        Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().equals("Seleccione lugar")) {
                                            Toast.makeText(getApplicationContext(), "Por favor seleccione un lugar de salida.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals(dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue())) {
                                                Toast.makeText(getApplicationContext(), "La parada destino es igual a la parada salida",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                List<String> hijo = new ArrayList<>();
                                                String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                                                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();
                                                for (DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()) {
                                                    String paradas_checki = note.getKey();
                                                    hijo.add(paradas_checki);
                                                }

                                                for (int i = 0; i < hijo.size(); i++) {
                                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").child(hijo.get(i)).child("check").setValue("false");
                                                }
                                                Intent cambiar = new Intent(Inicio.this, Ruta.class);
                                                cambiar.putExtra("Google", datos_obt);
                                                cambiar.putExtra("Check", "0");
                                                finish();
                                                startActivity(cambiar);
                                            }
                                        }
                                    }
                                }
                            }).create();
                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        mFirebasedata = FirebaseDatabase.getInstance();
        mDatabase = mFirebasedata.getReference();
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("0");
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("inicializar").setValue("nothing");
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Destino").setValue("Seleccione lugar");
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Salida").setValue("Seleccione lugar");

        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> direcciones = new ArrayList<>();
                List<String> nombre_direcciones = new ArrayList<>();
                for (DataSnapshot note : dataSnapshot.child("Beacons").getChildren()) {
                    direcciones.add(note.child("direccion").getValue().toString());
                    nombre_direcciones.add(note.child("nombre").getValue().toString());
                    Log.i(String.valueOf(getApplicationContext()),"Direcciones" + direcciones + "Nombre " + nombre_direcciones);
                }

                for (int i = 0; i < direcciones.size(); i++) {
                    Log.i(String.valueOf(getApplicationContext()),"ENTRA");
                    String nombre = dataSnapshot.child("Disp-Nombre").child(nombre_direcciones.get(i)).getValue().toString();
                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Beacons").child(nombre).child("encontrado").setValue(false);
                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Beacons").child(nombre).child("ruta").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabase.child("Dispositivos").child("Array_dest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> dest = new ArrayList<String>();
                String[] list;
                if(dataSnapshot.exists()){
                    for(DataSnapshot note : dataSnapshot.getChildren()){
                        dest.add(note.getValue().toString());
                    }

                    list = new String[dest.size()];

                    for(int i = 0 ;i<dest.size();i++) {
                        String aux = dest.get(i);
                        list[i] = aux;
                    }

                    adapter = new ArrayAdapter<CharSequence>(Inicio.this,android.R.layout.simple_spinner_item,list);
                    salida_personas.setAdapter(adapter);
                    salida_personas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            aux_salida = parent.getItemAtPosition(position).toString();
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Salida").setValue(aux_salida);
                        }

                        public void onNothingSelected(AdapterView<?> parent) { }
                    });
                    combolugares.setAdapter(adapter);
                    combolugares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            aux = parent.getItemAtPosition(position).toString();
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Destino").setValue(aux);
                        }

                        public void onNothingSelected(AdapterView<?> parent) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        stopScanning();
        startScanning();

        datos = getIntent().getExtras();
        dialog = getIntent().getExtras();
        foto_perfil=findViewById(R.id.foto_perfil);
        datos_obt= datos.getString("Google");
        dialog_obt=dialog.getString("dialog");

        if(dialog_obt.equals("1")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Si usted activa la vibración y pone sonido a su teléfono el móvil le avisará de todo, sin necesidad de estar usted pendiente");
            builder.setNegativeButton(R.string.aceptar, null);
            builder.create();
            builder.show();
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
        favoritos = findViewById(R.id.favoritos);
        start=findViewById(R.id.Start);
        nombre_perfil=findViewById(R.id.nombre_perfil);

        if(user!=null) {
            if (datos_obt.equals("1")) {
                if(!(user.getDisplayName() == null)) {
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
                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("nombre").setValue(user.getDisplayName());
                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("correo").setValue(user.getEmail());
            } else {
                confi.setVisibility(View.GONE);
                setUserData(user);
            }
        }

        startScanning();
    }

    private void setUserData(FirebaseUser user){
        nombre_perfil.setText("Bienvenido/a " + user.getDisplayName());
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("nombre").setValue(user.getDisplayName());
        mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("correo").setValue(user.getEmail());
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

    public void Buscar_ruta(View view){
        mDatabase.child("Dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals("Seleccione lugar")){
                    Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if (dataSnapshot.child("Usuarios").child(user.getUid()).child("inicializar").getValue().equals("nothing") || !dataSnapshot.child("Usuarios").child(user.getUid()).child("aux_no").getValue().equals("0")) {  //Cambiar aux
                        if(dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().equals("Seleccione lugar")){
                            Toast.makeText(getApplicationContext(), "Por favor seleccione un lugar de salida. Ya que no se ha detectado ninguna parada",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            if(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals(aux_salida)){
                                Toast.makeText(getApplicationContext(), "La parada destino es igual a la parada salida",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                List<String> hijo = new ArrayList<>();
                                String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();
                                for(DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()){
                                    String paradas_checki = note.getKey();
                                    hijo.add(paradas_checki);
                                }

                                for(int i=0;i<hijo.size();i++) {
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").child(hijo.get(i)).child("check").setValue("false");
                                }
                                Intent cambiar = new Intent(Inicio.this, Ruta.class);
                                cambiar.putExtra("Google",datos_obt);
                                cambiar.putExtra("Check","0");
                                finish();
                                startActivity(cambiar);
                            }
                        }
                    }else{
                        if(dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals("Seleccione lugar")){
                            Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                                    Toast.LENGTH_SHORT).show();
                            mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("1");
                        }else {
                            if (dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().equals(inicializar)) {
                                Toast.makeText(getApplicationContext(), "Usted se encuentra ya en esta parada destino. Seleccione otra parada destino por favor",
                                        Toast.LENGTH_SHORT).show();
                                mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("aux_no").setValue("1");
                            } else {
                                List<String> hijo = new ArrayList<>();
                                String salida = dataSnapshot.child("Usuarios").child(user.getUid()).child("Salida").getValue().toString();
                                String destino = dataSnapshot.child("Usuarios").child(user.getUid()).child("Destino").getValue().toString();
                                for(DataSnapshot note : dataSnapshot.child("Rutas").child(salida).child(destino).child("Paradas").getChildren()){
                                    String paradas_checki = note.getKey();
                                    hijo.add(paradas_checki);
                                }

                                for(int i=0;i<hijo.size();i++) {
                                    mDatabase.child("Dispositivos").child("Usuarios").child(user.getUid()).child("Rutas").child(salida).child(destino).child("Paradas").child(hijo.get(i)).child("check").setValue("false");
                                }
                                Intent cambiar = new Intent(Inicio.this, Ruta.class);
                                cambiar.putExtra("Google",datos_obt);
                                cambiar.putExtra("Check","0");
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

    public void Favoritos(View view){
        Intent cambiar = new Intent(Inicio.this,Favorito.class);
        cambiar.putExtra("Google",datos_obt);
        finish();
        startActivity(cambiar);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

