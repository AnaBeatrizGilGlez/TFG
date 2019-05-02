package com.example.busguideapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Configuracion extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private static final String TAG = "Configuracion";
    EditText Contraseña,Nombre;
    ImageView camarafotos;

    Uri uriprofile;
    String aux="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);

        Contraseña = findViewById(R.id.contraseña);
        Nombre = findViewById(R.id.Nombre);
        camarafotos=findViewById(R.id.camarafotos);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        Log.i(TAG, "Usuario" + user.getEmail());
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                Uri photoUrl=profile.getPhotoUrl();
                if(name!=null){
                    Nombre.setText(name);
                }

                if(photoUrl!=null){
                    camarafotos.setImageURI(photoUrl);
                }
            }
        }

    }

    public void Cambiar_Foto(View view){
        Intent gallery= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,CHOOSE_IMAGE);
    }

    public void Salir(View view){
        startActivity(new Intent(Configuracion.this,Inicio.class));
    }

    public void Cambiar_Datos(View view){
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(!Nombre.getText().toString().isEmpty()) {
            String nombre_nuevo = Nombre.getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nombre_nuevo).build();
            user.updateProfile(profileUpdates);
        }

        if(!Contraseña.getText().toString().isEmpty()){
            String contraseña_nueva=Contraseña.getText().toString().trim();
            if(contraseña_nueva.length()< 6){
                Toast.makeText(this, "La contraseña tiene que tener un tamaño superior o igual a 6. No se ha cambiado la contraseña", Toast.LENGTH_SHORT).show();
            }else {
                user.updatePassword(contraseña_nueva);
                startActivity(new Intent(Configuracion.this,Inicio.class));
                finish();
            }
        }else{
            startActivity(new Intent(Configuracion.this,Inicio.class));
        }

        if(aux.equals("1")){
            startActivity(new Intent(Configuracion.this,Inicio.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK && requestCode==CHOOSE_IMAGE){
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            FirebaseUser user=firebaseAuth.getCurrentUser();
            uriprofile=data.getData();
            camarafotos.setImageURI(uriprofile);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uriprofile).build();
            user.updateProfile(profileUpdates);
            aux="1";
        }
    }
}
