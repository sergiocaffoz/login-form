package ar.gob.afip.mobile.android.tutorial.login;

import android.content.Context;
import android.util.Log;

class PasswordValidator {

    private static String TAG = PasswordValidator.class.getSimpleName();

    static Boolean validate(Context context, String password) {
        int minimumPasswordLength = context.getResources().getInteger(R.integer.minimum_password_length);
        Log.d( TAG, "R.integer.minimum_password_length=" + minimumPasswordLength);
        if (password.length() < minimumPasswordLength) {
            return false;
        }

        int countDigit = 0;
        int countLowerCase = 0;
        int countUpperCase = 0;
        for (Character c: password.toCharArray()) {
            if (Character.isDigit(c)) {
                countDigit++;
            } else if (Character.isUpperCase(c)) {
                countUpperCase++;
            } else if (Character.isLowerCase(c)) {
                countLowerCase++;
            }
        }

        boolean res = countDigit >= 2 && countLowerCase >= 1 && countUpperCase >= 1;
        Log.d( TAG, "countDigit=" + countDigit);
        Log.d( TAG, "countLowerCase=" + countLowerCase);
        Log.d( TAG, "countUpperCase=" + countUpperCase);
        return res;
    }

    public static CharSequence errorMessage(Context context) {
        return context.getString(R.string.password_error_message);
    }
}
