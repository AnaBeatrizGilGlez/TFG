package com.example.busguideapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Configuracion extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private static final String TAG = "Configuracion";
    EditText contraseña,Nombre;
    Button cambiar,salir;
    ImageView camarafotos;

    Uri uriprofile;
    FirebaseAuth mAuth;
    Integer aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);

        //contraseña = findViewById(R.id.contraseña);
        Nombre = findViewById(R.id.Nombre);
        if(!Nombre.equals(null)){
            aux=1;
        }
        /*cambiar= findViewById(R.id.cambiar);
        salir = findViewById(R.id.salir);
        camarafotos=findViewById(R.id.camarafotos);
        */
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                if(name!=null){
                    Nombre.setText(name);
                }
            }
        }

    }

    public void Cambiar_Foto(View view){
        //showImageChooser();
    }

    public void Salir(View view){
        startActivity(new Intent(Configuracion.this,Ruta.class));
    }

    public void Cambiar_Datos(View view){
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String nombre_nuevo=Nombre.getText().toString();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre_nuevo).build();
        user.updateProfile(profileUpdates);
        Log.i(TAG,"here"+ nombre_nuevo);
        startActivity(new Intent(Configuracion.this,Inicio.class));
    }
}
