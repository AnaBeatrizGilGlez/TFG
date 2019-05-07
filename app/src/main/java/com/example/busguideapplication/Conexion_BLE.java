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
import android.widget.EditText;
import com.example.busguideapplication.R;

public class Conexion_BLE extends AppCompatActivity {
    Button buscar;
    private static final String TAG = "Connexion";
    EditText listview;
    private String mDeviceList=null;
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
                mDeviceList=device.getAddress();
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

        mBluetoothAdapter.startDiscovery();

        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        buscar = findViewById(R.id.buscar);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = findViewById(R.id.listview);
                listview.setText(mDeviceList);
            }
        });
    }
}
