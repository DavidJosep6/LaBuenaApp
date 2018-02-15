package com.randomusers.davidjose.randomusers;

import android.graphics.Color;
import android.os.Bundle;
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
/**
 * Funcionalidad: Recuperar los datos introducidos por el usuario, en caso de haberlos, para poder validarlos y generar asi la URL correctamente,
 *                recibir el JSON con los usuarios y almacenarlos en la BBDD.
 */

public class Formulario extends AppCompatActivity {
    private static final String TAG = "Formulario.java";

    Button enviarFormulario;
    EditText nacionalidad, sexo, numInsertar, fechaRegistro;
    TextView nacionalidadTV, sexoTV, numinsertarTV, fechaRegistroTV;
    URL url = null;
    String data = "";
    String BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture, BDusername, BDpassword;
    GestionDatos helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() - INICIO");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Obtencion de las referencias de content_formulario.xml
        enviarFormulario = (Button) findViewById(R.id.enviarForm);
        nacionalidad = (EditText) findViewById(R.id.Nacionalidad);
        sexo = (EditText) findViewById(R.id.Sexo);
        numInsertar = (EditText) findViewById(R.id.NumeroUsuarios);
        fechaRegistro = (EditText) findViewById(R.id.FechaRegistro);
        nacionalidadTV = (TextView) findViewById(R.id.NacionalidadTV);
        numinsertarTV = (TextView) findViewById(R.id.RegistrosTV);
        sexoTV = (TextView) findViewById(R.id.SexoTV);
        fechaRegistroTV = (TextView) findViewById(R.id.FechaTV);

        //Establecer el color de los TextViews a gris al crearse la actividad
        nacionalidadTV.setTextColor(Color.DKGRAY);
        sexoTV.setTextColor(Color.DKGRAY);
        numinsertarTV.setTextColor(Color.DKGRAY);
        fechaRegistroTV.setTextColor(Color.DKGRAY);


        helper = new GestionDatos(this);
        enviarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numeroInserciones = 0; //Para mostrar al usuario el toast con el numero total de inserciones

                //Establecer el color de los TextViews a gris al pulsar el boton de enviar (aquellos que tengan error cambiaran a rojo)
                nacionalidadTV.setTextColor(Color.DKGRAY);
                sexoTV.setTextColor(Color.DKGRAY);
                numinsertarTV.setTextColor(Color.DKGRAY);
                fechaRegistroTV.setTextColor(Color.DKGRAY);

                //Logs para visualizar los valores introducidos por el usuario
                Log.i(TAG, "Nacionalidad introducida: " + nacionalidad.getText());
                Log.i(TAG, "Sexo introducido: " + sexo.getText());
                Log.i(TAG, "Numero usuarios introducidos: " + numInsertar.getText());
                Log.i(TAG, "Fecha introducida: " + fechaRegistro.getText());

                boolean controlDatoIncorrecto = false; //Variable para checkear que no haya ningun fallo en los EditText rellenados (false - no hay error, true - si hay error)
                String[] generosDisponibles = {"male", "female"};
                String[] nacionalidadesDisponibles = {"AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI", "FR", "GB", "IE", "IR", "NL", "NZ", "TR", "US"};
                if (!("").equals(nacionalidad.getText().toString())){
                    controlDatoIncorrecto = true;
                    for (int iter = 0; iter < nacionalidadesDisponibles.length; iter++){
                        if((nacionalidad.getText().toString()).equals(nacionalidadesDisponibles[iter])){
                            controlDatoIncorrecto = false;
                        }
                    }
                    if (controlDatoIncorrecto){ //Hay error por lo que introducimos un mensaje de error, cambiamos el color y vaciamos el campo
                        Log.i(TAG, "ERROR en nacionalidad: " + nacionalidad.getText());
                        nacionalidad.setError(getString(R.string.activity_formulario_nacionalidad_error));
                        nacionalidadTV.setTextColor(Color.RED);
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
                    if (controlDatoIncorrecto) { //Hay error por lo que introducimos un mensaje de error, cambiamos el color y vaciamos el campo
                        Log.i(TAG, "ERROR en sexo: " + sexo.getText());
                        sexo.setError(getString(R.string.activity_formulario_sexo_error));
                        sexoTV.setTextColor(Color.RED);
                        sexo.setText("");

                    }
                }

                String fechaUsuarioSinSetear = fechaRegistro.getText().toString();
                String fechaJSONSinSetear = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //Formato de la fecha que mete el usuario
                SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  //Formato de la fecha que se recibe del JSON
                Date fechaUsuario = null; //Fecha introducida por el usuario con formato
                Date fechaUsuarioJSON = null; //Fecha recibida del JSON con formato
                if (!("").equals(fechaUsuarioSinSetear)) {
                    try { //Intentamos formatear la fecha que solo puede ser del tipo dd/mm/aaaa, si no tiene ese formato saltara al catch
                        fechaUsuario = dateFormat.parse(fechaUsuarioSinSetear);
                    } catch (ParseException e) { //Hay error por lo que introducimos un mensaje de error, cambiamos el color y vaciamos el campo
                        Log.i(TAG, "ERROR en fechaRegistro: " + fechaRegistro.getText());
                        fechaRegistro.setError(getString(R.string.activity_formulario_fecha_error));
                        fechaRegistroTV.setTextColor(Color.RED);
                        controlDatoIncorrecto = true;
                        fechaRegistro.setText("");
                    }
                }
                //Permisos para poder establecer una conexion en el main, siendo conscientes de sus implicaciones
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                if (!controlDatoIncorrecto && ("").equals(fechaUsuarioSinSetear)) { //Sin fecha
                    {
                        try {
                            String inicioURL = "https://randomuser.me/api/";
                            url = new URL(inicioURL + "?nat=" + nacionalidad.getText() + "&gender=" + sexo.getText() + "&results=" + numInsertar.getText());
                            Log.i(TAG, "La URL construida es: " + url.toString());
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line = "";
                            while (line != null) {
                                line = bufferedReader.readLine();
                                data = data + line;
                            }
                            JSONObject JOresultados = new JSONObject(data); //Se guarda todo el objeto recibido formado por results e info
                            JSONArray JA = JOresultados.getJSONArray("results"); //Guardamos en un array los distintos results
                            //Objetos JSON para poder acceder luego a los datos
                            JSONObject JOname = new JSONObject();
                            JSONObject JOlocation = new JSONObject();
                            JSONObject JOpicture = new JSONObject();
                            JSONObject JOlogin = new JSONObject();
                            for (int i = 0; i < JA.length(); i++) {
                                JSONObject JO = (JSONObject) JA.get(i);
                                //Obtengo el objeto nombre, localizacion, imagen y login
                                JOname = (JSONObject) JO.get("name");
                                JOlocation = (JSONObject) JO.get("location");
                                JOpicture = (JSONObject) JO.get("picture");
                                JOlogin = (JSONObject) JO.get("login");

                                //Formateo de los generos female y male por F y M respectivamente
                                BDemail = JO.get("email").toString();
                                if (JO.get("gender").toString().equalsIgnoreCase("female")) {
                                    BDgender = "F";
                                }
                                else if (JO.get("gender").toString().equalsIgnoreCase("male")) {
                                    BDgender = "M";
                                }
                                else{
                                    BDgender = "genero";
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

                                numeroInserciones++;
                                Log.i(TAG, "Datos insercion: " + BDusername + " - " + BDpassword + " - " + BDemail + " - " + BDgender + " - " + BDtitle + " - " + BDfirst + " - " + BDlast + " - " + BDstreet + " - " + BDcity + " - " + BDstate + " - " + BDpostcode + " - " + BDregistered + " - " + BDpicture);
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
                        Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), getString(R.string.activity_formulario_generar_exito) + numeroInserciones, Toast.LENGTH_LONG);
                        toastNumeroInserciones.show();
                    }
                }

                if (!controlDatoIncorrecto && !("").equals(fechaUsuarioSinSetear)) {
                    try {
                        String inicioURL = "https://randomuser.me/api/";
                        url = new URL(inicioURL + "?nat=" + nacionalidad.getText() + "&gender=" + sexo.getText() + "&results=" + numInsertar.getText());
                        Log.i(TAG, "La URL construida es: " + url.toString());
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = "";
                        while (line != null) {
                            line = bufferedReader.readLine();
                            data = data + line;
                        }
                        JSONObject JOresultados = new JSONObject(data); //Se guarda todo el objeto recibido formado por results e info
                        JSONArray JA = JOresultados.getJSONArray("results"); //Guardamos en un array los distintos results
                        //Objetos JSON para poder acceder luego a los datos
                        JSONObject JOname = new JSONObject();
                        JSONObject JOlocation = new JSONObject();
                        JSONObject JOpicture = new JSONObject();
                        JSONObject JOlogin = new JSONObject();
                        for (int i = 0; i < JA.length(); i++) {
                            JSONObject JO = (JSONObject) JA.get(i);
                            //Obtengo el objeto nombre, localizacion, imagen y login
                            JOname = (JSONObject) JO.get("name");
                            JOlocation = (JSONObject) JO.get("location");
                            JOpicture = (JSONObject) JO.get("picture");
                            JOlogin = (JSONObject) JO.get("login");

                            BDemail = JO.get("email").toString();
                            //Formateo de los generos female y male por F y M respectivamente
                            if (JO.get("gender").toString().equalsIgnoreCase("female")) {
                                BDgender = "F";
                            }
                            else if (JO.get("gender").toString().equalsIgnoreCase("male")) {
                                BDgender = "M";
                            }
                            else{
                                BDgender = "genero";
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
                            try {
                                fechaUsuarioJSON = dateFormatJSON.parse(fechaJSONSinSetear); //Formateo de la fecha recibida del JSON
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (fechaUsuarioJSON.before(fechaUsuario)) { //Si la fecha del JSON es anterior a la que introduce el usuario, hago el insert
                                numeroInserciones++;
                                Log.i(TAG, "La fecha generada : " + fechaUsuarioJSON + " es anterior a la introducida por el usuario: " + fechaUsuario);
                                Log.i(TAG, "Datos insercion: " + BDusername + " - " + BDpassword + " - " + BDemail + " - " + BDgender + " - " + BDtitle + " - " + BDfirst + " - " + BDlast + " - " + BDstreet + " - " + BDcity + " - " + BDstate + " - " + BDpostcode + " - " + BDregistered + " - " + BDpicture);
                                helper.insertData(BDusername, BDpassword, BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
                            } else { //La fecha del JSON no es anterior a la que introduce el usuario por lo que no lo inserto
                                Log.i(TAG, "La fecha generada : " + fechaUsuarioJSON + " es posterior a la introducida por el usuario: " + fechaUsuario);
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), getString(R.string.activity_formulario_generar_exito) + numeroInserciones, Toast.LENGTH_LONG);
                    toastNumeroInserciones.show();
                    Formulario.super.onBackPressed();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Muestro la flecha de atras para facilitar la navegacion al usuario
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate() - FIN");
    }

}
