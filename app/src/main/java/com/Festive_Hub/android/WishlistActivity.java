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

        // Step 1: Fetch all items from the Wishlist table
        String wishlistQuery = "SELECT EVENTID FROM WISHLIST";
        Cursor wishlistCursor = db.rawQuery(wishlistQuery, null);

        while (wishlistCursor.moveToNext()) {
            int eventID = wishlistCursor.getInt(0);

            // Step 2: For each item in the wishlist, fetch its details from EVENT_LIST
            String eventQuery = "SELECT ID, VENDOR, NAME, PRICE, DISC_PRICE, DISCOUNT, IMAGE FROM EVENT_LIST WHERE ID = "
                    + eventID;
            Cursor eventCursor = db.rawQuery(eventQuery, null);

            if (eventCursor.moveToFirst()) {
                WishlistList wish = new WishlistList();
                wish.setEventID(eventCursor.getInt(0));
                wish.setVendorName(eventCursor.getString(1));
                wish.setEventName(eventCursor.getString(2));
                wish.setEventPrice(eventCursor.getString(3));
                wish.setEventDiscountPrice(eventCursor.getString(4));
                wish.setDiscount(eventCursor.getString(5));
                wish.setImage(eventCursor.getString(6));
                arrayList.add(wish);
            }
            eventCursor.close();
        }
        wishlistCursor.close();

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
