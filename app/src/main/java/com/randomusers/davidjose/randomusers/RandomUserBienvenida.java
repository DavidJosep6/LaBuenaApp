package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

public class RandomUserBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle objetoRandomUsers) {
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
            } //Fin del metodo public void run()
        }; //Fin del Thread reloj = new Thread()
        reloj.start();
    } //Fin del protected void onCreate(Bundle objetoRandomUsers)

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    } //Fin del metodo protected void onPause()

}
