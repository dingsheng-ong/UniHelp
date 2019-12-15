package my.edu.um.fsktm.unihelp.util;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class HttpRequest {

    private static HttpRequest instance;
    private RequestQueue requestQueue;
    private Cache cache;
    private Network network;

    private HttpRequest(Context context) {
        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static synchronized HttpRequest getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequest(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        instance.requestQueue.add(req);
    }
}