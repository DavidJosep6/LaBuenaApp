package com.randomusers.davidjose.randomusers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DavidJose on 07/02/2018.
 */

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    //to store the animal images
    private final Integer[] imageIDarray;
    //to store the list of countries
    private final String[] nameArray;
    //to store the list of countries
    private final String[] genderArray;
    //to store the list of countries
    private final String[] dateArray;

    public CustomListAdapter(Activity context, String[] nameArrayParam, String[] genderArrayParam, String[] dateArrayParam, Integer[] imageIDArrayParam){
        super(context,R.layout.lista_vertical_row , nameArrayParam);

        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.genderArray = genderArrayParam;
        this.dateArray = dateArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lista_vertical_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nombreRow);
        TextView genderTextField = (TextView) rowView.findViewById(R.id.generoRow);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.fechaRow);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        genderTextField.setText(genderArray[position]);
        dateTextField.setText(dateArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}
