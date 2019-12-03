package my.edu.um.fsktm.unihelp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCE_FILE = "uniHelpPreference";
    private static final String LOGIN_INFO = "loginData";

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    public static void setLogin(Context context, String data) {
        SharedPreferences sharedPref = getSharedPreference(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LOGIN_INFO, data);
        editor.apply();
    }

    public static String getLogin(Context context) {
        SharedPreferences sharedPref = getSharedPreference(context);
        return sharedPref.getString(LOGIN_INFO, null);
    }

}
