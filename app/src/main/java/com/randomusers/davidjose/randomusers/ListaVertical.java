package com.randomusers.davidjose.randomusers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
/**
 * Funcionalidad: Establecer los valores a cada fila de la lista y redireccionar a la lista que sea en funcion de su orientacion.
 */

public class ListaVertical extends AppCompatActivity {
    private static final String TAG = "ListaVertical.java";

    GestionDatos helper;

    //Listas dinamicas para almacenar los datos que se muestran en el listado (comun para vertical y horizontal)
    List<String> nameList = new ArrayList<>();
    List<String> genderList = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> imageStringList = new ArrayList<>();
    List<String> locationList = new ArrayList<>();
    List<String> usernameList = new ArrayList<>();
    List<String> passwordList = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() - INICIO");

        //Obtengo los datos de la BBDD
        helper = new GestionDatos(this);
        String data = helper.getData(); //Devuelve un string con los usuarios separados por \n y cada dato separado por ;
        String[] datosRecibidosPorUsuario = data.split("\n"); //Se usara para almacenar en cada posicion todos los datos de un unico usuario

        //Almacenamiento de datos para cada usuario, asegurando asi que todos los datos de un mismo usuario estaran en la misma posicion
        for (int i = 0; i<datosRecibidosPorUsuario.length ; i++){
            String[] datosUnicoUsuario = datosRecibidosPorUsuario[i].split(";"); //Array que contiene los datos de un usuario separados por ;
            nameList.add(datosUnicoUsuario[5]);
            genderList.add(datosUnicoUsuario[3]);
            dateList.add(datosUnicoUsuario[7]);
            imageStringList.add(datosUnicoUsuario[8]);
            locationList.add(datosUnicoUsuario[6]);
            usernameList.add(datosUnicoUsuario[0]);
            passwordList.add(datosUnicoUsuario[1]);
        }

        //Conversion de ArrayList a Array para pasarselo a la funcion de CustomListAdapter o CustomListAdapterHorizontal
        String[] nameArray = nameList.toArray(new String[nameList.size()]);
        String[] genderArray = genderList.toArray(new String[genderList.size()]);
        String[] dateArray = dateList.toArray(new String[dateList.size()]);
        String[] imageStringArray = imageStringList.toArray(new String[imageStringList.size()]);
        String[] locationArray = locationList.toArray(new String[locationList.size()]);
        String[] usernameArray = usernameList.toArray(new String[usernameList.size()]);
        String[] passwordArray = passwordList.toArray(new String[passwordList.size()]);

        //Cuando se gira el dispositivo se reinicia la actividad y por lo tanto se comprueba si esta en horizontal o en vertical
        int rotacion = getWindowManager().getDefaultDisplay().getRotation();
        if (rotacion == Surface.ROTATION_0 || rotacion == Surface.ROTATION_180) { //Si es vertical
            Log.i(TAG, "Estoy en vertical");
            setContentView(R.layout.activity_lista_vertical);
            CustomListAdapter whatever = new CustomListAdapter(this, nameArray, genderArray, dateArray, imageStringArray, locationArray);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        } else { //Si es horizontal
            Log.i(TAG, "Estoy en horizontal");
            setContentView(R.layout.activity_lista_horizontal);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            CustomListAdapterHorizontal whatever = new CustomListAdapterHorizontal(this, nameArray, genderArray, dateArray, imageStringArray, usernameArray, passwordArray);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Muestro la flecha de atras para facilitar la navegacion al usuario
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate() - FIN");
    }
}
