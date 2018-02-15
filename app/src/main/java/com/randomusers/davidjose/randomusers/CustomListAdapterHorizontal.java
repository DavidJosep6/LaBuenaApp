package com.randomusers.davidjose.randomusers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Funcionalidad: Adapter definido para el listado horizontal. En el se obtienen las referencias del listado horizontal y se les da valor.
 */

public class CustomListAdapterHorizontal extends ArrayAdapter {
    private final String TAG = "Cust...Horizontal.java";

    //Para poder referenciar a la actividad
    private final Activity context;
    //Para almacenar las imagenes, nombres, generos, fechas, usuarios y contrasenyas de los usuarios
    private final String[] imageIDarray;
    private final String[] nameArray;
    private final String[] genderArray;
    private final String[] dateArray;
    private final String[] usernameArray;
    private final String[] passwordArray;

    //Constructor con los datos que se mostraran en la lista horizontal
    public CustomListAdapterHorizontal(Activity context, String[] nameArrayParam, String[] genderArrayParam, String[] dateArrayParam, String[] imageIDArrayParam, String[] usernameArrayParam, String[] passwordArrayParam) {
        super(context, R.layout.lista_horizontal_row, nameArrayParam);
        this.context = context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.genderArray = genderArrayParam;
        this.dateArray = dateArrayParam;
        this.usernameArray = usernameArrayParam;
        this.passwordArray = passwordArrayParam;
    }

    public View getView(int position, View view, final ViewGroup parent) {
        Log.i(TAG, "getView() - INICIO");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lista_horizontal_row, null, true);

        //Obtencion de las referencias de lista_horizontal_row.xml
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nombreRow);
        TextView genderTextField = (TextView) rowView.findViewById(R.id.generoRow);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.fechaRow);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);
        TextView usernameTextField = (TextView) rowView.findViewById(R.id.usernameRow);
        TextView passwordTextField = (TextView) rowView.findViewById(R.id.passwordRow);

        //Dar valor a las referencias anteriores con la posicion del array correspondiente
        nameTextField.setText(nameArray[position]);
        genderTextField.setText(genderArray[position]);
        dateTextField.setText(dateArray[position]);
        usernameTextField.setText(usernameArray[position]);
        passwordTextField.setText(passwordArray[position]);
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