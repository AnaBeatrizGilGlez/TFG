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
import android.widget.CheckBox;
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
    Bundle datos, salida, valor, check;
    String datos_obt, salida_obt,valor_obt, check_obt;
    String Dulceria="Dulceria el Rayo";
    String LL="Intercambiador La Laguna";
    String Norte = "Autopista Norte";
    String Calle_LL="Calle La Laguna Nº1";
    String Peras="Calle Las peras Nº7";
    String SC="Intercambiador Santa Cruz";

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mDeviceList=result.getDevice().getAddress();
            if (mDeviceList.equals("FC:23:60:ED:0B:B7")) {
                Intent cambiar = new Intent(Ruta.this,Beacon.class);
                stopScanning();
                cambiar.putExtra("Datos",mDeviceList);
                cambiar.putExtra("Destino",datos_obt);
                cambiar.putExtra("Salida",salida_obt);
                cambiar.putExtra("Google",valor_obt);
                cambiar.putExtra("Check",check_obt);
                startActivity(cambiar);  ///PROBAR A PONERLO DESPUES DE LOS 4 IF
            }
            if(mDeviceList.equals("E2:C3:B1:E0:2D:8B")){
                Intent cambiar = new Intent(Ruta.this,Beacon.class);
                stopScanning();
                cambiar.putExtra("Datos",mDeviceList);
                cambiar.putExtra("Destino",datos_obt);
                cambiar.putExtra("Salida",salida_obt);
                cambiar.putExtra("Google",valor_obt);
                cambiar.putExtra("Check",check_obt);
                startActivity(cambiar);
            }
            if(mDeviceList.equals("E1:FF:56:62:7F:F3")){
                Intent cambiar = new Intent(Ruta.this,Beacon.class);
                stopScanning();
                cambiar.putExtra("Datos",mDeviceList);
                cambiar.putExtra("Google",valor_obt);
                cambiar.putExtra("Destino",datos_obt);
                cambiar.putExtra("Salida",salida_obt);
                cambiar.putExtra("Check",check_obt);
                startActivity(cambiar);
            }
            if(mDeviceList.equals("E3:10:F4:C0:4F:0E")){
                Intent cambiar = new Intent(Ruta.this,Beacon.class);
                stopScanning();
                cambiar.putExtra("Datos",mDeviceList);
                cambiar.putExtra("Destino",datos_obt);
                cambiar.putExtra("Google",valor_obt);
                cambiar.putExtra("Salida",salida_obt);
                cambiar.putExtra("Check",check_obt);
                startActivity(cambiar);
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

        buscar= findViewById(R.id.buscar);
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
        if((salida_obt.equals(Peras) || salida_obt.equals(LL) || salida_obt.equals(Calle_LL))
                && datos_obt.equals(Dulceria)){
            uno.setText("No se baje en Calle La Laguna Nº1, continue hasta la siguiente parada");
            dos.setText("No se baje en Calle Las Peras, continue hasta la siguiente parada");
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            cuatro.setVisibility(View.GONE);
            numeroparadas.setText("3");
            tiempo.setText("30 minutos");
            if(check_obt.equals("1")){
                uno.setChecked(true);
            }else{
                if(check_obt.equals("2")){
                    uno.setChecked(true);
                    dos.setChecked(true);
                }
            }
            if(salida_obt.equals(Calle_LL)){
                numeroparadas.setText("2");
                tiempo.setText("20 minutos");
                uno.setText(dos.getText());
                dos.setText(tres.getText());
                tres.setVisibility(View.GONE);
                if(check_obt.equals("1")){
                    uno.setChecked(true);
                }
            }else{
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
                uno.setText(tres.getText());
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
            }
        }else{
            if(((salida_obt.equals(SC) || (salida_obt.equals(Norte))) && datos_obt.equals(Dulceria))){
                uno.setText("No se baje en Autopista Norte, continue hasta la siguiente parada");
                dos.setText("Baje en Calle Las Peras, y coja la guagua número 8");
                tres.setText("Bajese en la parada Destino: " + datos_obt);
                cuatro.setVisibility(View.GONE);
                numeroparadas.setText("3");
                tiempo.setText("50 minutos");

                if(check_obt.equals("1")){
                    uno.setChecked(true);
                }else{
                    if(check_obt.equals("2")){
                        uno.setChecked(true);
                        dos.setChecked(true);
                    }
                }
                if(salida_obt.equals(Norte)){
                    numeroparadas.setText("2");
                    tiempo.setText("20 minutos");
                    uno.setText(dos.getText());
                    dos.setText(tres.getText());
                    tres.setVisibility(View.GONE);
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                    }
                }
            }
        }
        if(datos_obt.equals(Calle_LL)){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(LL) || salida_obt.equals(Dulceria)){
                uno.setText(tres.getText());
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
            }else{
                if(salida_obt.equals(SC) || salida_obt.equals(Norte)){
                    uno.setText("No se baje en Autopista Norte.");
                    dos.setText("Bajese en calle Las Peras Nº7 y coja la guagua 305");
                    tres.setVisibility(View.VISIBLE);
                    numeroparadas.setText("3");
                    tiempo.setText("50 minutos");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                    }else{
                        if(check_obt.equals("2")){
                            uno.setChecked(true);
                            dos.setChecked(true);
                        }
                    }
                    if(salida_obt.equals(Norte)){
                        uno.setText(dos.getText());
                        dos.setText(tres.getText());
                        tres.setVisibility(View.GONE);
                        numeroparadas.setText("2");
                        tiempo.setText("20 minutos");
                        if(check_obt.equals("2")){
                            uno.setChecked(true);
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Peras)){
            dos.setText("Bajese en la parada Destino: " + datos_obt);
            tres.setVisibility(View.GONE);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(Dulceria) || salida_obt.equals(Calle_LL) || salida_obt.equals(Norte)){
                uno.setText(dos.getText());
                dos.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                uno.setText("No se baje en " + Norte);
                tiempo.setText("40 minutos");
                numeroparadas.setText("2");
                if(check_obt.equals("1")){
                    uno.setChecked(true);
                }
                if(salida_obt.equals(LL)){
                    tiempo.setText("20 minutos");
                    uno.setText("No se baje en " + Calle_LL);
                }
            }
        }
        if(datos_obt.equals(LL)){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(Calle_LL)){
                uno.setText(cuatro.getText());
                dos.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras)){
                    uno.setText("No se baje en " + Calle_LL);
                    dos.setText(cuatro.getText());
                    cuatro.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                    }
                }else{
                    if(salida_obt.equals(Norte) || (salida_obt.equals(Dulceria))){
                        uno.setText("No se baje en " + Peras);
                        dos.setText("No se baje en " + Calle_LL);
                        tres.setText(cuatro.getText());
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                            }
                        }
                        if(salida_obt.equals(Norte)){
                            uno.setText("Bajese en " + Peras + "y coja la guagua numero 308");
                        }
                    }else{
                        if(salida_obt.equals(SC)){
                            uno.setText("No se baje en " + Norte);
                            dos.setText("Bajese en " + Peras + " y coja la guagua numero 308");
                            tres.setText("No se baje en " + Calle_LL);
                            cuatro.setVisibility(View.VISIBLE);
                            tiempo.setText("1 hora");
                            numeroparadas.setText("4");
                            if(check_obt.equals("1")){
                                uno.setChecked(true);
                            }else{
                                if(check_obt.equals("2")){
                                    uno.setChecked(true);
                                    dos.setChecked(true);
                                }else{
                                    if(check_obt.equals("3")){
                                        uno.setChecked(true);
                                        dos.setChecked(true);
                                        tres.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Norte)){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(SC) || salida_obt.equals(Peras)){
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
                if(salida_obt.equals(Dulceria) || (salida_obt.equals(Calle_LL))){
                    uno.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                    dos.setText(tres.getText());
                    tres.setVisibility(View.GONE);
                    cuatro.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                    }
                }else{
                    if(salida_obt.equals(LL)){
                        uno.setText("No se baje en la parada "+ Calle_LL);
                        dos.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        tres.setVisibility(View.VISIBLE);
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                            }
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(SC)){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(Norte)){
                uno.setText(cuatro.getText());
                dos.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("30 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras)){
                    uno.setText("No se baje en la parada" + Norte);
                    dos.setText(cuatro.getText());
                    cuatro.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("40 minutos");
                    numeroparadas.setText("2");
                    if(check_obt.equals("1")){
                        uno.setChecked(true);
                    }
                }else{
                    if(salida_obt.equals(Dulceria) || (salida_obt.equals(Calle_LL))){
                        uno.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        dos.setText("No se baje en la parada " + Norte);
                        tres.setText(cuatro.getText());
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("50 minutos");
                        numeroparadas.setText("3");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                            }
                        }
                    }else{
                        uno.setText("No se baje en la parada" + Calle_LL);
                        dos.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        tres.setText("No se baje en la parada " + Norte);
                        cuatro.setVisibility(View.VISIBLE);
                        tiempo.setText("1 hora");
                        numeroparadas.setText("4");
                        if(check_obt.equals("1")){
                            uno.setChecked(true);
                        }else{
                            if(check_obt.equals("2")){
                                uno.setChecked(true);
                                dos.setChecked(true);
                            }else{
                                if(check_obt.equals("3")){
                                    uno.setChecked(true);
                                    dos.setChecked(true);
                                    tres.setChecked(true);
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
                Log.i(String.valueOf(getApplicationContext()),"Hellooooo");
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
        startActivity(cambiar );
    }
}
