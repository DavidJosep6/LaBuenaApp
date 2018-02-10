package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Inicio extends AppCompatActivity {

    private static final String TAG = "Inicio.java";
    int contador;
    Button sumar, restar;
    public static TextView mostrar;
    String data  = "";
    private static final Intent abrirFormulario = new Intent("com.randomusers.davidjose.randomusers.FORMULARIO");
    private static final Intent abrirLista = new Intent("com.randomusers.davidjose.randomusers.LISTA_VERTICAL");


    //--------8<--------------------8<--------------------8<--------------------8<------------
    String[] nameArray = {"Octopus","Pig","Sheep","Rabbit","Snake","Spider" };

    String[] infoArray = {
            "8 tentacled monster",
            "Delicious in rolls",
            "Great for jumpers",
            "Nice in a stew",
            "Great for shoes",
            "Scary."
    };

    Integer[] imageArray = {R.drawable.dicelogo,
            R.drawable.dicelogo,
            R.drawable.dicelogo,
            R.drawable.dicelogo,
            R.drawable.dicelogo,
            R.drawable.dicelogo};
    ListView listView;

    //--------8<--------------------8<--------------------8<--------------------8<------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        contador = 0;
        sumar = (Button) findViewById(R.id.Sumar);
        restar = (Button) findViewById(R.id.Restar);
        mostrar = (TextView) findViewById(R.id.tvMostrar);
        sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(abrirFormulario);
                /*Para generar una actividad con una url
                Uri uri = Uri.parse("https://randomuser.me/api/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                */
            }
        });

        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador -= 1;
                mostrar.setText("Su total es " + contador);
                startActivity(abrirLista);
            }
        });

        GestionDatos helper = new GestionDatos(this);
        helper.insertData("username", "password", "email", "gender", "title", "first", "last", "street", "city", "state", "postcode", "registered", "https://www.limestone.edu/sites/default/files/user.png");

    }

}
