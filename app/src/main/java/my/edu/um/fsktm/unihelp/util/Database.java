package my.edu.um.fsktm.unihelp.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;

public class Database {

    public static void sendQuery(Context context, String query, Response.Listener listener) {
        final String URL = context.getResources().getString(R.string.database_url);
        JSONObject json = new JSONObject();
        try {
            json.put("query", query);
        } catch (JSONException e) { Log.e("JSON Error", e.toString()); }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, URL, json, listener,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Db 29", error.toString());
                }
            }
        );
        HttpRequest.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
