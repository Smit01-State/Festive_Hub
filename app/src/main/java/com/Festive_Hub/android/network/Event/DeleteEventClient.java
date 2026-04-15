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

public class DeleteEventClient {

    private static final String URL = "http://10.0.2.2/FinalGpgApp/Events/Delete.php"; // 🔁 change this

    public interface Callback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void execute(
            Context context,
            int id,
            Callback callback
    ) {
        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.optString("status", "");
                        if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("true") || status.equals("1")) {
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
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }
}
