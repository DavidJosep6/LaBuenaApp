package com.randomusers.davidjose.randomusers;

/**
 * Created by DavidJose on 04/02/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainActivity {
    private static final String TAG = "MainActivity.java";
    MainActivityHelper myhelper;

    public MainActivity(Context context) {
        myhelper = new MainActivityHelper(context);
    }

    public long insertData(String username, String password, String email, String gender, String title, String first, String last, String street, String city, String state, String postcode, String registered, String picture) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MainActivityHelper.USERNAME, username);
        contentValues.put(MainActivityHelper.PASSWORD, password);
        contentValues.put(MainActivityHelper.EMAIL, email);
        contentValues.put(MainActivityHelper.GENDER, gender);
        contentValues.put(MainActivityHelper.TITLE, title);
        contentValues.put(MainActivityHelper.FIRST, first);
        contentValues.put(MainActivityHelper.LAST, last);
        contentValues.put(MainActivityHelper.STREET, street);
        contentValues.put(MainActivityHelper.CITY, city);
        contentValues.put(MainActivityHelper.STATE, state);
        contentValues.put(MainActivityHelper.POSTCODE, postcode);
        contentValues.put(MainActivityHelper.REGISTERED, registered);
        contentValues.put(MainActivityHelper.PICTURE, picture);
        long id = dbb.insert(MainActivityHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getData() {
        Log.i(TAG, "getData() - INICIO");
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myhelper.USERNAME,myhelper.PASSWORD,myhelper.EMAIL, myhelper.GENDER, myhelper.TITLE, myhelper.FIRST, myhelper.LAST, myhelper.STREET, myhelper.CITY, myhelper.STATE, myhelper.POSTCODE, myhelper.REGISTERED, myhelper.PICTURE,};
        Log.i(TAG, "getData() - cursor");
        Cursor cursor = db.query(myhelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        Log.i(TAG, "getData() - while");
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(myhelper.USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(myhelper.PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(myhelper.EMAIL));
            String gender = cursor.getString(cursor.getColumnIndex(myhelper.GENDER));
            String title = cursor.getString(cursor.getColumnIndex(myhelper.TITLE));
            String first = cursor.getString(cursor.getColumnIndex(myhelper.FIRST));
            String last = cursor.getString(cursor.getColumnIndex(myhelper.LAST));
            String street = cursor.getString(cursor.getColumnIndex(myhelper.STREET));
            String city = cursor.getString(cursor.getColumnIndex(myhelper.CITY));
            String state = cursor.getString(cursor.getColumnIndex(myhelper.STATE));
            String postcode = cursor.getString(cursor.getColumnIndex(myhelper.POSTCODE));
            String registered = cursor.getString(cursor.getColumnIndex(myhelper.REGISTERED));
            String picture = cursor.getString(cursor.getColumnIndex(myhelper.PICTURE));
            buffer.append(username + ";" + password + ";" + email + ";" + gender + ";" + title + ";" + first + " " + last + ";" + street + " " + city + " " + state + "" + postcode + ";" + registered + ";" + picture + "\n");
            Log.i(TAG,  username + ";" + password + ";" + email + ";" + gender + ";" + title + ";" + first + " " + last + ";" + street + " " + city + " " + state + "" + postcode + ";" + registered + ";" + picture + "\n");

        }
        Log.i(TAG, "getData() - FIN");
        return buffer.toString();
    }

    static class MainActivityHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "RandomDB";    // Database Name
        private static final String TABLE_NAME = "RandomTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String USERNAME = "USERNAME";     // Column 1 (Primary Key)
        private static final String PASSWORD = "PASSWORD";     // Column 1
        private static final String EMAIL = "EMAIL";     // Column 1
        private static final String GENDER = "GENDER";    //Column 2
        private static final String TITLE = "TITLE";    // Column 3
        private static final String FIRST = "FIRST";    // Column 4
        private static final String LAST = "LAST";    // Column 5
        private static final String STREET = "STREET";    // Column 6
        private static final String CITY = "CITY";    // Column 7
        private static final String STATE = "STATE";    // Column 8
        private static final String POSTCODE = "POSTCODE";    // Column 9
        private static final String REGISTERED = "REGISTERED";    // Column 10
        private static final String PICTURE = "PICTURE";    // Column 11


        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + USERNAME + " VARCHAR(255) PRIMARY KEY," +
                PASSWORD + " VARCHAR(255) ," +
                EMAIL + " VARCHAR(255) ," +
                GENDER + " VARCHAR(255) ," +
                TITLE + " VARCHAR(225) , " +
                FIRST + " VARCHAR(225) , " +
                LAST + " VARCHAR(225) , " +
                STREET + " VARCHAR(225) , " +
                CITY + " VARCHAR(225) , " +
                STATE + " VARCHAR(225) , " +
                POSTCODE + " VARCHAR(225) , " +
                REGISTERED + " VARCHAR(225) , " +
                PICTURE + " VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public MainActivityHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            Log.i(TAG, "MainActivityHelper - INICIO");
            this.context = context;
            Log.i(TAG, "MainActivityHelper - FIN");
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i(TAG, "onCreate - INICIO");
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onCreate - FIN");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.i(TAG, "onUpgrade - INICIO");
                //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onUpgrade - FIN");
        }
    }


}
