package com.example.busguideapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    private static final Object TAG = "Signin" ;
    private static String bin;
    private static String Loribina;
    private EditText mail, Contraseña, Repetir;
    private GoogleApiClient googleApiClient;
    Integer aux=0;
    Integer aux_2=1;
    private FirebaseAuth firebaseAuth;
    CheckBox boxi;
    FirebaseAuth.AuthStateListener firebaseauthlistener;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        mail= findViewById(R.id.mail);
        Contraseña= findViewById(R.id.Contraseña);
        Repetir= findViewById(R.id.Repetir);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();*/

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                    goMainScreen();
                }
            }
        };


        Button google = findViewById(R.id.google);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux_2=0;
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });
    }

    @Override
    protected  void onStart(){
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseauthlistener);
    }

    public void registrar(View view){
        String email =mail.getText().toString().trim();
        String pass=Contraseña.getText().toString();
        String repeaters=Repetir.getText().toString();

        if ((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass))){
            Toast.makeText(Registro.this, "No debe existir ningún campo vacío", Toast.LENGTH_LONG).show();
            return;
        }else{
            if(pass.length()<6){
                Toast.makeText(Registro.this, "La contraseña debe ser de tamaño superior o igual a 6", Toast.LENGTH_LONG).show();
                return;
            }else {
                if(pass.equals(repeaters) && repeaters.equals(pass)){
                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                    if(pattern.matcher(email).matches()==false){
                        Toast.makeText(Registro.this, "El e-mail no esta en formato letra@gmail.com", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(Registro.this, "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        registrarUsuario(email,pass);
    }

    private void registrarUsuario(String email,String pass){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Registro.this, "Este e-mail ya esta registrado", Toast.LENGTH_LONG).show(); //Tiene que ser mismo correo y pass para uqe pase esot revisar
                }else{
                    aux=1;
                }
            }
        });

        if(aux==1) {
            aux_2=1;
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(Registro.this,Inicio.class);
                        intent.putExtra("Google","1");
                        intent.putExtra("dialog","1");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==777){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
        }else{
            Toast.makeText(this, "No es posible entrar con ese correo", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"No se puede autenticar con Firebase", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goMainScreen(){
        Intent intent = new Intent(this,Inicio.class);
        if(aux_2==0) {
            intent.putExtra("Google", "0");
            intent.putExtra("dialog","1");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }else{
            intent.putExtra("Google","1");
            intent.putExtra("dialog","1");
        }
        startActivity(intent);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(firebaseauthlistener!=null){
            firebaseAuth.removeAuthStateListener(firebaseauthlistener);
        }
    }

}
