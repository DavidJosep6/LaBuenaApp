package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.logging.Logger;

public class Inicio extends AppCompatActivity {

    private static final String TAG = "Inicio.java";
    int contador;
    Button sumar, restar;
    public static TextView mostrar;
    String data  = "";


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
                fetchData process = new fetchData();
                process.execute();
                /*Para generar una actividad con una url
                Uri uri = Uri.parse("https://randomuser.me/api/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                */
                Log.i(TAG,"Nueva pagina web");
                contador += 1;
                mostrar.setText("Su total es " + contador);
            }
        });

        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador -= 1;
                mostrar.setText("Su total es " + contador);
            }
        });
    }

}
