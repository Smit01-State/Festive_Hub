package com.Festive_Hub.android.network;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController {

    private static AppController instance;
    private RequestQueue requestQueue;
    private Context context;

    // ── Private constructor ──────────────────────────
    private AppController(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = Volley.newRequestQueue(this.context);
    }

    // ── One instance for whole app ───────────────────
    public static synchronized AppController getInstance(Context context) {
        if (instance == null) {
            instance = new AppController(context);
        }
        return instance;
    }

    // ── Get the shared queue ─────────────────────────
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
