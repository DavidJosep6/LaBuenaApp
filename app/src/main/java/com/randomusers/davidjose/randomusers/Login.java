package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login.java";

    EditText usuario, passwordSinCifrar;
    Button login;

    GestionDatosLogin helperLogin = new GestionDatosLogin(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() - INICIO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        login = (Button)  findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibo el usuario y la contrasenya
                usuario = (EditText) findViewById(R.id.Usuario);
                passwordSinCifrar = (EditText) findViewById(R.id.Contrasenya);
                Log.i(TAG, "onCreate() - RECIBIDOS Usuario: " + usuario.getText().toString() + "\n" + "Password: " + passwordSinCifrar.getText().toString());

                //Compruebo si existe el usuario
                String datosBBDD = helperLogin.getData();
                String [] arrayTodosDatos = datosBBDD.split("\n"); //[0] = primer usuario PUES SOLO HABRA UNO
                String [] arrayDatos = arrayTodosDatos[0].split(";"); //[0] = username ; [1] = password
                Log.i(TAG, "onCreate() - BBDD Usuario: " + arrayDatos[0] + "\n" + "Password: " + arrayDatos[1]);

                //Si el usuario existe compruebo la contrasenya cifrada
                if(arrayDatos[0].equals(usuario.getText().toString())){
                    String passwordCifrada = RandomUserBienvenida.sha256(passwordSinCifrar.getText().toString());
                    Log.i(TAG, "onCreate() - passwordCifrada:" + passwordCifrada + "-");
                    Log.i(TAG, "onCreate() -  arrayDatos[1]:" +  arrayDatos[1] + "-");
                    Log.i(TAG, "onCreate() - passwordCifrada length: " + passwordCifrada.length());
                    Log.i(TAG, "onCreate() - arrayDatos[1] length: " + arrayDatos[1].length());
                    //Si la contraseña es correcta redirecciono al inicio
                    if(passwordCifrada.equalsIgnoreCase(arrayDatos[1])){
                        Log.i(TAG, "onCreate() - El usuario es correcto");
                        Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), "¡ Bienvenido " + usuario.getText() + " !", Toast.LENGTH_LONG);
                        toastNumeroInserciones.show();
                        Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.INICIO");
                        startActivity(abrirInicio);
                    }
                    else{
                        passwordSinCifrar.setError(getString(R.string.activity_login_contrasenya_error));
                        passwordSinCifrar.setText("");
                    }
                }
                else{
                    passwordSinCifrar.setText("");
                    usuario.setText("");
                    usuario.setError(getString(R.string.activity_login_usuario_error));
                }
            }
        });
        Log.i(TAG, "onCreate() - FIN");
    }
}
