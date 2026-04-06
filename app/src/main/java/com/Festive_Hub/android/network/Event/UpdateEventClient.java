package com.Festive_Hub.android.network.Event;

import android.content.Context;

import com.Festive_Hub.android.network.AppController;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateEventClient {

    private static final String URL = "https://yourserver.com/api/update.php"; // 🔁 change this

    public interface Callback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void execute(
            Context context,
            int id,
            int scid,
            String vendor,
            String name,
            String price,
            String discPrice,
            String discount,
            String image,
            String eventDate,
            String eventTime,
            String location,
            Callback callback
    ) {

        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            callback.onSuccess(json.getString("message"));
                        } else {
                            callback.onError(json.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",         String.valueOf(id));
                params.put("scid",       String.valueOf(scid));
                params.put("vendor",     vendor);
                params.put("name",       name);
                params.put("price",      price);
                params.put("disc_price", discPrice);
                params.put("discount",   discount);
                params.put("image",      image);
                params.put("event_date", eventDate);
                params.put("event_time", eventTime);
                params.put("location",   location);
                return params;
            }
        };
        queue.add(request);
       // AppController.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}