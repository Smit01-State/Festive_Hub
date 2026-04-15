package com.Festive_Hub.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Festive_Hub.android.network.Cart.CartApiClient;
import com.bumptech.glide.Glide;

public class EventDetailActivity extends AppCompatActivity {

    ImageView mainImage, wishlistBtn, minusBtn, plusBtn, eventCartBtn;
    TextView vendorLabel, nameLabel, currentPriceLabel, originalPriceLabel, discountLabel, quantityLabel;
    TextView dateLabel, timeLabel, locationLabel;
    android.widget.LinearLayout qtyLayout;
    android.widget.Button payNowBtn;

    SQLiteDatabase db;
    int eventID;
    int uid;
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
        String uidString = sharedPref.getString(ConstantSP.USERID, "0");
        try {
            uid = Integer.parseInt(uidString);
        } catch(NumberFormatException e) {
            uid = 0;
        }
        eventID = sharedPref.getInt(ConstantSP.EVENTID, 0);
        sName = sharedPref.getString(ConstantSP.EVENTNAME, "");
        sVendor = sharedPref.getString(ConstantSP.EVENTVENDORNAME, "");
        sPrice = sharedPref.getString(ConstantSP.EVENTPRICE, "");
        sDiscountPrice = sharedPref.getString(ConstantSP.EVENTDISCOUNTPRICE, "");
        sDiscount = sharedPref.getString(ConstantSP.EVENTDISCOUNT, "");
        sImage = sharedPref.getString(ConstantSP.EVENTIMAGE, "");
        sDate = sharedPref.getString(ConstantSP.EVENTDATE, "");
        sTime = sharedPref.getString(ConstantSP.EVENTTIME, "");
        sLocation = sharedPref.getString(ConstantSP.ENVENTLOCATION, "");
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
        CartApiClient.getCartQty(this, uid, eventID, new CartApiClient.CartCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        currentQty = response.getInt("qty");
                        eventCartBtn.setVisibility(android.view.View.GONE);
                        qtyLayout.setVisibility(android.view.View.VISIBLE);
                    } else {
                        currentQty = 1;
                        eventCartBtn.setVisibility(android.view.View.VISIBLE);
                        qtyLayout.setVisibility(android.view.View.GONE);
                    }
                    quantityLabel.setText(String.valueOf(currentQty));
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                currentQty = 1;
                eventCartBtn.setVisibility(android.view.View.VISIBLE);
                qtyLayout.setVisibility(android.view.View.GONE);
                quantityLabel.setText(String.valueOf(currentQty));
            }
        });
    }

    private void addToCart() {
        CartApiClient.addToCart(this, uid, eventID, currentQty, new CartApiClient.CartCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        eventCartBtn.setVisibility(android.view.View.GONE);
                        qtyLayout.setVisibility(android.view.View.VISIBLE);
                        quantityLabel.setText(String.valueOf(currentQty));
                        Toast.makeText(EventDetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventDetailActivity.this, response.optString("message", "Error adding"), Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EventDetailActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromCart() {
        CartApiClient.removeFromCart(this, uid, eventID, new CartApiClient.CartCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        eventCartBtn.setVisibility(android.view.View.VISIBLE);
                        qtyLayout.setVisibility(android.view.View.GONE);
                        currentQty = 1;
                        quantityLabel.setText(String.valueOf(currentQty));
                        Toast.makeText(EventDetailActivity.this, "Removed from Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventDetailActivity.this, response.optString("message", "Error removing"), Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EventDetailActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartQty() {
        quantityLabel.setText(String.valueOf(currentQty));
        CartApiClient.updateQty(this, uid, eventID, currentQty, new CartApiClient.CartCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                // Log or handle success
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EventDetailActivity.this, "Error updating quantity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
