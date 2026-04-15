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

    private static final String URL = "http://10.0.2.2/FinalGpgApp/Events/View.php"; // 🔁 change this

    public interface Callback {
        void onFetched(JSONArray events);
        void onError(String error);
    }

    // Fetch all events
    public static void execute(Context context, Callback callback) {
        execute(context, null, callback);
    }

    // Fetch events filtered by CID
    public static void execute(Context context, Integer cid, Callback callback) {
        String finalUrl = (cid != null) ? URL + "?cid=" + cid : URL;

        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, finalUrl,
                response -> {
                    try {
                        android.util.Log.d("SelectEventClient", "Raw response: " + response);
                        JSONObject json = new JSONObject(response);
                        String status = json.optString("status", "");
                        if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("true") || status.equals("1")) {
                            JSONArray dataArray = new JSONArray();
                            if (json.has("data")) {
                                dataArray = json.getJSONArray("data");
                            } else if (json.has("events")) {
                                dataArray = json.getJSONArray("events");
                            } else {
                                android.util.Log.w("SelectEventClient", "No 'data' or 'events' array found in response object");
                            }
                            callback.onFetched(dataArray);
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