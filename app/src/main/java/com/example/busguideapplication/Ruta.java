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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

public class Ruta extends AppCompatActivity {
    private static final Object Bienvenido = 1 ;
    private static final Object TAG = Bienvenido;
    TextView paradas,numeroparadas, lugar,detectar,tiempotitulo,tiempo;
    Button buscar,cancelar;
    CheckedTextView uno,dos,tres,cuatro;
    private String mDeviceList=null;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Dispositivo Intercambiador_SC, Intercambiador_LL,LL,Rayo, Peras, Norte;
    Bundle datos, salida, valor, check;
    String datos_obt, salida_obt,valor_obt, check_obt;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mDeviceList=result.getDevice().getAddress();
            if (mDeviceList.equals(Intercambiador_SC.getDispositivo())) {
                if(!(Intercambiador_SC.disp_enc()) && Intercambiador_SC.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Objeto_sc",Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL",Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll",LL);
                    cambiar.putExtra("Objeto_rayo",Rayo);
                    cambiar.putExtra("Objeto_peras",Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Check", check_obt);
                    startActivity(cambiar);
                }
            }
            if(mDeviceList.equals(Peras.getDispositivo())){
                if(!(Peras.disp_enc()) && Peras.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Objeto_sc", Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL", Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll", LL);
                    cambiar.putExtra("Objeto_rayo", Rayo);
                    cambiar.putExtra("Objeto_peras", Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Check", check_obt);
                    startActivity(cambiar);
                }
            }
            if(mDeviceList.equals(Rayo.getDispositivo())){
                if(!(Rayo.disp_enc()) && Rayo.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Objeto_sc", Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL", Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll", LL);
                    cambiar.putExtra("Objeto_rayo", Rayo);
                    cambiar.putExtra("Objeto_peras", Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("Check", check_obt);
                    startActivity(cambiar);
                }
            }
            if(mDeviceList.equals(Norte.getDispositivo())){
                if(!(Norte.disp_enc())&& Norte.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("Objeto_sc", Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL", Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll", LL);
                    cambiar.putExtra("Objeto_rayo", Rayo);
                    cambiar.putExtra("Objeto_peras", Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Check", check_obt);
                    startActivity(cambiar);
                }
            }
            if(mDeviceList.equals(Intercambiador_LL.getDispositivo())){
                if(!(Intercambiador_LL.disp_enc()) && Intercambiador_LL.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("Objeto_sc", Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL", Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll", LL);
                    cambiar.putExtra("Objeto_rayo", Rayo);
                    cambiar.putExtra("Objeto_peras", Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Check", check_obt);
                    startActivity(cambiar);
                }
            }
            if (mDeviceList.equals(LL.getDispositivo())) {
                if(!(LL.disp_enc()) && LL.isEn_ruta()) {
                    Intent cambiar = new Intent(Ruta.this, Beacon.class);
                    stopScanning();
                    cambiar.putExtra("Datos", mDeviceList);
                    cambiar.putExtra("Destino", datos_obt);
                    cambiar.putExtra("Objeto_sc", Intercambiador_SC);
                    cambiar.putExtra("Objeto_IntLL", Intercambiador_LL);
                    cambiar.putExtra("Objeto_ll", LL);
                    cambiar.putExtra("Objeto_rayo", Rayo);
                    cambiar.putExtra("Objeto_peras", Peras);
                    cambiar.putExtra("Objeto_norte", Norte);
                    cambiar.putExtra("Google", valor_obt);
                    cambiar.putExtra("dialog","0");
                    cambiar.putExtra("Salida", salida_obt);
                    cambiar.putExtra("Check", check_obt);
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i(String.valueOf(getApplicationContext()),"Helloooo");
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

        datos = getIntent().getExtras();
        salida=getIntent().getExtras();
        valor = getIntent().getExtras();
        check=getIntent().getExtras();
        datos_obt= datos.getString("Datos");
        salida_obt=salida.getString("Salida");
        valor_obt = valor.getString("Google");
        check_obt = check.getString("Check");
        Intercambiador_SC = (Dispositivo)getIntent().getSerializableExtra("Objeto_sc");
        Intercambiador_LL = (Dispositivo)getIntent().getSerializableExtra("Objeto_IntLL");
        LL = (Dispositivo)getIntent().getSerializableExtra("Objeto_ll");
        Peras = (Dispositivo)getIntent().getSerializableExtra("Objeto_peras");
        Rayo=(Dispositivo)getIntent().getSerializableExtra("Objeto_rayo");
        Norte = (Dispositivo)getIntent().getSerializableExtra("Objeto_norte");

        buscar= findViewById(R.id.buscar);
        stopScanning();
        startScanning();

        paradas = findViewById(R.id.paradas);
        numeroparadas = findViewById(R.id.numeroparadas);
        lugar =findViewById(R.id.lugar);
        cancelar =findViewById(R.id.cancelar);
        detectar = findViewById(R.id.detectar);
        tiempotitulo=findViewById(R.id.tiempotitulo);
        tiempo=findViewById(R.id.tiempo);
        uno=findViewById(R.id.Uno);
        tres=findViewById(R.id.Tres);
        dos=findViewById(R.id.Dos);
        cuatro=findViewById(R.id.Cuatro);

        lugar.setText(salida_obt + "-" + datos_obt);
        if((salida_obt.equals(Peras.getNombre()) || salida_obt.equals(Intercambiador_LL.getNombre()) || salida_obt.equals(LL.getNombre()))
                && datos_obt.equals(Rayo.getNombre())){
            uno.setText("No se baje en Calle La Laguna Nº1, continue hasta la siguiente parada");
            LL.setEn_ruta(true);
            dos.setText("No se baje en Calle Las Peras, continue hasta la siguiente parada");
            Peras.setEn_ruta(true);
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            Rayo.setEn_ruta(true);
            cuatro.setVisibility(View.GONE);
            numeroparadas.setText("3");
            tiempo.setText("30 minutos");
            if(check_obt.equals("1")){
                uno.setChecked(true);
                numeroparadas.setText("2");
                tiempo.setText("20 minutos");
            }else{
                if(check_obt.equals("2")){
                    uno.setChecked(true);
                    dos.setChecked(true);
                    numeroparadas.setText("1");
                    tiempo.setText("10 minutos");
                }
            }
            if(salida_obt.equals(LL.getNombre())){
                LL.setEn_ruta(false);
                numeroparadas.setText("2");
                tiempo.setText("20 minutos");
                uno.setText(dos.getText());
                dos.setText(tres.getText());
                tres.setVisibility(View.GONE);
                if(check_obt.equals("1")){
                    uno.setChecked(true);
                    numeroparadas.setText("1");
                    tiempo.setText("10 minutos");
                }
            }else{
                Peras.setEn_ruta(false);
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
                uno.setText(tres.getText());
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
            }
        }else{
            if(((salida_obt.equals(Intercambiador_SC.getNombre()) || (salida_obt.equals(Norte.getNombre()))) && datos_obt.equals(Rayo.getNombre()))){
                uno.setText("No se baje en Autopista Norte, continue hasta la siguiente parada");
                Norte.setEn_ruta(true);
                dos.setText("Baje en Calle Las Peras, y coja la guagua número 8");
                Peras.setEn_ruta(true);
                tres.setText("Bajese en la parada Destino: " + datos_obt);
                Rayo.setEn_ruta(true);
                cuatro.setVisibility(View.GONE);
                numeroparadas.setText("3");
                tiempo.setText("50 minutos");

                if(check_obt.equals("1")){
                    uno.setChecked(true);
                    numeroparadas.setText("2");
                    tiempo.setText("20 minutos");
                }else{
                    if(check_obt.equals("2")){
                        uno.setChecked(true);
                        dos.setChecked(true);
                        numeroparadas.setText("1");
                        tiempo.setText("10 minutos");
                    }
                }
                if(salida_obt.equals(Norte.getNombre())){
                    Norte.setEn_ruta(false);
                    numeroparadas.setText("2");
                    tiempo.setText("20 minutos");
                    uno.setText(dos.getText());
                    dos.setText(tres.getText());
                    tres.setVisibility(View.GONE);
                    if(check_obt.equals("1")){
                        numeroparadas.setText("1");
                        uno.setChecked(true);
                        tiempo.setText("10 minutos");
                    }
                }
            }
        }
        if(datos_obt.equals(LL.getNombre())){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            LL.setEn_ruta(true);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(Intercambiador_LL.getNombre()) || salida_obt.equals(Rayo.getNombre()) || salida_obt.equals(LL.getNombre())){
                uno.setText(tres.getText());
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
            }else{
                if(salida_obt.equals(Intercambiador_SC.getNombre()) || salida_obt.equals(Norte.getNombre())){
                    uno.setText("No se baje en Autopista Norte.");
                    Norte.setEn_ruta(true);
                    dos.setText("Bajese en calle Las Peras Nº7 y coja la guagua 305");
                    Peras.setEn_ruta(true);
                    tres.setVisibility(View.VISIBLE);
                    numeroparadas.setText("3");
                    tiempo.setText("50 minutos");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                        numeroparadas.setText("2");
                        tiempo.setText("20 minutos");
                    }else{
                        if(check_obt.equals("2")){
                            uno.setChecked(true);
                            dos.setChecked(true);
                            numeroparadas.setText("1");
                            tiempo.setText("10 minutos");
                        }
                    }
                    if(salida_obt.equals(Norte.getNombre())){
                        Norte.setEn_ruta(false);
                        uno.setText(dos.getText());
                        Peras.setEn_ruta(true);
                        dos.setText(tres.getText());
                        tres.setVisibility(View.GONE);
                        numeroparadas.setText("2");
                        tiempo.setText("20 minutos");
                        if(check_obt.equals("2")){
                            uno.setChecked(true);
                            numeroparadas.setText("1");
                            tiempo.setText("10 minutos");
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Peras.getNombre())){
            dos.setText("Bajese en la parada Destino: " + datos_obt);
            Peras.setEn_ruta(true);
            tres.setVisibility(View.GONE);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(Rayo.getNombre()) || salida_obt.equals(LL.getNombre()) || salida_obt.equals(Norte.getNombre())){
                uno.setText(dos.getText());
                dos.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                uno.setText("No se baje en " + Norte.getNombre());
                Norte.setEn_ruta(true);
                tiempo.setText("40 minutos");
                numeroparadas.setText("2");
                if(check_obt.equals("1")){
                    uno.setChecked(true);
                    numeroparadas.setText("1");
                    tiempo.setText("10 minutos");
                }
                if(salida_obt.equals(Intercambiador_LL.getNombre())){
                    tiempo.setText("20 minutos");
                    Norte.setEn_ruta(false);
                    LL.setEn_ruta(true);
                    uno.setText("No se baje en " + LL.getNombre());
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                        numeroparadas.setText("1");
                        tiempo.setText("10 minutos");
                    }
                }
            }
        }
        if(datos_obt.equals(Intercambiador_LL.getNombre())){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            Intercambiador_LL.setEn_ruta(true);
            if(salida_obt.equals(LL.getNombre())){
                uno.setText(cuatro.getText());
                dos.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras.getNombre())){
                    uno.setText("No se baje en " + LL.getNombre());
                    LL.setEn_ruta(true);
                    dos.setText(cuatro.getText());
                    cuatro.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                        numeroparadas.setText("1");
                        tiempo.setText("10 minutos");
                    }
                }else{
                    if(salida_obt.equals(Norte.getNombre()) || (salida_obt.equals(Rayo.getNombre()))){
                        uno.setText("No se baje en " + Peras.getNombre());
                        Peras.setEn_ruta(true);
                        dos.setText("No se baje en " + LL.getNombre());
                        LL.setEn_ruta(true);
                        tres.setText(cuatro.getText());
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                            numeroparadas.setText("2");
                            tiempo.setText("20 minutos");
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                                numeroparadas.setText("1");
                                tiempo.setText("10 minutos");
                            }
                        }
                        if(salida_obt.equals(Norte.getNombre())){
                            uno.setText("Bajese en " + Peras.getNombre() + " y coja la guagua numero 308");
                            LL.setEn_ruta(false);
                        }
                    }else{
                        if(salida_obt.equals(Intercambiador_SC.getNombre())){
                            uno.setText("No se baje en " + Norte.getNombre());
                            Norte.setEn_ruta(true);
                            dos.setText("Bajese en " + Peras.getNombre() + " y coja la guagua numero 308");
                            Peras.setEn_ruta(true);
                            tres.setText("No se baje en " + LL.getNombre());
                            LL.setEn_ruta(true);
                            cuatro.setVisibility(View.VISIBLE);
                            tiempo.setText("1 hora");
                            numeroparadas.setText("4");
                            if(check_obt.equals("1")){
                                uno.setChecked(true);
                                numeroparadas.setText("3");
                                tiempo.setText("30 minutos");
                            }else{
                                if(check_obt.equals("2")){
                                    uno.setChecked(true);
                                    dos.setChecked(true);
                                    numeroparadas.setText("2");
                                    tiempo.setText("20 minutos");
                                }else{
                                    if(check_obt.equals("3")){
                                        uno.setChecked(true);
                                        dos.setChecked(true);
                                        tres.setChecked(true);
                                        numeroparadas.setText("1");
                                        tiempo.setText("10 minutos");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Norte.getNombre())){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            Norte.setEn_ruta(true);
            if(salida_obt.equals(Intercambiador_SC.getNombre()) || salida_obt.equals(Peras.getNombre())){
                uno.setText(tres.getText());
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tiempo.setText("30 minutos");
                numeroparadas.setText("1");
                if(salida_obt.equals(Peras)){
                    tiempo.setText("10 minutos");
                }
            }else{
                if(salida_obt.equals(Rayo.getNombre()) || (salida_obt.equals(LL.getNombre()))){
                    uno.setText("Bajese en la parada " + Peras.getNombre() + " y coja la guagua 18");
                    Peras.setEn_ruta(true);
                    dos.setText(tres.getText());
                    tres.setVisibility(View.GONE);
                    cuatro.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                        numeroparadas.setText("1");
                        tiempo.setText("10 minutos");
                    }
                }else{
                    if(salida_obt.equals(Intercambiador_LL.getNombre())){
                        uno.setText("No se baje en la parada "+ LL.getNombre());
                        LL.setEn_ruta(true);
                        dos.setText("Bajese en la parada " + Peras.getNombre() + " y coja la guagua 18");
                        Peras.setEn_ruta(true);
                        tres.setVisibility(View.VISIBLE);
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                            numeroparadas.setText("2");
                            tiempo.setText("20 minutos");
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                                numeroparadas.setText("1");
                                tiempo.setText("10 minutos");
                            }
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Intercambiador_SC.getNombre())){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            Intercambiador_SC.setEn_ruta(true);
            if(salida_obt.equals(Norte.getNombre())){
                uno.setText(cuatro.getText());
                dos.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("30 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras.getNombre())){
                    uno.setText("No se baje en la parada " + Norte.getNombre());
                    Norte.setEn_ruta(true);
                    dos.setText(cuatro.getText());
                    cuatro.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("40 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                        numeroparadas.setText("1");
                        tiempo.setText("10 minutos");
                    }
                }else{
                    if(salida_obt.equals(Rayo.getNombre()) || (salida_obt.equals(LL.getNombre()))){
                        uno.setText("Bajese en la parada " + Peras.getNombre() + " y coja la guagua 18");
                        Peras.setEn_ruta(true);
                        dos.setText("No se baje en la parada " + Norte.getNombre());
                        Norte.setEn_ruta(true);
                        tres.setText(cuatro.getText());
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("50 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                            numeroparadas.setText("2");
                            tiempo.setText("20 minutos");
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                                numeroparadas.setText("1");
                                tiempo.setText("10 minutos");
                            }
                        }
                    }else{
                        if(salida_obt.equals(Intercambiador_LL.getNombre())) {
                            uno.setText("No se baje en la parada" + LL.getNombre());
                            LL.setEn_ruta(true);
                            dos.setText("Bajese en la parada " + Peras.getNombre() + " y coja la guagua 18");
                            Peras.setEn_ruta(true);
                            tres.setText("No se baje en la parada " + Norte.getNombre());
                            Norte.setEn_ruta(true);
                            cuatro.setVisibility(View.VISIBLE);
                            tiempo.setText("1 hora");
                            numeroparadas.setText("4");
                            if (check_obt.equals("1")) {
                                uno.setChecked(true);
                                numeroparadas.setText("3");
                                tiempo.setText("30 minutos");
                            } else {
                                if (check_obt.equals("2")) {
                                    uno.setChecked(true);
                                    dos.setChecked(true);
                                    numeroparadas.setText("2");
                                    tiempo.setText("20 minutos");
                                } else {
                                    if (check_obt.equals("3")) {
                                        uno.setChecked(true);
                                        dos.setChecked(true);
                                        tres.setChecked(true);
                                        numeroparadas.setText("1");
                                        tiempo.setText("10 minutos");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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
        Intent cambiar= new Intent(Ruta.this,Inicio.class);
        cambiar.putExtra("Google", valor_obt);
        cambiar.putExtra("dialog","0");
        finish();
        startActivity(cambiar);
    }
}
