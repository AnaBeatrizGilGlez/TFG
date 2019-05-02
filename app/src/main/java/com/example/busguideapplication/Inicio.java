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
import android.widget.*;

public class Inicio extends AppCompatActivity {
    private static final String TAG = "Inicio" ;
    Spinner combolugares,salida_personas;
    Button buscar;
    ImageView imagen;
    String aux,aux_salida;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothdevice;
    private String mDeviceList=null;
    String inicializar="null";
    ArrayAdapter<CharSequence> adapter;
    TextView start;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        BluetoothDevice device;
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"Bluetoothx2");
            String action=intent.getAction();
            if (mBluetoothdevice.ACTION_FOUND.equals(action)) {
                Log.i(TAG,"Bluetooth");
                device = intent.getParcelableExtra(mBluetoothdevice.EXTRA_DEVICE);
                mDeviceList=device.getAddress();
                if (mDeviceList.equals("FC:23:60:ED:0B:B7")) {
                    inicializar="Calle La Laguna Nº1";
                }
                if(mDeviceList.equals("CC:F7:38:83:39:83")){
                    inicializar="Intercambiador La Laguna";
                }
                if(mDeviceList.equals("E2:C3:B1:E0:2D:8B")){
                    inicializar="Calle Las peras Nº7";
                }
                if(mDeviceList.equals("C7:9B:B3:C7:B0:88")){
                    inicializar="Intercambiador Santa Cruz";
                }
                if(mDeviceList.equals("E3:10:F4:C0:4F:0E")){
                    inicializar="Autopista Norte";
                }
                if(mDeviceList.equals("E1:FF:56:62:7F:F3")){
                    inicializar="Dulceria el Rayo";
                }
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        combolugares= findViewById(R.id.spinnerlugares);
        salida_personas=findViewById(R.id.salida_persona);
        imagen = findViewById(R.id.imagen);
        buscar=findViewById(R.id.buscar);
        start=findViewById(R.id.Start);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        getBaseContext().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

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

    public void Configurar(View view){
        startActivity(new Intent(Inicio.this,Configuracion.class));
    }

    public void Buscar_ruta(View view){
        if(aux.equals("Seleccione lugar")){
            Toast.makeText(getApplicationContext(), "Seleccione un lugar destino por favor",
                    Toast.LENGTH_SHORT).show();
        }else{
            if (inicializar.equals( "null")) {
                if(aux_salida.equals("Seleccione lugar")){
                    Toast.makeText(getApplicationContext(), "Por favor seleccione un lugar de salida. Ya que no se ha detectado ninguna parada",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(aux.equals(aux_salida)){
                        Toast.makeText(getApplicationContext(), "La parada destino es igual a la parada salida",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Intent cambiar = new Intent(Inicio.this, Ruta.class);
                        mBluetoothAdapter.cancelDiscovery();
                        cambiar.putExtra("Salida", aux_salida);
                        cambiar.putExtra("Datos", aux);
                        startActivity(cambiar);
                        //finish();
                    }
                }
            }else {
                salida_personas.setVisibility(View.GONE);
                start.setVisibility(View.GONE);
                if (aux.equals(inicializar)) {
                    Toast.makeText(getApplicationContext(), "Usted se encuentra ya en esta parada destino. Seleccione otra parada destino por favor",
                            Toast.LENGTH_SHORT).show();
                }else{
                    mBluetoothAdapter.cancelDiscovery();
                    Intent cambiar = new Intent(Inicio.this, Ruta.class);
                    cambiar.putExtra("Salida", inicializar);
                    cambiar.putExtra("Datos", aux);
                    startActivity(cambiar);
                    //finish();
                }
            }
        }
    }
}
