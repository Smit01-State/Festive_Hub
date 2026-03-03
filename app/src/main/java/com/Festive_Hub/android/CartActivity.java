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

        // Fetching events that are in the Cart table
        String query = "SELECT EVENT_LIST.ID, VENDOR, NAME, PRICE, DISC_PRICE, DISCOUNT, IMAGE, CART.QTY " +
                "FROM EVENT_LIST " +
                "INNER JOIN CART ON EVENT_LIST.ID = CART.EVENTID";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            CartList cart = new CartList();
            cart.setEventID(cursor.getInt(0));
            cart.setVendorName(cursor.getString(1));
            cart.setEventName(cursor.getString(2));
            cart.setEventPrice(cursor.getString(3));
            cart.setEventDiscountPrice(cursor.getString(4));
            cart.setDiscount(cursor.getString(5));
            cart.setImage(cursor.getString(6));
            cart.setQuantity(cursor.getInt(7));
            arrayList.add(cart);
        }
        cursor.close();

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
