package com.Festive_Hub.android.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2/FinalGpgApp/";
    private Context context;

    public ApiClient(Context context) {
        this.context = context;
    }

    // ── Callback Interface ───────────────────────────
    public interface ApiCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    // ── Register User ────────────────────────────────
    public void registerUser(
            String name,
            String email,
            String contact,
            String password,
            String gender,
            String city,
            ApiCallback callback
    ) {
        String url = BASE_URL + "signup.php";
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("REGISTER_SUCCESS", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            callback.onSuccess(json.getString("message"));
                        } else {
                            callback.onError(json.getString("message"));
                        }
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                        Log.e("parse error", "registerUser: "+e.getMessage());
                    }
                },
                error -> {
                    Log.e("REGISTER_ERROR", error.toString());
                    callback.onError("Network error: " + error.toString());
                }

        ) {
            // ── POST parameters sent to PHP ──────────
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name",     name);
                params.put("email",    email);
                params.put("contact",  contact);
                params.put("password", password);
                params.put("gender",   gender);
                params.put("city",     city);
                return params;
            }
        };
        queue.add(request);
    }
}