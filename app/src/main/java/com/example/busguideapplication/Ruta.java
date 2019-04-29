package com.example.busguideapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class Ruta extends AppCompatActivity {
    private static final Object Bienvenido = 1 ;
    private static final Object TAG = Bienvenido;
    TextView paradas,numeroparadas, lugar,detectar,tiempotitulo,tiempo;
    Button buscar,cancelar;
    CheckedTextView uno,dos,tres,cuatro;
    private String mDeviceList=null;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothdevice;
    Bundle datos, salida;
    String datos_obt, salida_obt;
    String Dulceria="Dulceria el Rayo";
    String LL="Intercambiador La Laguna";
    String Norte = "Autopista Norte";
    String Calle_LL="Calle La Laguna Nº1";
    String Peras="Calle Las peras Nº7";
    String SC="Intercambiador Santa Cruz";


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        BluetoothDevice device;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (mBluetoothdevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(mBluetoothdevice.EXTRA_DEVICE);
                mDeviceList=device.getAddress();
                if (mDeviceList.equals("FC:23:60:ED:0B:B7")) {
                    Intent cambiar = new Intent(Ruta.this,Beacon.class);
                    mBluetoothAdapter.cancelDiscovery();
                    cambiar.putExtra("Datos",mDeviceList);
                    cambiar.putExtra("Destino",datos_obt);
                    cambiar.putExtra("Salida",salida_obt);
                    startActivity(cambiar);  ///PROBAR A PONERLO DESPUES DE LOS 4 IF
                }
                if(mDeviceList.equals("E2:C3:B1:E0:2D:8B")){
                    Intent cambiar = new Intent(Ruta.this,Beacon.class);
                    mBluetoothAdapter.cancelDiscovery();
                    cambiar.putExtra("Datos",mDeviceList);
                    cambiar.putExtra("Destino",datos_obt);
                    cambiar.putExtra("Salida",salida_obt);
                    startActivity(cambiar);
                }
                if(mDeviceList.equals("E1:FF:56:62:7F:F3")){
                    Intent cambiar = new Intent(Ruta.this,Beacon.class);
                    mBluetoothAdapter.cancelDiscovery();
                    cambiar.putExtra("Datos",mDeviceList);
                    cambiar.putExtra("Destino",datos_obt);
                    cambiar.putExtra("Salida",salida_obt);
                    startActivity(cambiar);
                }
                if(mDeviceList.equals("E3:10:F4:C0:4F:0E")){
                    Intent cambiar = new Intent(Ruta.this,Beacon.class);
                    mBluetoothAdapter.cancelDiscovery();
                    cambiar.putExtra("Datos",mDeviceList);
                    cambiar.putExtra("Destino",datos_obt);
                    cambiar.putExtra("Salida",salida_obt);
                    startActivity(cambiar);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruta);

        datos = getIntent().getExtras();
        salida=getIntent().getExtras();
        datos_obt= datos.getString("Datos");
        salida_obt=salida.getString("Salida");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buscar= findViewById(R.id.buscar);

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

        uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tres.setChecked(true);
            }
        });
        dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tres.setChecked(true);
            }
        });
        tres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tres.setChecked(true);
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tres.setChecked(true);
            }
        });

        lugar.setText(salida_obt + "-" + datos_obt);
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        if((salida_obt.equals(Peras) || salida_obt.equals(LL) || salida_obt.equals(Calle_LL))
                && datos_obt.equals(Dulceria)){
            uno.setText("No se baje en Calle La Laguna Nº1, continue hasta la siguiente parada");
            uno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uno.setChecked(true);
                }
            });
            dos.setText("No se baje en Calle Las Peras, continue hasta la siguiente parada");
            dos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dos.setChecked(true);
                }
            });
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            tres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tres.setChecked(true);
                }
            });
            cuatro.setVisibility(View.GONE);
            numeroparadas.setText("3");
            tiempo.setText("30 minutos");
            if(salida_obt.equals(Calle_LL)){
                numeroparadas.setText("2");
                tiempo.setText("20 minutos");
                uno.setVisibility(View.GONE);
            }else{
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
                uno.setVisibility(View.GONE);
                dos.setVisibility(View.GONE);
            }
        }else{
            if(((salida_obt.equals(SC) || (salida_obt.equals(Norte))) && datos_obt.equals(Dulceria))){
                uno.setText("No se baje en Autopista Norte, continue hasta la siguiente parada");
                dos.setText("Baje en Calle Las Peras, y coja la guagua número 8");
                tres.setText("Bajese en la parada Destino: " + datos_obt);
                cuatro.setVisibility(View.GONE);
                numeroparadas.setText("3");
                tiempo.setText("50 minutos");
                if(salida_obt.equals(Norte)){
                    numeroparadas.setText("2");
                    tiempo.setText("20 minutos");
                    uno.setVisibility(View.GONE);
                    dos.setVisibility(View.GONE);
                }
            }
        }
        if(datos_obt.equals(Calle_LL)){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(LL) || salida_obt.equals(Dulceria)){
                uno.setVisibility(View.GONE);
                dos.setVisibility(View.GONE);
                numeroparadas.setText("1");
                tiempo.setText("10 minutos");
            }else{
                if(salida_obt.equals(SC) || salida_obt.equals(Norte)){
                    uno.setText("No se baje en Autopista Norte.");
                    dos.setText("Bajese en calle Las Peras Nº7 y coja la guagua 305");
                    numeroparadas.setText("3");
                    tiempo.setText("50 minutos");
                    if(salida_obt.equals(Norte)){
                        uno.setVisibility(View.GONE);
                        numeroparadas.setText("2");
                        tiempo.setText("20 minutos");
                    }
                }
            }
        }
        if(datos_obt.equals(Peras)){
            dos.setText("Bajese en la parada Destino: " + datos_obt);
            tres.setVisibility(View.GONE);
            cuatro.setVisibility(View.GONE);
            if(salida_obt.equals(Dulceria) || salida_obt.equals(Calle_LL) || salida_obt.equals(Norte)){
                uno.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                uno.setText("No se baje en Autopista Norte.");
                tiempo.setText("40 minutos");
                numeroparadas.setText("2");
                if(salida_obt.equals(LL)){
                    tiempo.setText("20 minutos");
                    uno.setText("No se baje en " + Calle_LL);
                }
            }
        }
        if(datos_obt.equals(LL)){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(Calle_LL)){
                uno.setVisibility(View.GONE);
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("10 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras)){
                    uno.setText("No se baje en " + Calle_LL);
                    dos.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                }else{
                    if(salida_obt.equals(Norte) || (salida_obt.equals(Dulceria))){
                        uno.setText("No se baje en " + Peras);
                        dos.setText("No se baje en " + Calle_LL);
                        tres.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                        if(salida_obt.equals(Norte)){
                            uno.setText("Bajese en " + Peras + "y coja la guagua numero 308");
                        }
                    }else{
                        if(salida_obt.equals(SC)){
                            uno.setText("No se baje en " + Norte);
                            dos.setText("Bajese en " + Peras + " y coja la guagua numero 308");
                            tres.setText("No se baje en " + Calle_LL);
                            tiempo.setText("1 hora");
                            numeroparadas.setText("4");
                        }
                    }
                }
            }
        }
        if(datos_obt.equals(Norte)){
            tres.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(SC) || salida_obt.equals(Peras)){
                uno.setVisibility(View.GONE);
                dos.setVisibility(View.GONE);
                cuatro.setVisibility(View.GONE);
                tiempo.setText("30 minutos");
                numeroparadas.setText("1");
                if(salida_obt.equals(Peras)){
                    tiempo.setText("10 minutos");
                }
            }else{
                if(salida_obt.equals(Dulceria) || (salida_obt.equals(Calle_LL))){
                    uno.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                    dos.setVisibility(View.GONE);
                    cuatro.setVisibility(View.GONE);
                    tiempo.setText("20 minutos");
                    numeroparadas.setText("2");
                }else{
                    if(salida_obt.equals(LL)){
                        uno.setText("No se baje en la parada "+ Calle_LL);
                        dos.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        cuatro.setVisibility(View.GONE);
                        tiempo.setText("30 minutos");
                        numeroparadas.setText("3");
                    }
                }
            }
        }
        if(datos_obt.equals(SC)){
            cuatro.setText("Bajese en la parada Destino: " + datos_obt);
            if(salida_obt.equals(Norte)){
                uno.setVisibility(View.GONE);
                dos.setVisibility(View.GONE);
                tres.setVisibility(View.GONE);
                tiempo.setText("30 minutos");
                numeroparadas.setText("1");
            }else{
                if(salida_obt.equals(Peras)){
                    uno.setText("No se baje en la parada" + Norte);
                    dos.setVisibility(View.GONE);
                    tres.setVisibility(View.GONE);
                    tiempo.setText("40 minutos");
                    numeroparadas.setText("2");
                }else{
                    if(salida_obt.equals(Dulceria) || (salida_obt.equals(Calle_LL))){
                        uno.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        dos.setText("No se baje en la parada " + Norte);
                        tres.setVisibility(View.GONE);
                        tiempo.setText("50 minutos");
                        numeroparadas.setText("3");
                    }else{
                        uno.setText("No se baje en la parada" + Calle_LL);
                        dos.setText("Bajese en la parada " + Peras + " y coja la guagua 18");
                        tres.setText("No se baje en la parada " + Norte);
                        tiempo.setText("1 hora");
                        numeroparadas.setText("4");
                    }
                }
            }
        }
    }

    public void Cancelar_Ruta (View view){
        startActivity(new Intent(Ruta.this,Inicio.class));
    }
}
