package com.example.busguideapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Iniciar extends AppCompatActivity {
    private EditText mail, Contraseña;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String Mensdescifr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar);

        mail = findViewById(R.id.mail);
        Contraseña = findViewById(R.id.Contraseña);

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };
    }

    public void iniciar(View view){
        String email =mail.getText().toString().trim();
        String pass=Contraseña.getText().toString();

        if ((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass))){
            Toast.makeText(Iniciar.this, "No debe existir ningún campo vacío. ", Toast.LENGTH_LONG).show();
        }else{
            iniciarusuario(email,pass);
        }

    }

    private void iniciarusuario(String email,String pass){
        //Vernamm(pass);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Iniciar.this, "Usuario entrando", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Iniciar.this,Inicio.class);
                    intent.putExtra("Google","1");
                    Log.i(String.valueOf(getApplicationContext()),"Usuariooooo");
                    startActivity(intent);
                }else{
                    Toast.makeText(Iniciar.this, "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*public String Vernamm(String pass) {
        Mensdescifr=XOR(Menscifrbin,claveale);
        return Mensdescifr;
    }

    public static String XOR(String a, String b) {
        String Result="";
        String bin="01";
        for(int i=a.length()-1;i>=0;i--){
            if((b.charAt(i))==a.charAt(i)){
                Result="0"+Result;
            }else{
                Result="1"+Result;
            }
        }
        return Result;
    }*/

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    protected  void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
