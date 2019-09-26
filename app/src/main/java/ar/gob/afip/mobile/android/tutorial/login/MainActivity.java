package ar.gob.afip.mobile.android.tutorial.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import admin.APIClient;
import admin.MessageBroker;
import admin.Observable;
import ar.gob.afip.mobile.android.library.dnidigital.activities.DNIDigitalReaderActivity;
import ar.gob.afip.mobile.android.library.dnidigital.model.DNIInfo;
import ar.gob.afip.mobile.android.library.liblogin.activities.AFIPWebAuthActivity;
import ar.gob.afip.mobile.android.library.liblogin.model.LoginResponse;
import ar.gob.afip.mobile.android.rootdetection.RootDetection;
import ar.gob.afip.mobile.android.rootdetection.RootDetectionImpl;
import ar.gob.afip.mobile.android.rootdetection.RootDetectionListener;
import ar.gob.afip.mobile.android.rootdetection.RootMethod;
import okhttp3.Cookie;


public class MainActivity extends AppCompatActivity implements Observable , View.OnClickListener {

    @Override
    public void onEvent(String event, Boolean success, JSONObject object) {
        if (success) {

        }
    }



    private ImageView mImgUp;
    ImageView mImgDown;
    Button mDniBtn;
    Button mLoginBtn;
    String action = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImgUp = findViewById(R.id.imgUp);
        mImgDown = findViewById(R.id.imgDown);
        mImgDown.setOnClickListener(this);
        mDniBtn = findViewById(R.id.btnDni);
        mDniBtn.setOnClickListener(this);
        mLoginBtn = findViewById(R.id.btnLogin);
        mLoginBtn.setOnClickListener(this);



        String pathUp =""; //"https://www.afip.gob.ar/appMovil/monotributo/assets/facturaElectronica.png";
        String pathBG = ""; //https://www.afip.gob.ar/appMovil/monotributo/assets/facturaElectronicaBG.png";




            String conf = ((Context)this).getSharedPreferences("AppPrefs",
                Context.MODE_PRIVATE).getString("Configuracion",null);

        try {
            JSONObject confJson = new JSONObject(conf);// null;
            pathBG = confJson.getJSONObject("news").getString("backImageUrl");
            pathUp = confJson.getJSONObject("news").getString("frontImageUrl");
            action = confJson.getJSONObject("news").getJSONObject("onTap").getString("url");

        } catch(Throwable tx){

        }

        Picasso.with(this).load(pathUp)
                .into(mImgUp, new Callback.EmptyCallback() {
                    @Override public void onSuccess() {
                        //to-do
                    }
                    @Override
                    public void onError() {
                        //to-do
                    }
                });


        //cargo la segunda foto
        Picasso.with(this).load(pathBG)
                .into(mImgDown, new Callback.EmptyCallback() {
                    @Override public void onSuccess() {
                        //to-do
                    }
                    @Override
                    public void onError() {
                        //to-do
                    }
                });


        RootDetection d = RootDetectionImpl.getInstance();
        d.detectRootedDevice(MainActivity.this, new RootDetectionListener() {
            @Override
            public void deviceRooted(Class aClass, RootMethod rootMethod, boolean b) {
                // el dispositivo esta rooteado
            }

            @Override
            public void rootNotDetected(boolean b) {
                // el dispositivo no esta rooteado
                Toast.makeText(MainActivity.this, "No esta rooteado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void cannotVerifyIfRooted(boolean b) {
                // no se puede verificar si esta rooteado, quizás por algún error en la biblioteca.
            }
        });

        /*
        admin.APIClient apiClient = APIClient.getInstance(this);

        MessageBroker messageBroker = MessageBroker.getInstance();
        messageBroker.observe("https://www.afip.gob.ar/sitio/images/afip.png", this);

        apiClient.getString("https://www.afip.gob.ar/sitio/images/afip.png");
*/
        /*
        Esto esta mal, hay que cargarlo en otro hilo
                intentabamos bajar compile 'com.squareup.picasso:picasso:2.5.2' en gradle.config

        try {
            URL newurl = new URL("https://www.afip.gob.ar/sitio/images/afip.png");
            Bitmap bm = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

            mImgUp = findViewById(R.id.imgUp);
            mImgUp.setImageBitmap(bm);
        }
        catch (Exception ex) {
            Log.e("AAA", ex.getMessage());
            Toast t = Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT);
            t.show();
        }
        */
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==mDniBtn.getId()){
            Intent intent = new Intent(MainActivity.this, DNIDigitalReaderActivity.class);
            startActivityForResult(intent,1);

        }else if(v.getId()==mLoginBtn.getId()){
            Intent intent  = new Intent(MainActivity.this , AFIPWebAuthActivity.class);

            Bundle b = new Bundle();
            b.putString(AFIPWebAuthActivity.AUTH_URL, "https://auth.afip.gob.ar/contribuyente");
            b.putString(AFIPWebAuthActivity.SYSTEM_HOST, "serviciosweb.afip.gob.ar");
            b.putString(AFIPWebAuthActivity.SYSTEM_ID, "portal");
            intent.putExtras(b);

            startActivityForResult(intent,15);
        } else if(v.getId()== mImgDown.getId()){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(action));
            if(i.resolveActivity(getPackageManager())!=null){
                startActivity(i);
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 &&
                resultCode == Activity.RESULT_OK && data != null) {
            DNIInfo dniInfo = (DNIInfo) data.getExtras().get(DNIDigitalReaderActivity.DNI_INFO);
            String dniPhoto = (String) data.getExtras().get(DNIDigitalReaderActivity.DNI_PHOTO);
            Toast.makeText(this, dniInfo.getFirstName() + " - " +dniInfo.getLastName(), Toast.LENGTH_LONG).show();
        }
        else if (requestCode == 15 &&
                resultCode == Activity.RESULT_OK && data != null) {

            Toast.makeText(this, "AuthAfip", Toast.LENGTH_LONG).show();

            LoginResponse lr = (LoginResponse) data.getExtras().get(AFIPWebAuthActivity.LOGIN_RESPONSE);
            String str_headers = new JSONObject(lr.getHeaders()).toString(); // String conteniendo los headers de la url del servicio
            String str_cookies = lr.getCookies().toString(); // String conteniendo las cookies del servicio


            Intent ciudadano = new Intent(this, CiudadanoActivity.class);
            ciudadano.putExtra("cookieHeader", str_headers);
            ciudadano.putExtra("cookieCookie", str_cookies);
            startActivity(ciudadano);
        }
    }
}
