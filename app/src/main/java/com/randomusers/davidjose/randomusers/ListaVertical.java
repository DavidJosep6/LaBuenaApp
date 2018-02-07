package com.randomusers.davidjose.randomusers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListaVertical extends AppCompatActivity {
    MainActivity helper;

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

    Integer[] imageArray = {R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background};
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vertical);

        helper = new MainActivity(this);
        String data = helper.getData(); //Devuelve un string con los usuarios separados por \n y cada dato separado por ;
        String[] datosRecibidosPorUsuario = data.split("\n"); //Se usara para almacenar en cada posicion todos los datos de un usuario
        int tamanyoDatosRecibidosPorUsuario = datosRecibidosPorUsuario.length;

        for (int i = 0; i<datosRecibidosPorUsuario.length ; i++){
            String[] datosUnicoUsuario = datosRecibidosPorUsuario[i].split(";");
            nameList.add(datosUnicoUsuario[5]);
            genderList.add(datosUnicoUsuario[3]);
            dateList.add(datosUnicoUsuario[7]);
        }
        String[] nameArray = nameList.toArray(new String[nameList.size()]);
        String[] genderArray = genderList.toArray(new String[genderList.size()]);
        String[] dateArray = dateList.toArray(new String[dateList.size()]);

        CustomListAdapter whatever = new CustomListAdapter(this, nameArray, genderArray, dateArray, imageArray);
        listView = (ListView) findViewById(R.id.listviewID);
        listView.setAdapter(whatever);

    }
}
