package com.randomusers.davidjose.randomusers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sqlcipher.database.*;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Calendar;

import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login.java";
    EditText usuario, passwordSinCifrar;
    Button login;

    GestionDatosLogin helperLogin = new GestionDatosLogin(this);

    public static final String MisPreferencias = "MyPrefs";
    public static final String Nombre = "key_Nombre";
    public static final String Contraseña = "Key_passw";

    SharedPreferences sprefs;
    public static final String KEY_ALIAS = "AndroidKeyStoreAlias";

    @Override
    protected void onCreate(Bundle objShaPrefs) {
        Log.i(TAG, "onCreate() - INICIO");
        super.onCreate(objShaPrefs);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.Usuario);
        passwordSinCifrar = (EditText) findViewById(R.id.Contrasenya);
        sprefs = getSharedPreferences(MisPreferencias, Context.MODE_PRIVATE);
        if (sprefs.contains(Nombre))
            usuario.setText(sprefs.getString(Nombre, ""));
        if (sprefs.contains(Contraseña))
            passwordSinCifrar.setText(sprefs.getString(Contraseña, ""));

        login = (Button) findViewById(R.id.btn_login);
        SQLiteDatabase.loadLibs(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibo el usuario y la contrasenya
                Log.i(TAG, "onCreate() - RECIBIDOS Usuario: " + usuario.getText().toString() + "\n" + "Password: " + passwordSinCifrar.getText().toString());

                //Compruebo si existe el usuario
                String datosBBDD = helperLogin.getData();
                String[] arrayTodosDatos = datosBBDD.split("\n"); //[0] = primer usuario PUES SOLO HABRA UNO
                String[] arrayDatos = arrayTodosDatos[0].split(";"); //[0] = username ; [1] = password
                Log.i(TAG, "onCreate() - BBDD Usuario: " + arrayDatos[0] + "\n" + "Password: " + arrayDatos[1]);

                //Si el usuario existe compruebo la contrasenya cifrada
                if (arrayDatos[0].equals(usuario.getText().toString())) {
                    String passwordCifrada = RandomUserBienvenida.sha256(passwordSinCifrar.getText().toString());
                    Log.i(TAG, "onCreate() - passwordCifrada:" + passwordCifrada + "-");
                    Log.i(TAG, "onCreate() -  arrayDatos[1]:" + arrayDatos[1] + "-");
                    Log.i(TAG, "onCreate() - passwordCifrada length: " + passwordCifrada.length());
                    Log.i(TAG, "onCreate() - arrayDatos[1] length: " + arrayDatos[1].length());
                    //Si la contraseña es correcta redirecciono al inicio
                    if (passwordCifrada.equalsIgnoreCase(arrayDatos[1])) {
                        Log.i(TAG, "onCreate() - El usuario es correcto");
                        Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), "¡ Bienvenido " + usuario.getText() + " !", Toast.LENGTH_LONG);
                        toastNumeroInserciones.show();

                        //Solo se va a almacenar el usuario en las preferencias si es correcto, en caso contrario no se guardara
                        String n = usuario.getText().toString();
                        String cs = passwordSinCifrar.getText().toString();
                        SharedPreferences.Editor editor = sprefs.edit();
                        editor.putString(Nombre, n);
                        editor.putString(Contraseña, cs);
                        editor.commit();
                        Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.INICIO");
                        startActivity(abrirInicio);
                    } else {
                        passwordSinCifrar.setError(getString(R.string.activity_login_contrasenya_error));
                        passwordSinCifrar.setText("");
                    }
                } else {
                    passwordSinCifrar.setText("");
                    usuario.setText("");
                    usuario.setError(getString(R.string.activity_login_usuario_error));
                }
            }

        });
        Log.i(TAG, "onCreate() - FIN");
    }

}
