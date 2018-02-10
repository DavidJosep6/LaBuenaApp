package com.randomusers.davidjose.randomusers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ListResourceBundle;
/**
 * Created by DavidJose on 07/02/2018.
 */

public class CustomListAdapterHorizontal extends ArrayAdapter {
    Button MapsButton;

    private final String TAG = "CustomHorizontal.java";
    //to reference the Activity
    private final Activity context;
    //to store the animal images
    private final String[] imageIDarray;
    //to store the list of countries
    private final String[] nameArray;
    //to store the list of countries
    private final String[] genderArray;
    //to store the list of countries
    private final String[] dateArray;
    //to store the list of countries
    private final String[] usernameArray;
    //to store the list of countries
    private final String[] passwordArray;
    //to store the list of countries

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
        Log.i(TAG, "POSITION -  :" + position);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lista_horizontal_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nombreRow);
        TextView genderTextField = (TextView) rowView.findViewById(R.id.generoRow);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.fechaRow);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);
        TextView usernameTextField = (TextView) rowView.findViewById(R.id.usernameRow);
        TextView passwordTextField = (TextView) rowView.findViewById(R.id.passwordRow);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        genderTextField.setText(genderArray[position]);
        dateTextField.setText(dateArray[position]);
        usernameTextField.setText(usernameArray[position]);
        passwordTextField.setText(passwordArray[position]);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(" http://corporate2.bdjobs.com/21329.jpg").getContent());
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageIDarray[position]).getContent());
            //  http://corporate2.bdjobs.com/21329.jpg
            imageView.setImageBitmap(bitmap);
            //convertView.setBackgroundResource(R.drawable.cardlayout);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageView.setImageResource(imageIDarray[position]);

        //introducirImagen(imageIDarray[position], imageView);

        return rowView;
    }

    public void introducirImagen(String url, ImageView img) {
        URL imageUrl = null;
        HttpURLConnection conn = null;

        try {

            imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
            img.setImageBitmap(imagen);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


}

