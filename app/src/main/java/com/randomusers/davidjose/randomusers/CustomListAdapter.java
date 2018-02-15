package com.randomusers.davidjose.randomusers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Funcionalidad: Adapter definido para el listado vertical. En el se obtienen las referencias del listado vertical y se les da valor.
 */

public class CustomListAdapter extends ArrayAdapter {
    private final String TAG = "CustomListAdapter.java";

    //Para poder referenciar a la actividad
    private final Activity context;
    //Para almacenar las imagenes, nombres, generos, fechas y localizaciones de los usuarios
    private final String[] imageIDarray;
    private final String[] nameArray;
    private final String[] genderArray;
    private final String[] dateArray;
    private final String[] locationArray;

    //Constructor con los datos que se mostraran en la lista vertical
    public CustomListAdapter(Activity context, String[] nameArrayParam, String[] genderArrayParam, String[] dateArrayParam, String[] imageIDArrayParam, String[] locationArrayParam) {
        super(context, R.layout.lista_vertical_row, nameArrayParam);
        this.context = context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.genderArray = genderArrayParam;
        this.dateArray = dateArrayParam;
        this.locationArray = locationArrayParam;
    }

    public View getView(int position, View view, final ViewGroup parent) {
        Log.i(TAG, "getView() - INICIO");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lista_vertical_row, null, true);

        //Obtencion de las referencias de lista_vertical_row.xml
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nombreRow);
        TextView genderTextField = (TextView) rowView.findViewById(R.id.generoRow);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.fechaRow);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);
        TextView locationTextField = (TextView) rowView.findViewById(R.id.localizacionRow);
        Button mapsButton = (Button) rowView.findViewById(R.id.mapsButton);

        //Dar valor a las referencias anteriores con la posicion del array correspondiente
        final String map = "http://maps.google.co.in/maps?q=" + locationArray[position].toString().replace(", ", " ");
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                Log.i(TAG, "MAPS -  :" + map);
                parent.getContext().startActivity(intent);
            }
        });
        nameTextField.setText(nameArray[position]);
        genderTextField.setText(genderArray[position]);
        dateTextField.setText(dateArray[position]);
        locationTextField.setText(locationArray[position]);
        try {
            //Permisos para poder establecer una conexion en el main, siendo conscientes de sus implicaciones
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageIDarray[position]).getContent());
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getView() - FIN");
        return rowView;
    }

}