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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Connexion extends AppCompatActivity {
    Button buscar;
    private static final Object Conexion = 1;
    private static final Object TAG = Conexion;
    ListView listview;
    private ArrayList<String> mDeviceList = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothdevice;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        BluetoothDevice device;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.i(String.valueOf(getBaseContext()), "Bluetooth");
            if (mBluetoothdevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(mBluetoothdevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName()+"\n" + device.getAddress());
                Log.i(String.valueOf(getBaseContext()), "Blue" + device);
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
        setContentView(R.layout.connexion);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter!=null){
            if(mBluetoothAdapter.isEnabled()){
                Toast.makeText(Connexion.this, "Bluetooth ya activo", Toast.LENGTH_LONG).show();
                Log.i(String.valueOf(getBaseContext()), "Bluetoothhh");
            }else{
                mBluetoothAdapter.enable();
            }
        }

        mBluetoothAdapter.startDiscovery();
        getApplicationContext().getApplicationContext().registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));;

        buscar = findViewById(R.id.buscar);
    }

    public void InicioBusqueda(View view){
        listview = findViewById(R.id.listview);
        listview.setAdapter(new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, mDeviceList));
    }
}
