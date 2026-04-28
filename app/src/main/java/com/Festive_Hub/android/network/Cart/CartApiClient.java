package com.Festive_Hub.android.network.Cart;

import android.content.Context;

import com.Festive_Hub.android.ConstantSP;
import com.Festive_Hub.android.network.AppController;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartApiClient {

    private static final String URL = ConstantSP.NET_URL + "Cart/Cart.php";

    // ─────────────────────────────────────────────────────────────
    // Callback Interface
    // ─────────────────────────────────────────────────────────────
    public interface CartCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    // ─────────────────────────────────────────────────────────────
    // ADD TO CART
    // ─────────────────────────────────────────────────────────────
    public static void addToCart(Context context, int uid, int eventId, int qty,
                                 CartCallback callback) {

        RequestQueue queue = AppController.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action",  "add");
                params.put("uid",     String.valueOf(uid));
                params.put("eventid", String.valueOf(eventId));
                params.put("qty",     String.valueOf(qty));
                return params;
            }
        };
        queue.add(request);
        //AppController.getInstance(context).addToRequestQueue(request);
    }

    // ─────────────────────────────────────────────────────────────
    // GET CART QUANTITY
    // ─────────────────────────────────────────────────────────────
    public static void getCartQty(Context context, int uid, int eventId,
                                  CartCallback callback) {
        String getUrl = URL + "?action=get&uid=" + uid + "&eventid=" + eventId;
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl,
                null,
                response -> callback.onSuccess(response),
                error  -> callback.onError(error.toString())
        );
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    // ─────────────────────────────────────────────────────────────
    // GET ALL CART ITEMS  (POST — avoids Volley GET caching)
    // ─────────────────────────────────────────────────────────────
    public static void getCartItems(Context context, int uid, CartCallback callback) {
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        callback.onError("JSON parse error: " + e.getMessage());
                    }
                },
                error -> callback.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get_all");
                params.put("uid",    String.valueOf(uid));
                return params;
            }
        };
        queue.add(request);
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE QTY
    // ─────────────────────────────────────────────────────────────
    public static void updateQty(Context context, int uid, int eventId, int qty,
                                 CartCallback callback) {
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action",  "update");
                params.put("uid",     String.valueOf(uid));
                params.put("eventid", String.valueOf(eventId));
                params.put("qty",     String.valueOf(qty));
                return params;
            }
        };
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    // ─────────────────────────────────────────────────────────────
    // REMOVE ITEM
    // ─────────────────────────────────────────────────────────────
    public static void removeFromCart(Context context, int uid, int eventId,
                                      CartCallback callback) {
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action",  "remove");
                params.put("uid",     String.valueOf(uid));
                params.put("eventid", String.valueOf(eventId));
                return params;
            }
        };
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    // ─────────────────────────────────────────────────────────────
    // CLEAR FULL CART
    // ─────────────────────────────────────────────────────────────
    public static void clearCart(Context context, int uid, CartCallback callback) {
        RequestQueue queue = AppController.getInstance(context).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "clear");
                params.put("uid",    String.valueOf(uid));
                return params;
            }
        };
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }
}