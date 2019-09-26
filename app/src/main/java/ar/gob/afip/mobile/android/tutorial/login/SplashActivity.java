package ar.gob.afip.mobile.android.tutorial.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import admin.APIClient;
import admin.MessageBroker;
import admin.Observable;


public class SplashActivity extends AppCompatActivity implements Observable {

    @Override
    public void onEvent(String event, Boolean success, JSONObject object) {
        if (success) {
            String PREFS_NAME = "AppPrefs";
            SharedPreferences prefs = ((Context) this).getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);

            prefs
                    .edit()
                    .putString("Configuracion", object.toString())
                    .apply();

            loadConfig(object);
        }
        else
        {
            Log.e(this.getClass().getName(), "Ocurrio un error cargando la configuracion desde internet.");
            //Finalizar la aplicacion
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String PREFS_NAME = "AppPrefs";
        SharedPreferences prefs = ((Context)this).getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (prefs.contains("Configuracion")) {
            String configuracion = prefs.getString("Configuracion", null);
            if (configuracion != null) {
                //Convertir el string configuracion en un objeto.
                try {
                    JSONObject object = new JSONObject(configuracion);// null;
                    loadConfig(object);
                } catch(Throwable tx){

                }

            }
            else
                findConfig();
        }
        else
            findConfig();
    }

    private void findConfig()
    {
        admin.APIClient apiClient = APIClient.getInstance(this);

        MessageBroker messageBroker = MessageBroker.getInstance();

        messageBroker.observe("Configuracion", this);
        //apiClient.getConfiguration(this);
        apiClient.getConfiguration();
    }


    private void loadConfig(JSONObject object)
    {
        Log.i(this.getClass().getName(), "Se cargo la configuracion en SplashActivity");


        //Crear intent
        // Start ACtivity en el intent.
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }



    @Override
    protected void onPause() {
        MessageBroker messageBroker = MessageBroker.getInstance();

        messageBroker.deObserve("Configuracion", this);
        super.onPause();
    }
}