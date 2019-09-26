package ar.gob.afip.mobile.android.tutorial.login;

import android.content.Context;
import android.service.autofill.RegexValidator;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmailValidator {
    private static String TAG = EmailValidator.class.getSimpleName();

    static boolean validate(String email) {
        String regex = "[-a-zA-Z0-9_]+@[-a-zA-Z0-9_]+\\.[-a-zA-Z0-9_]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean matches = m.matches();
        Log.d( TAG, "RESULTADO BOOLEAN EMAIL=" + matches);
        return matches;
    }

    public static CharSequence errorMessage(Context context) {
        return context.getString(R.string.email_error_message);
    }
}
