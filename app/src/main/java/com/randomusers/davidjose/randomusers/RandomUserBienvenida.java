package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Funcionalidad: Imagen de carga como bienvenida a la aplicacion.
 */

public class RandomUserBienvenida extends AppCompatActivity {
    private static final String TAG = "Random...enida.java";

    @Override
    protected void onCreate(Bundle objetoRandomUsers) {
        Log.i(TAG, "onCreate() - INICIO");

        super.onCreate(objetoRandomUsers);
        setContentView(R.layout.activity_random_user_bienvenida);

        //Introducimos en BBDD el usuario admin y su contrasenya cifrada
        String adminPass = "admin";
        String passCifrada = sha256(adminPass);
        GestionDatosLogin helper = new GestionDatosLogin(this);
        if(!helper.getData().contains("admin")){ //Si no contiene el usuario de apoyo entonces insertalo
            helper.insertData("admin", passCifrada);
        }

        SharedPreferences sprefs;

        Thread reloj = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.LOGIN");
                    startActivity(abrirInicio);
                }
            }
        };
        reloj.start();
        Log.i(TAG, "onCreate() - FIN");
    }

    @Override
    protected void onPause(){
        Log.i(TAG, "onPause() - INICIO");
        super.onPause();
        finish();
        Log.i(TAG, "onPause() - FIN");
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
