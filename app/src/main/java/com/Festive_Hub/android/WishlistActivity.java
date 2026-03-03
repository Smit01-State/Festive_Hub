package com.Festive_Hub.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<WishlistList> arrayList;
    WishAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.wishlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadWishlistData();
    }

    private void loadWishlistData() {
        arrayList = new ArrayList<>();
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);

        // Fetching events that are in the Wishlist table
        String query = "SELECT EVENT_LIST.ID, VENDOR, NAME, PRICE, DISC_PRICE, DISCOUNT, IMAGE " +
                "FROM EVENT_LIST " +
                "INNER JOIN WISHLIST ON EVENT_LIST.ID = WISHLIST.EVENTID";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            WishlistList wish = new WishlistList();
            wish.setEventID(cursor.getInt(0));
            wish.setVendorName(cursor.getString(1));
            wish.setEventName(cursor.getString(2));
            wish.setEventPrice(cursor.getString(3));
            wish.setEventDiscountPrice(cursor.getString(4));
            wish.setDiscount(cursor.getString(5));
            wish.setImage(cursor.getString(6));
            arrayList.add(wish);
        }
        cursor.close();

        adapter = new WishAdapter(WishlistActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning from detail page (it might have been removed from
        // wishlist there)
        loadWishlistData();
    }
}
