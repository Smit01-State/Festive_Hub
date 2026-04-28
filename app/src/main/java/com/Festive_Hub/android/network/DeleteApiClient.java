package com.Festive_Hub.android.network;
import android.content.Context;

import com.Festive_Hub.android.ConstantSP;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeleteApiClient {

    private static final String DELETE_URL = ConstantSP.NET_URL+"deleteProfile.php";

    public interface DeleteUserCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }

    public static void deleteUser(Context context, String userId, DeleteUserCallback callback) {

        RequestQueue queue = AppController.getInstance(context.getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                DELETE_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");
                        String message = jsonObject.getString("message");

                        if (status) {
                            callback.onSuccess(message);
                        } else {
                            callback.onError(message);
                        }

                    } catch (JSONException e) {
                        callback.onError("Response parsing failed: " + e.getMessage());
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userId);
                return params;
            }
        };
            queue.add(stringRequest);
        //AppController.getInstance().addToRequestQueue(stringRequest);
    }
}