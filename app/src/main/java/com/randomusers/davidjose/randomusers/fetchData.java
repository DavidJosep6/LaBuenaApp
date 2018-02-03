package com.randomusers.davidjose.randomusers;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by DavidJose on 03/02/2018.
 */

public class fetchData extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "fetchData.java";
    String data = "";
    String dataParsed = "";
    String singleParsed = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://randomuser.me/api/?results=2");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
                Log.i(TAG, "Estoy leyendo" + data);
            }

            JSONObject JOresultados = new JSONObject(data); //Se guarda todo el objeto recibido formado por results e info
            JSONArray JA = JOresultados.getJSONArray("results"); //Guardamos en un array los distintos results
            JSONObject JOname = new JSONObject();
            JSONObject JOlocation = new JSONObject();
            JSONObject JOpicture = new JSONObject();
            Log.i(TAG,"El numero de objetos que hay es: " + JA.length());
            for (int i=0; i<JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                //Obtengo el objeto nombre
                JOname =  (JSONObject)  JO.get("name");
                //Obtengo el objeto localizacion
                JOlocation =  (JSONObject)  JO.get("location");
                //Obtengo el objeto imagen
                JOpicture =  (JSONObject)  JO.get("picture");
                singleParsed =  "Genero: " + JO.get("gender") + "\n" +
                                "Nombre: " + JOname.get("title") + " " + JOname.get("first") + " " + JOname.get("last") + "\n" +
                                "Localizacion: " + JOlocation.get("street") + " " + JOlocation.get("city") + " " + JOlocation.get("state") + " " + JOlocation.get("postcode") + "\n" +
                                "Fecha de registro: " + JO.get("registered") + "\n" +
                                "Imagen: " + JOpicture.get("large") + " " + JOpicture.get("medium") + " " + JOpicture.get("thumbnail");
                dataParsed = dataParsed + singleParsed;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i(TAG,"Los datos obtenidos son: " + dataParsed);
        Inicio.mostrar.setText(this.dataParsed);
    }
}
