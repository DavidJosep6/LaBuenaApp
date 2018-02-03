package com.randomusers.davidjose.randomusers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class Formulario extends AppCompatActivity {
    private static final String TAG = "Formulario.java";
    Button enviarFormulario;
    EditText nacionalidad, sexo, numInsertar, fechaRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        enviarFormulario = (Button) findViewById(R.id.enviarForm);
        nacionalidad = (EditText) findViewById(R.id.Nacionalidad);
        sexo = (EditText) findViewById(R.id.Sexo);
        numInsertar = (EditText) findViewById(R.id.NumeroUsuarios);
        fechaRegistro = (EditText) findViewById(R.id.FechaRegistro);

        enviarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Para generar una actividad con una url
                Uri uri = Uri.parse("https://randomuser.me/api/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                */
                String[] params = {nacionalidad.getText().toString(), sexo.getText().toString(), numInsertar.getText().toString(), fechaRegistro.getText().toString()};
                fetchData process = new fetchData();
                process.execute(params);
                Log.i(TAG,"Nacionalidad: " + nacionalidad.getText());
                Log.i(TAG,"Sexo: " + sexo.getText());
                Log.i(TAG,"Numero usuarios: " + numInsertar.getText());
                Log.i(TAG,"Fecha: " + fechaRegistro.getText());

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
