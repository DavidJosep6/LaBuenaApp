package com.randomusers.davidjose.randomusers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sqlcipher.database.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login.java";
    public static String aleatorioBD;
    public static String encriptadoBD;
    public static String desencriptadoBD = "";
    EditText usuario, passwordSinCifrar;
    Button login;

    GestionDatosLogin helperLogin = new GestionDatosLogin(this);

    public static final String MisPreferencias = "MyPrefs";
    public static final String Nombre = "key_Nombre";
    public static final String Contraseña = "Key_passw";
    public static final String BBDDEncriptado = "Key_BBDD";

    SharedPreferences sprefs;
    KeyStore keyStore;
    KeyPair keyPair = null;
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String KEY_ALIAS = "AndroidKeyStoreAlias";

    @Override
    protected void onCreate(Bundle objShaPrefs) {
        Log.i(TAG, "onCreate() - INICIO");
        super.onCreate(objShaPrefs);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.Usuario);
        passwordSinCifrar = (EditText) findViewById(R.id.Contrasenya);
        sprefs = getSharedPreferences(MisPreferencias, Context.MODE_PRIVATE);
        if (sprefs.contains(Nombre))
            usuario.setText(sprefs.getString(Nombre, ""));
        if (sprefs.contains(Contraseña))
            passwordSinCifrar.setText(sprefs.getString(Contraseña, ""));

        login = (Button) findViewById(R.id.btn_login);
        SQLiteDatabase.loadLibs(this);

        //KeyStore
        loadKeyStore();
        try {
            if (!keyStoreExist(KEY_ALIAS)){
                Log.i(TAG, "onCreate() - KeyStore: " + keyStore.getType());
                generateNewKeyPair(KEY_ALIAS, this);
                loadPublicKey(KEY_ALIAS);
                String cifradoKS = encryptStringWithPublicKey(KEY_ALIAS, "Pepe");
                String descifradoKS = decryptStringWithPrivateKey(KEY_ALIAS, cifradoKS);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate() - KeyStore ERROR: " + e);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibo el usuario y la contrasenya
                Log.i(TAG, "onCreate() - RECIBIDOS Usuario: " + usuario.getText().toString() + "\n" + "Password: " + passwordSinCifrar.getText().toString());

                //Compruebo si existe el usuario
                String datosBBDD = helperLogin.getData();
                String[] arrayTodosDatos = datosBBDD.split("\n"); //[0] = primer usuario PUES SOLO HABRA UNO
                String[] arrayDatos = arrayTodosDatos[0].split(";"); //[0] = username ; [1] = password
                Log.i(TAG, "onCreate() - BBDD Usuario: " + arrayDatos[0] + "\n" + "Password: " + arrayDatos[1]);

                //Si el usuario existe compruebo la contrasenya cifrada
                if (arrayDatos[0].equals(usuario.getText().toString())) {
                    String passwordCifrada = RandomUserBienvenida.sha256(passwordSinCifrar.getText().toString());
                    Log.i(TAG, "onCreate() - passwordCifrada:" + passwordCifrada + "-");
                    Log.i(TAG, "onCreate() -  arrayDatos[1]:" + arrayDatos[1] + "-");
                    Log.i(TAG, "onCreate() - passwordCifrada length: " + passwordCifrada.length());
                    Log.i(TAG, "onCreate() - arrayDatos[1] length: " + arrayDatos[1].length());
                    //Si la contraseña es correcta redirecciono al inicio
                    if (passwordCifrada.equalsIgnoreCase(arrayDatos[1])) {
                        Log.i(TAG, "onCreate() - El usuario es correcto");
                        Toast toastNumeroInserciones = Toast.makeText(getApplicationContext(), "¡ Bienvenido " + usuario.getText() + " !", Toast.LENGTH_LONG);
                        toastNumeroInserciones.show();

                        //Solo se va a almacenar el usuario en las preferencias si es correcto, en caso contrario no se guardara
                        String n = usuario.getText().toString();
                        String cs = passwordSinCifrar.getText().toString();
                        SharedPreferences.Editor editor = sprefs.edit();
                        editor.putString(Nombre, n);
                        editor.putString(Contraseña, cs);
                        //Si no contiene el cifrado de la BBDD lo genera y lo almacena, si lo tiene no hace nada pues las variables ya tienen el valor
                        if (!sprefs.contains(BBDDEncriptado)) {
                            aleatorioBD = Crypto.deriveKeyPbkdf2(Crypto.generateSalt(), cs).getEncoded().toString();
                            encriptadoBD = encrypt(aleatorioBD, cs);
                            editor.putString(BBDDEncriptado, encriptadoBD);
                        }
                        editor.commit();
                        sprefs = getSharedPreferences(MisPreferencias, Context.MODE_PRIVATE);
                        Log.d(TAG, "aleatorioBD: " + aleatorioBD);
                        Log.d(TAG, "encriptadoBD: " + encriptadoBD);
                        if (sprefs.contains(BBDDEncriptado)) {
                            desencriptadoBD = decrypt(sprefs.getString(BBDDEncriptado, ""), cs);
                            Log.d(TAG, "desencriptadoBD: " + desencriptadoBD);
                        }


                        Intent abrirInicio = new Intent("com.randomusers.davidjose.randomusers.INICIO");
                        startActivity(abrirInicio);
                    } else {
                        passwordSinCifrar.setError(getString(R.string.activity_login_contrasenya_error));
                        passwordSinCifrar.setText("");
                    }
                } else {
                    passwordSinCifrar.setText("");
                    usuario.setText("");
                    usuario.setError(getString(R.string.activity_login_usuario_error));
                }
            }

        });
        Log.i(TAG, "onCreate() - FIN");
    }


    SecretKey key;

    String getRawKey() {
        if (key == null) {
            return null;
        }

        return Crypto.toHex(key.getEncoded());
    }


    public SecretKey deriveKey(String password, byte[] salt) {
        return Crypto.deriveKeyPbkdf2(salt, password);
    }

    public String encrypt(String plaintext, String password) {
        byte[] salt = Crypto.generateSalt();
        key = deriveKey(password, salt);
        Log.d(TAG, "Generated key: " + getRawKey());
        return Crypto.encrypt(plaintext, key, salt);
    }

    public String decrypt(String ciphertext, String password) {
        return Crypto.decryptPbkdf2(ciphertext, password);
    }


    //KeyStore
    public void loadKeyStore() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
        } catch (Exception e) {
            // TODO: Handle this appropriately in your app
            e.printStackTrace();
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

    public PublicKey loadPublicKey(String alias) throws Exception {
        Log.i(TAG, "loadPublicKey() - INICIO");
        Key key = keyStore.getKey(alias, null);
        // Get certificate of public key
        Certificate cert = keyStore.getCertificate(alias);
        // Get public key
        if (cert != null){
            PublicKey publicKey = cert.getPublicKey();
            Log.i(TAG,"loadPublicKey(): " + publicKey.getEncoded().toString());
            Log.i(TAG, "loadPublicKey() - FIN");
            return publicKey;
        }else{
            Log.i(TAG, "loadPublicKey() - El certificado es nulo FIN");
            return null;
        }
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

    public String encryptStringWithPublicKey(String alias, String toEncrypt) {
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
    }

    public String decryptStringWithPrivateKey(String alias, String toDecrypt) {
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
    }

}
