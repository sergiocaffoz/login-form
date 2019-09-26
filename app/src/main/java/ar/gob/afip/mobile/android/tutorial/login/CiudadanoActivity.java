package ar.gob.afip.mobile.android.tutorial.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import admin.APIClient;
import admin.MessageBroker;
import admin.Observable;

public class CiudadanoActivity extends AppCompatActivity implements View.OnClickListener, Observable {

    Button mBtnDatos;
    Button mBtnDeudas;
    Button mBtnVencimientos;
    TextView mTvData;
    String cookie;
    String headersStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudadano);

        mBtnDatos = findViewById(R.id.btnDatos);
        mBtnDeudas = findViewById(R.id.btnDeudas);
        mBtnVencimientos = findViewById(R.id.btnVencimientos);
        mTvData = findViewById(R.id.tvData);
        mBtnDatos.setOnClickListener(this);
        mBtnDeudas.setOnClickListener(this);
        mBtnVencimientos.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        cookie = extras.getString("cookieCookie");
        headersStr = extras.getString("cookieHeader");

        if (extras == null)
        {
            Toast.makeText(this, "AuthAfipCiudadano", Toast.LENGTH_LONG).show();
        }
        else
        {
            //mTvData.setText(extras.getString("cookieCookie"));
            Toast.makeText(this, cookie, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View v) {

        APIClient apiClient = APIClient.getInstance(this);

        MessageBroker messageBroker = MessageBroker.getInstance();

        messageBroker.observe("InformacionCiudadano", this);


        if (v.getId() == mBtnDatos.getId())
        {
            apiClient.getInformacionCiudadano("Datos",cookie,headersStr);

        }
        else if (v.getId() == mBtnDeudas.getId())
        {
            apiClient.getInformacionCiudadano("getDeuda",cookie,headersStr);


        }
        else if (v.getId() == mBtnVencimientos.getId())
        {
            apiClient.getInformacionCiudadano("getVencimientos",cookie,headersStr);
        }

    }

    @Override
    public void onEvent(String event, Boolean success, JSONObject object) {
        if (success){

            mTvData.setText(object.toString());
        } else {
            mTvData.setText("");
            Toast.makeText(this, object.toString(), Toast.LENGTH_LONG).show();
        }


    }
}
