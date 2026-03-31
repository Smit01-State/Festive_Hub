package com.Festive_Hub.android.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginApiClient {

    private static final String BASE_URL = "http://10.0.2.2/FinalGpgApp/";
    private Context context;

    // ── Constructor ──────────────────────────────────
    public LoginApiClient(Context context) {
        this.context = context;
    }

    // ── Callback Interface ───────────────────────────
    public interface LoginCallback {
        void onSuccess(JSONObject userData) throws JSONException;
        void onError(String error);
    }

    // ── Login User ───────────────────────────────────
    public void loginUser(
            String email,
            String password,
            LoginCallback callback
    ) {
        String url = BASE_URL + "login.php";

        RequestQueue queue = AppController.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("LOGIN_RESPONSE", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        // common
                        // ── "status" — matches your PHP ──
                        boolean status = json.getBoolean("status");

                        if (status) {
                            callback.onSuccess(json);
                        } else {
                            callback.onError(json.getString("message"));
                        }

                    } catch (Exception e) {
                        Log.e("LOGIN_PARSE_ERROR", e.getMessage());
                        callback.onError("Something went wrong: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("LOGIN_ERROR", error.toString());
                    callback.onError("Network error: " + error.toString());
                }
        ) {
            // ── POST params sent to login.php ────────
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email",    email);
                params.put("password", password);
                params.put("password", password);
                params.put("password", password);
                params.put("password", password);
                params.put("password", password);
                return params;
            }
        };

        queue.add(request);
    }
}