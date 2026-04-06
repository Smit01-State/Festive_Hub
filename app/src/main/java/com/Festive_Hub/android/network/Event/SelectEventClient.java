package com.Festive_Hub.android.network.Event;

import android.content.Context;

import com.Festive_Hub.android.network.AppController;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectEventClient {

    private static final String URL = "https://yourserver.com/api/select.php"; // 🔁 change this

    public interface Callback {
        void onFetched(JSONArray events);
        void onError(String error);
    }

    // Fetch all events
    public static void execute(Context context, Callback callback) {
        execute(context, null, callback);
    }

    // Fetch events filtered by SCID
    public static void execute(Context context, Integer scid, Callback callback) {
        String finalUrl = (scid != null) ? URL + "?scid=" + scid : URL;

        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, finalUrl,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            callback.onFetched(json.getJSONArray("data"));
                        } else {
                            callback.onError(json.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage())
        );

        // AppController.getInstance().addToRequestQueue(request);
        queue.add(request);
    }
}