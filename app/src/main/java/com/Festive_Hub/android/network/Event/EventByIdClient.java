package com.Festive_Hub.android.network.Event;

import android.content.Context;
import android.util.Log;

import com.Festive_Hub.android.network.AppController;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventByIdClient {

    private static final String BASE_URL = "http://10.0.2.2/FinalGpgApp/";
    private Context context;

    // ── Constructor ──────────────────────────────────
    public EventByIdClient(Context context) {
        this.context = context;
    }

    // ── Callback Interface ───────────────────────────
    public interface EventByIdCallback {
        void onSuccess(JSONObject eventData);
        void onError(String error);
    }

    // ── Get Event By ID ──────────────────────────────
    public void getEventById(
            String id,
            EventByIdCallback callback
    ) {
        String url = BASE_URL + "Events/getEventById.php";

        RequestQueue queue = AppController.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("EVENT_BY_ID_RESPONSE", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean status = json.getBoolean("status");

                        if (status) {
                            callback.onSuccess(json.getJSONObject("event"));
                        } else {
                            callback.onError(json.getString("message"));
                        }

                    } catch (Exception e) {
                        Log.e("EVENT_BY_ID_PARSE_ERROR", e.getMessage());
                        callback.onError("Something went wrong: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("EVENT_BY_ID_ERROR", error.toString());
                    callback.onError("Network error: " + error.toString());
                }
        ) {
            // ── POST params sent to getEventById.php ─
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        request.setTag("EVENT_BY_ID");
        queue.add(request);
    }
}