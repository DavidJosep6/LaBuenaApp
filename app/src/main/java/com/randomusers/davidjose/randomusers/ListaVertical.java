package com.randomusers.davidjose.randomusers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListaVertical extends AppCompatActivity {

    GestionDatos helper;

    List<String> nameList = new ArrayList<>();
    //String[] nameArray = {"Luisa Fernandez","David José Martínez Morales","Soulimane Ouali El Bardiai","Rabbit","Snake","Spider" };

    List<String> genderList = new ArrayList<>();
    /*String[] genderArray = {
            "M",
            "F",
            "F",
            "M",
            "F",
            "M"
    };
    */

    List<String> dateList = new ArrayList<>();
    /*
    String[] dateArray = {
            "12-12-2012",
            "13-12-2012",
            "14-12-2012",
            "15-12-2012",
            "16-12-2012",
            "17-12-2012"
    };
    */

    List<String> imageStringList = new ArrayList<>();
    /*
    Integer[] imageArray = {R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background};
    */
    List<String> locationList = new ArrayList<>();
    List<String> usernameList = new ArrayList<>();
    List<String> passwordList = new ArrayList<>();


    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Obtengo los datos de la BBDD
        helper = new GestionDatos(this);
        String data = helper.getData(); //Devuelve un string con los usuarios separados por \n y cada dato separado por ;
        String[] datosRecibidosPorUsuario = data.split("\n"); //Se usara para almacenar en cada posicion todos los datos de un usuario

        for (int i = 0; i<datosRecibidosPorUsuario.length ; i++){
            String[] datosUnicoUsuario = datosRecibidosPorUsuario[i].split(";");
            nameList.add(datosUnicoUsuario[5]);
            genderList.add(datosUnicoUsuario[3]);
            dateList.add(datosUnicoUsuario[7]);
            imageStringList.add(datosUnicoUsuario[8]);
            locationList.add(datosUnicoUsuario[6]);
            usernameList.add(datosUnicoUsuario[0]);
            passwordList.add(datosUnicoUsuario[1]);
        }
        String[] nameArray = nameList.toArray(new String[nameList.size()]);
        String[] genderArray = genderList.toArray(new String[genderList.size()]);
        String[] dateArray = dateList.toArray(new String[dateList.size()]);
        String[] imageStringArray = imageStringList.toArray(new String[imageStringList.size()]);
        String[] locationArray = locationList.toArray(new String[locationList.size()]);
        String[] usernameArray = usernameList.toArray(new String[usernameList.size()]);
        String[] passwordArray = passwordList.toArray(new String[passwordList.size()]);

        int rotacion = getWindowManager().getDefaultDisplay().getRotation();
        if (rotacion == Surface.ROTATION_0 || rotacion == Surface.ROTATION_180) { //Si es vertical
            setContentView(R.layout.activity_lista_vertical);
            CustomListAdapter whatever = new CustomListAdapter(this, nameArray, genderArray, dateArray, imageStringArray, locationArray);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        } else { //Si es horizontal
            setContentView(R.layout.activity_lista_horizontal);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            CustomListAdapterHorizontal whatever = new CustomListAdapterHorizontal(this, nameArray, genderArray, dateArray, imageStringArray, usernameArray, passwordArray);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
