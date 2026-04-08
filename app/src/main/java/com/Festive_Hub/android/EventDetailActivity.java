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

    ImageView mainImage, wishlistBtn, minusBtn, plusBtn, eventCartBtn;
    TextView vendorLabel, nameLabel, currentPriceLabel, originalPriceLabel, discountLabel, quantityLabel;
    TextView dateLabel, timeLabel, locationLabel;
    android.widget.LinearLayout qtyLayout;
    android.widget.Button payNowBtn;

    SQLiteDatabase db;
    int eventID;
    int currentQty = 1;
    boolean isWishlisted = false;

    // Data from previous page
    String sName, sVendor, sPrice, sDiscountPrice, sDiscount, sImage, sDate, sTime, sLocation;

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
            } else {
                removeFromCart();
            }
        });

        // Cart Button Click
        eventCartBtn.setOnClickListener(v -> {
            currentQty = 1;
            addToCart();
        });

        // Wishlist Toggle
        wishlistBtn.setOnClickListener(v -> toggleWishlist());

        // Pay Now Click
        payNowBtn.setOnClickListener(v -> {
            Toast.makeText(EventDetailActivity.this, "Proceeding to Payment...", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        mainImage = findViewById(R.id.event_detail_image);
        payNowBtn = findViewById(R.id.event_detail_pay_now_btn);
        wishlistBtn = findViewById(R.id.event_detail_wishlist);
        minusBtn = findViewById(R.id.event_detail_minus);
        plusBtn = findViewById(R.id.event_detail_plus);
        vendorLabel = findViewById(R.id.event_detail_vendor);
        nameLabel = findViewById(R.id.event_detail_name);
        dateLabel = findViewById(R.id.event_detail_date);
        timeLabel = findViewById(R.id.event_detail_time);
        locationLabel = findViewById(R.id.event_detail_location);
        currentPriceLabel = findViewById(R.id.event_detail_discounted_price);
        originalPriceLabel = findViewById(R.id.event_detail_original_price);
        discountLabel = findViewById(R.id.event_detail_discount);
        quantityLabel = findViewById(R.id.event_detail_quantity);
        eventCartBtn = findViewById(R.id.event_detail_cart_btn);
        qtyLayout = findViewById(R.id.event_detail_qty_layout);

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
        sDate = sharedPref.getString(ConstantSP.PRODUCTDATE, "");
        sTime = sharedPref.getString(ConstantSP.PRODUCTTIME, "");
        sLocation = sharedPref.getString(ConstantSP.PRODUCTLOCATION, "");
    }

    private void updateUI() {
        nameLabel.setText(sName);
        vendorLabel.setText(sVendor);
        currentPriceLabel.setText(ConstantSP.PRICE_SYMBOL + sDiscountPrice);
        originalPriceLabel.setText(ConstantSP.PRICE_SYMBOL + sPrice);
        discountLabel.setText(sDiscount + "% OFF");
        quantityLabel.setText(String.valueOf(currentQty));

        if (sDate != null && !sDate.isEmpty()) {
            dateLabel.setText("Date: " + sDate);
            dateLabel.setVisibility(android.view.View.VISIBLE);
        } else {
            dateLabel.setVisibility(android.view.View.GONE);
        }

        if (sTime != null && !sTime.isEmpty()) {
            timeLabel.setText("Time: " + sTime);
            timeLabel.setVisibility(android.view.View.VISIBLE);
        } else {
            timeLabel.setVisibility(android.view.View.GONE);
        }

        if (sLocation != null && !sLocation.isEmpty()) {
            locationLabel.setText("Location: " + sLocation);
            locationLabel.setVisibility(android.view.View.VISIBLE);
        } else {
            locationLabel.setVisibility(android.view.View.GONE);
        }

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
            eventCartBtn.setVisibility(android.view.View.GONE);
            qtyLayout.setVisibility(android.view.View.VISIBLE);
        } else {
            currentQty = 1;
            eventCartBtn.setVisibility(android.view.View.VISIBLE);
            qtyLayout.setVisibility(android.view.View.GONE);
        }
        quantityLabel.setText(String.valueOf(currentQty));
        cursor.close();
    }

    private void addToCart() {
        db.execSQL("INSERT INTO CART(EVENTID, QTY) VALUES(" + eventID + ", " + currentQty + ")");
        eventCartBtn.setVisibility(android.view.View.GONE);
        qtyLayout.setVisibility(android.view.View.VISIBLE);
        quantityLabel.setText(String.valueOf(currentQty));
        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    private void removeFromCart() {
        db.execSQL("DELETE FROM CART WHERE EVENTID = " + eventID);
        eventCartBtn.setVisibility(android.view.View.VISIBLE);
        qtyLayout.setVisibility(android.view.View.GONE);
        currentQty = 1;
        quantityLabel.setText(String.valueOf(currentQty));
        Toast.makeText(this, "Removed from Cart", Toast.LENGTH_SHORT).show();
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
