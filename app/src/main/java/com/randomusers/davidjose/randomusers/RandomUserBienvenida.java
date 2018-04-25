package com.randomusers.davidjose.randomusers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.sqlcipher.database.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

/**
 * Funcionalidad: Imagen de carga como bienvenida a la aplicacion.
 */

public class RandomUserBienvenida extends AppCompatActivity {
    private static final String TAG = "Random...enida.java";
    KeyStore keyStore;
    KeyPair keyPair = null;
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String KEY_ALIAS = "AndroidKeyStoreAlias";
    public static String clavePublica;
    @Override
    protected void onCreate(Bundle objetoRandomUsers) {
        Log.i(TAG, "onCreate() - INICIO");

        super.onCreate(objetoRandomUsers);
        setContentView(R.layout.activity_random_user_bienvenida);

        //Introducimos en BBDD el usuario admin y su contrasenya cifrada
        String adminPass = "admin123";
        String passCifrada = sha256(adminPass);
        //KeyStore
        loadKeyStore();
        try {
            if (!keyStoreExist(KEY_ALIAS)){
                Log.i(TAG, "onCreate() - KeyStore: " + keyStore.getType());
                generateNewKeyPair(KEY_ALIAS, this);
            }
            PublicKey clavePublicaPK = loadPublicKey(KEY_ALIAS);
            clavePublica = clavePublicaPK.getEncoded().toString();
            Log.i(TAG, "onCreate() - clavePublica-claveBD: " + clavePublica);
        } catch (Exception e) {
            Log.e(TAG, "onCreate() - KeyStore ERROR: " + e);
        }
        GestionDatosLogin helper = new GestionDatosLogin(this);
        SQLiteDatabase.loadLibs(this);
        if(!helper.getData().contains("admin")){ //Si no contiene el usuario de apoyo entonces insertalo
            helper.insertData("admin", passCifrada);
        }

        SharedPreferences sprefs;

        Thread reloj = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.LOGIN");
                    startActivity(abrirInicio);
                }
            }
        };
        reloj.start();
        Log.i(TAG, "onCreate() - FIN");
    }

    @Override
    protected void onPause(){
        Log.i(TAG, "onPause() - INICIO");
        super.onPause();
        finish();
        Log.i(TAG, "onPause() - FIN");
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    //KeyStore
    public void loadKeyStore() {
        Log.i(TAG, "loadKeyStore() - INICIO");
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            Log.i(TAG, "loadKeyStore() - FIN");
        } catch (Exception e) {
            Log.e(TAG, "loadKeyStore() - ERROR: " + e);
        }
    }

    public void generateNewKeyPair(String alias, Context context) throws Exception {
        Log.i(TAG, "generateNewKeyPair() - INICIO");
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            // expires 1 year from today
            end.add(Calendar.YEAR, 1);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + alias))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            // use the Android keystore
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE);
            gen.initialize(spec);
            // generates the keypair
            keyPair = gen.generateKeyPair();
        } catch (Exception e) {
            Log.e(TAG, "generateNewKeyPair() - ERROR. " + e);
        }
        Log.i(TAG, "generateNewKeyPair() - FIN");
    }

    public boolean keyStoreExist(String alias) throws Exception {
        if (!keyStore.isKeyEntry(alias)) {
            Log.i(TAG, "Could not find key alias: " + alias);
            return false;
        }
        else{
            Log.i(TAG, "Could find key alias: " + alias);
            return true;
        }
    }

    public PublicKey loadPublicKey(String alias) throws Exception {
        Log.i(TAG, "loadPublicKey() - INICIO");
        if(keyStoreExist(alias)){
            Certificate cert = keyStore.getCertificate(alias);
            // Get public key
            PublicKey publicKey = cert.getPublicKey();
            Log.i(TAG, "loadPublicKey() - FIN");
            return publicKey;
        }
        else {
            Log.i(TAG, "loadPublicKey() - No existe KeyStore - FIN");
            return null;
        }
    }

    /*public String encryptStringWithPublicKey(String alias, String toEncrypt) {
        Log.i(TAG, "encryptStringWithPublicKey() - INICIO");
        String textoCifrado = "";
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inCipher);
            cipherOutputStream.write(toEncrypt.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
            textoCifrado = Base64.encodeToString(vals, Base64.DEFAULT);
            Log.i(TAG, "encryptStringWithPublicKey() - textoCifrado: " + textoCifrado);
            Log.i(TAG, "encryptStringWithPublicKey() - FIN");
            return textoCifrado;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        Log.i(TAG, "encryptStringWithPublicKey() - FIN");
        return null;
    }*/

    /*public String decryptStringWithPrivateKey(String alias, String toDecrypt) {
        Log.i(TAG, "decryptStringWithPrivateKey() - INICIO");
        String textoDescifrado = "";
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(toDecrypt, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }
            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            textoDescifrado = new String(bytes, 0, bytes.length, "UTF-8");
            Log.i(TAG, "decryptStringWithPrivateKey() - textoDescifrado: " + textoDescifrado);
            Log.i(TAG, "decryptStringWithPrivateKey() - FIN");
            return textoDescifrado;

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        Log.i(TAG, "decryptStringWithPrivateKey() - FIN");
        return null;
    }*/
}
