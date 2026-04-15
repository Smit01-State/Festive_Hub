package com.Festive_Hub.android.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateApiClient {

    private static final String BASE_URL = "http://10.0.2.2/FinalGpgApp/";

    private  UpdateApiClient(){

    }

    public interface UpdateCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void updateUser(
            Context context,
            String userId, String name, String email,
                                  String contact, String password,
                                  String gender, String city,
                                  UpdateCallback callback) {


        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();

        String UPDATE_URL = BASE_URL +"updateProfile.php";

        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("status")) {
                            callback.onSuccess(json.getString("message"));
                        } else {
                            callback.onError(json.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Invalid server response");
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid",   userId);
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