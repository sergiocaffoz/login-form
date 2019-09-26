package admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ar.gob.afip.mobile.android.tutorial.login.R;

public class APIClient {
    private static APIClient ourInstance;
    private RequestQueue queue;


    public static APIClient getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new APIClient(context);

        return ourInstance;
    }

    private APIClient(Context context)
    {
        queue = Volley.newRequestQueue(context);
    }

    public void getConfiguration() {
        String URL = "https://www.afip.gob.ar/appMovil/monotributo/config.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("INICIALIZACION OK", response.toString());
                        MessageBroker.getInstance().publish("Configuracion", true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("INICIALIZACION ERROR", error.getMessage());
                        MessageBroker.getInstance().publish("Configuracion", false, null);
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    public void getInformacionCiudadano(String metodo, final String cookie, String headerStr) {
        String URL = "https://serviciosweb.afip.gob.ar/ClaveFiscal/Portal_Ciudadano/portal.aspx/"+metodo;





            Type type = new TypeToken<Map<String, String>>(){}.getType();
           final Map<String,String> headersMap = new Gson().fromJson(headerStr, type);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("INFORMACION CIUDADANO:", response.toString());
                        MessageBroker.getInstance().publish("InformacionCiudadano", true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERROR_INF_CIUDADANO", error.getMessage());
                        MessageBroker.getInstance().publish("InformacionCiudadano", false, null);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                headers.putAll(headersMap);
                headers.put("Cookie",cookie);
                headers.put("Content-Type","application/json");
                headers.remove("Content-Length");
                headers.remove("Set-Cookie");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


    /*
    public void getString(String URL) {
        StringRequest stringRequest = new StringRequest (
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MessageBroker.getInstance().publish(URL, true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MessageBroker.getInstance().publish(URL, false, null);

                    }
                }
        );
        queue.add(stringRequest);
    }
    */
}
