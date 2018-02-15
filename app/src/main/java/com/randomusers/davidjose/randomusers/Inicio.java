package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
/**
 * Funcionalidad: Primera pagina que redireccionara a generar nuevos usuarios (FORMULARIO) o a visualizar los usuarios (LISTA).
 */

public class Inicio extends AppCompatActivity {
    private static final String TAG = "Inicio.java";

    Button generar, visualizar;

    private static final Intent abrirFormulario = new Intent("com.randomusers.davidjose.randomusers.FORMULARIO");
    private static final Intent abrirLista = new Intent("com.randomusers.davidjose.randomusers.LISTA_VERTICAL");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() - INICIO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        generar = (Button) findViewById(R.id.Generar);
        visualizar = (Button) findViewById(R.id.Visualizar);

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(abrirFormulario);
            }
        });

        visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(abrirLista);
            }
        });

        GestionDatos helper = new GestionDatos(this);
        if(!helper.getData().contains("username")){ //Si no contiene el usuario de apoyo entonces insertalo
            helper.insertData("username", "password", "email", "gender", "title", "first", "last", "street", "city", "state", "postcode", "registered", "https://www.limestone.edu/sites/default/files/user.png");
        }
        Log.i(TAG, "onCreate() - FIN");
    }

}
