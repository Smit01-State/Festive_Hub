package com.Festive_Hub.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CartList> arrayList;
    CartAdapter adapter;
    SQLiteDatabase db;
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
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);

        // Step 1: Fetch all items from the Cart table
        String cartQuery = "SELECT EVENTID, QTY FROM CART";
        Cursor cartCursor = db.rawQuery(cartQuery, null);

        while (cartCursor.moveToNext()) {
            int eventID = cartCursor.getInt(0);
            int quantity = cartCursor.getInt(1);

            // Step 2: For each item in the cart, fetch its details from EVENT_LIST
            String eventQuery = "SELECT ID, VENDOR, NAME, PRICE, DISC_PRICE, DISCOUNT, IMAGE FROM EVENT_LIST WHERE ID = "
                    + eventID;
            Cursor eventCursor = db.rawQuery(eventQuery, null);

            if (eventCursor.moveToFirst()) {
                CartList cart = new CartList();
                cart.setEventID(eventCursor.getInt(0));
                cart.setVendorName(eventCursor.getString(1));
                cart.setEventName(eventCursor.getString(2));
                cart.setEventPrice(eventCursor.getString(3));
                cart.setEventDiscountPrice(eventCursor.getString(4));
                cart.setDiscount(eventCursor.getString(5));
                cart.setImage(eventCursor.getString(6));
                cart.setQuantity(quantity);
                arrayList.add(cart);
            }
            eventCursor.close();
        }
        cartCursor.close();

        adapter = new CartAdapter(CartActivity.this, arrayList, this);
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
        loadCartData();
    }
}
