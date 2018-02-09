package com.randomusers.davidjose.randomusers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formulario extends AppCompatActivity {
    private static final String TAG = "Formulario.java";
    Button enviarFormulario, verFormulario;
    EditText nacionalidad, sexo, numInsertar, fechaRegistro;
    TextView nacionalidadTV, sexoTV, numinsertarTV, fechaRegistroTV;
    URL url = null;
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    String BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture, BDusername, BDpassword;
    GestionDatos helper;

    private static final Intent redireccionarAInicio = new Intent("com.randomusers.davidjose.randomusers.INICIO");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        enviarFormulario = (Button) findViewById(R.id.enviarForm);
        verFormulario = (Button) findViewById(R.id.verForm);
        nacionalidad = (EditText) findViewById(R.id.Nacionalidad);
        sexo = (EditText) findViewById(R.id.Sexo);
        numInsertar = (EditText) findViewById(R.id.NumeroUsuarios);
        fechaRegistro = (EditText) findViewById(R.id.FechaRegistro);
        nacionalidadTV = (TextView) findViewById(R.id.NacionalidadTV);
        numinsertarTV = (TextView) findViewById(R.id.RegistrosTV);
        sexoTV = (TextView) findViewById(R.id.SexoTV);
        fechaRegistroTV = (TextView) findViewById(R.id.FechaTV);

        nacionalidadTV.setTextColor(Color.DKGRAY);
        sexoTV.setTextColor(Color.DKGRAY);
        numinsertarTV.setTextColor(Color.DKGRAY);
        fechaRegistroTV.setTextColor(Color.DKGRAY);

        helper = new GestionDatos(this);
        enviarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nacionalidadTV.setTextColor(Color.DKGRAY);
                sexoTV.setTextColor(Color.DKGRAY);
                numinsertarTV.setTextColor(Color.DKGRAY);
                fechaRegistroTV.setTextColor(Color.DKGRAY);
                nacionalidadTV.setError(null);
                sexoTV.setError(null);
                fechaRegistroTV.setError(null);

                Log.i(TAG, "Nacionalidad introducida: " + nacionalidad.getText());
                Log.i(TAG, "Sexo introducido: " + sexo.getText());
                Log.i(TAG, "Numero usuarios introducidos: " + numInsertar.getText());
                Log.i(TAG, "Fecha introducida: " + fechaRegistro.getText());
                String fechaUsuarioSinSetear = fechaRegistro.getText().toString();
                String fechaJSONSinSetear = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date fechaUsuario = null; //Fecha que introduce el usuario en el formulario
                Date fechaUsuarioJSON = null;
                boolean controlDatoIncorrecto = false;
                String[] generosDisponibles = {"male", "female"};
                String[] nacionalidadesDisponibles = {"AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI", "FR", "GB", "IE", "IR", "NL", "NZ", "TR", "US"};
                String toasStringtErrorDatos = "";
                if (!("").equals(nacionalidad.getText().toString())){
                    controlDatoIncorrecto = true;
                    for (int iter = 0; iter < nacionalidadesDisponibles.length; iter++){
                        if((nacionalidad.getText().toString()).equals(nacionalidadesDisponibles[iter])){
                            controlDatoIncorrecto = false;
                        }
                    }
                    if (controlDatoIncorrecto){
                        nacionalidadTV.setError("");
                        nacionalidadTV.setTextColor(Color.RED);
                        toasStringtErrorDatos = toasStringtErrorDatos + "\n- La nacionalidad no es correcta, pruebe con: \n AU, BR, CA, CH, DE, DK, ES, FI, FR, GB, IE, IR, NL, NZ, TR, US\n";
                        //Toast toastNacionalidadIncorrecta = Toast.makeText(getApplicationContext(), "La nacionalidad no es correcta, pruebe con: \n AU, BR, CA, CH, DE, DK, ES, FI, FR, GB, IE, IR, NL, NZ, TR, US", Toast.LENGTH_LONG);
                        //toastNacionalidadIncorrecta.show();
                        nacionalidad.setText("");
                    }
                }

                if (!("").equals(sexo.getText().toString())){
                    controlDatoIncorrecto = true;
                    for (int iter = 0; iter < generosDisponibles.length; iter++){
                        if((sexo.getText().toString()).equals(generosDisponibles[iter])){
                            controlDatoIncorrecto = false;
                        }
                    }
                    if (controlDatoIncorrecto) {
                        sexoTV.setError("");
                        sexoTV.setTextColor(Color.RED);
                        toasStringtErrorDatos = toasStringtErrorDatos + "\n- El genero no es correcto, pruebe con: \n male,female\n";
                        //Toast toastGeneroIncorrecta = Toast.makeText(getApplicationContext(), "El genero no es correcto, pruebe con: \n male,female", Toast.LENGTH_LONG);
                        //toastGeneroIncorrecta.show();
                        sexo.setText("");
                    }
                }

                if (!("").equals(fechaUsuarioSinSetear)) {
                    try {
                        fechaUsuario = dateFormat.parse(fechaUsuarioSinSetear);
                        Log.i(TAG, "Fecha seteada: " + fechaUsuario.toString());
                    } catch (ParseException e) {
                        fechaRegistroTV.setError("");
                        fechaRegistroTV.setTextColor(Color.RED);
                        controlDatoIncorrecto = true;
                        toasStringtErrorDatos = toasStringtErrorDatos + "\n- El formato de la fecha no es correcto, use el formato: \n dd/mm/aaaa\n";
                        //Toast toastFechaIncorrecta = Toast.makeText(getApplicationContext(), "El formato de la fecha no es correcto", Toast.LENGTH_LONG);
                        //toastFechaIncorrecta.show();
                        fechaRegistro.setText("");
                    }
                }

                if(controlDatoIncorrecto){
                    Toast toastErrorDatos = Toast.makeText(getApplicationContext(), toasStringtErrorDatos, Toast.LENGTH_LONG);
                    toastErrorDatos.show();
                }
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                int numeroInserciones = 0;

                if (!controlDatoIncorrecto && ("").equals(fechaUsuarioSinSetear)) {
                    {
                        try {
                            String inicioURL = "https://randomuser.me/api/";
                            url = new URL(inicioURL + "?nat=" + nacionalidad.getText() + "&gender=" + sexo.getText() + "&results=" + numInsertar.getText());
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line = "";
                            while (line != null) {
                                line = bufferedReader.readLine();
                                data = data + line;
                                Log.i(TAG, "Estoy leyendo");
                            }
                            JSONObject JOresultados = new JSONObject(data); //Se guarda todo el objeto recibido formado por results e info
                            JSONArray JA = JOresultados.getJSONArray("results"); //Guardamos en un array los distintos results
                            JSONObject JOname = new JSONObject();
                            JSONObject JOlocation = new JSONObject();
                            JSONObject JOpicture = new JSONObject();
                            JSONObject JOlogin = new JSONObject();
                            Log.i(TAG, "El numero de objetos que hay es: " + JA.length());
                            for (int i = 0; i < JA.length(); i++) {
                                Log.i(TAG, "Dentro del for");
                                JSONObject JO = (JSONObject) JA.get(i);
                                //Obtengo el objeto nombre
                                JOname = (JSONObject) JO.get("name");
                                //Obtengo el objeto localizacion
                                JOlocation = (JSONObject) JO.get("location");
                                //Obtengo el objeto imagen
                                JOpicture = (JSONObject) JO.get("picture");
                                //Obtengo el objeto login
                                JOlogin = (JSONObject) JO.get("login");
                                Log.i(TAG, "singleParsed");
                                singleParsed = "Genero: " + JO.get("gender") + "\n" +
                                        "Email: " + JO.get("email") + "\n" +
                                        "Nombre: " + JOname.get("title") + " " + JOname.get("first") + " " + JOname.get("last") + "\n" +
                                        "Localizacion: " + JOlocation.get("street") + " " + JOlocation.get("city") + " " + JOlocation.get("state") + " " + JOlocation.get("postcode") + "\n" +
                                        "Fecha de registro: " + JO.get("registered") + "\n" +
                                        "Login: " + JOlogin.get("username") + " " + JOlogin.get("password") +
                                        "Imagen: " + JOpicture.get("large") + " " + JOpicture.get("medium") + " " + JOpicture.get("thumbnail");
                                Log.i(TAG, "dataParsed");
                                dataParsed = dataParsed + singleParsed;
                                BDemail = JO.get("email").toString();
                                if (JO.get("gender").toString().equalsIgnoreCase("female")) {
                                    BDgender = "F";
                                }
                                if (JO.get("gender").toString().equalsIgnoreCase("male")) {
                                    BDgender = "M";
                                }
                                BDtitle = JOname.get("title").toString();
                                BDfirst = JOname.get("first").toString();
                                BDlast = JOname.get("last").toString();

                                BDstreet = JOlocation.get("street").toString();
                                BDcity = JOlocation.get("city").toString();
                                BDstate = JOlocation.get("state").toString();
                                BDpostcode = JOlocation.get("postcode").toString();

                                BDregistered = JO.get("registered").toString();
                                BDpicture = JOpicture.get("large").toString();

                                BDusername = JOlogin.get("username").toString();
                                BDpassword = JOlogin.get("password").toString();

                                fechaJSONSinSetear = BDregistered;
                                Log.i(TAG, "Fecha JSON sin seteada: " + BDregistered.toString());
                                numeroInserciones++;
                                helper.insertData(BDusername, BDpassword, BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
                                Formulario.super.onBackPressed();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "La URL construida es: " + url.toString());
                        Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), "El total de registros guardados es:  " + numeroInserciones, Toast.LENGTH_LONG);
                        toastNumeroInserciones.show();
                    }
                }

                if (!controlDatoIncorrecto && !("").equals(fechaUsuarioSinSetear)) {
                    try {
                        String inicioURL = "https://randomuser.me/api/";
                        url = new URL(inicioURL + "?nat=" + nacionalidad.getText() + "&gender=" + sexo.getText() + "&results=" + numInsertar.getText());
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = "";
                        while (line != null) {
                            line = bufferedReader.readLine();
                            data = data + line;
                            Log.i(TAG, "Estoy leyendo");
                        }
                        JSONObject JOresultados = new JSONObject(data); //Se guarda todo el objeto recibido formado por results e info
                        JSONArray JA = JOresultados.getJSONArray("results"); //Guardamos en un array los distintos results
                        JSONObject JOname = new JSONObject();
                        JSONObject JOlocation = new JSONObject();
                        JSONObject JOpicture = new JSONObject();
                        JSONObject JOlogin = new JSONObject();
                        Log.i(TAG, "El numero de objetos que hay es: " + JA.length());
                        for (int i = 0; i < JA.length(); i++) {
                            Log.i(TAG, "Dentro del for");
                            JSONObject JO = (JSONObject) JA.get(i);
                            //Obtengo el objeto nombre
                            JOname = (JSONObject) JO.get("name");
                            //Obtengo el objeto localizacion
                            JOlocation = (JSONObject) JO.get("location");
                            //Obtengo el objeto imagen
                            JOpicture = (JSONObject) JO.get("picture");
                            //Obtengo el objeto login
                            JOlogin = (JSONObject) JO.get("login");
                            Log.i(TAG, "singleParsed");
                            singleParsed = "Genero: " + JO.get("gender") + "\n" +
                                    "Email: " + JO.get("email") + "\n" +
                                    "Nombre: " + JOname.get("title") + " " + JOname.get("first") + " " + JOname.get("last") + "\n" +
                                    "Localizacion: " + JOlocation.get("street") + " " + JOlocation.get("city") + " " + JOlocation.get("state") + " " + JOlocation.get("postcode") + "\n" +
                                    "Fecha de registro: " + JO.get("registered") + "\n" +
                                    "Login: " + JOlogin.get("username") + " " + JOlogin.get("password") +
                                    "Imagen: " + JOpicture.get("large") + " " + JOpicture.get("medium") + " " + JOpicture.get("thumbnail");
                            Log.i(TAG, "dataParsed");
                            dataParsed = dataParsed + singleParsed;
                            BDemail = JO.get("email").toString();
                            if (JO.get("gender").toString().equalsIgnoreCase("female")) {
                                BDgender = "F";
                            }
                            if (JO.get("gender").toString().equalsIgnoreCase("male")) {
                                BDgender = "M";
                            }
                            BDtitle = JOname.get("title").toString();
                            BDfirst = JOname.get("first").toString();
                            BDlast = JOname.get("last").toString();

                            BDstreet = JOlocation.get("street").toString();
                            BDcity = JOlocation.get("city").toString();
                            BDstate = JOlocation.get("state").toString();
                            BDpostcode = JOlocation.get("postcode").toString();

                            BDregistered = JO.get("registered").toString();
                            BDpicture = JOpicture.get("large").toString();

                            BDusername = JOlogin.get("username").toString();
                            BDpassword = JOlogin.get("password").toString();

                            fechaJSONSinSetear = BDregistered;
                            Log.i(TAG, "Fecha JSON sin seteada: " + BDregistered.toString());
                            try {
                                fechaUsuarioJSON = dateFormatJSON.parse(fechaJSONSinSetear);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i(TAG, "Fecha JSON seteada: " + fechaUsuarioJSON.toString());
                            if (fechaUsuarioJSON.before(fechaUsuario)) {
                                numeroInserciones++;
                                Log.i(TAG, "Las fechas generadas : " + fechaUsuarioJSON + " son anteriores a la introducida por el usuario: " + fechaUsuario);
                                helper.insertData(BDusername, BDpassword, BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
                            } else {
                                Log.i(TAG, "Las fechas generadas : " + fechaUsuarioJSON + " son posteriores a la introducida por el usuario: " + fechaUsuario);
                            }

                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "La URL construida es: " + url.toString());
                    Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), "El total de registros guardados es:  " + numeroInserciones, Toast.LENGTH_LONG);
                    toastNumeroInserciones.show();
                    Formulario.super.onBackPressed();
                }
            }
        });

        verFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = helper.getData();
                Toast toast1 = Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG);
                toast1.show();

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
/*
    public void viewdata(View view) {
String data = helper.getData();
                Toast.makeText(Formulario.this, data, Toast.LENGTH_LONG);
    }

    public void addUser(View view)
            long id = helper.insertData(BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
    }
    */

}
