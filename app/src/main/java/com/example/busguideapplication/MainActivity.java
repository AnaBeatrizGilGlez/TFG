package com.example.busguideapplication;

import android.app.Dialog;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT =0 ;
    Button regist, inici;
    ImageView Guagua;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        regist=(Button) findViewById(R.id.regist);
        inici=(Button) findViewById(R.id.inici);
        Guagua=(ImageView) findViewById(R.id.Guagua);

        if (mBluetoothAdapter == null) {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage(R.string.fundir);
            builder.setNegativeButton(R.string.aceptar,null);
            Dialog dialog=builder.create();
            dialog.show();
        }else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                if (!mBluetoothAdapter.isEnabled()) {
                    finish();
                }
            }
        }

        if((!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) && (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            finish();
                        }
                    });
            Dialog dialog=builder.create();
            dialog.show();
        }
    }

    public void Registrar (View view){
        Intent objeto = new Intent(MainActivity.this,Registro.class);
        startActivity(objeto);
    }

    public void Iniciar_Sesion(View view){
        Intent objeto = new Intent(MainActivity.this,Iniciar.class);
        startActivity(objeto);
    }

    public void Conexion(View view){
        startActivity(new Intent(MainActivity.this,Connexion.class));
    }
}
