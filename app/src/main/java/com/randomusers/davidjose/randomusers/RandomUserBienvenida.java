package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        Thread reloj = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.INICIO");
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

}
