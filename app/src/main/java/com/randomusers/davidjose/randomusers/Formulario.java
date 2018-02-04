package com.randomusers.davidjose.randomusers;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Formulario extends AppCompatActivity {
    private static final String TAG = "Formulario.java";
    Button enviarFormulario;
    EditText nacionalidad, sexo, numInsertar, fechaRegistro;
    URL url = null;
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    String BDemail,BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture;
    MainActivity helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        enviarFormulario = (Button) findViewById(R.id.enviarForm);
        nacionalidad = (EditText) findViewById(R.id.Nacionalidad);
        sexo = (EditText) findViewById(R.id.Sexo);
        numInsertar = (EditText) findViewById(R.id.NumeroUsuarios);
        fechaRegistro = (EditText) findViewById(R.id.FechaRegistro);

        helper = new MainActivity(this);
        enviarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Nacionalidad: " + nacionalidad.getText());
                Log.i(TAG, "Sexo: " + sexo.getText());
                Log.i(TAG, "Numero usuarios: " + numInsertar.getText());
                Log.i(TAG, "Fecha: " + fechaRegistro.getText());
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    String inicioURL = "https://randomuser.me/api/";
                    url = new URL(inicioURL + "?nat=" + nacionalidad.getText() + "&gender=" + sexo.getText() + "&results=" + numInsertar.getText() + "&registered" + fechaRegistro.getText());
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
                        Log.i(TAG, "singleParsed");
                        singleParsed = "Genero: " + JO.get("gender") + "\n" +
                                "Email: " + JO.get("email") + "\n" +
                                "Nombre: " + JOname.get("title") + " " + JOname.get("first") + " " + JOname.get("last") + "\n" +
                                "Localizacion: " + JOlocation.get("street") + " " + JOlocation.get("city") + " " + JOlocation.get("state") + " " + JOlocation.get("postcode") + "\n" +
                                "Fecha de registro: " + JO.get("registered") + "\n" +
                                "Imagen: " + JOpicture.get("large") + " " + JOpicture.get("medium") + " " + JOpicture.get("thumbnail");
                        Log.i(TAG, "dataParsed");
                        dataParsed = dataParsed + singleParsed;
                        BDemail = JO.get("email").toString();
                        BDgender = JO.get("gender").toString();
                        BDtitle = JOname.get("title").toString();
                        BDfirst = JOname.get("first").toString();
                        BDlast = JOname.get("last").toString();

                        BDstreet = JOlocation.get("street").toString();
                        BDcity = JOlocation.get("city").toString();
                        BDstate = JOlocation.get("state").toString();
                        BDpostcode = JOlocation.get("postcode").toString();

                        BDregistered = JO.get("registered").toString();
                        BDpicture = JOpicture.get("large").toString();
                        Log.i(TAG, "Objeto MainActivity 1");
                        Log.i(TAG, "Objeto MainActivity 2");
                        helper.insertData(BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "La URL construida es: " + url.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    /*
    public void addUser(View view)
            long id = helper.insertData(BDemail, BDgender, BDtitle, BDfirst, BDlast, BDstreet, BDcity, BDstate, BDpostcode, BDregistered, BDpicture);
    }
    */

}
