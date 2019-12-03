package my.edu.um.fsktm.unihelp.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpRequest {

    private static HttpRequest instance;
    private RequestQueue requestQueue;
    private static Context context;

    private HttpRequest(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequest getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        instance.requestQueue.add(req);
    }
}