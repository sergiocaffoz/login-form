package ar.gob.afip.mobile.android.tutorial.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    String TAG = this.getClass().getSimpleName();
    TextInputEditText mUserEmail;
    TextInputLayout mUserEmailLayout;
    TextInputEditText mUserPassword;
    TextInputLayout mUserPasswordLayout;
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserEmail = findViewById(R.id.user_email_edit_text);
        mUserEmailLayout = findViewById(R.id.user_email_layout);
        mUserPassword = findViewById(R.id.user_password_edit_text);
        mUserPasswordLayout = findViewById(R.id.user_password_layout);
        mLoginButton = findViewById(R.id.login_button);

        mUserEmail.addTextChangedListener(this);
        mUserPassword.addTextChangedListener(this);
        mLoginButton.setOnClickListener(this);


        Log.d( TAG, "onCreate: Se levanta la aplicación");
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if( viewId == R.id.login_button){
            String email = mUserEmail.getText().toString();
            String password = mUserPassword.getText().toString();
            if (!EmailValidator.validate(email) || !PasswordValidator.validate(this, password)) {
                Toast toast=Toast.makeText(getApplicationContext(),"No se superaron las validaciones para el ingreso",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
                Toast.makeText(this, "Estozs son los parámetros por default del Toast", Toast.LENGTH_SHORT).show();
            } else {
                Toast toast= Toast.makeText(getApplicationContext(),"Ingreso exitoso",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String email = mUserEmail.getText().toString();
        if (!EmailValidator.validate(email)) {
            mUserEmailLayout.setError(EmailValidator.errorMessage(this));
        } else {
            mUserEmailLayout.setError(null);
        }

        String password = mUserPassword.getText().toString();
        if (!PasswordValidator.validate(this, password)) {
            mUserPasswordLayout.setError(PasswordValidator.errorMessage(this));
        } else {
            mUserPasswordLayout.setError(null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
}
