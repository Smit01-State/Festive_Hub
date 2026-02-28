package com.Festive_Hub.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EventDetailActivity extends AppCompatActivity {

    ImageView mainImage, wishlistBtn, minusBtn, plusBtn;
    TextView vendorLabel, nameLabel, currentPriceLabel, originalPriceLabel, discountLabel, quantityLabel;

    SQLiteDatabase db;
    int eventID;
    int currentQty = 1;
    boolean isWishlisted = false;

    // Data from previous page
    String sName, sVendor, sPrice, sDiscountPrice, sDiscount, sImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initViews();
        setupDB();
        loadDataFromSP();
        updateUI();
        checkWishlistStatus();
        checkCartStatus();

        // Plus Click
        plusBtn.setOnClickListener(v -> {
            currentQty++;
            updateCartQty();
        });

        // Minus Click
        minusBtn.setOnClickListener(v -> {
            if (currentQty > 1) {
                currentQty--;
                updateCartQty();
            }
        });

        // Wishlist Toggle
        wishlistBtn.setOnClickListener(v -> toggleWishlist());
    }

    private void initViews() {
        mainImage = findViewById(R.id.event_detail_image);
        wishlistBtn = findViewById(R.id.event_detail_wishlist);
        minusBtn = findViewById(R.id.event_detail_minus);
        plusBtn = findViewById(R.id.event_detail_plus);
        vendorLabel = findViewById(R.id.event_detail_vendor);
        nameLabel = findViewById(R.id.event_detail_name);
        currentPriceLabel = findViewById(R.id.event_detail_discounted_price);
        originalPriceLabel = findViewById(R.id.event_detail_original_price);
        discountLabel = findViewById(R.id.event_detail_discount);
        quantityLabel = findViewById(R.id.event_detail_quantity);

        // Strikethrough for original price
        originalPriceLabel.setPaintFlags(originalPriceLabel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setupDB() {
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS CART(ID INTEGER PRIMARY KEY AUTOINCREMENT, EVENTID INTEGER, QTY INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS WISHLIST(ID INTEGER PRIMARY KEY AUTOINCREMENT, EVENTID INTEGER)");
    }

    private void loadDataFromSP() {
        android.content.SharedPreferences sharedPref = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        eventID = sharedPref.getInt(ConstantSP.PRODUCTID, 0);
        sName = sharedPref.getString(ConstantSP.PRODUCTNAME, "");
        sVendor = sharedPref.getString(ConstantSP.PRODUCTVENDORNAME, "");
        sPrice = sharedPref.getString(ConstantSP.PRODUCTPRICE, "");
        sDiscountPrice = sharedPref.getString(ConstantSP.PRODUCTDISCOUNTPRICE, "");
        sDiscount = sharedPref.getString(ConstantSP.PRODUCTDISCOUNT, "");
        sImage = sharedPref.getString(ConstantSP.PRODUCTIMAGE, "");
    }

    private void updateUI() {
        nameLabel.setText(sName);
        vendorLabel.setText(sVendor);
        currentPriceLabel.setText(ConstantSP.PRICE_SYMBOL + sDiscountPrice);
        originalPriceLabel.setText(ConstantSP.PRICE_SYMBOL + sPrice);
        discountLabel.setText(sDiscount + "% OFF");
        quantityLabel.setText(String.valueOf(currentQty));

        Glide.with(this).load(sImage).placeholder(R.mipmap.ic_launcher).into(mainImage);
    }

    private void checkWishlistStatus() {
        Cursor cursor = db.rawQuery("SELECT * FROM WISHLIST WHERE EVENTID = " + eventID, null);
        if (cursor.getCount() > 0) {
            isWishlisted = true;
            wishlistBtn.setImageResource(R.drawable.wishlist_fill);
        } else {
            isWishlisted = false;
            wishlistBtn.setImageResource(R.drawable.wishlist_empty);
        }
        cursor.close();
    }

    private void toggleWishlist() {
        if (isWishlisted) {
            db.execSQL("DELETE FROM WISHLIST WHERE EVENTID = " + eventID);
            wishlistBtn.setImageResource(R.drawable.wishlist_empty);
            isWishlisted = false;
            Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
        } else {
            db.execSQL("INSERT INTO WISHLIST(EVENTID) VALUES(" + eventID + ")");
            wishlistBtn.setImageResource(R.drawable.wishlist_fill);
            isWishlisted = true;
            Toast.makeText(this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCartStatus() {
        Cursor cursor = db.rawQuery("SELECT QTY FROM CART WHERE EVENTID = " + eventID, null);
        if (cursor.moveToFirst()) {
            currentQty = cursor.getInt(0);
        } else {
            currentQty = 1;
        }
        quantityLabel.setText(String.valueOf(currentQty));
        cursor.close();
    }

    private void updateCartQty() {
        quantityLabel.setText(String.valueOf(currentQty));

        Cursor cursor = db.rawQuery("SELECT * FROM CART WHERE EVENTID = " + eventID, null);
        if (cursor.getCount() > 0) {
            db.execSQL("UPDATE CART SET QTY = " + currentQty + " WHERE EVENTID = " + eventID);
        } else {
            db.execSQL("INSERT INTO CART(EVENTID, QTY) VALUES(" + eventID + ", " + currentQty + ")");
        }
        cursor.close();
    }
}
