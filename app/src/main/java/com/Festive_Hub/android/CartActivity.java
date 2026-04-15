package com.Festive_Hub.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Festive_Hub.android.network.Cart.CartApiClient;
import com.Festive_Hub.android.network.Event.EventByIdClient;

import org.json.JSONArray; //successfully
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CartList> arrayList;
    CartAdapter adapter;
    TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_recycler);
        totalPriceText = findViewById(R.id.cart_total_price);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartData();
    }

    private void loadCartData() {
        arrayList = new ArrayList<>();

        android.content.SharedPreferences sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        String uidString = sp.getString(ConstantSP.USERID, "0");
        int uid;
        try {
            uid = Integer.parseInt(uidString);
        } catch (NumberFormatException e) {
            uid = 0;
        }

        Log.d("CART_DEBUG", "loadCartData called, uid=" + uid);

        CartApiClient.getCartItems(this, uid, new CartApiClient.CartCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("CART_DEBUG", "getCartItems response: " + response.toString());
                try {
                    if (response.getBoolean("status")) {
                        // Log all keys for debugging in case the field name differs
                        Log.d("CART_DEBUG", "Response keys: " + response.keys().toString());

                        JSONArray data = response.optJSONArray("cartItem");
                        /*
                         * if (data == null) {
                         * // Try alternate key names the server might use
                         * data = response.optJSONArray("data");
                         * if (data == null)
                         * data = response.optJSONArray("cart");
                         * if (data == null)
                         * data = response.optJSONArray("items");
                         * }
                         */
                        if (data == null) {
                            Log.d("CART_DEBUG", "No 'data' array found in response. Cart may be empty.");
                            runOnUiThread(() -> setupAdapter());
                            return;
                        }
                        Log.d("CART_DEBUG", "Cart items count from server: " + data.length());

                        // Collect (eventID, quantity) pairs
                        List<String[]> cartEntries = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String eventID = item.has("eventid") ? item.getString("eventid")
                                    : item.optString("EVENTID", "0");
                            String quantity = item.has("qty") ? item.getString("qty") : item.optString("QTY", "1");
                            Log.d("CART_DEBUG", "Cart entry -> eventID=" + eventID + ", qty=" + quantity);
                            if (!eventID.equals("0") && !eventID.isEmpty()) {
                                cartEntries.add(new String[] { eventID, quantity });
                            }
                        }

                        if (cartEntries.isEmpty()) {
                            Log.d("CART_DEBUG", "No valid cart entries found, showing empty cart.");
                            runOnUiThread(() -> setupAdapter());
                            return;
                        }

                        // Counter-based completion: setup adapter only after all fetches finish
                        final int[] pending = { cartEntries.size() };
                        EventByIdClient eventByIdClient = new EventByIdClient(CartActivity.this);

                        for (String[] entry : cartEntries) {
                            final String eventID = entry[0];
                            final String quantity = entry[1];

                            eventByIdClient.getEventById(eventID,
                                    new EventByIdClient.EventByIdCallback() {
                                        @Override
                                        public void onSuccess(JSONObject eventData) {
                                            Log.d("CART_DEBUG", "EventById success for id=" + eventID + ": "
                                                    + eventData.toString());
                                            try {
                                                CartList cart = new CartList();
                                                cart.setEventID(Integer.parseInt(eventID));
                                                cart.setVendorName(eventData.optString("vendor", ""));
                                                cart.setEventName(eventData.optString("name", ""));
                                                cart.setEventPrice(eventData.optString("price", "0"));
                                                cart.setEventDiscountPrice(eventData.optString("disc_price", "0"));
                                                cart.setDiscount(eventData.optString("discount", "0"));
                                                cart.setImage(eventData.optString("image", ""));
                                                cart.setQuantity(Integer.parseInt(quantity));

                                                synchronized (arrayList) {
                                                    arrayList.add(cart);
                                                    pending[0]--;
                                                    Log.d("CART_DEBUG", "Added event to list. Pending: " + pending[0]);
                                                    if (pending[0] == 0) {
                                                        runOnUiThread(() -> setupAdapter());
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("CART_DEBUG", "Error parsing event data: " + e.getMessage());
                                                synchronized (arrayList) {
                                                    pending[0]--;
                                                    if (pending[0] == 0) {
                                                        runOnUiThread(() -> setupAdapter());
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.e("CART_DEBUG", "Error fetching event " + eventID + ": " + error);
                                            synchronized (arrayList) {
                                                pending[0]--;
                                                if (pending[0] == 0) {
                                                    runOnUiThread(() -> setupAdapter());
                                                }
                                            }
                                        }
                                    });
                        }

                    } else {
                        Log.d("CART_DEBUG", "Server returned status=false: " + response.optString("message", ""));
                        runOnUiThread(() -> Toast.makeText(CartActivity.this,
                                response.optString("message", "Cart is empty"),
                                Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException e) {
                    Log.e("CART_DEBUG", "JSON exception in getCartItems onSuccess: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CartActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter() {
        adapter = new CartAdapter(CartActivity.this, arrayList, CartActivity.this);
        recyclerView.setAdapter(adapter);
        updateTotal();
    }

    public void updateTotal() {
        double total = 0;
        for (CartList item : arrayList) {
            try {
                double price = Double.parseDouble(item.getEventDiscountPrice());
                total += (price * item.getQuantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        totalPriceText.setText(ConstantSP.PRICE_SYMBOL + (int) total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // loadCartData is called from onCreate; calling it again here would
        // reset arrayList while async callbacks from onCreate are still running,
        // causing a race condition that empties the cart view.
    }
}
